package com.scheduler.scheduler.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    
    private JavaMailSender mailSender;

    public void sendEmail(String email, String resetLink) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setSubject("Reset your password");
        mail.setText("Click on the following link to reset your password: " + resetLink);
        mailSender.send(mail);
    }

}
