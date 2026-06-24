package web.controlevacinacao.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import web.controlevacinacao.model.Cliente;
import web.controlevacinacao.model.Pedido;
import web.controlevacinacao.model.Status;

public class PedidoDTOInput {

    private Long codigo;
    @NotNull(message = "A data do pedido é obrigatória")
    private LocalDate data;
    @NotNull(message = "É preciso selecionar um cliente")
    private Cliente cliente;
    private Status status = Status.ATIVO;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Pedido toPedido() {
        Pedido pedido = new Pedido();
        pedido.setCodigo(codigo);
        pedido.setData(data);
        pedido.setCliente(cliente);
        pedido.setStatus(status);
        return pedido;
    }

    public static PedidoDTOInput fromPedido(Pedido pedido) {
        PedidoDTOInput dto = new PedidoDTOInput();
        dto.setCodigo(pedido.getCodigo());
        dto.setData(pedido.getData());
        dto.setCliente(pedido.getCliente());
        dto.setStatus(pedido.getStatus());
        return dto;
    }

    @Override
    public String toString() {
        return "PedidoDTOInput [codigo=" + codigo + ", data=" + data + ", status=" + status + "]";
    }

}