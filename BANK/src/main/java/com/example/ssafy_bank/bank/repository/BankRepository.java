package com.example.ssafy_bank.bank.repository;

import com.example.ssafy_bank.bank.entity.SsafyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<SsafyUser, Long> {
    Optional<SsafyUser> findByEmail(String email);

    Optional<SsafyUser> findByAccountNum(String accountNum);
}
