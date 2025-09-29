package br.com.desafio.apiservice.domain.repository;

import br.com.desafio.apiservice.domain.entity.IdadeDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para gerenciar as operações de CRUD para a entidade IdadeDocument.
 * Esta interface herda métodos padrão para salvar, buscar, atualizar e deletar
 * documentos da coleção 'idadeDocument' no MongoDB.
 */

@Repository
public interface IdadeRepository extends MongoRepository<IdadeDocument, String> {
    @Override
    List<IdadeDocument> findAll();
}
