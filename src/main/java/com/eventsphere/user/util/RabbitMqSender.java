package com.eventsphere.user.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMqSender {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing-key.user.delete}")
    private String userDeleteRk;

    public void sendDeletedUserId(Long id) {
        rabbitTemplate.convertAndSend(exchange, userDeleteRk, id);
    }
}