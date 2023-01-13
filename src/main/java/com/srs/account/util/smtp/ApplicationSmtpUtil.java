package com.srs.account.util.smtp;

import com.srs.common.kafka.message.AppStatusUpdatedEmailKafkaMessage;
import com.srs.common.smtp.SmtpProperties;
import com.srs.common.smtp.SmtpTemplate;
import com.srs.common.smtp.SmtpUtil;
import com.srs.rental.WorkflowStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;


@Component
@RequiredArgsConstructor
@Log4j2
public class ApplicationSmtpUtil {

    private final SmtpUtil smtpUtil;
    private final SmtpProperties sesProperties;


    public void sendApplicationStatusUpdatedEmail(AppStatusUpdatedEmailKafkaMessage message) throws MessagingException {
        String subject;
        switch (message.getApplicationTypeNumber()) {
            default:
                subject = String.format("[MDAD] Your Application %s has been set to %s",
                        message.getApplicationNumber(), message.getApplicationStatus());
                break;
        }
        String content;
        if (message.getApplicationStatusNumber() == WorkflowStatus.DISAPPROVED_VALUE) {
            content = smtpUtil.buildEmailBody(message, SmtpTemplate.APP_APPROVED);
        } else if (message.getApplicationStatusNumber() == WorkflowStatus.APPROVED_VALUE) {
            content = smtpUtil.buildEmailBody(message, SmtpTemplate.APP_APPROVED);
        } else {
            content = smtpUtil.buildEmailBody(message, SmtpTemplate.APP_APPROVED);
        }
        smtpUtil.sendEmail(message.getToEmail(), message.getCcEmails(),
                sesProperties.getDefaultCcEmails(), subject, content);

    }


}
