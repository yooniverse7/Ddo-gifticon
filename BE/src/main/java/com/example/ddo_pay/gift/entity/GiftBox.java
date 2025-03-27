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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "giftBox")
    private Gift gift;

    public GiftBox(User user, Gift gift) {
        this.user = user;
        this.gift = gift;
    }
}
