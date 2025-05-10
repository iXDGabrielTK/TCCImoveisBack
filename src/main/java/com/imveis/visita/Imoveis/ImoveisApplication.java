package com.imveis.visita.Imoveis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
public class ImoveisApplication {

	public static void main(String[] args) {


		// Inicia a aplicação normalmente (spring-dotenv já cuida do .env)
		ConfigurableApplicationContext context = SpringApplication.run(ImoveisApplication.class, args);

		// Você pode acessar os valores do .env diretamente via Spring Environment
		ConfigurableEnvironment env = context.getEnvironment();
		System.out.println("🔐 JWT_SECRET via Spring Environment: " + env.getProperty("JWT_SECRET"));
		System.out.println("🌐 SERVER_PORT via Spring Environment: " + env.getProperty("SERVER_PORT"));

		// ⚠️ Geração temporária de hash de senha
		/*
		PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
		String rawPassword = "gabrieltk12";
		String hashedPassword = encoder.encode(rawPassword);
		System.out.println("🔑 Hashed password: " + hashedPassword);
		*/
	}
}
