package web.controlevacinacao.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Teste {
    @NotNull(message = "O nome é obrigatório")
    private String nome;
    @NotNull(message = "O email é obrigatório")
    private String email;
    @NotNull(message = "A senha é obrigatória")
    private String senha;
    @NotNull(message = "O código é obrigatório")
    private Long codigo;
    @NumberFormat(pattern = "#,##0.00")
    @NotNull(message = "O valor é obrigatório")
    private BigDecimal valor;
    @NotNull(message = "O CPF é obrigatório")
    private String cpf;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @NotNull(message = "A data é obrigatória")
    private LocalDate data;
    @NotNull(message = "A hora é obrigatória")
    private LocalTime hora;
    @NumberFormat(pattern = "#,##0")
    @NotNull(message = "A quantidade é obrigatória")
    private Integer qtd;
    @NumberFormat(pattern = "#,##0.00")
    @NotNull(message = "O peso é obrigatório")
    private Double peso;
    @NotNull(message = "A observacao é obrigatória")
    private String observacao;
    @NotNull(message = "Se está desativado ou não é obrigatório")
    @AssertTrue(message = "Tem que ser verdadeiro")
    private Boolean desativado;
    @Size(min = 1, message="Escolha ao menos uma")
    private List<Opcoes> opcoes = new ArrayList<>();
    @NotNull(message = "A opção é obrigatória")
    private Opcoes opcao = Opcoes.OPCAO1;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Integer getQtd() {
        return qtd;
    }

    public void setQtd(Integer qtd) {
        this.qtd = qtd;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Boolean getDesativado() {
        return desativado;
    }

    public void setDesativado(Boolean desativado) {
        this.desativado = desativado;
    }

    public List<Opcoes> getOpcoes() {
        return opcoes;
    }

    public void setOpcoes(List<Opcoes> opcoes) {
        this.opcoes = opcoes;
    }

    public Opcoes getOpcao() {
        return opcao;
    }

    public void setOpcao(Opcoes opcao) {
        this.opcao = opcao;
    }

    @Override
    public String toString() {
        return "Teste [nome=" + nome + ", email=" + email + ", senha=" + senha + ", codigo="
                + codigo + ", valor=" + valor + ", cpf=" + cpf + ", data=" + data + ", hora=" + hora
                + ", qtd=" + qtd + ", peso=" + peso + ", observacao=" + observacao + ", desativado="
                + desativado + ", opcoes=" + opcoes + ", opcao=" + opcao + "]";
    }

}
