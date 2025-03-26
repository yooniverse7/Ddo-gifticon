package com.example.ddo_pay.gift.entity;

import com.example.ddo_pay.gift.dto.update.GiftUpdateRequestDto;
import com.example.ddo_pay.restaurant.entity.Restaurant;
import com.example.ddo_pay.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Gift {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gift_id")
    private Long id;

    private String title; // 이름
    private Integer amount; // 금액
    private String phoneNum; // B 전화번호
    private String message; // 메세지 내용
    private String image; // 사진
    private LocalDateTime expirationDate; // 유효기간
    private String menuCombination;
    @Enumerated(EnumType.STRING)
    private USED usedStatus; // 사용 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 사용자와 다 대 1

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giftbox_id")
    private GiftBox giftBox; // 받은 기프티콘과 1 대 1

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant; // 맛집과 다 대 1

    @Builder
    public Gift(Long id, String title, Integer amount, String phoneNum, String message, String image, LocalDateTime expirationDate, USED usedStatus, String menuCombination, User user, GiftBox giftBox, Restaurant restaurant) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.phoneNum = phoneNum;
        this.message = message;
        this.image = image;
        this.expirationDate = expirationDate;
        this.usedStatus = usedStatus;
        this.menuCombination = menuCombination;
        this.user = user;
        this.giftBox = giftBox;
        this.restaurant = restaurant;
    }

    public void changePhoneNum(GiftUpdateRequestDto dto) {
        this.phoneNum = dto.getPhoneNum();
    }

    public void setGiftBox(GiftBox giftBox) {
        this.giftBox = giftBox;
    }
}

