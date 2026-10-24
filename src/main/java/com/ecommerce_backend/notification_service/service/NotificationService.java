package com.ecommerce_backend.notification_service.service;

import com.ecommerce_backend.notification_service.entity.Notification;
import com.ecommerce_backend.notification_service.enums.NotificationStatus;
import com.ecommerce_backend.notification_service.enums.NotificationType;
import com.ecommerce_backend.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Flux<Notification> findAll() {
        return notificationRepository.findAll();
    }

    public Mono<Notification> findById(Long id) {
        return notificationRepository.findById(id);
    }

    public Mono<Notification> createPending(
            Long userId,
            Long orderId,
            NotificationType type,
            String recipientEmail,
            String subject,
            String message
    ) {
        Notification notification = Notification.builder()
                .userId(userId)
                .orderId(orderId)
                .type(type)
                .recipientEmail(recipientEmail)
                .subject(subject)
                .message(message)
                .status(NotificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        return notificationRepository.save(notification);
    }

    public Mono<Notification> markAsSent(Notification notification) {
        notification.setStatus(NotificationStatus.SENT);
        return notificationRepository.save(notification);
    }

    public Mono<Notification> markAsFailed(Notification notification) {
        notification.setStatus(NotificationStatus.FAILED);
        return notificationRepository.save(notification);
    }

}
