package com.welab.backend_user.api.open;

import com.welab.backend_user.common.dto.ApiResponseDto;
import com.welab.backend_user.domain.dto.SiteUserLoginDto;
import com.welab.backend_user.domain.dto.SiteUserRegisterDto;
import com.welab.backend_user.remote.noti.service.SiteUserService;
import com.welab.backend_user.secret.jwt.dto.TokenDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/user/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserAuthController {

    private final SiteUserService siteUserService;

    @PostMapping(value = "/register")
    public ApiResponseDto<String> register(@RequestBody @Valid SiteUserRegisterDto userRegisterDto) {
        siteUserService.registerUser(userRegisterDto);
        return ApiResponseDto.defaultOk();
    }

    @PostMapping(value = "/login")
    public ApiResponseDto<TokenDto.AccessRefreshToken> login(@RequestBody @Valid SiteUserLoginDto loginDto) {

        TokenDto.AccessRefreshToken token = siteUserService.login(loginDto);
        return ApiResponseDto.createOk(token);

    }
}
