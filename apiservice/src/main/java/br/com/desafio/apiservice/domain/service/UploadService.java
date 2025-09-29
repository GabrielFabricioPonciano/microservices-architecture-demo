package br.com.desafio.apiservice.domain.service;

import br.com.desafio.apiservice.application.dto.LinhaDTO;
import br.com.desafio.apiservice.application.dto.ResultadoParseDTO;
import br.com.desafio.apiservice.application.dto.UploadResultadoDTO;
import br.com.desafio.apiservice.domain.entity.UsuarioDocument;
import br.com.desafio.apiservice.domain.repository.UsuarioRepository;
import br.com.desafio.apiservice.util.LerArquivoUtil;
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
 * <p>
 * A sua responsabilidade é:
 * 1. Coordenar a leitura do arquivo usando uma classe utilitária.
 * 2. Aplicar a regra de negócio de não inserir CPFs duplicados.
 * 3. Persistir os novos usuários no banco de dados.
 * 4. Retornar um resumo completo da operação.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UploadService {
    private final UsuarioRepository usuarioRepository;
    private final LerArquivoUtil lerArquivoUtil;

    /**
     * Processa um arquivo, valida os seus dados e insere novos usuários no banco de dados.
     *
     * @param arquivo O {@link MultipartFile} enviado na requisição.
     * @return Um {@link UploadResultadoDTO} com o resumo da operação.
     * @throws IOException      Se houver um erro irrecuperável na leitura do arquivo.
     * @throws IllegalArgumentException Se o arquivo enviado estiver vazio.
     */
    @Transactional
    public UploadResultadoDTO create(final MultipartFile arquivo) throws IOException{
        // Passo 1: Validação de entrada
        if (arquivo == null || arquivo.isEmpty()) throw new IllegalArgumentException("arquivo não pode ser vazio");
        log.info("iniciando processamento do arquivo: {}",arquivo.getOriginalFilename());

        // Passo 2: Leitura e parsing do arquivo
        final ResultadoParseDTO resultadoParse;
        try(InputStream arquivoStream = arquivo.getInputStream()){
            resultadoParse = lerArquivoUtil.parse(arquivoStream);
        }

        long inseridos = 0;
        long cpfjaexistentes = 0;

        log.info("Processando {} linhas validas encontradas no arquivo: ", resultadoParse.getLinhasValidas().size());

        // Passo 3: Lógica de persistência
        for (final LinhaDTO linhaDTO: resultadoParse.getLinhasValidas()){
            if (usuarioRepository.existsByCpf(linhaDTO.getCpf())){
                cpfjaexistentes++;
            } else {
                UsuarioDocument usuarioDocument = new UsuarioDocument();
                usuarioDocument.setNome(linhaDTO.getNome());
                usuarioDocument.setCpf(linhaDTO.getCpf());
                usuarioDocument.setDataNascimento(linhaDTO.getDataNascimento());
                usuarioRepository.save(usuarioDocument);
                inseridos++;
            }

        }

        log.info("Processamento de arquivo finalizado. Novos usuários inseridos: {}. CPFs já existentes: {}", inseridos, cpfjaexistentes);

        // Passo 4: Construção do DTO de resposta
        return UploadResultadoDTO.builder()
                .lidas(resultadoParse.getTotalLidas())
                .inseridas(inseridos)
                .cpfsJaExistentes(cpfjaexistentes)
                .errosDeFormato(resultadoParse.getTotalComErros())
                .build();
    }
}