package web.controlevacinacao.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.NumberFormat;
import web.controlevacinacao.model.Produto;
import web.controlevacinacao.model.Status;

public class ProdutoDTOInput {

    private Long codigo;
    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 255, message = "O tamanho máximo do nome é 255 caracteres")
    private String nome;
    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 255, message = "O tamanho máximo da descrição é 255 caracteres")
    private String descricao;
    @NotNull(message = "O preço é obrigatório")
    @NumberFormat(pattern = "#,##0.00")
    private BigDecimal preco;
    private Status status = Status.ATIVO;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Produto toProduto() {
        Produto produto = new Produto();
        produto.setCodigo(codigo);
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setPreco(preco);
        produto.setStatus(status);
        return produto;
    }

    public static ProdutoDTOInput fromProduto(Produto produto) {
        ProdutoDTOInput dto = new ProdutoDTOInput();
        dto.setCodigo(produto.getCodigo());
        dto.setNome(produto.getNome());
        dto.setDescricao(produto.getDescricao());
        dto.setPreco(produto.getPreco());
        dto.setStatus(produto.getStatus());
        return dto;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ProdutoDTOInput [codigo=" + codigo + ", nome=" + nome + ", descricao=" + descricao
                + ", preco=" + preco + ", status=" + status + "]";
    }

}