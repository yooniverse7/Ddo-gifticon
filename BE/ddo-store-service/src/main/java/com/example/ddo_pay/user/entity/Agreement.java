package com.example.ddo_pay.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Agreement {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agreement_id")
    private Long id;

    private Boolean pushAlarm; // 푸시 알림
    private Boolean marketing; // 마케팅 동의
    private Boolean personalInfo; // 개인정보 동의

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
