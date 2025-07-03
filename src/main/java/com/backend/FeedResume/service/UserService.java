package com.backend.FeedResume.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.FeedResume.dto.AuthRequest;
import com.backend.FeedResume.dto.RegisterRequest;
import com.backend.FeedResume.dto.TokenResponse;
import com.backend.FeedResume.entity.User;

import com.backend.FeedResume.repository.userRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class UserService {

    private final userRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(RegisterRequest request) {
       if (!request.getPassword().equals(request.getConfirmPassword())) {
    throw new RuntimeException("Passwords do not match");
}
        User user = User.builder()
    .email(request.getEmail())
    .username(request.getUsername())
    .password(passwordEncoder.encode(request.getPassword()))
    .role(request.getRole())
    .build();


        return userRepository.save(user);
    }
    public User getUserByEmail(String email) {
    return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
}


}
