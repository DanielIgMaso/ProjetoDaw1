package web.controlevacinacao.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.controlevacinacao.model.Usuario;
import web.controlevacinacao.repository.UsuarioRepository;

@Service
public class CadastroUsuarioService {

	private UsuarioRepository usuarioRepository;

	public CadastroUsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Transactional
	public void salvar(Usuario usuario) {
		usuarioRepository.save(usuario);
	}
}
