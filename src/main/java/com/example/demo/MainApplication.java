package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MainApplication {
	public static void main(String[] args) {
		System.out.println("=== Main started ===");
		SpringApplication.run(MainApplication.class, args);
	}
}
