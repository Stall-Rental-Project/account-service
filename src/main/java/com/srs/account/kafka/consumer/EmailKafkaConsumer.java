package com.srs.account.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.srs.account.kafka.service.EmailKafkaService;
import com.srs.common.kafka.KafkaTopic;
import com.srs.common.kafka.message.AppStatusUpdatedEmailKafkaMessage;
import com.srs.common.kafka.message.core.email.EmailDelegatedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = {
        KafkaTopic.EMAIL_FLOW
}, containerFactory = "CustomKafkaListenerContainerFactory")
@RequiredArgsConstructor
@Log4j2
public class EmailKafkaConsumer {
    private final ObjectMapper objectMapper;

    private final EmailKafkaService emailKafkaService;

    @KafkaHandler
    public void doSendEmail(@Payload AppStatusUpdatedEmailKafkaMessage message) {
        if (!EmailDelegatedService.ACCOUNT.equals(message.getDelegateTo())) {
            return;
        }
        try {
            emailKafkaService.sendApplicationStatusUpdatedEmail(message);
        } catch (Exception e) {
            log.error("Failed to send email out to user after application status got updated. {} - {}", e.getClass().getSimpleName(), e.getMessage());
            e.printStackTrace();
        }
    }

    @KafkaHandler(isDefault = true)
    public void unrecognizedMessage(Object unrecognizedMessage) {
        try {
            log.warn("Unrecognized message found with type {} and content {}",
                    unrecognizedMessage.getClass().getSimpleName(),
                    objectMapper.writeValueAsString(unrecognizedMessage));
        } catch (JsonProcessingException e) {
            log.error("Error when write message to string. {}", e.getMessage());
            log.warn("Unrecognized malformed message found with type {}",
                    unrecognizedMessage.getClass().getSimpleName());
        }
    }
}
