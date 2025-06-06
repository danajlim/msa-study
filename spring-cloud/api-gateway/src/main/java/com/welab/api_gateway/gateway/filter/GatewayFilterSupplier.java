package com.welab.api_gateway.gateway.filter;

import org.springframework.cloud.gateway.server.mvc.filter.SimpleFilterSupplier;
import org.springframework.context.annotation.Configuration;

//Spring Cloud Gateway MVC에서 정의한 필터들을 실제로 등록하는 설정 클래스
@Configuration
public class GatewayFilterSupplier extends SimpleFilterSupplier {

    //Spring Cloud Gateway가 GatewayFilterFunctions 안에 정의된 필터들을 자동 인식하고 등록
    public GatewayFilterSupplier() {
        super(GatewayFilterFunctions.class);
    }
}