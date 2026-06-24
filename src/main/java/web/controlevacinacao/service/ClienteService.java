package web.controlevacinacao.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.controlevacinacao.filter.ClienteFilter;
import web.controlevacinacao.model.Cliente;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.repository.ClienteRepository;

@Service
public class ClienteService {

    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);

    private ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public Page<Cliente> pesquisar(ClienteFilter filtro, Pageable pageable) {
        logger.info("Pesquisando clientes com o filtro {}", filtro);
        return clienteRepository.pesquisar(filtro, pageable);
    }

    @Transactional(readOnly = true)
    public Cliente buscarPeloCPF(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }

    @Transactional(readOnly = true)
    public List<Cliente> buscarContendoCPF(String cpf) {
        return clienteRepository.findByCpfContainingAndStatus(cpf, Status.ATIVO);
    }

    @Transactional
    public void salvar(Cliente cliente) {
        logger.info("Salvando cliente: {}", cliente);
        clienteRepository.save(cliente);
    }

    @Transactional
    public void atualizar(Cliente cliente) {
        logger.info("Atualizando cliente: {}", cliente);
        clienteRepository.save(cliente);
    }

    @Transactional
    public void remover(Long codigo) {
        logger.info("Removendo cliente com código: {}", codigo);
        clienteRepository.deleteById(codigo);
    }

    @Transactional(readOnly = true)
    public Cliente buscar(Long codigo) {
        logger.info("Buscando o cliente com código: {}", codigo);
        return clienteRepository.findByCodigoAndStatus(codigo, Status.ATIVO).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Cliente> pesquisarGeral(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCaseAndStatus(nome, Status.ATIVO);
    }
}