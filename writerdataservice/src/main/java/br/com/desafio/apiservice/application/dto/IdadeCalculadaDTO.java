package br.com.desafio.apiservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdadeCalculadaDTO {
    String cpf;

    long idade;
}
