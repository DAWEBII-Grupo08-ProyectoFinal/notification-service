package com.ecommerce_backend.notification_service.config;

import io.vertx.core.Vertx;
import io.vertx.ext.mail.LoginOption;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.StartTLSOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailConfig {

    @Bean
    public Vertx vertx() {
        return Vertx.vertx();
    }

    @Bean
    public MailClient mailClient(
            Vertx vertx,
            @Value("${mail.hostname}") String hostname,
            @Value("${mail.port}") int port,
            @Value("${mail.username}") String username,
            @Value("${mail.password}") String password
    ) {

        io.vertx.ext.mail.MailConfig config =
                new io.vertx.ext.mail.MailConfig()
                        .setHostname(hostname)
                        .setPort(port)
                        .setStarttls(StartTLSOptions.REQUIRED)
                        .setLogin(LoginOption.REQUIRED)
                        .setAuthMethods("PLAIN LOGIN")
                        .setUsername(username)
                        .setPassword(password);

        return MailClient.createShared(
                vertx,
                config,
                "technova-mail-client"
        );
    }

}
