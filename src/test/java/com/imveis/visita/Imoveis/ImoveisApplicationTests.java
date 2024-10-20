package com.imveis.visita.Imoveis;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@EnableWebMvc
@SpringBootTest
class ImoveisApplicationTests {

	public Connection getConnection() throws SQLException {
		var host = System.getenv("DATABASE_HOST");
		var password = System.getenv("DATABASE_PASSWORD");
		var user = System.getenv("DATABASE_USER");
		return DriverManager.getConnection(host, user, password);
	}

	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}

}
