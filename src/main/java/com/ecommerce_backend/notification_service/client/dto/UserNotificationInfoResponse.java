package com.ecommerce_backend.notification_service.client.dto;

public record UserNotificationInfoResponse(
        Long userId,
        String firstName,
        String email
) {
}
