package web.controlevacinacao.repository.queries.itempedido;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import web.controlevacinacao.filter.ItemDoPedidoFilter;
import web.controlevacinacao.model.ItemDoPedido;
import web.controlevacinacao.pagination.PaginacaoUtil;

public class ItemDoPedidoQueriesImpl implements ItemDoPedidoQueries {

    @PersistenceContext
    private EntityManager em;

    public Page<ItemDoPedido> pesquisar(ItemDoPedidoFilter filtro, Pageable pageable) {

        StringBuilder queryItens = new StringBuilder(
                "select distinct i from ItemDoPedido i join fetch i.pedido join fetch i.pedido.cliente join fetch i.produto");
        StringBuilder condicoes = new StringBuilder();
        Map<String, Object> parametros = new HashMap<>();

        preencherCondicoesEParametros(filtro, condicoes, parametros);

        if (condicoes.isEmpty()) {
            condicoes.append(" where i.status = 'ATIVO'");
        } else {
            condicoes.append(" and i.status = 'ATIVO'");
        }

        queryItens.append(condicoes);
        PaginacaoUtil.prepararOrdemJPQL(queryItens, "i", pageable);
        TypedQuery<ItemDoPedido> typedQuery = em.createQuery(queryItens.toString(), ItemDoPedido.class);
        PaginacaoUtil.prepararIntervalo(typedQuery, pageable);
        PaginacaoUtil.preencherParametros(parametros, typedQuery);
        List<ItemDoPedido> itens = typedQuery.getResultList();

        long totalItens =
                PaginacaoUtil.getTotalRegistros("ItemDoPedido", "i", condicoes, parametros, em);

        return new PageImpl<>(itens, pageable, totalItens);
    }

    private void preencherCondicoesEParametros(ItemDoPedidoFilter filtro, StringBuilder condicoes,
                                               Map<String, Object> parametros) {
        boolean condicao = false;

        if (filtro.getCodigoItem() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("i.codigo = :codigo");
            parametros.put("codigo", filtro.getCodigoItem());
            condicao = true;
        }
        if (filtro.getDataInicial() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("i.pedido.data >= :dataInicial");
            parametros.put("dataInicial", filtro.getDataInicial());
            condicao = true;
        }
        if (filtro.getDataFinal() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("i.pedido.data <= :dataFinal");
            parametros.put("dataFinal", filtro.getDataFinal());
            condicao = true;
        }
        if (filtro.getCliente() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("i.pedido.cliente = :cliente");
            parametros.put("cliente", filtro.getCliente());
            condicao = true;
        }
        if (filtro.getProduto() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("i.produto = :produto");
            parametros.put("produto", filtro.getProduto());
            condicao = true;
        }
    }

}