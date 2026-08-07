package model;

public class Fornecedor {

    private int idFornecedor;
    private String nome;
    private String cnpj;
    private String telefone;
    private String email;
    private String cidade;
    private boolean ativo;

    public Fornecedor() {

    }

    public Fornecedor(String nome, String cnpj, String telefone, String email, String cidade) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.telefone = telefone;
        this.email = email;
        this.cidade = cidade;
    }

    public Fornecedor(int idFornecedor, String nome, String cnpj, String telefone, String email, String cidade, boolean ativo) {
        this.idFornecedor = idFornecedor;
        this.nome = nome;
        this.cnpj = cnpj;
        this.telefone = telefone;
        this.email = email;
        this.cidade = cidade;
        this.ativo = ativo;
    }

    public int getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(int idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }


    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }


    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }


    @Override
    public String toString() {
        return nome;
    }
}