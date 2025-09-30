package br.com.desafio.apiservice.domain.service;

import br.com.desafio.apiservice.application.dto.LinhaProcessadaDto;
import br.com.desafio.apiservice.application.dto.ResultadoParseDTO;
import br.com.desafio.apiservice.application.dto.UploadResultResponse;
import br.com.desafio.apiservice.domain.entity.UsuarioDocument;
import br.com.desafio.apiservice.domain.entity.UsuarioStatus;
import br.com.desafio.apiservice.domain.repository.UsuarioRepository;
import br.com.desafio.apiservice.util.FileProcessorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * Serviço responsável por orquestrar o processo de upload e persistência
 * de dados de usuários a partir de um arquivo.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UploadService {

    private final UsuarioRepository usuarioRepository;
    private final FileProcessorUtil fileProcessorUtil;

    @Transactional
    public UploadResultResponse create(final MultipartFile arquivo) throws IOException {

        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException("Arquivo não pode ser vazio ou nulo");
        }

        log.info("Iniciando processamento do arquivo: {}, tamanho: {} bytes",
                arquivo.getOriginalFilename(), arquivo.getSize());

        final ResultadoParseDTO resultadoParse;
        try (InputStream is = arquivo.getInputStream()) {
            resultadoParse = fileProcessorUtil.parse(is);
        }

        long inseridos = 0;
        long cpfsJaExistentes = 0;

        log.info("Processando {} linhas válidas encontradas no arquivo",
                resultadoParse.getLinhasValidas().size());

        for (final LinhaProcessadaDto linhaDto : resultadoParse.getLinhasValidas()) {
            if (usuarioRepository.existsByCpf(linhaDto.getCpf())) {
                cpfsJaExistentes++;
                log.debug("CPF já existente: {}", linhaDto.getCpf());
            } else {
                UsuarioDocument usuarioDocument = criarUsuarioDocument(linhaDto);
                usuarioRepository.save(usuarioDocument);
                inseridos++;
                log.debug("Usuário inserido: {} - CPF: {}", linhaDto.getNome(), linhaDto.getCpf());
            }
        }

        log.info("Processamento de arquivo finalizado. Arquivo: {}, Novos usuários inseridos: {}, CPFs já existentes: {}",
                arquivo.getOriginalFilename(), inseridos, cpfsJaExistentes);

        return new UploadResultResponse(
                (int) resultadoParse.getTotalLidas(),
                (int) inseridos,
                (int) cpfsJaExistentes,
                (int) resultadoParse.getTotalComErros(),
                arquivo.getOriginalFilename()
        );
    }

    private UsuarioDocument criarUsuarioDocument(final LinhaProcessadaDto linhaDto) {
        UsuarioDocument usuarioDocument = new UsuarioDocument();
        usuarioDocument.setNome(linhaDto.getNome());
        usuarioDocument.setCpf(linhaDto.getCpf());
        usuarioDocument.setDataNascimento(linhaDto.getDataNascimento());
        usuarioDocument.setStatus(UsuarioStatus.Processamento);
        return usuarioDocument;
    }
}