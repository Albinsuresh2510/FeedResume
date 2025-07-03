package com.backend.FeedResume.controller;

import com.backend.FeedResume.config.JwtService;
import com.backend.FeedResume.dto.AuthRequest;
import com.backend.FeedResume.dto.AuthResponse;
import com.backend.FeedResume.dto.RefreshRequest;
import com.backend.FeedResume.dto.RegisterRequest;
import com.backend.FeedResume.dto.TokenResponse;
import com.backend.FeedResume.entity.User;
import com.backend.FeedResume.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private UserService userService;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtService jwtService;

    // 🚪 Login with form credentials
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userService.getUserByEmail(request.getEmail());
        String accessToken = jwtService.generateAccessToken(user.getEmail(), "form");
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken, user.getEmail(), user.getRole()));
    }

    // 📝 New user registration
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        User user = userService.register(request);

        String accessToken = jwtService.generateAccessToken(user.getEmail(), "form");
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken, user.getEmail(), user.getRole()));
    }

    // 🔁 Refresh token endpoint
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody RefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        if (jwtService.isRefreshTokenValid(refreshToken)) {
            String username = jwtService.extractUsername(refreshToken);
            String provider = jwtService.extractAuthProvider(refreshToken) != null
                    ? jwtService.extractAuthProvider(refreshToken)
                    : "form";

            String newAccessToken = jwtService.generateAccessToken(username, provider);
            return ResponseEntity.ok(new TokenResponse(newAccessToken, refreshToken));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 🔒 Simple protected endpoint to test token
    @GetMapping("/user")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("🧪 Auth Test Successful!");
    }
}
