package com.welab.backend_user.api.open;

import com.welab.backend_user.common.dto.ApiResponseDto;
import com.welab.backend_user.common.exception.NotFound;
import com.welab.backend_user.common.web.context.GatewayRequestHeaderUtils;
import com.welab.backend_user.remote.noti.RemoteNotiService;
import com.welab.backend_user.remote.noti.dto.SendSmsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/user/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {

    private final RemoteNotiService remoteNotiService;

    @GetMapping(value = "/test")
    public ApiResponseDto<String> test() {
    // String response = remoteAlimService.sms();
        //헤더에 담긴 userId 가져옴
        String userId = GatewayRequestHeaderUtils.getUserIdOrThrowException();
        log.info("userId = {}", userId);
        return ApiResponseDto.createOk(userId);
    }

    @PostMapping(value = "/sms")
    public ApiResponseDto<SendSmsDto.Response> sms(@RequestBody SendSmsDto.Request request) {
        var result = remoteNotiService.sendSms(request);
        return ApiResponseDto.createOk(result);
    }
}