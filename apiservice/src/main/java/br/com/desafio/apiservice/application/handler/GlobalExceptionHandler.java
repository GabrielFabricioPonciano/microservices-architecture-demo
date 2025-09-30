package br.com.desafio.apiservice.application.handler;

import br.com.desafio.apiservice.application.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import br.com.desafio.apiservice.application.dto.ErrorResponse;
import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioNaoEncontrado(UsuarioNaoEncontradoException ex) {
        log.warn("Usuário não encontrado: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse("USUARIO_NAO_ENCONTRADO", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(ConstraintViolationException ex) {
        log.warn("Erro de validação: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse("VALIDACAO_ERRO", "Dados fornecidos são inválidos");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.warn("Erro de validação de argumentos: {}", ex.getMessage());
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .findFirst()
                .orElse("Dados inválidos");
        
        ErrorResponse error = new ErrorResponse("VALIDACAO_ERRO", mensagem);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn("Erro de tipo de argumento: {}", ex.getMessage());
        String mensagem = String.format("Parâmetro '%s' deve ser do tipo %s", 
                ex.getName(), 
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "válido");
        
        ErrorResponse error = new ErrorResponse("TIPO_INVALIDO", mensagem);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Argumento inválido: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse("ARGUMENTO_INVALIDO", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericError(Exception ex) {
        log.error("Erro interno: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse("ERRO_INTERNO", "Erro interno do servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}