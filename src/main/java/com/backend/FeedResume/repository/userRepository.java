package com.backend.FeedResume.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.backend.FeedResume.entity.User;
@Service
public interface userRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}