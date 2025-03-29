package com.example.ddo_pay.pay.repository;

import com.example.ddo_pay.pay.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // 필요하면 여기에 추가 메서드 작성
}
