package web.controlevacinacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.controlevacinacao.model.Pedido;
import web.controlevacinacao.repository.queries.pedido.PedidoQueries;

public interface PedidoRepository extends JpaRepository<Pedido, Long>, PedidoQueries {

}