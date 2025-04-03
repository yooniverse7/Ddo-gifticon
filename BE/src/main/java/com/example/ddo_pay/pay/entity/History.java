package com.example.ddo_pay.pay.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
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

    @Builder
    public History(String title, LocalDateTime time, int inOutAmount, AssetType type, DdoPay ddoPay) {
        this.title = title;
        this.time = time;
        this.inOutAmount = inOutAmount;
        this.type = type;
        this.ddoPay = ddoPay;
    }

}
