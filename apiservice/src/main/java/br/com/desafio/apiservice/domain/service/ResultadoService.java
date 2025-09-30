package br.com.desafio.apiservice.domain.service;

import br.com.desafio.apiservice.application.dto.IdadeResponse;
import br.com.desafio.apiservice.application.exception.UsuarioNaoEncontradoException;
import br.com.desafio.apiservice.domain.entity.IdadeDocument;
import br.com.desafio.apiservice.domain.repository.IdadeRepository;
import br.com.desafio.apiservice.util.CpfUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResultadoService {

    private final IdadeRepository idadeRepository;

    @Transactional(readOnly = true)
    public IdadeResponse findIdadeByCpf(final String cpf) {
        final String cpfNormalizado = CpfUtil.normalizar(cpf);
        if (!CpfUtil.isValido(cpfNormalizado)) {
            log.warn("CPF inválido na consulta de idade: {}", cpf);
            throw new IllegalArgumentException("CPF inválido: " + cpf);
        }

        return idadeRepository.findById(cpfNormalizado)
                .map(this::converterParaIdadeResponse)
                .orElseThrow(() -> {
                    log.warn("Idade não encontrada para CPF: {}", cpfNormalizado);
                    return new UsuarioNaoEncontradoException("Usuário com CPF " + cpfNormalizado + " não encontrado.");
                });
    }

    @Transactional(readOnly = true)
    public List<IdadeResponse> findAllIdade() {
        return idadeRepository.findAll()
                .stream()
                .map(this::converterParaIdadeResponse)
                .collect(Collectors.toList());
    }

    private IdadeResponse converterParaIdadeResponse(final IdadeDocument usuario) {
        return new IdadeResponse(usuario.getCpf(), usuario.getIdade());
    }
}