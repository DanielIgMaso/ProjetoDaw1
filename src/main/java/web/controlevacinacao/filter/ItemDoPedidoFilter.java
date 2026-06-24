package web.controlevacinacao.filter;

import java.time.LocalDate;
import web.controlevacinacao.model.Cliente;
import web.controlevacinacao.model.Produto;

public class ItemDoPedidoFilter {

    private Long codigoItem;
    private LocalDate dataInicial;
    private LocalDate dataFinal;
    private Cliente cliente;
    private Produto produto;

    public Long getCodigoItem() {
        return codigoItem;
    }

    public void setCodigoItem(Long codigoItem) {
        this.codigoItem = codigoItem;
    }

    public LocalDate getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(LocalDate dataInicial) {
        this.dataInicial = dataInicial;
    }

    public LocalDate getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(LocalDate dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @Override
    public String toString() {
        return "ItemDoPedidoFilter [codigoItem=" + codigoItem + ", dataInicial=" + dataInicial
                + ", dataFinal=" + dataFinal + ", cliente=" + cliente + ", produto=" + produto + "]";
    }

}