package br.com.desafio.apiservice.util;

import br.com.desafio.apiservice.application.dto.LinhaProcessadaDto;
import br.com.desafio.apiservice.application.dto.ResultadoParseDTO;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
/**
 * Utilitário para processar arquivos de texto contendo dados de usuários.
 * <p>
 * Regras de Processamento:
 * <ul>
 *   <li><b>Formato esperado:</b> NOME DO USUARIO - 123.456.789-00 - dd/MM/yyyy</li>
 *   <li><b>CPF:</b> É normalizado para conter apenas dígitos e deve ter 11 caracteres.</li>
 *   <li><b>Data de Nascimento:</b> Deve estar no formato "dd/MM/yyyy" e ser uma data válida (ex: "30/02/2023" é rejeitado).</li>
 *   <li><b>Tratamento de Erros:</b> Linhas em branco, com formato incorreto, CPF inválido ou data inválida
 *       são contadas como erro e ignoradas, sem interromper o processamento total do arquivo.</li>
 * </ul>
 */
@Component
public class FileProcessorUtil {
    /**
     * Define o formato de data esperado ("dd/MM/yyyy") de forma estrita.
     * O `ResolverStyle.STRICT` garante que datas inválidas como "30/02/2023" sejam rejeitadas.
     */
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    /**
     * Lê um {@code InputStream} linha a linha, valida cada registro e retorna um DTO
     * com os dados processados e estatísticas da operação.
     *
     * @param inputStream O fluxo de dados do arquivo a ser lido.
     * @return Um objeto {@link ResultadoParseDTO} contendo a lista de linhas válidas e os contadores da operação.
     * @throws IOException Se ocorrer um erro de I/O irrecuperável durante a leitura do arquivo.
     */
    public ResultadoParseDTO parse(InputStream inputStream) throws IOException {
       final List<LinhaProcessadaDto> linhasvalidas = new ArrayList<>();
        long totalLidas = 0;
        long totalComErros = 0;

        // Usa 'try-with-resources' para garantir que o reader seja fechado automaticamente.
        try(BufferedReader linhaatual = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))){
         String linha;
            while ((linha = linhaatual.readLine()) != null) {
                totalLidas++;
                // Passo 1: Validação básica da linha. Ignora linhas em branco.
                if (linha.trim().isEmpty()) continue;

                // Passo 2: Split robusto da linha, esperando 3 partes.
                String[] partes = linha.split("\\s+-\\s+", 3);
                if (partes.length != 3) {
                    totalComErros++;
                    continue;
                }

                // Passo 3: Extração, normalização e validação dos campos.
                final String nome = partes[0].toUpperCase().trim();
                final String cpf = partes[1].replaceAll("\\D", "");

                if (cpf.length() != 11){
                    totalComErros++;
                    continue;
                }

                // Passo 4: Parsing da data
                try {
                    final LocalDate dataNascimento = LocalDate.parse(partes[2].trim(),FMT);
                    // Se todas as validações passaram, a linha é considerada válida.
                    linhasvalidas.add(new LinhaProcessadaDto(nome,cpf,dataNascimento));
                }catch (DateTimeParseException e){
                    // A data estava num formato inválido ou era uma data impossível.
                    totalComErros++;
                }
            }
        }
        return new ResultadoParseDTO(linhasvalidas,totalLidas,totalComErros);
    }
}