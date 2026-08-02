package model;

public class Usuario {

    private int idUsuario;
    private String nome;
    private String login;
    private String senhaHash;
    private String perfil;
    private boolean ativo;

    public Usuario() {

    }

    public Usuario(String nome, String login, String senhaHash, String perfil, boolean ativo) {
        this.nome = nome;
        this.login = login;
        this.senhaHash= senhaHash;
        this.perfil = perfil;
        this.ativo = ativo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash= senhaHash;
    }


    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }


    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
