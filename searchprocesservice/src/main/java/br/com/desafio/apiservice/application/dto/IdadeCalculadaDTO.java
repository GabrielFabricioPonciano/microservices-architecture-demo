package br.com.desafio.apiservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdadeCalculadaDTO {
    private String cpf;
    private long idade;

}
