package web.controlevacinacao.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;
import web.controlevacinacao.model.Opcoes;
import web.controlevacinacao.model.Teste;


@Controller
public class TesteController {

    @GetMapping("/teste/pesquisar")
    public String abrirPesquisar() {
        return "teste/pesquisar :: formulario";
    }

    @PostMapping("/teste/pesquisar")
    public String pesquisar(Teste teste) {
        return "teste/pesquisar :: formulario";
    }

    @GetMapping("/teste/cadastrar")
    public String abrirCadastrar(Teste teste) {
        return "teste/cadastrar :: formulario";
    }

    @PostMapping("/teste/cadastrar")
    public String cadastrar(@Valid Teste teste, BindingResult resultado) {
        if (resultado.hasErrors()) {
            return "teste/cadastrar :: formulario";
        } else {
            return "redirect:/teste/cadastrar";
        }
    }

    @GetMapping("/teste/alterar")
    public String abrirAlterar(Teste teste) {
        teste.setCodigo(1000L);
        teste.setNome("Grosbilda");
        teste.setEmail("graobilda@seilah.com");
        teste.setSenha("12345");
        teste.setCpf("123.456.789-09");
        teste.setData(LocalDate.now());
        teste.setHora(LocalTime.now());
        teste.setValor(new BigDecimal("1537258.45"));
        teste.setQtd(1546784);
        teste.setPeso(96.4);
        teste.setObservacao("um monte de texto inutil so para encher linguica");
        teste.getOpcoes().add(Opcoes.OPCAO2);
        teste.setOpcao(Opcoes.OPCAO3);
        return "teste/alterar :: formulario";
    }

    @PostMapping("/teste/alterar")
    public String alterar(@Valid Teste teste, BindingResult resultado) {
        if (resultado.hasErrors()) {
            return "teste/alterar :: formulario";
        } else {
            return "redirect:/teste/alterar";
        }
    }

    
}
