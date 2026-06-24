package web.controlevacinacao.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import web.controlevacinacao.model.Cliente;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.validation.UniqueValueAttribute;
import web.controlevacinacao.validation.cpfunico.CPFUnicoService;

@UniqueValueAttribute(attribute = "cpf", message = "Esse CPF já foi usado por outro cliente", service = CPFUnicoService.class)
public class ClienteDTOInput {

    private Long codigo;
    @NotBlank(message = "O nome do cliente é obrigatório")
    @Size(max = 255, message = "O tamanho máximo do nome é 255 caracteres")
    private String nome;
    @NotBlank(message = "O CPF do cliente é obrigatório")
    private String cpf;
    @NotNull(message = "A data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve ser anterior ao dia de hoje")
    private LocalDate dataNascimento;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Cliente toCliente() {
        Cliente cliente = new Cliente();
        cliente.setCodigo(codigo);
        cliente.setNome(nome);
        cliente.setCpf(cpf);
        cliente.setDataNascimento(dataNascimento);
        cliente.setStatus(status);
        return cliente;
    }

    public static ClienteDTOInput fromCliente(Cliente cliente) {
        ClienteDTOInput dto = new ClienteDTOInput();
        dto.setCodigo(cliente.getCodigo());
        dto.setNome(cliente.getNome());
        dto.setCpf(cliente.getCpf());
        dto.setDataNascimento(cliente.getDataNascimento());
        dto.setStatus(cliente.getStatus());
        return dto;
    }

    @Override
    public String toString() {
        return "ClienteDTOInput [codigo=" + codigo + ", nome=" + nome + ", cpf=" + cpf
                + ", dataNascimento=" + dataNascimento + ", status=" + status + "]";
    }

}