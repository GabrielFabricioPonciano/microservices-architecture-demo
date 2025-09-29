package br.com.desafio.apiservice.domain.service;

import br.com.desafio.apiservice.application.dto.IdadeCalculadaDTO;
import br.com.desafio.apiservice.domain.entity.UsuarioDocument;
import br.com.desafio.apiservice.domain.entity.UsuarioStatus;
import br.com.desafio.apiservice.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {
    @Value("${kafka.topic.name}")
    private String topicName;

    private final KafkaTemplate<String, IdadeCalculadaDTO> kafkaTemplate;
    public void enviarMensagem(IdadeCalculadaDTO dto) {
        log.info("Enviando mensagem para o Kafka. CPF: {}, Idade: {}", dto.getCpf(), dto.getIdade());
            kafkaTemplate.send(topicName,dto.getCpf(),dto);

    }

}