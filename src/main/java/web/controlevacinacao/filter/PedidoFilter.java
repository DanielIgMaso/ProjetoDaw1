package web.controlevacinacao.filter;

import java.time.LocalDate;
import web.controlevacinacao.model.Cliente;

public class PedidoFilter {

    private Long codigo;
    private LocalDate dataInicial;
    private LocalDate dataFinal;
    private Cliente cliente;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
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

    @Override
    public String toString() {
        return "PedidoFilter [codigo=" + codigo + ", dataInicial=" + dataInicial + ", dataFinal="
                + dataFinal + ", cliente=" + cliente + "]";
    }

}