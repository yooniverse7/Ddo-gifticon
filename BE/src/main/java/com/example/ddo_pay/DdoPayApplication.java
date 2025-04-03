package com.example.ddo_pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = {"com.example.ddo_pay.client"})
@SpringBootApplication
public class DdoPayApplication {

	public static void main(String[] args) {
		SpringApplication.run(DdoPayApplication.class, args);
	}
}
