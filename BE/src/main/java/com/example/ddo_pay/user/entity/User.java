package com.example.ddo_pay.user.entity;

import com.example.ddo_pay.gift.entity.Gift;
import com.example.ddo_pay.gift.entity.GiftBox;
import com.example.ddo_pay.pay.entity.DdoPay;
import com.example.ddo_pay.restaurant.entity.UserRestaurant;
import com.example.ddo_pay.user.dto.UserDto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String loginId; // 로그인 ID
    private String password; // 로그인 PW
    private String name; // 이름
    private String email; // 이메일 주소
    private String phoneNum; // 전화번호
    private LocalDateTime birthday; // 생일
    private String refreshToken; // 리프레시 토큰

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Agreement agreement; // 동의항목과 1 대 1

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Gift> giftList = new ArrayList<>(); // 기프티콘과 1 대 다

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<GiftBox> giftBoxList = new ArrayList<>(); // 받은 기프티콘과 1 대 다

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Followee> followeeList = new ArrayList<>(); // 팔로위와 1 대 다

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Alarm> alarmList = new ArrayList<>(); // 알림과 1 대 다

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserRestaurant> userRestaurantList = new ArrayList<>(); // 맛집 리스트와 1 대 다

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private DdoPay ddoPay; // 또페이와 1 대 1

    public void changePrivateInfo(UserDto userDto) {
        if (userDto.getName() != null) {
            this.name = userDto.getName();
        }
        if (userDto.getEmail() != null) {
            this.email = userDto.getEmail();
        }
        if (userDto.getPhoneNum() != null) {
            this.phoneNum = userDto.getPhoneNum();
        }
        if (userDto.getBirth() != null && !userDto.getBirth().isEmpty()) {
            this.birthday = LocalDateTime.parse(userDto.getBirth() + "T00:00:00",
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
    }

}
