package web.controlevacinacao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import web.controlevacinacao.dto.ProdutoDTOInput;
import web.controlevacinacao.filter.ProdutoFilter;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Produto;
import web.controlevacinacao.notification.NotificacaoSweetAlert2;
import web.controlevacinacao.notification.TipoNotificaoSweetAlert2;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.service.ProdutoService;

@Controller
public class ProdutoController {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoController.class);

    private ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping("/produto/abrirpesquisar")
    public String abrirPesquisa() {
        return "produtos/pesquisar :: formulario";
    }

    @GetMapping("/produto/pesquisar")
    public String pesquisar(ProdutoFilter filtro, Model model,
                            @PageableDefault(size = 9) @SortDefault(sort = "codigo",
                                    direction = Sort.Direction.ASC) Pageable pageable,
                            HttpServletRequest request) {
        Page<Produto> pagina = produtoService.pesquisar(filtro, pageable);
        logger.info("Produtos pesquisados: {}", pagina.getContent());
        PageWrapper<Produto> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "produtos/mostrar :: tabela";
    }

    @GetMapping("/produto/cadastrar")
    public String abrirCadastro(ProdutoDTOInput dto) {
        return "produtos/cadastrar :: formulario";
    }

    @PostMapping("/produto/cadastrar")
    public String cadastrar(@Valid ProdutoDTOInput dto, BindingResult resultado,
                            RedirectAttributes atributos) {
        if (resultado.hasErrors()) {
            logger.info("O produto recebido para cadastrar não é válido.");
            logger.info("Erros encontrados:");
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("{}", erro);
            }
            for (ObjectError erro : resultado.getGlobalErrors()) {
                logger.info("{}", erro);
            }
            return "produtos/cadastrar :: formulario";
        } else {
            produtoService.salvar(dto.toProduto());
            atributos.addFlashAttribute("notificacaoSA2", new NotificacaoSweetAlert2("Produto cadastrado com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
            return "redirect:/produto/cadastrar";
        }
    }

    @GetMapping("/produto/alterar/{codigo}")
    public String abrirAlterar(@PathVariable Long codigo, Model model) {
        ProdutoDTOInput dto = ProdutoDTOInput.fromProduto(produtoService.buscar(codigo));
        model.addAttribute("produtoDTOInput", dto);
        return "produtos/alterar :: formulario";
    }

    @PostMapping("/produto/alterar")
    public String alterar(@Valid ProdutoDTOInput dto, BindingResult resultado, RedirectAttributes atributos) {
        if (resultado.hasErrors()) {
            logger.info("O produto recebido para alterar não é válido.");
            logger.info("Erros encontrados:");
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("{}", erro);
            }
            for (ObjectError erro : resultado.getGlobalErrors()) {
                logger.info("{}", erro);
            }
            return "produtos/alterar :: formulario";
        } else {
            produtoService.atualizar(dto.toProduto());
            atributos.addFlashAttribute("notificacaoSA2", new NotificacaoSweetAlert2("Produto alterado com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
            return "redirect:/produto/abrirpesquisar";
        }
    }

    @GetMapping("/produto/remover/{codigo}")
    public String remover(@PathVariable Long codigo, RedirectAttributes atributos) {
        Produto produto = produtoService.buscar(codigo);
        if (produto != null) {
            produto.setStatus(Status.INATIVO);
            produtoService.atualizar(produto);
            atributos.addFlashAttribute("notificacaoSA2", new NotificacaoSweetAlert2("Produto removido com sucesso", TipoNotificaoSweetAlert2.SUCCESS, 4000));
        } else {
            atributos.addFlashAttribute("notificacaoSA2", new NotificacaoSweetAlert2("Não foi encontrado um produto com esse codigo", TipoNotificaoSweetAlert2.SUCCESS, 4000));
        }
        return "redirect:/produto/abrirpesquisar";
    }

}