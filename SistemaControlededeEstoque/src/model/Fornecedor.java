package model;

public class Fornecedor {

    private int idFornecedor;
    private String nome;

    public Fornecedor() {

    }

    public Fornecedor(int idFornecedor, String nome) {
        this.idFornecedor = idFornecedor;
        this.nome = nome;
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


    @Override
    public String toString() {
        return nome;
    }
}
