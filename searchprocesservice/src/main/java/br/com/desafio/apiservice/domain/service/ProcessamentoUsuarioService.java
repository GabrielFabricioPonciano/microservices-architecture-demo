package br.com.desafio.apiservice.domain.service;

import br.com.desafio.apiservice.application.dto.IdadeCalculadaDTO;
import br.com.desafio.apiservice.domain.entity.UsuarioDocument;
import br.com.desafio.apiservice.domain.entity.UsuarioStatus;
import br.com.desafio.apiservice.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessamentoUsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final KafkaProducerService kafkaProducerService;

    private final Set<String> cpfsJaEnviados = java.util.concurrent.ConcurrentHashMap.newKeySet();
    /**
     * Método auxiliar privado para calcular a idade a partir da data de nascimento.
     */
    private long calcularIdade(LocalDate localDate){
        if (localDate == null) return 0;
        return Period.between(localDate,LocalDate.now()).getYears();
    }

    public void processarUsuariosPendentes(){

    // 1. Busca os usuários do banco
    List<UsuarioDocument> usuariosParaProcessar  = usuarioRepository.findByStatus(UsuarioStatus.Processamento);

    if (usuariosParaProcessar.isEmpty()){
        log.info("nenhum usuario encontrado para processar");
        return;
    }
        log.info("{} usuários encontrados para processamento.", usuariosParaProcessar.size());

    // 2. Itera sobre cada usuário, calcula a idade e envia para o Kafka
        for (UsuarioDocument usuario: usuariosParaProcessar) {
            if (cpfsJaEnviados.contains(usuario.getCpf())){
                log.info("CPF {} já foi processado nesta sessão. Ignorando.", usuario.getCpf());
                continue;}

            IdadeCalculadaDTO user = new IdadeCalculadaDTO(usuario.getCpf(), calcularIdade(usuario.getDataNascimento()));

            // 3. Delega o envio para o serviço especialista
            kafkaProducerService.enviarMensagem(user);

            //cache
            cpfsJaEnviados.add(usuario.getCpf());
        }
        log.info("Processamento de {} usuários concluído.", usuariosParaProcessar.size());

    }
}
