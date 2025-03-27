package com.example.ddo_pay.restaurant.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ddo_pay.restaurant.entity.UserRestaurant;

public interface UserRestaurantRepository extends JpaRepository<UserRestaurant, Long> {
	// "user" 필드의 "id" + "restaurant" 필드의 "id"를 이용해 검색
	Optional<UserRestaurant> findByUser_IdAndRestaurant_Id(Long userId, Long restaurantId);
	List<UserRestaurant> findByUser_Id(Long userId);

}
