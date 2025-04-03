package com.example.ddo_pay.user.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ddo_pay.user.entity.User;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);


}
