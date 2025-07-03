package com.backend.FeedResume.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.FeedResume.entity.PasswordResetToken;
import com.backend.FeedResume.entity.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByOtp(String otp);
    void deleteAllByUser(User user);
}