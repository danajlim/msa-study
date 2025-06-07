package com.welab.backend_user.domain.dto;

import com.welab.backend_user.domain.SiteUser;
import lombok.Getter;
import lombok.Setter;

//외부 API나 다른 서비스에 전달하기 위해 SiteUser 엔티티에서 필요한 정보만 뽑아 담는 용도의 객체
@Getter @Setter
public class SiteUserInfoDto {

    private String userId;

    private String phoneNumber;

    // Entity 객체(SiteUser)를 DTO로 변환해주는 정적 팩토리 메서드
    public static SiteUserInfoDto fromEntity(SiteUser siteUser) {
        SiteUserInfoDto dto = new SiteUserInfoDto();

        dto.userId = siteUser.getUserId();
        dto.phoneNumber = siteUser.getPhoneNumber();

        return dto;
    }
}
