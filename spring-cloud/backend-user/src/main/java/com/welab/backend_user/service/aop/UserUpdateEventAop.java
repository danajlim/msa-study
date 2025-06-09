package com.welab.backend_user.service.aop;

import com.welab.backend_user.common.type.ActionAndId;
import com.welab.backend_user.domain.SiteUser;
import com.welab.backend_user.domain.event.SiteUserInfoEvent;
import com.welab.backend_user.domain.repository.SiteUserRepository;
import com.welab.backend_user.event.producer.KafkaMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

//SiteUserService에서 AndNotify로 끝나는 메서드가 실행된 후
//자동으로 Kafka 메세지를 발행함
@Slf4j
@Aspect //Advice + Pointcut -> AOP 기능 수행
@Component
@RequiredArgsConstructor
public class UserUpdateEventAop {

    private final SiteUserRepository siteUserRepository;
    private final KafkaMessageProducer kafkaMessageProducer;

    //AOP가 적용되는 메서드 타겟 지정함. AndNotify로 끝나는 모든 메서드가 타겟
    //리턴값은 ActionAndId여야함
    @AfterReturning(
            value = "execution(* com.welab.backend_user.remote.noti.service.SiteUserService.*AndNotify(..))",
            returning = "actionAndId"
    )

    //실제로 AOP 실행 시 호출되는 부분
    public void publishUserUpdateEvent(JoinPoint joinPoint, ActionAndId actionAndId) {
        publishUserUpdateEvent(actionAndId);
    }

    //Kafka 메시지 발행 로직
    public void publishUserUpdateEvent(ActionAndId actionAndId){
        try {
            //1. id로 SiteUser 다시 조회
            SiteUser siteUser = siteUserRepository.findById(actionAndId.getId())
                    .orElse(null);
            //2. 없으면 종료 (예외 방지)
            if (siteUser == null) {
                return;
            }
            //3. Kafka 이벤트 객체 생성
            SiteUserInfoEvent event = SiteUserInfoEvent.fromEntity(actionAndId.getAction(), siteUser);
            //4. Kafka 전송
            kafkaMessageProducer.send(SiteUserInfoEvent.Topic, event);
        } catch (Exception e) {
            log.warn("사용자 정보 업데이트 이벤트 전송 못함, id = {}",
                    actionAndId.getId());
        }
    }

}
