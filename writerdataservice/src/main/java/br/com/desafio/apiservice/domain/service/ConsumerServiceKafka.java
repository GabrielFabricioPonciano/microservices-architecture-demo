package br.com.desafio.apiservice.domain.service;

import br.com.desafio.apiservice.application.dto.IdadeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumerServiceKafka {

    private final InsertIdadeMongo insertIdadeMongo;


    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumir(IdadeDto mensagem){
        if (mensagem ==null){
            log.warn("mensagem nula recebida ignorando");
            return;
        }
        log.info("Mensagem recebida do Kafka. CPF: {}, Idade: {}", mensagem.getCpf(), mensagem.getIdade());
        insertIdadeMongo.inserirIdade(mensagem);
    }
}
