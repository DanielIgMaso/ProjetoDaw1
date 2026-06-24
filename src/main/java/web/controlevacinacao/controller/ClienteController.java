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
import web.controlevacinacao.dto.ClienteDTOInput;
import web.controlevacinacao.filter.ClienteFilter;
import web.controlevacinacao.model.Cliente;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.notification.NotificacaoSweetAlert2;
import web.controlevacinacao.notification.TipoNotificaoSweetAlert2;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.service.ClienteService;


@Controller
public class ClienteController {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    private ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/cliente/abrirpesquisar")
    public String abrirPesquisa() {
        return "clientes/pesquisar :: formulario";
    }

    @GetMapping("/cliente/pesquisar")
    public String pesquisar(ClienteFilter filtro, Model model,
                            @PageableDefault(size = 9) @SortDefault(sort = "codigo",
                                    direction = Sort.Direction.ASC) Pageable pageable,
                            HttpServletRequest request) {
        Page<Cliente> pagina = clienteService.pesquisar(filtro, pageable);
        logger.info("Clientes pesquisados: {}", pagina.getContent());
        PageWrapper<Cliente> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "clientes/mostrar :: tabela";
    }

    @GetMapping("/cliente/cadastrar")
    public String abrirCadastro(ClienteDTOInput dto) {
        return "clientes/cadastrar :: formulario";
    }

    @PostMapping("/cliente/cadastrar")
    public String cadastrar(@Valid ClienteDTOInput dto, BindingResult resultado,
                            RedirectAttributes atributos) {
        if (resultado.hasErrors()) {
            logger.info("O cliente recebido para cadastrar não é válido.");
            logger.info("Erros encontrados:");
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("{}", erro);
            }
            for (ObjectError erro : resultado.getGlobalErrors()) {
                logger.info("{}", erro);
            }
            return "clientes/cadastrar :: formulario";
        } else {
            clienteService.salvar(dto.toCliente());
            atributos.addFlashAttribute("notificacaoSA2", new NotificacaoSweetAlert2(
                    "Cliente cadastrado com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
            return "redirect:/cliente/cadastrar";
        }
    }

    @GetMapping("/cliente/alterar/{codigo}")
    public String abrirAlterar(@PathVariable Long codigo, Model model) {
        ClienteDTOInput dto = ClienteDTOInput.fromCliente(clienteService.buscar(codigo));
        model.addAttribute("clienteDTOInput", dto);
        return "clientes/alterar :: formulario";
    }

    @PostMapping("/cliente/alterar")
    public String alterar(@Valid ClienteDTOInput dto, BindingResult resultado,
                          RedirectAttributes atributos) {
        if (resultado.hasErrors()) {
            logger.info("O cliente recebido para alterar não é válido.");
            logger.info("Erros encontrados:");
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("{}", erro);
            }
            for (ObjectError erro : resultado.getGlobalErrors()) {
                logger.info("{}", erro);
            }
            return "clientes/alterar :: formulario";
        } else {
            clienteService.atualizar(dto.toCliente());
            atributos.addFlashAttribute("notificacaoSA2", new NotificacaoSweetAlert2("Cliente alterado com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
            return "redirect:/cliente/abrirpesquisar";
        }
    }

    @GetMapping("/cliente/remover/{codigo}")
    public String remover(@PathVariable Long codigo, RedirectAttributes atributos) {
        Cliente cliente = clienteService.buscar(codigo);
        if (cliente != null) {
            cliente.setStatus(Status.INATIVO);
            clienteService.atualizar(cliente);
            atributos.addFlashAttribute("mensagem", "Cliente removido com sucesso");
        } else {
            atributos.addFlashAttribute("mensagem",
                    "Não foi encontrado um cliente com esse codigo");
        }
        return "redirect:/mensagem";
    }

}