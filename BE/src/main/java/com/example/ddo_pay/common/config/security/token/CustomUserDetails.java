package com.example.ddo_pay.common.config.security.token;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserDetails extends User {
    private final Long userId;

    public CustomUserDetails(Long userId) {
        super("testUser", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
