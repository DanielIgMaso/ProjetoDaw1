package web.controlevacinacao.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.controlevacinacao.model.Cliente;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.repository.queries.cliente.ClienteQueries;

public interface ClienteRepository extends JpaRepository<Cliente, Long>, ClienteQueries {

    Optional<Cliente> findByCodigoAndStatus(long codigo, Status status);

    @Query("SELECT c FROM Cliente c WHERE " +
            "REPLACE(REPLACE(c.cpf, '.', ''), '-', '') LIKE %:cpf% " +
            "AND c.status = :status")
    List<Cliente> findByCpfContainingAndStatus(String cpf, Status status);

    Cliente findByCpf(String cpf);

    List<Cliente> findByNomeContainingIgnoreCaseAndStatus(String nome, Status status);

}