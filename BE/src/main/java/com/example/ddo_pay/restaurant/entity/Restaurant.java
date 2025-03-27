package com.example.ddo_pay.restaurant.entity;

import com.example.ddo_pay.gift.entity.Gift;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    private String placeName; // 가게명
    private String addressName; // 가게 주소
    private Double lat; // 가게 위도
    private Double lng; // 가게 경도
    private String mainImageUrl; // 가게 이미지
    private String userIntro; // 나만의 소개

    @Column(name = "star_rating", precision = 3, scale = 2)
    private BigDecimal starRating; // 별점

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Menu> menuList = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<UserRestaurant> userRestaurantList = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Gift> giftList = new ArrayList<>();

    // 클래스 레벨에서 @Builder를 쓴다면, 아래 생성자를 직접 작성하지 않아도 되지만
    // 필요 시 DTO <-> Entity 변환 시 편의를 위해 오버로딩해서 사용할 수 있습니다.
    @Builder
    public Restaurant(String placeName,
                      String addressName,
                      Double lat,
                      Double lng,
                      String mainImageUrl,
                      String userIntro,
                      BigDecimal starRating,
                      List<Menu> menuList) {
        this.placeName = placeName;
        this.addressName = addressName;
        this.lat = lat;
        this.lng = lng;
        this.mainImageUrl = mainImageUrl;
        this.userIntro = userIntro;
        this.starRating = starRating;
        this.menuList = menuList;
    }
}
