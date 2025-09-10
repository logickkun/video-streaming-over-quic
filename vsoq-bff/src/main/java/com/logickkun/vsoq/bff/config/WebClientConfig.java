package com.logickkun.vsoq.bff.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    WebClient http(WebClient.Builder builder) {
        return builder.build(); // 절대 URL 사용 예정
    }
}
