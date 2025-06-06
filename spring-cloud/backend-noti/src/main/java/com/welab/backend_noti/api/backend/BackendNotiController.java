package com.welab.backend_noti.api.backend;

import com.welab.backend_noti.domain.dto.SendSmsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/backend/noti/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class BackendNotiController {

    @GetMapping(value = "/hello")
    public String sms() {
        return "알림 백엔드 서비스가 호출되었습니다";
    }

    @PostMapping(value = "/sms")
    public SendSmsDto.Response sendSms(@RequestBody SendSmsDto.Request request) {
        log.info("회원가입을 축하드립니다. userId={}", request.getUserId());
        SendSmsDto.Response response = new SendSmsDto.Response();
        response.setResult("OK");
        return response;
    }
    
}