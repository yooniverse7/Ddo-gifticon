package com.example.ddo_pay.gift.entity;

import com.example.ddo_pay.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class GiftBox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "giftbox_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gift_id", unique = true)
    private Gift gift;

    public GiftBox(User user, Gift gift) {
        this.user = user;
        this.gift = gift;
    }
    public GiftBox(User user) {
        this.user = user;
    }

    public void changeGift(Gift gift) {
        this.gift = gift;
        gift.changeGiftBox(this);
    }
}
