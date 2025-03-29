package com.example.ddo_pay.pay.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountNum; // 계좌 번호

    @ManyToOne(fetch = FetchType.LAZY)
    private DdoPay ddoPay;

    @Builder
    public Account(Long id, String accountNum, DdoPay ddoPay) {
        this.id = id;
        this.accountNum = accountNum;
        this.ddoPay = ddoPay;
    }
}
