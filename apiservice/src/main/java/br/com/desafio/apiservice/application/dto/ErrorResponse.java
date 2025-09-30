package br.com.desafio.apiservice.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    
    private String codigo;
    
    private String mensagem;
    
    private String campo;
    
    private LocalDateTime timestamp;
    
    public ErrorResponse(String codigo, String mensagem) {
        this.codigo = codigo;
        this.mensagem = mensagem;
        this.timestamp = LocalDateTime.now();
    }

}