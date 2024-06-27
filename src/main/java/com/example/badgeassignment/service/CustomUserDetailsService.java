package com.example.badgeassignment.service;

import com.example.badgeassignment.model.UserCommon;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final RestTemplate restTemplate;

    public CustomUserDetailsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String userInfoEndpoint = "http://localhost:7002/acessa-info?usuario=" + username; // Ajuste a URL conforme necessário
        UserCommon user = restTemplate.getForObject(userInfoEndpoint, UserCommon.class);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), "", Collections.emptyList());
    }
}
