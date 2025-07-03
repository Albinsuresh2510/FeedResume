package com.backend.FeedResume.controller;

import com.backend.FeedResume.dto.EmailRequest;
import com.backend.FeedResume.dto.ResetPasswordRequest;
import com.backend.FeedResume.service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class ForgotPasswordController {

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody EmailRequest request) {
        forgotPasswordService.sendOtp(request.getEmail());
        return "OTP sent to email";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequest request) {
        forgotPasswordService.resetPassword(request.getOtp(), request.getPassword());
        return "Password reset successful";
    }
}
