package com.welab.api_gateway.jwt.authentication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.Objects;

//인증된 사용자 정보 표현 객체 (JWT에서 사용자 정보를 꺼낸 뒤 담아두는 객체)
@Getter
@RequiredArgsConstructor // final 필드를 파라미터로 받는 생성자 자동 생성
public class UserPrincipal implements Principal {

    private final String userId;

    public boolean hasName(){
        return userId!=null;
    }

    public boolean hasMandatory(){
        return userId!=null;
    }

    @Override
    public String getName() { //Spring Security에서 사용자 정보 꺼낼 때 사용됨
        return userId;
    }

    @Override
    public int hashCode() {
        int result = userId!=null?userId.hashCode() : 0;
        return result;
    }

    @Override
    public boolean equals(Object another) { //UserPrincipal끼리 비교할 때 userId 기준으로 비교
        if (this==another) return true;
        if (another==null) return false;
        if (!getClass().isAssignableFrom(another.getClass())) return false;

        UserPrincipal principal = (UserPrincipal) another;

        if (!Objects.equals(userId, principal.userId)) return false;
        return true;
    }

    @Override
    public String toString() {
        return getName();
    }
}
