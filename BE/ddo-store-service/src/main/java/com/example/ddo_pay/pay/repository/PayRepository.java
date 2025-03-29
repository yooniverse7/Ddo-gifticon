package com.example.ddo_pay.pay.repository;

import com.example.ddo_pay.pay.entity.DdoPay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayRepository extends JpaRepository<DdoPay, Long> {
}
