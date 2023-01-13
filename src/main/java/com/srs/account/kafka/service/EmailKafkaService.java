package com.srs.account.kafka.service;

import com.srs.common.kafka.message.AppStatusUpdatedEmailKafkaMessage;

import javax.mail.MessagingException;


public interface EmailKafkaService {
    void sendApplicationStatusUpdatedEmail(AppStatusUpdatedEmailKafkaMessage message) throws MessagingException;

}
