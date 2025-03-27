package com.example.ddo_pay.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ddo_pay.restaurant.entity.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
}
