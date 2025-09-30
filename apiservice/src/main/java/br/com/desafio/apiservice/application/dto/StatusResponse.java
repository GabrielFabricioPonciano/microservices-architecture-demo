package br.com.desafio.apiservice.application.dto;

import br.com.desafio.apiservice.domain.entity.UsuarioStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusResponse {
    
    public StatusResponse(String cpf2, UsuarioStatus status2, String nome2) {
        cpf = cpf2;
        status = status2;
        nome = nome2;
    }

    private String nome;
    
    private String cpf;
    
    private LocalDate dataNascimento;
    
    private UsuarioStatus status;
    
    private Integer idade;
}