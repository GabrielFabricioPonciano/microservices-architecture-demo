package br.com.desafio.apiservice.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando uma operação tenta acessar um usuário
 * que não existe no banco de dados.
 * <p>
 * A anotação {@code @ResponseStatus(HttpStatus.NOT_FOUND)} instrui o Spring a
 * automaticamente retornar o código HTTP 404 quando esta exceção não é tratada.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UsuarioNaoEncontradoException extends RuntimeException {

    public UsuarioNaoEncontradoException(String message) {
        super(message);
    }
}