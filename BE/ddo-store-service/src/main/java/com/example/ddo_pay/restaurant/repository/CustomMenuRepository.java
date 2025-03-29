package com.example.ddo_pay.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ddo_pay.restaurant.entity.CustomMenu;

public interface CustomMenuRepository extends JpaRepository<CustomMenu, Long> {
}
