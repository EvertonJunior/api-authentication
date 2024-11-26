package com.ej.ms_email.listener;

import com.ej.ms_email.domain.EmailEvent;
import com.ej.ms_email.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class EmailPendenteListener {

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = "${rabbitmq.queue.email.pendente}")
    public void EmailPendente(EmailEvent event){
        emailService.sendEmail(event);
    }

}
