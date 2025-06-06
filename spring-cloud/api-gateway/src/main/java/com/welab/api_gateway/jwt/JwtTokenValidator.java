package com.welab.api_gateway.jwt;


import com.welab.api_gateway.jwt.authentication.JwtAuthentication;
import com.welab.api_gateway.jwt.authentication.UserPrincipal;
import com.welab.api_gateway.jwt.props.JwtConfigProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//유효성 검사
//JWT 토큰이 클라이언트로부터 왔을 때 이 토큰이 정상인지, 만료되진 않았는지, 토큰 종류가 맞는지, 해당 사용자 정보로 인증 객체(JwtAuthentication)를 만들어 반환
@Component
@RequiredArgsConstructor
public class JwtTokenValidator {

    private final JwtConfigProperties configProperties;
    private volatile SecretKey secretKey; //JWT 서명을 검증할 때 쓰는 비밀키

    //Secretkey는 thread-safe하게 한 번만 초기화
    private SecretKey getSecretKey() {
        if (secretKey == null) {
            synchronized (this) {
                if (secretKey == null) {
                    secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(configProperties.getSecretKey()));
                }
            }
        }

        return secretKey;
    }

    //사용자 인증이 필요할 때 Spring Security에 전달할 인증 객체를 만들어냄
    public JwtAuthentication validateToken(String token) {
        String userId = null;

        //JWT 파싱 및 서명 검증
        final Claims claims = this.verifyAndGetClaims(token);
        if (claims == null) {
            return null;
        }
        //만료 여부 확인
        Date expirationDate = claims.getExpiration();
        if (expirationDate == null || expirationDate.before(new Date())) {
            return null;
        }

        //userId, tokenType 추출
        userId = claims.get("userId", String.class);
        String tokenType = claims.get("tokenType", String.class);
        if (!"access".equals(tokenType)) {
            return null;
        }

        //UserPrincipal 생성
        UserPrincipal principal = new UserPrincipal(userId);

        //JwtAuthentication 생성 → 리턴
        return new JwtAuthentication(getGrantedAuthorities("user"), token, principal);
    }

    //JWT 서명을 검증하고, 안에 들어있는 payload(Claims)를 꺼내는 함수
    private Claims verifyAndGetClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    //사용자의 권한을 GrantedAuthority 객체 리스트로 바꿔줌
    private List<GrantedAuthority> getGrantedAuthorities(String role) {
        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (role != null) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role));
        }
        return grantedAuthorities;
    }

    //Http 요청 헤더에서 JWT 토큰을 꺼내주는 함수
    public String getToken(HttpServletRequest request) {
        String authHeader = getAuthHeaderFromHeader(request);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader(configProperties.getHeader());
    }
}
