package web.controlevacinacao.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import web.controlevacinacao.model.Papel;
import web.controlevacinacao.model.Usuario;
import web.controlevacinacao.service.NomeUsuarioUnicoService;
import web.controlevacinacao.validation.UniqueValueAttribute;
import web.controlevacinacao.validation.WellFormedEmail;

@UniqueValueAttribute(attribute = "nomeUsuario", service = NomeUsuarioUnicoService.class,
        message = "Já existe um usuário igual a este cadastrado")
public class UsuarioDTOInput {

    private Long codigo;
    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 255, message = "O tamanho máximo do nome é 255 caracteres")
    private String nome;
    @NotBlank(message = "O e-mail é obrigatório")
    @WellFormedEmail(message = "O formato do e-mail está errado")
    private String email;
    @NotBlank(message = "A senha é obrigatória")
    private String senha;
    // @ValidUsername(message = "O nome de usuário é inválido")
    @NotBlank(message = "O nome de usuário é obrigatório")
    private String nomeUsuario;
    @NotNull(message = "A data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve estar no passado")
    private LocalDate dataNascimento;
    private boolean ativo;
    @Size(min = 1, message = "Ao menos um papel é obrigatório")
    private List<Papel> papeis = new ArrayList<>();

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

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public List<Papel> getPapeis() {
        return papeis;
    }

    public void setPapeis(List<Papel> papeis) {
        this.papeis = papeis;
    }

    public Usuario toUsuario() {
        Usuario usuario = new Usuario();
        usuario.setCodigo(codigo);
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setNomeUsuario(nomeUsuario);
        usuario.setDataNascimento(dataNascimento);
        usuario.setAtivo(ativo);
        usuario.setPapeis(papeis);
        return usuario;
    }

    // public static VacinaDTOInput fromVacina(Vacina vacina) {
    //     VacinaDTOInput dto = new VacinaDTOInput();
    //     dto.setCodigo(vacina.getCodigo());
    //     dto.setNome(vacina.getNome());
    //     dto.setDescricao(vacina.getDescricao());
    //     dto.setStatus(vacina.getStatus());
    //     return dto;
    // }

    @Override
    public String toString() {
        return "UsuarioDTOInput [codigo=" + codigo + ", nome=" + nome + ", email=" + email
                + ", senha=" + senha + ", nomeUsuario=" + nomeUsuario + ", dataNascimento="
                + dataNascimento + ", ativo=" + ativo + ", papeis=" + papeis + "]";
    }

}
