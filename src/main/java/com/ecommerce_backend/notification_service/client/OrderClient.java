package com.ecommerce_backend.notification_service.client;

import com.ecommerce_backend.notification_service.client.dto.OrderNotificationInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class OrderClient {

    private final WebClient.Builder webClientBuilder;

    public Mono<OrderNotificationInfoResponse> getNotificationInfo(Long orderId) {
        return webClientBuilder.build()
                .get()
                .uri(
                        "http://order-service/api/orders/internal/{orderId}/notification-info",
                        orderId
                )
                .retrieve()
                .bodyToMono(OrderNotificationInfoResponse.class);
    }

}
