package com.welab.backend_user.secret.jwt.dto;

import lombok.*;

//토큰 관련 정보를 주고받기 위한 DTO 구조
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JwtToken { //로그인.재발급 시 사용 - 토큰과 만료시간 담고있음
        private String token;
        private Integer expiresIn;
    }

    @Getter
    @RequiredArgsConstructor
    public static class AccessToken { //인증 후 응답에 사용 - Access Token만 포함
        private final JwtToken access;
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class AccessRefreshToken { //로그인 성공 시 사용 - Access + Refresh 토큰을 함께 보내야 할 때
        private final JwtToken access;
        private final JwtToken refresh;
    }
}