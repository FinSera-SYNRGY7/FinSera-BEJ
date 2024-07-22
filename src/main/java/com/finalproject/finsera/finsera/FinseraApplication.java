package com.finalproject.finsera.finsera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FinseraApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinseraApplication.class, args);
	}

}
