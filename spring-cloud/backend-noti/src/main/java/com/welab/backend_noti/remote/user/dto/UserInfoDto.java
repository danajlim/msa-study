package com.welab.backend_noti.remote.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserInfoDto {

    @Getter
    @Setter
    public static class Request{
        private String userId;
    }

    @Getter @Setter
    public static class Response{
        private String userId;
        private String userName;
        private String phoneNumber;
    }
}
