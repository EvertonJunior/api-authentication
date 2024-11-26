package com.ej.ms_email.service;

import com.ej.ms_email.domain.EmailEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(EmailEvent emailEvent){
        var message = new SimpleMailMessage();
        message.setTo(emailEvent.getTo());
        message.setSubject(emailEvent.getSubject());
        message.setText(emailEvent.getText());
        mailSender.send(message);
    }

}
