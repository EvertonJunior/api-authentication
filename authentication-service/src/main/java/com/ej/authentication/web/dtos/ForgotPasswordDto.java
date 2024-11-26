package com.ej.authentication.web.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordDto {

    @Email(message = "Formato de email inválido", regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
    @NotBlank
    private String email;

}
