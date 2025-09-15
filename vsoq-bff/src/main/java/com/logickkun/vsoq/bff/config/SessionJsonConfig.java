package com.logickkun.vsoq.bff.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.jackson2.SecurityJackson2Modules;

@Configuration
public class SessionJsonConfig {

    /**
     * Spring Session이 자동 인식하는 이름.
     * 이 빈을 쓰면 세션 속성을 JSON으로 Redis에 저장한다.
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer(ObjectMapper objectMapper) {
        // (옵션) 세션에 Security 관련 객체를 저장한다면 모듈 등록
        objectMapper.registerModules(
                SecurityJackson2Modules.getModules(SessionJsonConfig.class.getClassLoader())
        );
        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }
}
