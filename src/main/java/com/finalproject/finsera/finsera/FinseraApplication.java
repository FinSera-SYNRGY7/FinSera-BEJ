package com.finalproject.finsera.finsera;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@OpenAPIDefinition(info = @Info(title = "Finsera API", version = "1.0.0", description = "Finsera Api Docs Information"))
public class FinseraApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinseraApplication.class, args);
	}

}
