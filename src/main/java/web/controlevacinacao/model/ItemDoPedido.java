package web.controlevacinacao.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "item_pedido")
public class ItemDoPedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "gerador4", sequenceName = "item_pedido_codigo_seq", allocationSize = 1)
    @GeneratedValue(generator = "gerador4", strategy = GenerationType.SEQUENCE)
    private Long codigo;
    private int quantidade;
    @ManyToOne
    @JoinColumn(name = "codigo_pedido")
    private Pedido pedido;
    @ManyToOne
    @JoinColumn(name = "codigo_produto")
    private Produto produto;
    @Enumerated(EnumType.STRING)
    private Status status = Status.ATIVO;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ItemDoPedido [codigo=" + codigo + ", quantidade=" + quantidade + ", status=" + status + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ItemDoPedido other = (ItemDoPedido) obj;
        return Objects.equals(codigo, other.codigo);
    }

}