package com.example.authservice.service;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserEventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TopicExchange exchange;

    @Value("${app.rabbitmq.routing.key}")
    private String routingKey;

    public void publishUserCreatedEvent(String username, String email) {
        String message = String.format("{\"username\":\"%s\", \"email\":\"%s\"}", username, email);
        rabbitTemplate.convertAndSend(exchange.getName(), routingKey, message);
    }
}
