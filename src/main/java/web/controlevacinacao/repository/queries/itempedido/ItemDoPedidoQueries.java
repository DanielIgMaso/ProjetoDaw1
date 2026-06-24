package web.controlevacinacao.repository.queries.itempedido;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import web.controlevacinacao.filter.ItemDoPedidoFilter;
import web.controlevacinacao.model.ItemDoPedido;

public interface ItemDoPedidoQueries {

    Page<ItemDoPedido> pesquisar(ItemDoPedidoFilter filtro, Pageable pageable);

}