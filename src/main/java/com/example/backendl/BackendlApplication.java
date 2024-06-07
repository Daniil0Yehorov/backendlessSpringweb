package com.example.backendl;

import com.example.backendl.config.BackendlessConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(BackendlessConfig.class)
public class BackendlApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendlApplication.class, args);

	}

}
