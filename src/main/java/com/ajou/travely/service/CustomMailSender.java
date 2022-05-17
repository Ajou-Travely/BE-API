package com.ajou.travely.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomMailSender {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String mainEmail;

    public void sendInvitationEmail(String email, String text) {
        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(email);
        emailMessage.setSubject("Travely로 초대합니다.");
        emailMessage.setText(text);

        sendEmail(emailMessage);
    }

    @Async
    void sendEmail(SimpleMailMessage emailMessage) {
        javaMailSender.send(emailMessage);
    }
}
