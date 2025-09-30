package br.com.desafio.apiservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoParseDTO {


    private List<LinhaProcessadaDto> linhasValidas;

    private long totalLidas;


    private long totalComErros;
}