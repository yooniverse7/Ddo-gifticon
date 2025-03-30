package com.example.ddo_pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.ddo_pay.feign")
public class DdoStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(DdoStoreApplication.class, args);
	}
}
