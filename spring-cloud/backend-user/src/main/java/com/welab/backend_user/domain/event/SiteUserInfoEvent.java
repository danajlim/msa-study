package com.welab.backend_user.domain.event;

import com.welab.backend_user.domain.SiteUser;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

//Kafka로 전송할 메세지 구조 정의한 DTO
@Getter @Setter
public class SiteUserInfoEvent {

    //kafka에서 사용할 topic 이
    public static final String Topic = "userinfo";

    private String action;
    private String userId;
    private String phoneNumber;
    private LocalDateTime eventTime;

    //SiteUser 엔티티로부터 정보를 꺼내서 이벤트 메시지 객체로 만들어줌
    public static SiteUserInfoEvent fromEntity(String action, SiteUser siteUser) {

        SiteUserInfoEvent message = new SiteUserInfoEvent();

        message.action = action;
        message.userId = siteUser.getUserId();
        message.phoneNumber = siteUser.getPhoneNumber();
        message.eventTime = LocalDateTime.now();

        return message;

    }

}
