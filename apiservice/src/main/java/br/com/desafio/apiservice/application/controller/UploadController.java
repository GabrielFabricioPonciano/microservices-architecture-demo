package br.com.desafio.apiservice.application.controller;

import br.com.desafio.apiservice.application.dto.UploadResultResponse;
import br.com.desafio.apiservice.application.dto.ErrorResponse;
import br.com.desafio.apiservice.domain.service.UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * Controller responsável pelos endpoints relacionados ao processamento de dados de usuários.
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Upload de Usuários", description = "Endpoints para upload e processamento de arquivos")
public class UploadController {

    private final UploadService uploadService;

    /**
     * Endpoint para receber um arquivo de texto, processá-lo e inserir novos usuários no banco de dados.
     *
     * @param file O arquivo enviado como 'multipart/form-data'. O nome do parâmetro deve ser "file".
     * @return Uma ResponseEntity contendo o resumo da operação ou uma mensagem de erro.
     */
    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "Processa um arquivo de usuários",
        description = "Recebe um arquivo no formato 'NOME - CPF - DD/MM/AAAA', processa e salva novos usuários. " +
                     "O arquivo deve estar em formato texto (.txt) e cada linha deve conter os dados separados por ' - '.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "202", 
            description = "Arquivo processado com sucesso (Accepted - processamento assíncrono)",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UploadResultResponse.class),
                examples = @ExampleObject(
                    name = "Exemplo de Sucesso",
                    value = "{\"lidas\": 100, \"inseridas\": 95, \"cpfsJaExistentes\": 3, \"errosDeFormato\": 2, \"arquivo\": \"usuarios.txt\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Requisição inválida (arquivo vazio, formato inválido, etc.)",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Exemplo de Arquivo Inválido", 
                    value = "{\"codigo\": \"ARQUIVO_INVALIDO\", \"mensagem\": \"O arquivo não pode ser nulo ou vazio\", \"timestamp\": \"2024-01-01T10:00:00\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Erro interno no servidor durante o processamento",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Exemplo de Erro Interno", 
                    value = "{\"codigo\": \"ERRO_INTERNO\", \"mensagem\": \"Erro ao processar arquivo\", \"timestamp\": \"2024-01-01T10:00:00\"}"
                )
            )
        )
    })
    public ResponseEntity<UploadResultResponse> uploadArquivoDeUsuarios(
            @Parameter(description = "Arquivo de texto contendo dados dos usuários")
            @RequestParam("file")
            @NotNull(message = "Arquivo é obrigatório")
            MultipartFile file) {
        
        // validação defensiva antes de acessar métodos do file
        if (file == null || file.isEmpty()) {
            log.warn("Tentativa de upload com arquivo nulo ou vazio");
            throw new IllegalArgumentException("O arquivo não pode ser nulo ou vazio");
        }

        log.info("Recebida requisição de upload - arquivo: {}, tamanho: {} bytes", 
                file.getOriginalFilename(), file.getSize());

        try {
            // Delega o processamento para o service
            UploadResultResponse resultado = uploadService.create(file);
            
            log.info("Upload processado com sucesso - arquivo: {}, linhas processadas: {}, inseridas: {}", 
                    file.getOriginalFilename(), resultado.getLidas(), resultado.getInseridas());

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(resultado);

        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação no upload do arquivo '{}': {}", file.getOriginalFilename(), e.getMessage());
            throw e; // GlobalExceptionHandler vai tratar
            
        } catch (IOException e) {
            log.error("Erro de I/O ao processar arquivo '{}': {}", file.getOriginalFilename(), e.getMessage(), e);
            throw new RuntimeException("Erro ao processar arquivo: " + e.getMessage(), e);
        }
    }
}