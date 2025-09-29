package br.com.desafio.apiservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe de configuração para a documentação da API usando SpringDoc OpenAPI (Swagger).
 * Define as informações globais da API que serão exibidas na UI do Swagger.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Processamento de Usuários - Desafio")
                        .version("v1.0")
                        .description("API REST para upload e consulta de dados de usuários, utilizando Spring Boot, MongoDB e Kafka.")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                );
    }
}