package br.com.desafio.apiservice.domain.service;

import br.com.desafio.apiservice.application.dto.IdadeResponse;
import br.com.desafio.apiservice.application.exception.UsuarioNaoEncontradoException;
import br.com.desafio.apiservice.domain.entity.IdadeDocument;
import br.com.desafio.apiservice.domain.repository.IdadeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ResultadoService{

    private final IdadeRepository idadeRepository;

    public IdadeResponse findIdadeByCpf(final String cpf){
        return idadeRepository.findById(cpf).map(this::converterParaIdadeResponse)
                .orElseThrow(()-> new UsuarioNaoEncontradoException
                        ("Usuário com CPF " + cpf + " não encontrado."));
    }

    public List<IdadeResponse> findAllIdade(){
        return idadeRepository.findAll()
                .stream().map(this::converterParaIdadeResponse)
                .collect(Collectors.toList());

    }

    private IdadeResponse converterParaIdadeResponse(IdadeDocument usuario){
        return new IdadeResponse(usuario.getCpf(), usuario.getIdade());
    }
}