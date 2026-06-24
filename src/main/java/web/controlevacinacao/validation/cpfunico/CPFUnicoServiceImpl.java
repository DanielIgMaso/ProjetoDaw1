package web.controlevacinacao.validation.cpfunico;

import java.security.InvalidParameterException;
import org.springframework.stereotype.Service;
import web.controlevacinacao.dto.ClienteDTOInput;
import web.controlevacinacao.model.Cliente;
import web.controlevacinacao.service.ClienteService;

@Service
public class CPFUnicoServiceImpl implements CPFUnicoService {
	private ClienteService clienteService;

	public CPFUnicoServiceImpl(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	@Override
	public boolean isValueUnique(Object value, String fieldName) throws UnsupportedOperationException {
		if (!fieldName.equals("cpf")) {
			throw new UnsupportedOperationException("A anotação deveria ser usada no atributo cpf");
		}

		Cliente novo = ((ClienteDTOInput) value).toCliente();
		//A validacao "foi preenchido um cpf" nao eh obrigacao dessa verificacao
		if (novo.getCpf() == null || novo.getCpf().isBlank()) {
			return true;
		}

		//Busca um cliente com esse CPF
		Cliente comEsseCPF = clienteService.buscarPeloCPF(novo.getCpf());

		//Nao existe um cliente com esse cpf, entao tudo bem
		if (comEsseCPF == null) {
			return true;
		} else {  //Existe um cliente com esse cpf
			//Estao tentando validar um novo cliente com um cpf que ja existe
			if (novo.getCodigo() == null) {
				return false;
			} else {  //O cliente sendo validado ja existe
				Cliente antigo = clienteService.buscar(novo.getCodigo());
				if (antigo == null) {
					throw new InvalidParameterException("O código do cliente a validar não existe.");
				}
				// Se o cpf sendo validado for o mesmo que ja existia no BD entao tudo bem
				if (comEsseCPF.equals(antigo)) {
					return true;
				}
				// Senao eh pq estao tentando validar um cpf que eh de outro cliente
				return false;
			}
		}
	}
}