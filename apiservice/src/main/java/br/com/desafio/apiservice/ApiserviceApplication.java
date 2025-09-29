package br.com.desafio.apiservice;

import br.com.desafio.apiservice.util.LerArquivoUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ApiserviceApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(ApiserviceApplication.class, args);
	}

}
