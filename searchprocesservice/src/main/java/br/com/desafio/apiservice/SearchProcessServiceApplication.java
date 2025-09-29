package br.com.desafio.apiservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SearchProcessServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchProcessServiceApplication.class, args);
	}

}
