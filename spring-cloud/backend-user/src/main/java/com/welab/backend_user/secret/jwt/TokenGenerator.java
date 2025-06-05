package com.welab.backend_user.secret.jwt;

import com.welab.backend_user.secret.jwt.dto.TokenDto;
import com.welab.backend_user.secret.jwt.props.JwtConfigProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.bv.AssertFalseValidator;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

//AccessToken, RefreshToken을 생성, 토큰 내부에 들어갈 정보(클레임), 만료시간, 서명 등을 설정하여 JWT 문자열 생성
@Component
@RequiredArgsConstructor
public class TokenGenerator {

    private final JwtConfigProperties configProperties; //Jwt 관련 설정 값 가져오기
    private final AssertFalseValidator assertFalseValidator;
    private volatile SecretKey secretKey; //실제 Jwt 서명할때 쓰이는 키. 한번만 디코딩해서 캐싱함

    //yml에 있는 secretKey 문자열을 한 번만 디코딩해서 SecretKey 객체로 변환
    private SecretKey getSecretKey(){
        if (secretKey == null) {
            synchronized (this) {
                if (secretKey == null) {
                    secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(configProperties.getSecretKey()));
                }
            }
        }
        return secretKey;
    }

    //Access 전용 토큰 생성
    public TokenDto.AccessToken generateAccessToken(String userId, String deviceType) {
        TokenDto.JwtToken jwtToken = this.generateJwtToken(userId, deviceType, false);
        return new TokenDto.AccessToken(jwtToken);
    }

    //Access + Refresh 두 종류의 토큰 한 번에 생성
    public TokenDto.AccessRefreshToken generateAccessRefreshToken(String userId, String deviceType) {
        TokenDto.JwtToken accessJwtToken = this.generateJwtToken(userId, deviceType, false);
        TokenDto.JwtToken refreshJwtToken = this.generateJwtToken(userId, deviceType, true);
        return new TokenDto.AccessRefreshToken(accessJwtToken, refreshJwtToken);
    }

    //JWT 문자열 생성
    public TokenDto.JwtToken generateJwtToken(String userId, String deviceType, boolean refreshToken) {
        int tokenExpiresIn = tokenExpiresIn(refreshToken, deviceType);
        String tokenType = refreshToken ? "refresh" : "access";

        String token = Jwts.builder()
                .issuer("welab") //발급자
                .subject(userId) //유저 식별자
                .claim("userId", userId) //커스텀 클레임
                .claim("deviceType", deviceType)
                .claim("tokenType", tokenType)
                .issuedAt(new Date()) //발급 시각
                .expiration(new Date(System.currentTimeMillis() + tokenExpiresIn * 1000L)) //만료 시각
                .signWith(getSecretKey()) //시크릿키로 서명
                .header().add("typ", "JWT") //헤더 설정
                .and()
                .compact();

        return new TokenDto.JwtToken(token,tokenExpiresIn);
    }

    //전달받은 deviceType 과 refreshToken 여부에 따라 만료 시간 설정
    private int tokenExpiresIn(boolean refreshToken, String deviceType) {
        int expiresIn = 60*15; //15분

        //각 기기별 만료시간
        if(refreshToken){
            if (deviceType != null) {
                if (deviceType.equals("WEB")) {
                    expiresIn=configProperties.getExpiresIn();
                } else if (deviceType.equals("MOBILE")) {
                    expiresIn= configProperties.getMobileExpiresIn();
                }
            } else{
                expiresIn = configProperties.getExpiresIn();
            }
        }

        return expiresIn;
    }

}
