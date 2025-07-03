package com.backend.FeedResume.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  // Required for Spring to deserialize JSON
@AllArgsConstructor // Optional, but helps if you want to use constructors
public class ResetPasswordRequest {

    @NotBlank(message = "OTP must not be blank")
    private String otp;

    @NotBlank(message = "Password cannot be blank")
    private String password; // ✅ camelCase like a true Java dev
}
