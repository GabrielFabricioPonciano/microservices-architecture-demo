package br.com.desafio.apiservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa o resumo final da operação de upload, a ser retornado pela API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadResultResponse {

    private Integer lidas;

    private Integer inseridas;

    private Integer cpfsJaExistentes;

    private Integer errosDeFormato;

    private String arquivo;
}