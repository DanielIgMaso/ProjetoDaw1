package web.controlevacinacao.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.controlevacinacao.filter.ProdutoFilter;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Produto;
import web.controlevacinacao.repository.ProdutoRepository;

@Service
public class ProdutoService {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoService.class);

    private ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional(readOnly = true)
    public List<Produto> pesquisar(ProdutoFilter filtro) {
        logger.info("Pesquisando produtos com o filtro {}", filtro);
        return produtoRepository.pesquisar(filtro);
    }

    @Transactional(readOnly = true)
    public Page<Produto> pesquisar(ProdutoFilter filtro, Pageable pageable) {
        logger.info("Pesquisando produtos com o filtro {}", filtro);
        return produtoRepository.pesquisar(filtro, pageable);
    }

    @Transactional
    public void salvar(Produto produto) {
        logger.info("Salvando produto: {}", produto);
        produtoRepository.save(produto);
    }

    @Transactional
    public void atualizar(Produto produto) {
        logger.info("Atualizando produto: {}", produto);
        produtoRepository.save(produto);
    }

    @Transactional
    public void remover(Long codigo) {
        logger.info("Removendo produto com código: {}", codigo);
        produtoRepository.deleteById(codigo);
    }

    @Transactional(readOnly = true)
    public Produto buscar(Long codigo) {
        logger.info("Buscando o produto com código: {}", codigo);
        return produtoRepository.findByCodigoAndStatus(codigo, Status.ATIVO).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Produto> pesquisarGeral(String busca) {
        return produtoRepository.pesquisarGeral(busca);
    }
}