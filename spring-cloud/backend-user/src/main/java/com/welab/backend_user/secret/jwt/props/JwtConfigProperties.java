package com.welab.backend_user.secret.jwt.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//application-local.yml에 있는 jwt 설정값을 자동으로 바인딩해주는 설정 클래스
@Component
@ConfigurationProperties(value = "jwt", ignoreUnknownFields = true)
@Getter
@Setter
public class JwtConfigProperties {
    private Integer expiresIn;
    private Integer mobileExpiresIn;
    private Integer tableExpiresIn;
    private String secretKey;
}