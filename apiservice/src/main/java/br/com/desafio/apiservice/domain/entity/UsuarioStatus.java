package br.com.desafio.apiservice.domain.entity;

/**
 * Define os possíveis status de processamento para um cadastro de usuário.
 * Utilizado para rastrear o ciclo de vida do registro do usuário no sistema.
 */
public enum UsuarioStatus {

    /**
     * O cadastro do usuário está em andamento ou aguardando processamento.
     * Este é o estado inicial padrão para um novo usuário.
     */
    Processamento,

    /**
     * O processamento do cadastro foi concluído com sucesso.
     */
    Finalizado
}