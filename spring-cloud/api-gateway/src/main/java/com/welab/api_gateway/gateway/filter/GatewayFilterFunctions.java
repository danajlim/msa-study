package com.welab.api_gateway.gateway.filter;

import org.springframework.cloud.gateway.server.mvc.common.Shortcut;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.HandlerFilterFunction.*;

//요청(Request)에 커스텀 인증 헤더를 추가하는 필터를 정의하는 함수
//필터 체인에 넣기 위해 준비된 필터 하나하나를 정의
public interface GatewayFilterFunctions {

    //1. 요청을 받아서 Request에서 UserPrincipal에서 userId 추출
    //2. X-Auth-UserId같은 헤더를 추가
    //3. 수정된 ServerRequest 객체를 체인에 넘김
    @Shortcut //Spring Cloud Gateway MVC에서 필터 이름을 지정할 수 있게 해주는 어노테이션
    static HandlerFilterFunction<ServerResponse, ServerResponse> addAuthenticationHeader() {
        return ofRequestProcessor(AuthenticationHeaderFilterFunction.addHeader());
    }

}
