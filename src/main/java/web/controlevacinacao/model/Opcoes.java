package web.controlevacinacao.model;

public enum Opcoes {
    OPCAO1("Opção 1"),
    OPCAO2("Opção 2"),
    OPCAO3("Opção 3");

    private String descricao;

    private Opcoes(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}