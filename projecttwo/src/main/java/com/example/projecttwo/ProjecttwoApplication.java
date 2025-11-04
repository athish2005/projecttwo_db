package com.example.projecttwo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjecttwoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjecttwoApplication.class, args);
		System.out.println("✅ Swagger running at: http://localhost:8082/swagger-ui.html");

	}

}
