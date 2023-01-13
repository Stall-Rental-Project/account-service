package com.srs.account.config;

import com.srs.common.smtp.SmtpProperties;
import com.srs.common.smtp.SmtpTemplateFactory;
import com.srs.common.smtp.SmtpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class SmtpConfig {
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("hoanggg2110@gmail.com");
        mailSender.setPassword("ulypdgvuguptufdj");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }


    @Bean
    public SmtpProperties smtpProperties(Environment environment) {
        return new SmtpProperties(environment);
    }

    @Bean
    public SmtpTemplateFactory smtpTemplateFactory() {
        return new SmtpTemplateFactory();
    }

    @Bean
    public SmtpUtil smtpUtil(SmtpProperties smtpProperties, SmtpTemplateFactory smtpTemplateFactory) {
        return new SmtpUtil(getJavaMailSender(),smtpProperties, smtpTemplateFactory);
    }
}
