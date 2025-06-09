package com.welab.backend_noti.event.consumer;

import com.welab.backend_noti.event.consumer.message.user.SiteUserInfoEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;

//Kafka에서 메시지를 받아 처리하는 Consumer
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessageConsumer {

    //@KafkaListener: Kafka 메시지를 구독(consume)하는 리스너 설정
    //userinfo라는 topic에 새 메시지가 도착하면 자동으로 실행
    @KafkaListener(
            topics= SiteUserInfoEvent.Topic, //수신한 topic
            properties = { //메시지를 어떤 클래스로 역직렬화할지 지정
                    JsonDeserializer.VALUE_DEFAULT_TYPE
                    + ":com.welab.backend_noti.event.consumer.message.user.SiteUserInfoEvent"
            })
    void handleSiteUserInfoEvent(SiteUserInfoEvent event, Acknowledgment ack) {
        //Kafka에서 받은 메시지의 userId 출력
        log.info("SiteUserInfoEvent 처리, userId= {}", event.getUserId());
        //Kafka에게 “나 이 메시지 잘 받았고 처리했어”라고 알림 (→ 수동 커밋)
        ack.acknowledge();
    }
}
