package com.ej.authentication.web.controllers;

import com.ej.authentication.entities.Usuario;
import com.ej.authentication.jwt.JwtUserDetails;
import com.ej.authentication.service.UsuarioService;
import com.ej.authentication.web.dtos.ResetPasswordDto;
import com.ej.authentication.web.dtos.UsuarioCreateDto;
import com.ej.authentication.web.dtos.UsuarioResponseDto;
import com.ej.authentication.web.dtos.ForgotPasswordDto;
import com.ej.authentication.web.exceptions.StandardError;
import com.ej.authentication.web.mappers.UsuarioMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios Endpoint")
public class UsuarioController {

    private final UsuarioService service;

    @Operation(summary= "Recurso para criar novo usuario", description = "Recurso requer um Bearer token",
            responses ={
                    @ApiResponse(responseCode = "201", description = "Usuario criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "Usuario nao processado, ja existe esse usuario no sistema",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation =  StandardError.class))),
                    @ApiResponse(responseCode = "422", description = "Usuario nao processado,  dados inseridos invalidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    @PostMapping
    public ResponseEntity<UsuarioResponseDto> create(@RequestBody @Valid UsuarioCreateDto dto){
        Usuario usuario = UsuarioMapper.toUsuario(dto);
        service.save(usuario);
        return ResponseEntity.status(201).body(UsuarioMapper.toDto(usuario));
    }

    @Operation(summary = "Recurso para buscar Usuario por id", description = "Recurso requer um Bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario recuperado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Usuario nao encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso nao processado, usuario sem permissao",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UsuarioResponseDto> findById(@PathVariable long id){
        return ResponseEntity.ok(UsuarioMapper.toDto(service.findById(id)));
    }

    @Operation(summary = "Recurso para buscar todos os usuarios",
            description = "Recurso requer um Bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuarios buscados com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class)))})
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponseDto>> findAll(){
        List<Usuario> users= service.findAll();
        List<UsuarioResponseDto> usersDto = users.stream().map(UsuarioResponseDto::new).toList();
        return ResponseEntity.ok(usersDto);
    }

    @Operation(summary = "Recurso para deletar usuario por id",
            description = "Recurso requer um Bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Usuario deletado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "404", description = "Usuario nao encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso nao processado, usuario sem permissao",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Recurso para mostrar detalhes do usuario logado",
            description = "Recurso requer um Bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario buscado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso nao processado, usuario sem permissao",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    @GetMapping("/my-details")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<UsuarioResponseDto> details(@AuthenticationPrincipal JwtUserDetails userDetails){
        Usuario usuario = service.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(UsuarioMapper.toDto(usuario));
    }

    @Operation(summary = "Recurso para criar um token e link para recuperacao de senha",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Token e link criados com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "404", description = "Usuario nao encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "409", description = "Token valido ja existe no sistema",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody @Valid ForgotPasswordDto dto){
        service.forgotPassword(dto.getEmail());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Recurso para redifinir  senha",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Senha redifinida com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "404", description = "Token nao encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "422", description = "Dados inseridos invalidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
            })
    @PatchMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestParam("token") String token, @RequestBody @Valid ResetPasswordDto dto){
        service.resetPassword(token, dto.getNewPassword(), dto.getConfirmNewPassword());
        return ResponseEntity.noContent().build();
    }
}
