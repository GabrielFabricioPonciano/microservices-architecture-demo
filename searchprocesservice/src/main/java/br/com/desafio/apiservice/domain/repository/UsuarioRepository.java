package br.com.desafio.apiservice.domain.repository;

import br.com.desafio.apiservice.domain.entity.UsuarioDocument;
import br.com.desafio.apiservice.domain.entity.UsuarioStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
/**
 * Repositório para gerir as operações de CRUD para a entidade UsuarioDocument.
 */
@Repository
public interface UsuarioRepository extends MongoRepository<UsuarioDocument,String> {

    List<UsuarioDocument> findByStatus(UsuarioStatus status);

}
