package com.welab.backend_user.remote.noti;

import com.welab.backend_user.remote.noti.dto.SendSmsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="backend-noti", path = "/backend/noti/v1")
public interface RemoteNotiService {

    @GetMapping(value = "/hello")
    String hello();

    @PostMapping(value="/sms")
    SendSmsDto.Response sendSms(@RequestBody SendSmsDto.Request request);
}
