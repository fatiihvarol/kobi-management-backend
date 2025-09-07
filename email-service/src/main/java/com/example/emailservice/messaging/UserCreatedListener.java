package com.example.emailservice.messaging;

import com.example.emailservice.service.EmailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserCreatedListener {

    private static final Logger log = LoggerFactory.getLogger(UserCreatedListener.class);
    private final EmailSenderService emailSenderService;

    public UserCreatedListener(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    // auth-service'te JSON string olarak {"username":"...","email":"..."}
    @RabbitListener(queues = "${app.rabbitmq.queue.name}")
    public void onMessage(@Payload String payload) {
        try {
            // Basit ve bağımlılıksız parse: çok basit JSON formatı
            String normalized = payload.trim();
            String username = extractValue(normalized, "username");
            String email = extractValue(normalized, "email");

            Map<String, String> vars = new HashMap<>();
            vars.put("username", username);
            vars.put("email", email);

            emailSenderService.sendTemplatedEmail(email, "user_welcome", vars);
        } catch (Exception e) {
            log.error("Failed to handle message {}: {}", payload, e.getMessage(), e);
        }
    }

    private String extractValue(String json, String key) {
        String needle = "\"" + key + "\"";
        int i = json.indexOf(needle);
        if (i == -1) return null;
        int colon = json.indexOf(':', i);
        int startQuote = json.indexOf('"', colon + 1);
        int endQuote = json.indexOf('"', startQuote + 1);
        if (startQuote == -1 || endQuote == -1) return null;
        return json.substring(startQuote + 1, endQuote);
    }
}


