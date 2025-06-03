package com.welab.backend_user.api.open;

import com.welab.backend_user.remote.noti.RemoteNotiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(
        value = "/api/user/v1",
        produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {
    private final RemoteNotiService remoteNotiService;
    @GetMapping(value = "/test")
    public String test() {
        return remoteNotiService.callNotiHello();
    }
}