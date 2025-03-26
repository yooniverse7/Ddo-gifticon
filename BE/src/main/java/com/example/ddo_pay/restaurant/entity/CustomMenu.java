package com.example.ddo_pay.restaurant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class CustomMenu {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "custom_menu_id")
    private Long id;

    private String customMenuName; // 메뉴명
    private Integer customMenuPrice; // 메뉴 가격
    private String customMenuImage; // 메뉴 사진

    /**
     * FK: res_list_id (UNSIGNED INT, NOT NULL)
     * 실제로는 user_restaurant 테이블의 PK와 연결
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_restaurant_id")
    private UserRestaurant userRestaurant;
}
