package com.example.ddo_pay.gift.repository;

import com.example.ddo_pay.gift.entity.Gift;
import com.example.ddo_pay.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GiftRepository extends JpaRepository<Gift, Integer> {
    List<Gift> findByUser(User user);
}
