package com.ecommerce_backend.notification_service.client;

import com.ecommerce_backend.notification_service.client.dto.UserNotificationInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class IdentityClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${internal.api-key}")
    private String internalApiKey;

    public Mono<UserNotificationInfoResponse> getNotificationInfo(Long userId) {

        return webClientBuilder.build()
                .get()
                .uri(
                        "http://identity-service/api/users/internal/{userId}/notification-info",
                        userId
                )
                .header("X-Internal-Api-Key", internalApiKey)
                .retrieve()
                .bodyToMono(UserNotificationInfoResponse.class);
    }

}
