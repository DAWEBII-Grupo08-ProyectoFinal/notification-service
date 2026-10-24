package com.ecommerce_backend.notification_service.service;

import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailClient mailClient;

    @Value("${mail.username}")
    private String senderEmail;

    public Mono<Void> send(
            String recipient,
            String subject,
            String body
    ) {
        MailMessage message = new MailMessage()
                .setFrom(senderEmail)
                .setTo(recipient)
                .setSubject(subject)
                .setText(body);

        return Mono.create(sink ->
                mailClient.sendMail(message)
                        .onSuccess(result -> sink.success())
                        .onFailure(sink::error)
        );
    }

}
