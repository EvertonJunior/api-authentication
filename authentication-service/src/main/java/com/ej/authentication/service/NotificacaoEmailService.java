package com.ej.authentication.service;

import com.ej.authentication.entities.EmailEvent;
import com.ej.authentication.repository.EmailEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class NotificacaoEmailService {

    private final NotificacaoRabbitService notificacaoRabbitService;
    private final EmailEventRepository emailEventRepository;
    private final String exchange;

    public NotificacaoEmailService(@Value("${rabbitmq.emailpendente.exchange}") String exchange,
                                   EmailEventRepository emailEventRepository,
                                   NotificacaoRabbitService notificacaoRabbitService) {
        this.exchange = exchange;
        this.emailEventRepository = emailEventRepository;
        this.notificacaoRabbitService = notificacaoRabbitService;
    }


    public void notificar(EmailEvent emailEvent){
        try{
            notificacaoRabbitService.notificar(emailEvent, exchange);
            emailEvent.setStatusRabbitNotificacao(EmailEvent.StatusRabbitNotificacao.ENVIADA);
            emailEventRepository.save(emailEvent);
        } catch (RuntimeException e){
            emailEvent.setStatusRabbitNotificacao(EmailEvent.StatusRabbitNotificacao.PENDENTE);
            emailEventRepository.save(emailEvent);
        }
    }


    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void reenviarNotificacao(){
        List<EmailEvent> emailsPendent = emailEventRepository.findAllByStatusRabbitNotificacao(EmailEvent.StatusRabbitNotificacao.PENDENTE);
        emailsPendent.forEach(this::notificar);
    }

}

