package web.controlevacinacao.repository.queries.pedido;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import web.controlevacinacao.filter.PedidoFilter;
import web.controlevacinacao.model.Pedido;
import web.controlevacinacao.pagination.PaginacaoUtil;

public class PedidoQueriesImpl implements PedidoQueries {

    @PersistenceContext
    private EntityManager em;

    public Page<Pedido> pesquisar(PedidoFilter filtro, Pageable pageable) {

        StringBuilder queryPedidos = new StringBuilder("select distinct p from Pedido p join fetch p.cliente");
        StringBuilder condicoes = new StringBuilder();
        Map<String, Object> parametros = new HashMap<>();

        preencherCondicoesEParametros(filtro, condicoes, parametros);

        if (condicoes.isEmpty()) {
            condicoes.append(" where p.status = 'ATIVO'");
        } else {
            condicoes.append(" and p.status = 'ATIVO'");
        }

        queryPedidos.append(condicoes);
        PaginacaoUtil.prepararOrdemJPQL(queryPedidos, "p", pageable);
        TypedQuery<Pedido> typedQuery = em.createQuery(queryPedidos.toString(), Pedido.class);
        PaginacaoUtil.prepararIntervalo(typedQuery, pageable);
        PaginacaoUtil.preencherParametros(parametros, typedQuery);
        List<Pedido> pedidos = typedQuery.getResultList();

        long totalPedidos =
                PaginacaoUtil.getTotalRegistros("Pedido", "p", condicoes, parametros, em);

        return new PageImpl<>(pedidos, pageable, totalPedidos);
    }

    private void preencherCondicoesEParametros(PedidoFilter filtro, StringBuilder condicoes,
                                               Map<String, Object> parametros) {
        boolean condicao = false;

        if (filtro.getCodigo() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("p.codigo = :codigo");
            parametros.put("codigo", filtro.getCodigo());
            condicao = true;
        }
        if (filtro.getDataInicial() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("p.data >= :dataInicial");
            parametros.put("dataInicial", filtro.getDataInicial());
            condicao = true;
        }
        if (filtro.getDataFinal() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("p.data <= :dataFinal");
            parametros.put("dataFinal", filtro.getDataFinal());
            condicao = true;
        }
        if (filtro.getCliente() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("p.cliente = :cliente");
            parametros.put("cliente", filtro.getCliente());
            condicao = true;
        }
    }

}