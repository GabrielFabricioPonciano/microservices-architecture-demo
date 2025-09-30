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
    

    private String cpf;
    private UsuarioStatus status;
    private String nome;
        
    }