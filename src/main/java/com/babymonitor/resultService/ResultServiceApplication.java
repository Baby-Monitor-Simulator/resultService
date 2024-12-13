package com.babymonitor.resultService;

import com.babymonitor.resultService.service.DotEnvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResultServiceApplication {

	public static void main(String[] args) {
		DotEnvLoader.loadEnv();
		SpringApplication.run(ResultServiceApplication.class, args);
	}

}
