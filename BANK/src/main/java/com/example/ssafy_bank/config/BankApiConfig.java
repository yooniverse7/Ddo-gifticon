package com.example.ssafy_bank.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

// apiKey 관리
@Configuration
@Data
@ConfigurationProperties(prefix = "bank")
public class BankApiConfig {
    private String apiKey;

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}