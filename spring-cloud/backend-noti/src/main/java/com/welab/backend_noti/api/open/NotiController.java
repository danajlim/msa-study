package com.welab.backend_noti.api.open;

import com.welab.backend_noti.remote.user.RemoteUserService;
import com.welab.backend_noti.remote.user.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/noti/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class NotiController {

    private final RemoteUserService remoteUserService;

    @GetMapping("/user/info")
    public UserInfoDto.Response userInfo(@RequestParam("userId") String userId){
        var request = new UserInfoDto.Request();
        request.setUserId(userId);
        return remoteUserService.userInfo(request);

    }
}
