package br.com.desafio.apiservice;

import br.com.desafio.apiservice.domain.service.ConsumerServiceKafka;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WriterDataService {

	public static void main(String[] args) {
		SpringApplication.run(WriterDataService.class, args);
	}

}
