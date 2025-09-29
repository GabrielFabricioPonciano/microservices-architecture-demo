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
    /**
     * Busca um usuário pelo seu CPF.
     * Esta é uma "query method". O Spring Data gera a consulta automaticamente
     * a partir do nome do método, sem a necessidade da anotação @Query.
     *
     * @param cpf O CPF a ser pesquisado.
     * @return um Optional contendo o UsuarioDocument se encontrado, ou um Optional vazio caso contrário.
     */
    Optional<UsuarioDocument> findByCpf(String cpf);
    boolean existsByCpf(String cpf);

    /**
     * Busca todos os usuários que possuem um status específico.
     * O Spring Data MongoDB gera a query automaticamente a partir do nome do método.
     *
     * @param status O status a ser filtrado (ex: processamento, finalizado).
     * @return Uma lista de documentos de usuário que correspondem ao status.
     */

    List<UsuarioDocument> findByStatus(UsuarioStatus status);

}
