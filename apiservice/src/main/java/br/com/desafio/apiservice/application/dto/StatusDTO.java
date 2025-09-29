package br.com.desafio.apiservice.application.dto;

import br.com.desafio.apiservice.domain.entity.UsuarioDocument;
import br.com.desafio.apiservice.domain.entity.UsuarioStatus;
import lombok.*;
import org.springframework.stereotype.Service;

/**
 * Data Transfer Object (DTO) para consultar ou retornar o status de processamento
 * de um cadastro de usuário específico, identificado pelo seu CPF.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusDTO {
    private String cpf;
    private UsuarioStatus status;
    private String nome;
}