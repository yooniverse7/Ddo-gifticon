package com.example.ddo_pay.gift.Repository;

import com.example.ddo_pay.gift.entity.Gift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GiftRepository extends JpaRepository<Gift, Integer> {


    @Query("SELECT g FROM Gift g WHERE g.user.id = :userId")
    public List<Gift> selectMyList(Long userId);
}
