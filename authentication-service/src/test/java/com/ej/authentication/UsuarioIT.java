package com.ej.authentication;


import com.ej.authentication.entities.ResetPasswordToken;
import com.ej.authentication.entities.Usuario;
import com.ej.authentication.repository.ResetPasswordTokenRepository;
import com.ej.authentication.web.dtos.ForgotPasswordDto;
import com.ej.authentication.web.dtos.ResetPasswordDto;
import com.ej.authentication.web.dtos.UsuarioCreateDto;
import com.ej.authentication.web.dtos.UsuarioResponseDto;
import com.ej.authentication.web.exceptions.StandardError;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.assertj.core.api.*;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/usuarios-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UsuarioIT {

    @Autowired
    private WebTestClient testClient;

    @Autowired
    private ResetPasswordTokenRepository tokenRepository;

    @Test
    void userObject_whenCreate_ThenReturnSavedNewUser(){
        UsuarioResponseDto response = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("teste@email.com", "123456")).exchange()
                .expectStatus().isCreated().expectBody(UsuarioResponseDto.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getUsername()).isEqualTo("teste@email.com");
    }

    @Test
    void userObject_whenCreate_ThenReturnExistingUserException(){
        StandardError response = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("maria@email.com", "123456")).exchange()
                .expectStatus().isEqualTo(409).expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(409);
    }

    @Test
    void userObject_whenCreateWithInvalidCharactersInEmail_ThenReturnMethodArgumentNotValidException(){
        StandardError response = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("email.com", "123456")).exchange()
                .expectStatus().isEqualTo(422).expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    void userObject_whenCreateWithInvalidPasswordCharacters_ThenReturnMethodArgumentNotValidException(){
        StandardError response = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("teste@email.com", "123")).exchange()
                .expectStatus().isEqualTo(422).expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    void userObject_whenFindById_ThenReturnUserObject(){
        UsuarioResponseDto response = testClient.get().uri("/api/v1/usuarios/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk().expectBody(UsuarioResponseDto.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getUsername()).isEqualTo("ana@email.com");

        response = testClient.get().uri("/api/v1/usuarios/101")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk().expectBody(UsuarioResponseDto.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getUsername()).isEqualTo("maria@email.com");
    }

    @Test
    void userObject_whenFindByIdUserWithoutPermission_ThenReturnStatusCode403(){
        StandardError response = testClient.get().uri("/api/v1/usuarios/102")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange()
                .expectStatus().isForbidden().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void userObject_whenFindById_ThenReturnNotFoundException(){
        StandardError response = testClient.get().uri("/api/v1/usuarios/500")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isNotFound().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void userObject_whenFindAll_ThenReturnUsersList(){
        List<UsuarioResponseDto> response = testClient.get().uri("/api/v1/usuarios")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectBodyList(UsuarioResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.size()).isEqualTo(3);
        Assertions.assertThat(response.getFirst().getUsername()).isEqualTo("ana@email.com");
    }

    @Test
    void userObject_whenFindAllUserWithoutPermission_ThenReturnStatusCode403(){
        StandardError response = testClient.get().uri("/api/v1/usuarios")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange().expectBody(StandardError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void userObject_whenDeleteById_ThenReturnDeletedUserAndStatusNoContent() {
        testClient.delete().uri("/api/v1/usuarios/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                .exchange().expectStatus().isNoContent();
    }

    @Test
    void userObject_whenDeleteByIdUserWithoutPermission_ThenReturnStatusCode403() {
        StandardError response=  testClient.delete().uri("/api/v1/usuarios/102")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange().expectStatus().isForbidden().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    void findDetails_WhenFindDetailsWithAuthenticatedUser_ThenReturnDetails(){
        UsuarioResponseDto response = testClient.get().uri("/api/v1/usuarios/my-details")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "maria@email.com", "123456"))
                .exchange().expectStatus().isOk().expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getUsername()).isEqualTo("maria@email.com");
    }

    @Test
    void forgotPassword_whenRequestWithValidEmail_ThenReturnNoContent204(){
        testClient.post().uri("/api/v1/usuarios/forgot-password").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ForgotPasswordDto("ana@email.com")).exchange()
                .expectStatus().isNoContent();

        List<ResetPasswordToken> token = tokenRepository.findByUsername("ana@email.com");
        Assertions.assertThat(token.size()).isEqualTo(1);
        Assertions.assertThat(token.getFirst().getUsername()).isEqualTo("ana@email.com");
        Assertions.assertThat(token.getFirst().getUsuario().getUsername()).isEqualTo("ana@email.com");
    }

    @Test
    void forgotPassword_whenEmailNotFound_ThenReturnNotFound404() {
        StandardError response =  testClient.post().uri("/api/v1/usuarios/forgot-password").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ForgotPasswordDto("sardinha@email.com")).exchange()
                .expectStatus().isNotFound().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }


    @Test
    void forgotPassword_whenAlreadyTokenValid_ThenReturnConflict409() {
        StandardError response =  testClient.post().uri("/api/v1/usuarios/forgot-password").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ForgotPasswordDto("maria@email.com")).exchange()
                .expectStatus().isEqualTo(409).expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(409);
    }

    @Test
    void resetPassword_whenRequestWithValidData_ThenReturnNoContent() {
        testClient.patch().uri("/api/v1/usuarios/reset-password?token=1dc82400-470a-42cd-a1f2-47cb8e9d041d").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ResetPasswordDto("1234567", "1234567")).exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void resetPassword_whenTokenInvalid_ThenReturnNotFound() {
        StandardError response =  testClient.patch().uri("/api/v1/usuarios/reset-password?token=1d0-470a-42cd-a1f2-47c41d").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ResetPasswordDto("123456", "123456")).exchange()
                .expectStatus().isNotFound().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void resetPassword_whenPasswordDivergent_ReturnBadRequest() {
        StandardError response = testClient.patch().uri("/api/v1/usuarios/reset-password?token=1dc82400-470a-42cd-a1f2-47cb8e9d041d").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ResetPasswordDto("1234567", "123456")).exchange()
                .expectStatus().isBadRequest().expectBody(StandardError.class).returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(400);
    }
}
