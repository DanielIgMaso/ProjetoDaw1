package web.controlevacinacao.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.controlevacinacao.filter.ItemDoPedidoFilter;
import web.controlevacinacao.model.ItemDoPedido;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.repository.ItemDoPedidoRepository;

@Service
public class ItemDoPedidoService {

    private ItemDoPedidoRepository itemDoPedidoRepository;

    public ItemDoPedidoService(ItemDoPedidoRepository itemDoPedidoRepository) {
        this.itemDoPedidoRepository = itemDoPedidoRepository;
    }

    @Transactional
    public void salvar(ItemDoPedido item) {
        itemDoPedidoRepository.save(item);
    }

    @Transactional(readOnly = true)
    public Page<ItemDoPedido> pesquisar(ItemDoPedidoFilter filtro, Pageable pageable) {
        return itemDoPedidoRepository.pesquisar(filtro, pageable);
    }

    @Transactional(readOnly = true)
    public List<ItemDoPedido> listarPorPedido(Long codigoPedido) {
        return itemDoPedidoRepository.findByPedidoCodigoAndStatusOrderByCodigo(codigoPedido, Status.ATIVO);
    }

    @Transactional
    public void remover(Long codigo) {
        ItemDoPedido item = itemDoPedidoRepository.findById(codigo).get();
        item.setStatus(Status.INATIVO);
        itemDoPedidoRepository.save(item);
    }

}