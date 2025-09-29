package br.com.desafio.apiservice.domain.service;

import br.com.desafio.apiservice.application.dto.StatusDTO;
import br.com.desafio.apiservice.config.UsuarioNaoEncontradoException;
import br.com.desafio.apiservice.domain.entity.UsuarioDocument;
import br.com.desafio.apiservice.domain.entity.UsuarioStatus;
import br.com.desafio.apiservice.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatusService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Busca o status de um usuário pelo seu CPF.
     *
     * @param cpf O CPF do usuário a ser consultado.
     * @return um {@link StatusDTO} contendo o CPF e o status do usuário.
     * @throws UsuarioNaoEncontradoException se nenhum usuário for encontrado com o CPF fornecido.
     */
    public StatusDTO findStatusByCpf(final String cpf) {
        return usuarioRepository.findByCpf(cpf)
                .map(usuario -> new StatusDTO(usuario.getCpf(), usuario.getStatus(), usuario.getNome()))
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário com CPF " + cpf + " não encontrado."));
    }

    /**
     * Retorna uma lista com o status de todos os usuários, opcionalmente filtrada por status.
     *
     * @param statusFilter O status para filtrar a busca. Se for nulo, todos os usuários são retornados.
     * @return Uma lista de {@link StatusDTO}.
     */
    public List<StatusDTO> findAll(final UsuarioStatus statusFilter) {

        List<UsuarioDocument> usuariosEncontrados;

        if (statusFilter != null) {
            usuariosEncontrados = usuarioRepository.findByStatus(statusFilter);
        } else {
            usuariosEncontrados = usuarioRepository.findAll();
        }

        return usuariosEncontrados.stream()
                .map(usuario -> new StatusDTO(usuario.getCpf(),usuario.getStatus(),usuario.getNome()))
                .collect(Collectors.toList());
}}