package com.ej.authentication.web.dtos;

import com.ej.authentication.entities.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDto {

    private Long id;
    private String username;

    public UsuarioResponseDto(Usuario usuario) {
        this.id = usuario.getId();
        this.username = usuario.getUsername();
    }
}
