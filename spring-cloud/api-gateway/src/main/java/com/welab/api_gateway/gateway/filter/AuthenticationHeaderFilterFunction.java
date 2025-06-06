package com.welab.api_gateway.gateway.filter;

import com.welab.api_gateway.jwt.authentication.UserPrincipal;
import java.util.function.Function;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.function.ServerRequest;

//Gateway 필터 체인에 사용자 인증 정보를 헤더에 삽입하는 로직 담당
public class AuthenticationHeaderFilterFunction {

    //요청을 받아서 가공해서 다시 요청으로 반환하는 요청 변환기 역할
    public static Function<ServerRequest, ServerRequest> addHeader() {
        return request-> {

            //1. 기존 요청(ServerRequest)에서 새로운 요청을 만들기 위한 빌더 생성
            ServerRequest.Builder requestBuilder = ServerRequest.from(request);

            //2. 사용자 인증 정보 꺼내기: Spring Security context에서 현재 인증된 사용자를 꺼냄
            //UserPrincipal이면 getUserId()를 꺼내 X-Auth-UserId라는 커스텀 헤더에 추가 -> "X-Auth-UserId: 123"
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserPrincipal userPrincipal) {
                requestBuilder.header("X-Auth-UserId", userPrincipal.getUserId());
            }

            //3. 추가적인 클라이언트 정보도 헤더에 삽입: IP 주소, 디바이스 정보
            String remoteAddr = "70.1.23.15";
            requestBuilder.header("X-Client-Address", remoteAddr);

            String device = "WEB";
            requestBuilder.header("X-Client-Device", device);

            return requestBuilder.build();

        };
    }

}
