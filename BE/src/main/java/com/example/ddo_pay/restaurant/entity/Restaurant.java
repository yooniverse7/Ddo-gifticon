package com.example.ddo_pay.restaurant.entity;

import com.example.ddo_pay.gift.entity.Gift;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Restaurant {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    private String resName; // 가게명
    private String resAddress; // 가게 주소
    private Double resLat; // 가게 위도
    private Double resLng; // 가게 경도
    private String resImage; // 가게 이미지
    private String userIntro; // 나만의 소개

    @Column(precision = 2, scale = 1)
    private BigDecimal starRating; // 별점


    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Menu> menuList = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<UserRestaurant> userRestaurantList = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Gift> giftList = new ArrayList<>();
}
