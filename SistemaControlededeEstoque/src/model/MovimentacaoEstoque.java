package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovimentacaoEstoque {

    private int idMovimentacao;
    private int idProduto;
    private String nomeProduto;
    private String tipo;
    private String nrNotaFiscal;
    private int quantidade;
    private BigDecimal valorUnitario;
    private String observacao;
    private LocalDateTime criadoEm;

    private String criadoEmFormatado;

    public MovimentacaoEstoque() {

    }

    public MovimentacaoEstoque(int idProduto, String tipo, String nrNotaFiscal,
                               int quantidade, BigDecimal valorUnitario, String observacao) {
        this.idProduto = idProduto;
        this.tipo = tipo;
        this.nrNotaFiscal = nrNotaFiscal;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.observacao = observacao;
    }

    public int getIdMovimentacao() {
        return idMovimentacao;
    }

    public void setIdMovimentacao(int idMovimentacao) {
        this.idMovimentacao = idMovimentacao;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNrNotaFiscal() {
        return nrNotaFiscal;
    }

    public void setNrNotaFiscal(String nrNotaFiscal) {
        this.nrNotaFiscal = nrNotaFiscal;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public String getCriadoEmFormatado() {
        return criadoEmFormatado;
    }

    public void setCriadoEmFormatado(String criadoEmFormatado) {
        this.criadoEmFormatado = criadoEmFormatado;
    }
}
