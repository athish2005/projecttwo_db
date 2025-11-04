package com.example.projecttwo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendEmail(String to, String subject, String message) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(from);
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(message);
            mailSender.send(mail);
            System.out.println("✅ Email sent successfully to " + to);
        } catch (Exception e) {
            System.err.println("❌ Email sending failed: " + e.getMessage());
        }
    }
}
