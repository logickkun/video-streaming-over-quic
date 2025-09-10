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
                // 게이트웨이가 프록시하므로 BFF에선 CSRF 비활성화
                .csrf(CsrfConfigurer::disable)
                // CORS는 게이트웨이에서 전담. BFF에선 기본만 켜두면 OK
                .cors(Customizer.withDefaults())
                // 기본 인증 방식 꺼두기(Basic/폼 로그인 화면 금지)
                .httpBasic(HttpBasicConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // 프리플라이트 허용
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers(
                                // OIDC 시작/콜백은 공개
                                "/bff/web/login", "/bff/web/callback"
                        ).permitAll()

                        .requestMatchers("/actuator/health").permitAll()

                        .anyRequest().authenticated()
                );

        // 세션 전략은 기본(IF_REQUIRED) 유지 — BFF는 서버 세션을 사용
        return http.build();
    }

}
