package com.welab.backend_user.remote.noti.service;

import com.welab.backend_user.common.exception.BadParameter;
import com.welab.backend_user.common.exception.NotFound;
import com.welab.backend_user.domain.SiteUser;
import com.welab.backend_user.domain.dto.SiteUserInfoDto;
import com.welab.backend_user.domain.dto.SiteUserLoginDto;
import com.welab.backend_user.domain.dto.SiteUserRefreshDto;
import com.welab.backend_user.domain.dto.SiteUserRegisterDto;
import com.welab.backend_user.domain.event.SiteUserInfoEvent;
import com.welab.backend_user.domain.repository.SiteUserRepository;
import com.welab.backend_user.event.producer.KafkaMessageProducer;
import com.welab.backend_user.remote.noti.RemoteNotiService;
import com.welab.backend_user.remote.noti.dto.SendSmsDto;
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
    private final RemoteNotiService remoteNotiService;
    private final KafkaMessageProducer kafkaMessageProducer;

    //회원가입 로직
    @Transactional
    public void registerUser(SiteUserRegisterDto registerDto) {
        SiteUser siteUser = registerDto.toEntity();
        siteUserRepository.save(siteUser);

        //kafka 메세지 생성하고 발행
        SiteUserInfoEvent message = SiteUserInfoEvent.fromEntity("Create", siteUser);
        kafkaMessageProducer.send(SiteUserInfoEvent.Topic,message);
    }

    //로그인 로직
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

    //리프레시 토큰을 이용해 새로운 액세스 토큰을 발급
    //SiteUserRefreshDto - 클라이언트가 보낸 리프레시 토큰
    public TokenDto.AccessToken refresh(SiteUserRefreshDto refreshDto) {

        //리프레시 토큰에서 유저 ID를 추출
        String userId = tokenGenerator.validateJwtToken(refreshDto.getToken());
        if (userId == null) {
            throw new BadParameter("토큰이 유효하지 않습니다");
        }

        //해당 유저가 실제 DB에 존재하는지 확인
        SiteUser user = siteUserRepository.findByUserId(userId);
        if (user == null) {
            throw new NotFound("사용자를 찾을 수 없습니다");
        }

        //새로운 Access Token 발급
        return tokenGenerator.generateAccessToken(userId, "WEB");
    }

    //사용자 정보 조회 후 SiteUserInfoDto로 변환
    @Transactional(readOnly = true)
    public SiteUserInfoDto userInfo(String userId){

        //userId로 사용자 조회
        SiteUser user = siteUserRepository.findByUserId(userId);
        if (user == null) {
            throw new NotFound("사용자를 찾을 수 없습니다");
        }

        //객체를 DTO로 변환하여 반환
        return SiteUserInfoDto.fromEntity(user);
    }


}
