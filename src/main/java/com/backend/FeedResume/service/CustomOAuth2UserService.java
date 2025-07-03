package com.backend.FeedResume.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.backend.FeedResume.entity.User;
import com.backend.FeedResume.repository.userRepository;
@Configuration
public class CustomOAuth2UserService  implements OAuth2UserService<OAuth2UserRequest, OAuth2User>  {
   @Autowired
    private userRepository userRepository;
@Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId(); // google / github
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(name);
            newUser.setRole("ROLE_USER");
            newUser.setAuthProvider(provider);
            return userRepository.save(newUser);
        });

        return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority(user.getRole())),
            oAuth2User.getAttributes(),
            "email"
        );
    }
}