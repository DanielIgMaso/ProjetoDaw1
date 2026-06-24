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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import web.controlevacinacao.dto.ItemDoPedidoDTOInput;
import web.controlevacinacao.dto.PedidoDTOInput;
import web.controlevacinacao.filter.PedidoFilter;
import web.controlevacinacao.model.ItemDoPedido;
import web.controlevacinacao.model.Pedido;
import web.controlevacinacao.notification.NotificacaoSweetAlert2;
import web.controlevacinacao.notification.TipoNotificaoSweetAlert2;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.service.ItemDoPedidoService;
import web.controlevacinacao.service.PedidoService;
import web.controlevacinacao.model.Cliente;
import web.controlevacinacao.service.ClienteService;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/pedido")
public class PedidoController {

    private static final Logger logger = LoggerFactory.getLogger(PedidoController.class);

    private PedidoService pedidoService;
    private ItemDoPedidoService itemDoPedidoService;
    private ClienteService clienteService;

    public PedidoController(PedidoService pedidoService, ItemDoPedidoService itemDoPedidoService,
                            ClienteService clienteService) {
        this.pedidoService = pedidoService;
        this.itemDoPedidoService = itemDoPedidoService;
        this.clienteService = clienteService;
    }

    @GetMapping("/pesquisarcliente")
    public String pesquisarCliente(String clienteBusca, Model model) {
        List<Cliente> clientes = clienteService.pesquisarGeral(clienteBusca);
        model.addAttribute("clientes", clientes);
        return "clientes/listar :: lista";
    }
    @GetMapping("/abrirpesquisar")
    public String abrirPesquisa() {
        return "pedidos/pesquisar :: formulario";
    }

    @GetMapping("/pesquisar")
    public String pesquisar(PedidoFilter filtro, Model model,
                            @PageableDefault(size = 9) @SortDefault(sort = "codigo",
                                    direction = Sort.Direction.ASC) Pageable pageable,
                            HttpServletRequest request) {
        Page<Pedido> pagina = pedidoService.pesquisar(filtro, pageable);
        logger.info("Pedidos pesquisados: {}", pagina.getContent());
        PageWrapper<Pedido> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "pedidos/mostrar :: tabela";
    }

    @GetMapping("/cadastrar")
    public String abrirCadastro(PedidoDTOInput dto) {
        return "pedidos/cadastrar :: formulario";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@Valid PedidoDTOInput dto, BindingResult resultado,
                            RedirectAttributes atributos) {
        if (resultado.hasErrors()) {
            logger.info("O pedido recebido para cadastrar não é válido.");
            logger.info("Erros encontrados:");
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("{}", erro);
            }
            for (ObjectError erro : resultado.getGlobalErrors()) {
                logger.info("{}", erro);
            }
            return "pedidos/cadastrar :: formulario";
        } else {
            Pedido pedido = dto.toPedido();
            pedidoService.salvar(pedido);
            atributos.addFlashAttribute("notificacaoSA2", new NotificacaoSweetAlert2(
                    "Pedido cadastrado com sucesso! Agora adicione os itens.", TipoNotificaoSweetAlert2.SUCCESS, 4000));
            return "redirect:/pedido/" + pedido.getCodigo() + "/itens";
        }
    }

    @GetMapping("/{codigo}/itens")
    public String abrirItens(@PathVariable Long codigo, Model model) {
        Pedido pedido = pedidoService.buscar(codigo);
        if (pedido == null) {
            model.addAttribute("mensagem", "Não foi encontrado um pedido com esse código");
            return "mensagem :: texto";
        }

        List<ItemDoPedido> itens = itemDoPedidoService.listarPorPedido(codigo);
        BigDecimal total = calcularTotal(itens);

        ItemDoPedidoDTOInput novoItemDto = new ItemDoPedidoDTOInput();
        novoItemDto.setPedido(pedido);

        model.addAttribute("pedido", pedido);
        model.addAttribute("itens", itens);
        model.addAttribute("total", total);
        model.addAttribute("itemDoPedidoDTOInput", novoItemDto);
        return "pedidos/itens :: pagina";
    }

    private BigDecimal calcularTotal(List<ItemDoPedido> itens) {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemDoPedido item : itens) {
            BigDecimal subtotal = item.getProduto().getPreco().multiply(BigDecimal.valueOf(item.getQuantidade()));
            total = total.add(subtotal);
        }
        return total;
    }

    @GetMapping("/remover/{codigo}")
    public String remover(@PathVariable Long codigo, RedirectAttributes atributos) {
        pedidoService.remover(codigo);
        atributos.addFlashAttribute("notificacaoSA2", new NotificacaoSweetAlert2("Pedido removido com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
        return "redirect:/pedido/abrirpesquisar";
    }

}