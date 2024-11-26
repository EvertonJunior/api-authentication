package com.ej.authentication.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailEvent extends BaseEntity{

    @Column(name = "email_to")
    private String to;
    private String subject;
    private String text;
    @Enumerated(EnumType.STRING)
    private StatusRabbitNotificacao statusRabbitNotificacao = StatusRabbitNotificacao.PENDENTE;

    public EmailEvent(String to, String subject, String text){
        this.to = to;
        this.subject = subject;
        this.text = text;
    }

    public enum StatusRabbitNotificacao{
        ENVIADA, PENDENTE
    }

}
