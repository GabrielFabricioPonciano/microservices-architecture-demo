package br.com.desafio.apiservice.application.controller;

import br.com.desafio.apiservice.application.dto.UploadResultadoDTO;
import br.com.desafio.apiservice.domain.service.UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Controller responsável pelos endpoints relacionados ao processamento de dados de usuários.
 */
@RestController
@RequestMapping("/v1/usuarios")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Upload de Usuários", description = "Endpoints para upload e processamento de arquivos")
public class UploadController {

    private final UploadService uploadService;

    /**
     * Endpoint para receber um arquivo de texto, processá-lo e inserir novos usuários no banco de dados.
     *
     * @param file O arquivo enviado como 'multipart/form-data'. O nome do parâmetro deve ser "arquivo".
     * @return Uma ResponseEntity contendo o resumo da operação ou uma mensagem de erro.
     */
    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Processa um arquivo de usuários",
            description = "Recebe um arquivo no formato 'NOME - CPF - DD/MM/AAAA', processa e salva novos usuários.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Arquivo processado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UploadResultadoDTO.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de Sucesso",
                                    value = "{\"lidas\": 100, \"inseridas\": 95, \"cpfsJaExistentes\": 3, \"errosDeFormato\": 2}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Requisição inválida (ex: arquivo vazio)",
                    content = @Content(mediaType = "text/plain",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(name = "Exemplo de Arquivo Vazio", value = "O arquivo não pode ser nulo ou vazio.")
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor durante o processamento",
                    content = @Content(mediaType = "text/plain",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(name = "Exemplo de Erro Interno", value = "Ocorreu um erro ao processar o arquivo.")
                    )
            )
    })
    public ResponseEntity<?> uploadArquivoDeUsuarios(@RequestParam("file") MultipartFile file) {
        log.info("Recebida requisição de upload para o arquivo: {}", file.getOriginalFilename());

        try {
            UploadResultadoDTO resultado = uploadService.create(file);

            return ResponseEntity.accepted().body(resultado);

        } catch (IllegalArgumentException e) {
            // --- Tratamento de Erro do Cliente (400) ---
            log.warn("Erro de validação na requisição de upload: {}", e.getMessage());

            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (IOException e) {
            // --- Tratamento de Erro do Servidor (500) ---
            log.error("Erro de I/O ao processar o arquivo: {}", file.getOriginalFilename(), e);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocorreu um erro inesperado ao processar o arquivo. Por favor, tente novamente mais tarde.");
        }
    }
}