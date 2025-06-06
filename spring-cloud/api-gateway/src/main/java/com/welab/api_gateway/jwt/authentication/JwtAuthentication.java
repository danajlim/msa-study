package com.welab.api_gateway.jwt.authentication;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

//검증한 사용자 정보 표현,  Spring Security 인증 시스템에 전달
@Getter
public class JwtAuthentication extends AbstractAuthenticationToken {

    public final String token; //인증에 사용된 JWT 문자열
    public final UserPrincipal principal; //인증된 사용자 정보 객체 (UserPrincipal, 사용자 ID 등 담고 있음)

    // Spring Security 내부에서 권한을 관리할 수 있도록 부모 클래스에 넘김
    public JwtAuthentication(Collection<? extends GrantedAuthority> authorities, String token, UserPrincipal principal) {
        super(authorities);
        this.token = token;
        this.principal = principal;
        this.setDetails(principal); // 추가 정보 저장
        setAuthenticated(true);// 이 인증 객체는 이미 검증됐다고 표시
    }

    @Override
    public boolean isAuthenticated() { //인증 객체가 인증되었는지 여부 확인용
        return true;
    }

    @Override
    public Object getCredentials() { //자격 증명(비밀번호 등) 반환
        return token;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
