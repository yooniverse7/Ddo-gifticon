package com.example.ddo_pay.restaurant.entity;

import com.example.ddo_pay.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserRestaurant  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_restaurant_id")
    private Long id;
    private Integer visitedCount; // 방문 횟수

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "userRestaurant", cascade = CascadeType.ALL)
    private List<CustomMenu> customMenuList = new ArrayList<>();


    @Builder
    public UserRestaurant(Long id,Integer visitedCount, User user, Restaurant restaurant) {
        this.id = id;
        this.visitedCount = visitedCount;
        this.user = user;
        this.restaurant = restaurant;
    }
}
