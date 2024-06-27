package com.example.badgeassignment.service;

// UserService.java

import com.example.badgeassignment.model.UserCommon;
import com.example.badgeassignment.model.UserEnterprise;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    private final RestTemplate restTemplate = new RestTemplate();

    public UserCommon getUserCommon(String email) {
        String url = "http://localhost:6002/acessa-info?email=" + email;
        return restTemplate.getForObject(url, UserCommon.class);
    }

    public UserEnterprise getUserEnterprise(String email) {
        String url = "http://localhost:6003/consultar?email=" + email;
        return restTemplate.getForObject(url, UserEnterprise.class);
    }
}