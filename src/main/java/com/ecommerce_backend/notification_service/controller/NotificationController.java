package com.ecommerce_backend.notification_service.controller;

import com.ecommerce_backend.notification_service.entity.Notification;
import com.ecommerce_backend.notification_service.service.MailService;
import com.ecommerce_backend.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final MailService mailService;

    @GetMapping
    public Flux<Notification> findAll() {
        return notificationService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Notification> findById(@PathVariable Long id) {
        return notificationService.findById(id);
    }

}
