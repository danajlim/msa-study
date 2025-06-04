package com.welab.backend_user.api.open;

import com.welab.backend_user.common.dto.ApiResponseDto;
import com.welab.backend_user.common.exception.NotFound;
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
        throw new NotFound("해당 게시물을 찾을 수 없습니다.");
//        String result = remoteNotiService.hello();
//        return ApiResponseDto.createOk(result);
    }

    @PostMapping(value = "/sms")
    public ApiResponseDto<SendSmsDto.Response> sms(@RequestBody SendSmsDto.Request request) {
        var result = remoteNotiService.sendSms(request);
        return ApiResponseDto.createOk(result);
    }
}