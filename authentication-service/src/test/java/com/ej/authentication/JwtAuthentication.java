package com.ej.authentication;

import com.ej.authentication.jwt.JwtToken;
import com.ej.authentication.web.dtos.UsuarioLoginDto;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Consumer;

public class JwtAuthentication {

    public static Consumer<HttpHeaders> getHeaderAuthorization(WebTestClient testClient, String username, String password){
        String token = testClient.post().uri("/api/v1/auth").bodyValue(new UsuarioLoginDto(username, password)).exchange()
                .expectStatus().isOk().expectBody(JwtToken.class).returnResult().getResponseBody().getToken();
        return headers -> headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }
}
