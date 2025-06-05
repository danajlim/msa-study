package com.welab.backend_user.domain.dto;

import com.welab.backend_user.domain.SiteUser;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SiteUserRegisterDto {

    @NotBlank(message = "아이디를 입력하세요.")
    private String userId;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;

    @NotBlank(message = "번호를 입력하세요.")
    private String phoneNumber;

    //DTO → Entity 변환
    public SiteUser toEntity() {
        SiteUser siteUser = new SiteUser();
        siteUser.setUserId(this.userId);
        siteUser.setPhoneNumber(this.phoneNumber);
        // TODO: SHA1 또는SHA256으로 password를 해시 값으로 변환
        String hashedPassword = this.password;
        siteUser.setPassword(hashedPassword);
        return siteUser;
    }

}
