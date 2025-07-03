package com.backend.FeedResume.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users") // Avoid clash with SQL keyword 'user'
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = true) // or just remove "nullable = false"
    private String password;

    @Transient
    private String confirmPassword;
    private String authProvider;
    @Column(nullable = false)
    private String role; // 🔐

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore // 💥 THIS IS IMPORTANT
    private List<PasswordResetToken> resetTokens = new ArrayList<>();

}
