package com.welab.backend_user.event.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

//메세지를 보내는 역할(Publisher)
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessageProducer {

    //KafkaTemplate: Kafka로 메세지를 보낼 수 있게 해주는 클래스
    private final KafkaTemplate<String,Object> kafkaTemplate;

    //topic: Kafka에 보낼 주제 이름, message: 실제 전송할 메세지 내용 (보통 DTO)
    public void send(String topic, Object message) {
        kafkaTemplate.send(topic, message);
    }
}
