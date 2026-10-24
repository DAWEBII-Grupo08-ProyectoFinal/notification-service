package com.ecommerce_backend.notification_service.client.dto;

public record OrderNotificationInfoResponse(
        Long orderId,
        Long userId
) {
}
