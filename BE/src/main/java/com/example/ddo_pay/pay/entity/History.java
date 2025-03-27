package com.example.ddo_pay.pay.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class History  {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long id;
    private String title; // 결제 내역 제목
    private LocalDateTime time; // 결제 시간
    private int inOutAmount; // 입출금 금액
    @Enumerated(EnumType.STRING)
    private AssetType type; // 잔고인지, 포인트인지
    @ManyToOne(fetch = FetchType.LAZY)
    private DdoPay ddoPay;
}
