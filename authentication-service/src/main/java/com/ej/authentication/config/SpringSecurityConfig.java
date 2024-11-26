package com.ej.authentication.config;

import ch.qos.logback.core.joran.action.ActionUtil;
import com.ej.authentication.jwt.JwtAuthenticationEntryPoint;
import com.ej.authentication.jwt.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@EnableWebMvc
@EnableMethodSecurity
@Configuration
public class SpringSecurityConfig {

    private static final String[] DOCUMENTATION_OPENAPI = {
            "/docs/index.html",
            "/docs-auth.html", "/docs-auth/**",
            "/v3/api-auth/**",
            "/swagger-ui-custom.html", "/swagger-ui.html", "/swagger-ui/**",
            "/**.html", "/webjars/**", "/configuration/**", "/swagger-resources/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/auth")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/usuarios")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/usuarios/forgot-password")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.PATCH, "/api/v1/usuarios/reset-password")).permitAll()
                        .requestMatchers(DOCUMENTATION_OPENAPI).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(authorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex-> ex.authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                .build();
    }

    @Bean
    public JwtAuthorizationFilter authorizationFilter(){
        return new JwtAuthorizationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
