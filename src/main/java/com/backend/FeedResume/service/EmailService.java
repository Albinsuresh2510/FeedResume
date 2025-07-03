package com.backend.FeedResume.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("🔐 Reset Your Password - FeedResume");
        message.setText("Here is your OTP: " + otp + "\n\nThis code will expire in 1 minutes.");

        mailSender.send(message);
        System.out.println("✅ OTP email sent to: " + toEmail);
    }
}
