package br.com.desafio.apiservice.job;

import br.com.desafio.apiservice.domain.service.ProcessamentoUsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UsuarioJobScheduler {
    private final ProcessamentoUsuarioService processamentoUsuarioService;

    @Scheduled(fixedDelayString ="${processing.job.delay}")
    public void executarProcessoAgendado(){
        log.info("JOB AGENDADO: Iniciando rotina de processamento de usuários...");
        processamentoUsuarioService.processarUsuariosPendentes();
        log.info("JOB AGENDADO: Rotina de processamento finalizada.");
    }
}
