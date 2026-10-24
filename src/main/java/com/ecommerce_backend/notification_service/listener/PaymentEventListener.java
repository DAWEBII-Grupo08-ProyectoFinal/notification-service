package com.ecommerce_backend.notification_service.listener;

import com.ecommerce_backend.notification_service.client.IdentityClient;
import com.ecommerce_backend.notification_service.client.OrderClient;
import com.ecommerce_backend.notification_service.client.dto.OrderNotificationInfoResponse;
import com.ecommerce_backend.notification_service.client.dto.UserNotificationInfoResponse;
import com.ecommerce_backend.notification_service.entity.Notification;
import com.ecommerce_backend.notification_service.enums.NotificationType;
import com.ecommerce_backend.notification_service.event.PaymentStatusChangedEvent;
import com.ecommerce_backend.notification_service.service.MailService;
import com.ecommerce_backend.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final OrderClient orderClient;
    private final IdentityClient identityClient;
    private final NotificationService notificationService;
    private final MailService mailService;

    @RabbitListener(queues = "notification.payment.queue")
    public void handlePaymentEvent(PaymentStatusChangedEvent event) {

        log.info(
                "Evento de pago recibido: orderId={}, status={}",
                event.orderId(),
                event.status()
        );

        orderClient.getNotificationInfo(event.orderId())

                .flatMap(orderInfo ->
                        identityClient.getNotificationInfo(orderInfo.userId())
                                .map(userInfo -> new NotificationData(
                                        orderInfo,
                                        userInfo
                                ))
                )

                .flatMap(data -> createNotification(event, data))

                .flatMap(notification ->
                        mailService.send(
                                        notification.getRecipientEmail(),
                                        notification.getSubject(),
                                        notification.getMessage()
                                )
                                .then(notificationService.markAsSent(notification))

                                .onErrorResume(error -> {
                                    log.error(
                                            "Error enviando correo para pedido {}: {}",
                                            notification.getOrderId(),
                                            error.getMessage()
                                    );

                                    return notificationService.markAsFailed(notification);
                                })
                )

                .doOnNext(notification ->
                        log.info(
                                "Notificación finalizada: id={}, orderId={}, email={}, status={}",
                                notification.getId(),
                                notification.getOrderId(),
                                notification.getRecipientEmail(),
                                notification.getStatus()
                        )
                )

                .doOnError(error ->
                        log.error(
                                "Error procesando notificación del pedido {}: {}",
                                event.orderId(),
                                error.getMessage()
                        )
                )

                .subscribe();
    }

    private Mono<Notification> createNotification(
            PaymentStatusChangedEvent event,
            NotificationData data
    ) {

        NotificationType type;
        String subject;
        String message;

        if ("APPROVED".equalsIgnoreCase(event.status())) {

            type = NotificationType.PAYMENT_APPROVED;
            subject = "Pago confirmado - Pedido #" + event.orderId();

            message = """
                    Hola %s,

                    Hemos confirmado correctamente el pago de tu pedido #%d.

                    Monto pagado: S/ %s

                    Tu pedido continuará con el proceso de preparación.

                    Gracias por comprar en Technova.
                    """.formatted(
                    data.userInfo().firstName(),
                    event.orderId(),
                    event.amount()
            );

        } else {

            type = NotificationType.PAYMENT_FAILED;
            subject = "Problema con el pago - Pedido #" + event.orderId();

            message = """
                    Hola %s,

                    No pudimos confirmar el pago de tu pedido #%d.

                    Monto: S/ %s

                    Puedes volver a intentar el pago desde Technova.
                    """.formatted(
                    data.userInfo().firstName(),
                    event.orderId(),
                    event.amount()
            );
        }

        return notificationService.createPending(
                data.userInfo().userId(),
                data.orderInfo().orderId(),
                type,
                data.userInfo().email(),
                subject,
                message
        );
    }

    private record NotificationData(
            OrderNotificationInfoResponse orderInfo,
            UserNotificationInfoResponse userInfo
    ) {
    }
}