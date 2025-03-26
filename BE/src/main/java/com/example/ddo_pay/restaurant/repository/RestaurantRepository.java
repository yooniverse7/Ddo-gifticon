package com.example.ddo_pay.restaurant.repository;

import com.example.ddo_pay.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
