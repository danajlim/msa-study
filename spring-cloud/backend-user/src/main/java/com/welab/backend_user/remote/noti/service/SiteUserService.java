package com.welab.backend_user.remote.noti.service;

import com.welab.backend_user.common.exception.BadParameter;
import com.welab.backend_user.common.exception.NotFound;
import com.welab.backend_user.domain.SiteUser;
import com.welab.backend_user.domain.dto.SiteUserLoginDto;
import com.welab.backend_user.domain.dto.SiteUserRegisterDto;
import com.welab.backend_user.domain.repository.SiteUserRepository;
import com.welab.backend_user.secret.hash.SecureHashUtils;
import com.welab.backend_user.secret.jwt.TokenGenerator;
import com.welab.backend_user.secret.jwt.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SiteUserService {

    private final SiteUserRepository siteUserRepository;
    private final TokenGenerator tokenGenerator;

    //저장 로직 처리
    @Transactional
    public void registerUser(SiteUserRegisterDto registerDto) {
        SiteUser siteUser = registerDto.toEntity();

        siteUserRepository.save(siteUser);
    }

    @Transactional(readOnly = true)
    public TokenDto.AccessRefreshToken login(SiteUserLoginDto loginDto) {

        //입력받은 userId에 해당하는 사용자를 DB에서 조회
        SiteUser user = siteUserRepository.findByUserId(loginDto.getUserId());

        //사용자 없을때
        if (user == null) {
            throw new NotFound("사용자를 찾을 수 없습니다.");
        }

        //비밀번호 검증 - 사용자가 입력한 비밀번호를 해시해서 db에 저장된 값과 비교
        if (!SecureHashUtils.matches(loginDto.getPassword(), user.getPassword())) {
            throw new BadParameter("비밀번호가 맞지 않습니다.");
        }

        //로그인 성공시 jwt access+refresh 토큰 생성
        return tokenGenerator.generateAccessRefreshToken(loginDto.getUserId(), "WEB");

    }
}
