package br.com.desafio.apiservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO que representa os dados de uma única linha válida extraída do arquivo de upload.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinhaDTO {

    /**
     * Nome completo do usuário.
     */
    private String nome;

    /**
     * CPF normalizado (apenas dígitos) do usuário.
     */
    private String cpf;

    /**
     * Data de nascimento do usuário.
     */
    private LocalDate dataNascimento;
}