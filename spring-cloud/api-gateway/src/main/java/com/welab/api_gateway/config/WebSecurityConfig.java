package com.welab.api_gateway.config;

import com.welab.api_gateway.jwt.JwtTokenValidator;
import com.welab.api_gateway.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

//Spring Security 보안 설정 클래스
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    //JWT를 검증하고 인증 객체를 생성해주는 컴포넌트
    private final JwtTokenValidator jwtTokenValidator;

    //Spring Security의 보안 필터 체인을 구성
    @Bean
    public SecurityFilterChain applicationSecurity(HttpSecurity http) throws Exception {
    http
            //corsConfigurationSource() 메서드에서 정의한 CORS 정책 적용: 모든 origin, method, header 허용 (*)
            .cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        })
                .csrf(AbstractHttpConfigurer::disable)
                .securityMatcher("/**") //보안 설정이 모든 경로(/**)에 적용
                .sessionManagement(sessionManagementConfigurer
                        -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //항상 Stateless로 설정
                .formLogin(AbstractHttpConfigurer::disable) //웹 로그인 폼 인증 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) //브라우저 팝업 뜨는 http 기본 인증 비활성화
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenValidator),
                        UsernamePasswordAuthenticationFilter.class) //커스텀 jwt 필터 등록: JwtAuthenticationFilter 먼저 실행시켜 토큰 검사 후 인증 객체를 등록
                .authorizeHttpRequests(registry -> registry // 요청별 접근 제어
                        .requestMatchers("/api/user/v1/auth/**").permitAll() //api/user/v1/auth/**: 로그인, 회원가입 등 인증 없이 접근 허용
                        .anyRequest().authenticated() //나머지 요청은 모두 인증 필요
                );
        return http.build();
    }

    //프론트엔드와 통신이 가능하도록 CORS 허용 정책 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        // config.setAllowedOrigins(List.of("*"));
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}