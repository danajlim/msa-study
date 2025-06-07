package com.welab.backend_user.api.backend;

import com.welab.backend_user.common.dto.ApiResponseDto;
import com.welab.backend_user.domain.dto.SiteUserInfoDto;
import com.welab.backend_user.domain.dto.UserInfoDto;
import com.welab.backend_user.remote.noti.service.SiteUserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/backend/user/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class BackendUserController {

    private final SiteUserService siteUserService;

    @GetMapping(value = "/hello")
    public String hello() {
        return "유저 백엔드 서비스가 호출되었습니다";
    }

    @PostMapping(value = "/info")
    public UserInfoDto.Response userInfo(@RequestBody UserInfoDto.Request request) {
        var response = new UserInfoDto.Response();
        response.setUserId(request.getUserId());
        response.setUserName("임지빈");
        response.setPhoneNumber("010-0000-0000");

        return response;

    }

    //DB나 서비스에서 조회한 실제 유저 정보
    @GetMapping(value = "/user/{userId}")
    public ApiResponseDto<SiteUserInfoDto> userInfo(@PathVariable String userId) {
        SiteUserInfoDto userInfoDto = siteUserService.userInfo(userId);
        return ApiResponseDto.createOk(userInfoDto);
    }
}
