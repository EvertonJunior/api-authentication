package com.ej.authentication.jwt;


import com.ej.authentication.entities.Usuario;
import com.ej.authentication.exceptions.TokenInvalidException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

@Getter
@NoArgsConstructor
public class JwtUtils {

    private static final String SECRET_KEY = "623678fgsd89-489trw7878-aa978423";
    private static final Integer EXPIRE_MINUTES = 60;

    private static SecretKey generatedKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public static JwtToken createToken(Usuario usuario){
        Date issueAt = new Date();
        Date limit = toExpireDate(issueAt);
        String token = Jwts.builder().header().add("typ", "JWT").
                and().subject(usuario.getUsername()).issuedAt(issueAt).expiration(limit)
                .signWith(generatedKey()).claim("role", usuario.getRole()).compact();
        return new JwtToken(token);
    }

    private static Date toExpireDate(Date issueAt) {
        return Date.from(issueAt.toInstant().plus(Duration.ofMinutes(EXPIRE_MINUTES)));
    }

    public static String isTokenValid(String token){
        try{
            return Jwts.parser().verifyWith(generatedKey()).build().parseSignedClaims(refactorToken(token)).getPayload().getSubject();
        } catch (JwtException e){
            throw new TokenInvalidException("Token Invalido");
        }
    }

    private static CharSequence refactorToken(String token) {
        if(token.contains("Bearer ")){
            return token.substring("Bearer ".length());
        }
        return token;
    }

}
