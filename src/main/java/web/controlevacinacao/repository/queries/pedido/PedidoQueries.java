package web.controlevacinacao.repository.queries.pedido;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import web.controlevacinacao.filter.PedidoFilter;
import web.controlevacinacao.model.Pedido;

public interface PedidoQueries {

    Page<Pedido> pesquisar(PedidoFilter filtro, Pageable pageable);

}