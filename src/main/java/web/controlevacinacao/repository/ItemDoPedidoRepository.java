package web.controlevacinacao.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import web.controlevacinacao.model.ItemDoPedido;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.repository.queries.itempedido.ItemDoPedidoQueries;

public interface ItemDoPedidoRepository extends JpaRepository<ItemDoPedido, Long>, ItemDoPedidoQueries {

    List<ItemDoPedido> findByPedidoCodigoAndStatusOrderByCodigo(Long codigoPedido, Status status);

}