package br.com.desafio.apiservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * DTO que representa o resumo final da operação de upload, a ser retornado pela API.
 */
@Data
@Builder
public class UploadResultadoDTO {

    /**
     * Total de linhas lidas do arquivo.
     */
    @JsonProperty("linhas_lidas")
    private long lidas;

    /**
     * Total de novos usuários inseridos no banco de dados.
     */
    @JsonProperty("inseridas")
    private long inseridas;

    /**
     * Total de usuários ignorados, pois o CPF já existia.
     */
    @JsonProperty("ignoradas_por_cpf")
    private long cpfsJaExistentes;

    /**
     * Total de linhas com erro de formato no arquivo.
     */
    @JsonProperty("erros")
    private long errosDeFormato;
}