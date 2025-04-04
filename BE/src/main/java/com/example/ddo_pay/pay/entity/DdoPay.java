package com.example.ddo_pay.pay.entity;

import com.example.ddo_pay.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Builder
    public DdoPay(Long id, int balance, int point, String payPassword, User user) {
        this.id = id;
        this.balance = balance;
        this.point = point;
        this.payPassword = payPassword;
        this.user = user;
    }

    public void setUser(User user) {
    }

    public void decreaseBalance(int amount) {
        this.balance -= amount;
    }

    public void increaseBalance(int amount) {
        this.balance = (int)(this.balance + (amount * 0.9f));
    }

    public void plueBalance(int amount) {
        this.balance = balance + amount;
    }


    // 결제가 가능하다면, true 아니면 false
    public boolean isAvailableToPay(int amount) {
        return this.balance >= amount;
    }

    // 기프티콘 결제 비밀번호 확인
    public boolean checkPassword(String password) {
        return this.payPassword.equals(password);
    }




}
