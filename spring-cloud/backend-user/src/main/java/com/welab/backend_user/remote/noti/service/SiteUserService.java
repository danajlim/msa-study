package com.welab.backend_user.remote.noti.service;

import com.welab.backend_user.domain.SiteUser;
import com.welab.backend_user.domain.dto.SiteUserRegisterDto;
import com.welab.backend_user.domain.repository.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SiteUserService {

    private final SiteUserRepository siteUserRepository;

    //저장 로직 처리
    @Transactional
    public void registerUser(SiteUserRegisterDto registerDto) {
        SiteUser siteUser = registerDto.toEntity();

        siteUserRepository.save(siteUser);
    }
}
