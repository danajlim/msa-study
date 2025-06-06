package com.welab.backend_user.common.web.context;

import com.welab.backend_user.common.exception.NotFound;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

//Gateway에서 바꿔서 보낸 헤더에서 값 추출, 읽고 값 없으면 예외처리
public class GatewayRequestHeaderUtils {

    // 주어진 key에 해당하는 요청 헤더 값을 문자열로 반환
    public static String getRequestHeaderParamAsString(String key) {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        return request.getHeader(key); // key에 해당하는 헤더 값 반환
    }

    //사용자 ID 헤더값 반환
    public static String getUserId(){
        return getRequestHeaderParamAsString("X-Auth-UserId");
    }

    //사용자 ID 없으면 예외 발생
    public static String getUserIdOrThrowException() {
        String userId = getUserId();
        if (userId == null) {
            throw new NotFound("헤더에 userId 정보가 없습니다.");
        }
        return userId;
    }

    //디바이스
    public static String getClientDevice() {
        return getRequestHeaderParamAsString("X-Client-Device");
    }

    public static String getClientDeviceOrThrowException() {
        String clientDevice = getClientDevice();
        if (clientDevice == null) {
            throw new NotFound("헤더에 사용자 디바이스 정보가 없습니다.");
        }
        return clientDevice;
    }

    //IP 주소
    public static String getClientAddress(){
        return getRequestHeaderParamAsString("X-Client-Address");
    }

    public static String getClientAddressOrThrowException() {
        String clientAddress = getClientAddress();
        if (clientAddress == null) {
            throw new NotFound("헤더에 사용자 IP 주소 정보가 없습니다.");
        }
        return clientAddress;
    }

}
