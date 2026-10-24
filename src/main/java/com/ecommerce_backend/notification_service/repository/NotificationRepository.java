package com.ecommerce_backend.notification_service.repository;

import com.ecommerce_backend.notification_service.entity.Notification;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface NotificationRepository extends ReactiveCrudRepository<Notification, Long> {
}
