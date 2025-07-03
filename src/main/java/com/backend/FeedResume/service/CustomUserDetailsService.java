package com.backend.FeedResume.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.backend.FeedResume.entity.User;
import com.backend.FeedResume.entity.UserPrincipal;
import com.backend.FeedResume.repository.userRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{
    @Autowired
    private userRepository userRepository;
    
     @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(username)
            .orElseThrow(() -> {
                System.out.println("User not found: " + username);
                return new UsernameNotFoundException("User not found: " + username);
            });
    return new UserPrincipal(user); // Assuming UserPrincipal implements UserDetails
    
}
}
