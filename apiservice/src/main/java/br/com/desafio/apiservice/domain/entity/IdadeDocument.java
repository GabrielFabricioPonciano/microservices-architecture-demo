package br.com.desafio.apiservice.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Representa um documento no MongoDB que armazena a idade calculada
 * de um usuário, associada ao seu CPF.
 */
@Document("idades")
@Data
public class IdadeDocument {

    /**
     * O CPF (Cadastro de Pessoas Físicas) do usuário, utilizado como
     */
    @Id
    @Indexed(unique = true)
    private String cpf;

    /**
     * A idade calculada do usuário em anos.
     */
    private Long idade;
}