package org.pronet.app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "FlexBank App",
				description = "REST API for FlexBank App",
				version = "v1.0",
				contact = @Contact(
						name = "FlexBank GitHub Repo",
						url = "https://github.com/TahmazliSanan/Mini-Bank-App"
				)
		)
)
public class FlexBankApplication {
	public static void main(String[] args) {
		SpringApplication.run(FlexBankApplication.class, args);
	}
}
