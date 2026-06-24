package web.controlevacinacao.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.controlevacinacao.filter.PedidoFilter;
import web.controlevacinacao.model.Pedido;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.repository.PedidoRepository;

@Service
public class PedidoService {

    private PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    public void salvar(Pedido pedido) {
        pedidoRepository.save(pedido);
    }

    @Transactional(readOnly = true)
    public Page<Pedido> pesquisar(PedidoFilter filtro, Pageable pageable) {
        return pedidoRepository.pesquisar(filtro, pageable);
    }

    @Transactional(readOnly = true)
    public Pedido buscar(Long codigo) {
        return pedidoRepository.findById(codigo).orElse(null);
    }

    @Transactional
    public void remover(Long codigo) {
        Pedido pedido = pedidoRepository.findById(codigo).get();
        pedido.setStatus(Status.INATIVO);
        pedidoRepository.save(pedido);
    }

}