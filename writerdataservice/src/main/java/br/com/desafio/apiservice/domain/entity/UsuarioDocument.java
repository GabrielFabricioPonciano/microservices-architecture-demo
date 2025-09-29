package br.com.desafio.apiservice.domain.entity;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * Representa um documento de usuário na coleção 'usuarios' do MongoDB.
 * Cada documento contém as informações cadastrais básicas de um usuário.
 */
@Document("usuarios")
@Data
@Getter
public class UsuarioDocument {

    /**
     * Identificador único do documento, gerado automaticamente pelo MongoDB.
     */
    @Id
    private String id;

    /**
     * Nome completo do usuário.
     */
    private String nome;

    /**
     * CPF (Cadastro de Pessoas Físicas) do usuário.
     * Este campo possui um índice único para garantir que não haja duplicatas.
     */
    @Indexed(unique = true)
    private String cpf;

    /**
     * Data de nascimento do usuário.
     */
    private LocalDate dataNascimento;

    /**
     * Status atual do cadastro do usuário, inicializado como 'Processamento'.
     */
    private UsuarioStatus status = UsuarioStatus.Processamento;

}