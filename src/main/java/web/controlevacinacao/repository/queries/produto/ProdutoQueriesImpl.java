package web.controlevacinacao.repository.queries.produto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import web.controlevacinacao.filter.ProdutoFilter;
import web.controlevacinacao.model.Produto;
import web.controlevacinacao.pagination.PaginacaoUtil;

public class ProdutoQueriesImpl implements ProdutoQueries {

    @PersistenceContext
    private EntityManager em;

    public List<Produto> pesquisar(ProdutoFilter filtro) {
        StringBuilder queryProdutos = new StringBuilder("select distinct p from Produto p");
        StringBuilder condicoes = new StringBuilder();
        Map<String, Object> parametros = new HashMap<>();
        preencherCondicoesEParametros(filtro, condicoes, parametros);
        if (condicoes.isEmpty()) {
            condicoes.append(" where p.status = 'ATIVO'");
        } else {
            condicoes.append(" and p.status = 'ATIVO'");
        }
        queryProdutos.append(condicoes);
        queryProdutos.append(" order by p.codigo");
        TypedQuery<Produto> typedQuery = em.createQuery(queryProdutos.toString(), Produto.class);
        PaginacaoUtil.preencherParametros(parametros, typedQuery);
        List<Produto> produtos = typedQuery.getResultList();
        return produtos;
    }

    public Page<Produto> pesquisar(ProdutoFilter filtro, Pageable pageable) {

        StringBuilder queryProdutos = new StringBuilder("select distinct p from Produto p");
        StringBuilder condicoes = new StringBuilder();
        Map<String, Object> parametros = new HashMap<>();

        preencherCondicoesEParametros(filtro, condicoes, parametros);

        if (condicoes.isEmpty()) {
            condicoes.append(" where p.status = 'ATIVO'");
        } else {
            condicoes.append(" and p.status = 'ATIVO'");
        }

        queryProdutos.append(condicoes);
        PaginacaoUtil.prepararOrdemJPQL(queryProdutos, "p", pageable);
        TypedQuery<Produto> typedQuery = em.createQuery(queryProdutos.toString(), Produto.class);
        PaginacaoUtil.prepararIntervalo(typedQuery, pageable);
        PaginacaoUtil.preencherParametros(parametros, typedQuery);
        List<Produto> produtos = typedQuery.getResultList();

        long totalProdutos =
                PaginacaoUtil.getTotalRegistros("Produto", "p", condicoes, parametros, em);

        return new PageImpl<>(produtos, pageable, totalProdutos);
    }

    public List<Produto> pesquisarGeral(String filtro) {
        StringBuilder queryProdutos = new StringBuilder("select distinct p from Produto p");
        StringBuilder condicoes = new StringBuilder();
        Map<String, Object> parametros = new HashMap<>();
        preencherCondicoesEParametros(filtro, condicoes, parametros);
        if (condicoes.isEmpty()) {
            condicoes.append(" where p.status = 'ATIVO'");
        } else {
            condicoes.append(" and p.status = 'ATIVO'");
        }
        queryProdutos.append(condicoes);
        TypedQuery<Produto> typedQuery = em.createQuery(queryProdutos.toString(), Produto.class);
        PaginacaoUtil.preencherParametros(parametros, typedQuery);
        List<Produto> produtos = typedQuery.getResultList();
        return produtos;
    }

    private void preencherCondicoesEParametros(String filtro, StringBuilder condicoes,
                                               Map<String, Object> parametros) {
        boolean condicao = false;
        try {
            Long codigo = Long.parseLong(filtro);
            if (!condicao) {
                condicoes.append(" where ");
            } else {
                condicoes.append(" or ");
            }
            condicoes.append("p.codigo = :codigo");
            parametros.put("codigo", codigo);
            condicao = true;
        } catch (NumberFormatException e) {
            if (!condicao) {
                condicoes.append(" where ");
            } else {
                condicoes.append(" or ");
            }
            condicoes.append("lower(p.nome) like :nome");
            parametros.put("nome", "%" + filtro.toLowerCase() + "%");
            condicao = true;
        }
    }

    private void preencherCondicoesEParametros(ProdutoFilter filtro, StringBuilder condicoes,
                                               Map<String, Object> parametros) {
        boolean condicao = false;

        if (filtro.getCodigo() != null) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("p.codigo = :codigo");
            parametros.put("codigo", filtro.getCodigo());
            condicao = true;
        }
        if (StringUtils.hasText(filtro.getNome())) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("lower(p.nome) like :nome");
            parametros.put("nome", "%" + filtro.getNome().toLowerCase() + "%");
            condicao = true;
        }
        if (StringUtils.hasText(filtro.getDescricao())) {
            PaginacaoUtil.fazerLigacaoCondicoes(condicoes, condicao);
            condicoes.append("lower(p.descricao) like :descricao");
            parametros.put("descricao", "%" + filtro.getDescricao().toLowerCase() + "%");
        }
    }

}