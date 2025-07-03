package com.backend.FeedResume.service;

import com.backend.FeedResume.entity.PasswordResetToken;
import com.backend.FeedResume.entity.User;
import com.backend.FeedResume.exception.OtpExpiredException;
import com.backend.FeedResume.repository.PasswordResetTokenRepository;
import com.backend.FeedResume.repository.userRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class ForgotPasswordService {

    @Autowired
    private userRepository userRepo;
    @Autowired
    private PasswordResetTokenRepository tokenRepo;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void sendOtp(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        tokenRepo.deleteAllByUser(user);

        String otp = String.valueOf(new Random().nextInt(899999) + 100000);
        PasswordResetToken token = PasswordResetToken.builder()
                .otp(otp)
                .expiryDate(LocalDateTime.now().plusMinutes(1))
                .user(user)
                .build();

        tokenRepo.save(token);
        emailService.sendOtpEmail(email, otp);
    }

    @Transactional
    public void resetPassword(String otp, String newPassword) {

        PasswordResetToken token = tokenRepo.findByOtp(otp)
                .orElseThrow(() -> new RuntimeException("Invalid OTP"));

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new OtpExpiredException("OTP expired");
        }

        User user = token.getUser();

        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        tokenRepo.deleteAllByUser(user);
    }

}
