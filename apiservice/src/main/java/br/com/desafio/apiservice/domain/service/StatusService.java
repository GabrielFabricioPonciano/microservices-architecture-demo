package br.com.desafio.apiservice.domain.service;
import br.com.desafio.apiservice.application.handler.*;
import br.com.desafio.apiservice.application.dto.StatusResponse;
import br.com.desafio.apiservice.application.exception.UsuarioNaoEncontradoException;
import br.com.desafio.apiservice.domain.entity.UsuarioDocument;
import br.com.desafio.apiservice.domain.entity.UsuarioStatus;
import br.com.desafio.apiservice.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatusService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Busca o status de um usuário pelo seu CPF.
     *
     * @param cpf O CPF do usuário a ser consultado (normalizado).
     * @return um {@link StatusResponse} contendo os dados completos do usuário.
     * @throws UsuarioNaoEncontradoException se nenhum usuário for encontrado com o CPF fornecido.
     */
    public StatusResponse findStatusByCpf(final String cpf) {
        log.debug("Buscando usuário por CPF: {}", cpf);
        
        return usuarioRepository.findByCpf(cpf)
                .map(this::converterParaStatusResponse)
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado para CPF: {}", cpf);
                    return new UsuarioNaoEncontradoException("Usuário com CPF " + cpf + " não encontrado.");
                });
    }

    /**
     * Retorna uma lista com o status de todos os usuários, opcionalmente filtrada por status.
     *
     * @param statusFilter O status para filtrar a busca. Se for nulo, todos os usuários são retornados.
     * @return Uma lista de {@link StatusResponse}.
     */
    public List<StatusResponse> findAll(final UsuarioStatus statusFilter) {

        List<UsuarioDocument> usuariosEncontrados;

        if (statusFilter != null) {
            usuariosEncontrados = usuarioRepository.findByStatus(statusFilter);
        } else {
            usuariosEncontrados = usuarioRepository.findAll();
        }

        return usuariosEncontrados.stream()
                .map(usuario -> new StatusResponse(usuario.getCpf(),usuario.getStatus(),usuario.getNome()))
                .collect(Collectors.toList());
    }

    private StatusResponse converterParaStatusResponse(UsuarioDocument usuario) {
        return new StatusResponse(usuario.getCpf(), usuario.getStatus(), usuario.getNome());
    }
}