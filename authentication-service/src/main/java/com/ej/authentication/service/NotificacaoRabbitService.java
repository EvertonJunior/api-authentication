package com.ej.authentication.service;

import com.ej.authentication.entities.EmailEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoRabbitService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void notificar(EmailEvent emailEvent, String exchange){
        rabbitTemplate.convertAndSend(exchange, "", emailEvent);
    }
}
