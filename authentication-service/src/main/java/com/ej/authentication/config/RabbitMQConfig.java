package com.ej.authentication.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.emailpendente.exchange}")
    private String exchange;

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }


    @Bean
    public Queue createQueueEmailPendent(){
       return QueueBuilder.durable("email-pendente.ms-email-service").build();
    }

    @Bean
    public RabbitAdmin createRabbitAdmin(ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> initializeAdmin(RabbitAdmin rabbitAdmin){
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public FanoutExchange createFanoutExchangeEmailPendente(){
        return ExchangeBuilder.fanoutExchange(exchange).build();
    }

    @Bean
    public Binding createBinding(){
        return BindingBuilder.bind(createQueueEmailPendent()).to(createFanoutExchangeEmailPendente());
    }

}
