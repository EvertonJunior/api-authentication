package com.ej.authentication.web.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDto {

    @NotBlank
    @Size(min = 6, max = 50)
    private String newPassword;

    @NotBlank
    @Size(min = 6, max = 50)
    private String confirmNewPassword;

}
