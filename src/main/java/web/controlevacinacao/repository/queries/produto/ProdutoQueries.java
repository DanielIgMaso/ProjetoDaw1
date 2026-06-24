package web.controlevacinacao.repository.queries.produto;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import web.controlevacinacao.filter.ProdutoFilter;
import web.controlevacinacao.model.Produto;

public interface ProdutoQueries {

    Page<Produto> pesquisar(ProdutoFilter filtro, Pageable pageable);

    List<Produto> pesquisar(ProdutoFilter filtro);

    List<Produto> pesquisarGeral(String filtro);

}