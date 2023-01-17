package com.srs.account.config;

import com.srs.common.smtp.SmtpProperties;
import com.srs.common.smtp.SmtpTemplateFactory;
import com.srs.common.smtp.SmtpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class SmtpConfig {
    @Bean
    public JavaMailSender getJavaMailSender() {
        return new JavaMailSenderImpl();
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
        return new SmtpUtil(getJavaMailSender(), smtpProperties, smtpTemplateFactory);
    }
}
