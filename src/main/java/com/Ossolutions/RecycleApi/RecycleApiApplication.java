package com.Ossolutions.RecycleApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
@ComponentScan(basePackages = "com.Ossolutions.RecycleApi")
@EntityScan(basePackages = "com.Ossolutions.RecycleApi.Model")

public class RecycleApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(RecycleApiApplication.class, args);
	}
}

