package com.welab.backend_post.remote.noti;

import com.welab.backend_post.remote.noti.dto.SendSmsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "backend-noti", path = "/backend/noti/v1")
public interface RemoteNotiService {

    @GetMapping(value = "/sms")
    public String sms();

    @PostMapping(value = "/sms")
    public SendSmsDto.Response sendSms(@RequestBody SendSmsDto.Request request);

}
