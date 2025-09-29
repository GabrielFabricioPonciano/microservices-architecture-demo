package br.com.desafio.apiservice.application.controller;


import br.com.desafio.apiservice.application.dto.StatusDTO;
import br.com.desafio.apiservice.domain.entity.UsuarioStatus;
import br.com.desafio.apiservice.domain.service.StatusService;
import br.com.desafio.apiservice.util.CpfUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/status") // URL base correta conforme a especificação
@RequiredArgsConstructor
@Tag(name = "Consulta de Status", description = "Endpoints para consultar o status de processamento dos usuários")
public class StatusController {

    final private StatusService statusService;

    @GetMapping("/{cpf}")
    @Operation(summary = "Busca o status por um CPF específico")
    public ResponseEntity<?> obterStatusInicial(
            @Parameter(description = "CPF do usuario") @PathVariable String cpf)
    {
        if(!CpfUtil.isValido(cpf)){
           return ResponseEntity.badRequest().body("O CPF fornecido é inválido. Ele deve conter 11 dígitos.");
        }

        final String cpfnormalizado = CpfUtil.normalizar(cpf);

        StatusDTO status = statusService.findStatusByCpf(cpfnormalizado);
        return ResponseEntity.ok(status);
    }

    @GetMapping
    @Operation(summary = "Lista o status de todos os usuários", description = "Permite filtrar os resultados por status.")
    public ResponseEntity<?> obterStatus(
            @Parameter(description = "filtro opcional, valores possiveis:Processamento,Finalizado")
                    @RequestParam(required = false) UsuarioStatus usuarioStatus)
    {
        List<StatusDTO> resultado = statusService.findAll(usuarioStatus);
        return ResponseEntity.ok(resultado);
    }
}
