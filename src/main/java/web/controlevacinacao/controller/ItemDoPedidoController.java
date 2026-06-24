package web.controlevacinacao.controller;

import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;
import web.controlevacinacao.dto.ItemDoPedidoDTOInput;
import web.controlevacinacao.model.ItemDoPedido;
import web.controlevacinacao.model.Pedido;
import web.controlevacinacao.model.Produto;
import web.controlevacinacao.service.ItemDoPedidoService;
import web.controlevacinacao.service.PedidoService;
import web.controlevacinacao.service.ProdutoService;

@Controller
@RequestMapping("/itempedido")
public class ItemDoPedidoController {

    private static final Logger logger = LoggerFactory.getLogger(ItemDoPedidoController.class);

    private ItemDoPedidoService itemDoPedidoService;
    private PedidoService pedidoService;
    private ProdutoService produtoService;

    public ItemDoPedidoController(ItemDoPedidoService itemDoPedidoService, PedidoService pedidoService,
                                  ProdutoService produtoService) {
        this.itemDoPedidoService = itemDoPedidoService;
        this.pedidoService = pedidoService;
        this.produtoService = produtoService;
    }

    @GetMapping("/pesquisarproduto")
    public String pesquisarProduto(String produtoBusca, Model model) {
        List<Produto> produtos = produtoService.pesquisarGeral(produtoBusca);
        logger.debug("Produtos buscados: {}", produtos);
        model.addAttribute("produtos", produtos);
        return "produtos/listar :: lista";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@Valid ItemDoPedidoDTOInput dto, BindingResult resultado, Model model) {
        Long codigoPedido = dto.getPedido() != null ? dto.getPedido().getCodigo() : null;

        if (resultado.hasErrors()) {
            logger.info("O item recebido para cadastrar não é válido.");
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("{}", erro);
            }
            for (ObjectError erro : resultado.getGlobalErrors()) {
                logger.info("{}", erro);
            }
        } else {
            itemDoPedidoService.salvar(dto.toItemDoPedido());
        }

        return montarFragmentoItens(codigoPedido, model);
    }

    @GetMapping("/remover/{codigo}/pedido/{codigoPedido}")
    public String remover(@PathVariable Long codigo, @PathVariable Long codigoPedido, Model model) {
        itemDoPedidoService.remover(codigo);
        return montarFragmentoItens(codigoPedido, model);
    }

    private String montarFragmentoItens(Long codigoPedido, Model model) {
        Pedido pedido = pedidoService.buscar(codigoPedido);
        List<ItemDoPedido> itens = itemDoPedidoService.listarPorPedido(codigoPedido);

        BigDecimal total = BigDecimal.ZERO;
        for (ItemDoPedido item : itens) {
            total = total.add(item.getProduto().getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())));
        }

        ItemDoPedidoDTOInput novoItemDto = new ItemDoPedidoDTOInput();
        novoItemDto.setPedido(pedido);

        model.addAttribute("pedido", pedido);
        model.addAttribute("itens", itens);
        model.addAttribute("total", total);
        model.addAttribute("itemDoPedidoDTOInput", novoItemDto);
        return "pedidos/itens :: listaItens";
    }

}