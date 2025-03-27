package com.example.ddo_pay.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Followee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "followee_id")
    private Long id;

    private String phoneNum; // 팔로우 당한 사람의 전화번호
    private Long followeeUserId; // 팔로우 당한 사람의 사용자 고유 번호 (fk로 연결되지 않음)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
