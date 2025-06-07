package com.welab.backend_post.common.web.context;

import com.welab.backend_post.common.exception.NotFound;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

//API Gateway를 통해 전달된 사용자 인증 정보를 각 마이크로서비스에서 쉽게 꺼내 쓰기 위한 유틸리티 클래스
public class GatewayRequestHeaderUtils {

    // 전달된 헤더 중 key에 해당하는 값을 꺼낸다
    public static String getRequestHeaderParamAsString(String key){
        // 현재 요청 컨텍스트 가져오기
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        // 해당 key에 대한 헤더 값 반환
        return request.getHeader(key);
    }

    // 사용자 ID (X-Auth-UserId) 헤더값 가져오기
    public static String getUserId() {
        return getRequestHeaderParamAsString("X-Auth-UserId");
    }

    // 사용자 ID가 없으면 예외 발생시키기
    public static String getUserIdOrThrowException(){
        String userId = getUserId();
        if (userId == null) {
            throw new NotFound("헤더에 userId 정보가 없습니다");
        }
        return userId;
    }

    // 클라이언트 디바이스 (X-Client-Device) 헤더값 가져오기
    public static String getClientDevice() {
        return getRequestHeaderParamAsString("X-Client-Device");
    }

    // 클라이언트 디바이스가 없으면 예외 발생
    public static String getClientDeviceOrThrowException() {
        String clientDevice = getClientDevice();
        if (clientDevice == null) {
            throw new NotFound("헤더에 사용자 디바이스 정보가 없습니다.");
        }
        return clientDevice;
    }

    // 클라이언트 IP 주소 (X-Client-Address) 헤더값 가져오기
    public static String getClientAddress() {
        return getRequestHeaderParamAsString("X-Client-Address");
    }

    // 클라이언트 IP 주소가 없으면 예외 발생
    public static String getClientAddressOrThrowException() {
        String clientAddress = getClientAddress();
        if (clientAddress == null) {
            throw new NotFound("헤더에 사용자 IP 주소 정보가 없습니다.");
        }
        return clientAddress;
    }

}
