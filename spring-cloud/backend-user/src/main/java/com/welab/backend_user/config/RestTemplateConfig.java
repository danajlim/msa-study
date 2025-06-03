package com.welab.backend_user.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced
    // RestTemplate: Spring에서 다른 서버로 HTTP 요청을 보내기 위한 클라이언트 객체
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
