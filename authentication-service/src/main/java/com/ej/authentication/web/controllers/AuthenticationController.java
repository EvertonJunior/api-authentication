package com.ej.authentication.web.controllers;

import com.ej.authentication.entities.Usuario;
import com.ej.authentication.jwt.JwtToken;
import com.ej.authentication.jwt.JwtUtils;
import com.ej.authentication.service.UsuarioService;
import com.ej.authentication.web.dtos.UsuarioLoginDto;
import com.ej.authentication.web.exceptions.StandardError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = " Authentication endpoint")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final UsuarioService service;

    @Operation(summary= "Recurso para autenticar o usuario", responses ={
            @ApiResponse(responseCode = "200", description = "Autenticado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtToken.class))),
            @ApiResponse(responseCode = "400", description = "Credencial invalida",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =  StandardError.class))),
            @ApiResponse(responseCode = "422", description = "Dados inseridos invalidos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @PostMapping
    public ResponseEntity<?> authentication(@RequestBody @Valid UsuarioLoginDto loginDto, HttpServletRequest request){
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            JwtToken token = JwtUtils.createToken(service.findByUsername(loginDto.getUsername()));
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new StandardError(request, HttpStatus.BAD_REQUEST, "Credenciais incorretas"));
        }
    }
}
