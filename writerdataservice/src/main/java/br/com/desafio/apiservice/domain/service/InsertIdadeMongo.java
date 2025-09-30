package br.com.desafio.apiservice.domain.service;

import br.com.desafio.apiservice.application.dto.IdadeCalculadaDto;
import br.com.desafio.apiservice.domain.entity.IdadeDocument;
import br.com.desafio.apiservice.domain.entity.UsuarioDocument;
import br.com.desafio.apiservice.domain.entity.UsuarioStatus;
import br.com.desafio.apiservice.domain.repository.IdadeRepository;
import br.com.desafio.apiservice.domain.repository.UsuarioRepository;
import com.mongodb.DuplicateKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsertIdadeMongo {
    private final UsuarioRepository usuarioRepository;
    private final IdadeRepository idadeRepository;

    public void inserirIdade(IdadeCalculadaDto idadeDto){
        try {
            IdadeDocument doc = new IdadeDocument(idadeDto.getCpf(), idadeDto.getIdade());
            IdadeDocument salvo = idadeRepository.save(doc);
            log.info("Idade upsert OK. cpf={}, idade={}", salvo.getCpf(), salvo.getIdade());
        } catch (DuplicateKeyException dup){
            log.warn("DuplicateKey em idades (cpf={}), seguindo por idempotência.", idadeDto.getCpf());
        }catch (Exception e){
            log.error("Falha ao upsert em 'idades' (cpf={}). Mantendo usuário em 'Processamento'. Erro: {}",
                    idadeDto.getCpf(), e.getMessage(), e);
            throw e;
        }
        try {
            UsuarioDocument usuario = usuarioRepository.findByCpf(idadeDto.getCpf());
            if (usuario == null){
                log.warn("Usuário não encontrado para cpf={} ao atualizar status. (Pode ter sido removido?)", idadeDto.getCpf());
                return;
            }
            usuario.setStatus(UsuarioStatus.Finalizado);
            usuarioRepository.save(usuario);
            log.info("Status do usuário atualizado para 'Finalizado'. cpf={}", idadeDto.getCpf());
        } catch (Exception e){
            log.error("Falha ao atualizar status do usuário (cpf={}). Será reprocessado futuramente. Erro: {}",
                    idadeDto.getCpf(), e.getMessage(), e);
            throw e;
        }
    }
}
