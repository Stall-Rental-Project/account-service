package com.srs.account.kafka.service.impl;

import com.srs.account.kafka.service.EmailKafkaService;
import com.srs.account.repository.UserDslRepository;
import com.srs.account.util.smtp.ApplicationSmtpUtil;
import com.srs.common.kafka.message.AppStatusUpdatedEmailKafkaMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailKafkaServiceImpl implements EmailKafkaService {
    private final UserDslRepository userDslRepository;
    private final ApplicationSmtpUtil applicationsmtpUtil;

    @Override
    public void sendApplicationStatusUpdatedEmail(AppStatusUpdatedEmailKafkaMessage message) throws MessagingException {
        applicationsmtpUtil.sendApplicationStatusUpdatedEmail(message);
    }

}
