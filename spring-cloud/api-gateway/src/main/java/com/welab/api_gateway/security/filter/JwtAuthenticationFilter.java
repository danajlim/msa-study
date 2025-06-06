package com.welab.api_gateway.security.filter;

import com.welab.api_gateway.jwt.JwtTokenValidator;
import com.welab.api_gateway.jwt.authentication.JwtAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
//모든 요청에서 JWT 토큰을 추출하고 검증해서 인증 처리하는 역할
//OncePerRequestFilter: Spring Security에서 요청당 한 번만 실행되는 필터
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //JWT를 검증해주는 컴포넌트 (토큰 유효성, 만료 여부, 사용자 정보 꺼내기 등)
    private final JwtTokenValidator jwtTokenValidator;

    //요청이 들어올 때마다 자동 실행되는 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //1. 토큰 추출 : Authorization 헤더에서 JWT 토큰을 꺼냄
        String jwtToken = jwtTokenValidator.getToken(request);

        //2. 토큰 검증 및 인증 객체 생성
        //토큰이 있으면 validateToken()로 검증, userPrincipal, JwtAuthenticaiton 생성
        if (jwtToken != null) {
            JwtAuthentication authentication = jwtTokenValidator.validateToken(jwtToken);

            //3. 인증 등록
            //인증 객체가 생성되었다면 SecurityContextpㅇ 저장
            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        //4. 다음 필터로 넘김
        filterChain.doFilter(request,response);
    }
}
