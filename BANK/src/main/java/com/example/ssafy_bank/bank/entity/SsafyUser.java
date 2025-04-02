package com.example.ssafy_bank.bank.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ssafyuser")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsafyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "account_num", nullable = false, length = 50)
    private String accountNum;

    @Column(name = "user_key", nullable = false, length = 100)
    private String userKey;

    @Column(name = "email", nullable = false, length = 100)
    private String email;
}
