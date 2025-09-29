package br.com.desafio.apiservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para encapsular o resultado do parsing de um arquivo.
 * Contém a lista de linhas válidas e os contadores do processo de leitura.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoParseDTO {

    /**
     * Lista de DTOs representando as linhas que foram lidas e validadas com sucesso.
     */
    private List<LinhaDTO> linhasValidas;

    /**
     * Número total de linhas lidas do arquivo.
     */
    private long totalLidas;

    /**
     * Número de linhas descartadas por erros de formato ou validação.
     */
    private long totalComErros;
}