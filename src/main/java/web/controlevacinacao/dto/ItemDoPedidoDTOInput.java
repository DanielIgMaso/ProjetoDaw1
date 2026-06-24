package web.controlevacinacao.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import web.controlevacinacao.model.ItemDoPedido;
import web.controlevacinacao.model.Pedido;
import web.controlevacinacao.model.Produto;
import web.controlevacinacao.model.Status;

public class ItemDoPedidoDTOInput {

    private Long codigo;
    @NotNull(message = "É preciso selecionar um pedido")
    private Pedido pedido;
    @NotNull(message = "É preciso selecionar um produto")
    private Produto produto;
    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser maior que zero")
    private Integer quantidade;
    private Status status = Status.ATIVO;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ItemDoPedido toItemDoPedido() {
        ItemDoPedido item = new ItemDoPedido();
        item.setCodigo(codigo);
        item.setPedido(pedido);
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setStatus(status);
        return item;
    }

    @Override
    public String toString() {
        return "ItemDoPedidoDTOInput [codigo=" + codigo + ", quantidade=" + quantidade
                + ", status=" + status + "]";
    }

}