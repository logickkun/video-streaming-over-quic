package com.logickkun.vsoq.bff.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
public class BffSecurityConfig {

    @Bean
    SecurityFilterChain bffSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(HttpBasicConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // 프리플라이트 허용
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(
                                // BFF WEB Login Request Endpoint
                                "/bff/web/login", "/bff/web/callback",

                                //Authorization Server Endpoint
                                "/oauth2/authorize", "/oauth2/token", "/oauth2/revoke", "/oauth2/jwks", "/connect/register", "/connect/userinfo", "/connect/logout",

                                // Error Page
                                "/error/**"

                        ).permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }

}
