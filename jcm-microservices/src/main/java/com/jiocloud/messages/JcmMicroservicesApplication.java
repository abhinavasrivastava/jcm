package com.jiocloud.messages;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class JcmMicroservicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(JcmMicroservicesApplication.class, args);
	}
}
