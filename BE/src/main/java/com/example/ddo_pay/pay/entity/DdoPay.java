package com.example.ddo_pay.pay.entity;

import com.example.ddo_pay.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class DdoPay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ddopay_id")
    private Long id;
    private int balance; // 페이 잔고
    private int point; // 페이 포인트
    private String payPassword; // 결제 비밀번호

    @OneToMany(mappedBy = "ddoPay", cascade = CascadeType.ALL)
    private List<Account> accountList = new ArrayList<>();
    @OneToMany(mappedBy = "ddoPay", cascade = CascadeType.ALL)
    private List<History> historyList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
