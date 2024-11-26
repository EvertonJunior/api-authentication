package com.ej.authentication.repository;

import com.ej.authentication.entities.EmailEvent;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailEventRepository extends JpaRepository<EmailEvent, Long> {

    List<EmailEvent> findAllByStatusRabbitNotificacao(EmailEvent.StatusRabbitNotificacao statusRabbitNotificacao);
}
