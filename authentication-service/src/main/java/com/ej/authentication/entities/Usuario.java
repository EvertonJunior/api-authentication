package com.ej.authentication.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_usuarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Usuario extends BaseEntity {

    @Column(unique = true, nullable = false, length = 150)
    private String username;
    @Column(nullable = false, length = 250)
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;

    public enum Role {
        ROLE_USER, ROLE_ADMIN
    }

    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String modifiedBy;

}
