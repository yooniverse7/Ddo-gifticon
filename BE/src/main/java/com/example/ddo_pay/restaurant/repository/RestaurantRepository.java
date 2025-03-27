package com.example.ddo_pay.restaurant.repository;

import java.util.Optional;

import com.example.ddo_pay.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
	Optional<Restaurant> findByPlaceNameAndAddressName(String placeName, String addressName);
}
