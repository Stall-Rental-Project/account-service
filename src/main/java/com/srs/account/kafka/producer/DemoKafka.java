package com.srs.account.kafka.producer;

import com.srs.common.kafka.BaseKafkaProducer;
import com.srs.common.kafka.KafkaTopic;
import com.srs.common.kafka.message.market.DemoKafkaMessage;
import com.srs.proto.dto.GrpcPrincipal;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class DemoKafka extends BaseKafkaProducer {
    public DemoKafka(KafkaTemplate<String, Object> kafkaTemplate) {
        super(kafkaTemplate);
    }

    public void sendMessageWhenCallUserApi(DemoKafkaMessage message, GrpcPrincipal principal) {
        this.sendThenLogResult(KafkaTopic.KAFKA_DEMO, message, log);
    }

}
