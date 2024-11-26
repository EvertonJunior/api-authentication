package com.ej.authentication.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordToken extends BaseEntity{

    private String token;
    private LocalDateTime expireDate = LocalDateTime.now().plusMinutes(30);
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    private String username;
    @Enumerated(EnumType.STRING)
    private Status status = Status.VALIDO;

    public ResetPasswordToken(String token, Usuario usuario, String username) {
        this.token = token;
        this.usuario = usuario;
        this.username = username;
    }

    public enum Status{
        VALIDO, EXPIRADO
    }

}
