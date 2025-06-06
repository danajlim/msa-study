package com.welab.backend_user.remote.noti.dto;

import com.welab.backend_user.domain.SiteUser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SendSmsDto {
    @Getter
    @Setter
    public static class Request {
        private String userId;
        private String phoneNumber;
        private String title;
        private String message;

        public static Request fromEntity(SiteUser siteUser) {
            Request request = new Request();
            request.userId = siteUser.getUserId();
            request.phoneNumber = siteUser.getPhoneNumber();
            request.message = "가입 축하 메세지";
            request.title = "가입축하 메세지 타이틀";

            return request;
        }
    }

    @Getter @Setter
    public static class Response {
        private String result;
    }
}
