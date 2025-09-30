package br.com.desafio.apiservice.application.controller;

import br.com.desafio.apiservice.application.dto.IdadeResponse;
import br.com.desafio.apiservice.application.dto.StatusResponse;
import br.com.desafio.apiservice.domain.entity.UsuarioStatus;
import br.com.desafio.apiservice.domain.service.ResultadoService;
import br.com.desafio.apiservice.domain.service.StatusService;
import br.com.desafio.apiservice.util.CpfUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/v1/result")
@RequiredArgsConstructor
@Tag(name = "Consulta de Idade", description = "Endpoints para consultar a idade")
public class ResultController {

    private final ResultadoService idadeService;

    @GetMapping("/{cpf}")
    @Operation(summary = "Busca a idade por um CPF específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Idade encontrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "CPF inválido"),
            @ApiResponse(responseCode = "404", description = "Idade não encontrado")
    })
    public ResponseEntity<IdadeResponse> obterIdadePorCpf(
            @Parameter(
                    description = "CPF do usuário com ou sem máscara",
                    example = "12345678901 ou 123.456.789-01"
            )
            @PathVariable
            @Pattern(
                    regexp = "^\\d{11}$|^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$",
                    message = "CPF deve ter 11 dígitos (12345678901) ou formato com máscara (123.456.789-01)"
            )
            String cpf) {

        log.info("Consultando status para CPF: {}", cpf);
        final String cpfNormalizado = CpfUtil.normalizar(cpf);

        if (!CpfUtil.isValido(cpfNormalizado)) {
            log.warn("CPF inválido fornecido: {}", cpf);
            throw new IllegalArgumentException("CPF inválido: " + cpf);
        }

        log.debug("CPF normalizado: {} -> {}", cpf, cpfNormalizado);
        IdadeResponse status = idadeService.findIdadeByCpf(cpfNormalizado);

        log.info("Status encontrado para CPF {}: {}", cpfNormalizado, status.getIdade());
        return ResponseEntity.ok(status);
    }

    @GetMapping
    @Operation(
            summary = "Lista a idade de todos os usuários"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de status retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    })
    public ResponseEntity<List<IdadeResponse>> obterTodosStatus(){


        List<IdadeResponse> resultado = idadeService.findAllIdade();

        log.info("Encontrados {} usuários", resultado.size());
        return ResponseEntity.ok(resultado);
    }
}
