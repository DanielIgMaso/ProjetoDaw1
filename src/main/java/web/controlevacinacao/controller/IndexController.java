package web.controlevacinacao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import web.controlevacinacao.notification.NotificacaoSweetAlert2;
import web.controlevacinacao.notification.TipoNotificaoSweetAlert2;

@Controller
public class IndexController {

	@GetMapping(value = { "/", "/index.html" })
	public String indexHTMX() {
		return "index :: conteudo";
	}

	@GetMapping("/login")
	public String login() {
		return "login :: formulario";
	}

	@PostMapping("/login-error")
	public String loginError(String username, Model model) {
		model.addAttribute("username", username);
		model.addAttribute("notificacaoSA2", new NotificacaoSweetAlert2(
				"Usuário e/ou senha inválidos.", TipoNotificaoSweetAlert2.ERROR));
		return "login :: formulario";
	}

	@GetMapping("/layout/header-atualizado")
	public String obterHeaderAtualizado() {
		// Retorna o fragmento específico que contém os botões de login/logout
		return "layout/fragments/header :: usuariologinlogout";
	}

	@GetMapping("/layout/menu-atualizado")
	public String obterMenuAtualizado() {
		// Retorna o fragmento específico que contém o menu
		return "layout/fragments/menu :: menu";
	}

}