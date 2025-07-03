package com.backend.FeedResume.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String otp;

    private LocalDateTime expiryDate;

    // 🔙 Many tokens belong to one user
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference // or @JsonIgnore if you don’t need to send it
    private User user;

}
