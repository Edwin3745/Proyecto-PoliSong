package com.polisong.polisong_marketplace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desactiva CSRF para pruebas
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll() // Permite tus endpoints REST
                .anyRequest().permitAll() // Permite todo lo demás
            )
            .formLogin(login -> login.disable()) // Desactiva el login por defecto
            .httpBasic(basic -> basic.disable()); // Desactiva la autenticación básica

        return http.build();
    }
}
