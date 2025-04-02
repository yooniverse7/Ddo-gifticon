package com.example.ssafy_bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.example.ssafy_bank.bank.entity")
@EnableJpaRepositories("com.example.ssafy_bank.bank.repository")
public class SsafyBankApplication {

    public static void main(String[] args) {

        SpringApplication.run(SsafyBankApplication.class, args);
    }

}
