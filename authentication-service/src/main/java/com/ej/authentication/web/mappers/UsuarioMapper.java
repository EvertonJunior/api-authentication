package com.ej.authentication.web.mappers;

import com.ej.authentication.entities.Usuario;
import com.ej.authentication.web.dtos.UsuarioCreateDto;
import com.ej.authentication.web.dtos.UsuarioResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UsuarioMapper {

    public static Usuario toUsuario(UsuarioCreateDto dto){
        return new ModelMapper().map(dto, Usuario.class);
    }

    public static UsuarioResponseDto toDto(Usuario usuario){
        return new ModelMapper().map(usuario, UsuarioResponseDto.class);
    }

}
