package com.ecommerce_backend.notification_service.entity;

import com.ecommerce_backend.notification_service.enums.NotificationStatus;
import com.ecommerce_backend.notification_service.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("notifications")
public class Notification {

    @Id
    private Long id;

    private Long userId;
    private Long orderId;

    private NotificationType type;

    private String recipientEmail;
    private String subject;
    private String message;

    private NotificationStatus status;
    private LocalDateTime createdAt;

}
