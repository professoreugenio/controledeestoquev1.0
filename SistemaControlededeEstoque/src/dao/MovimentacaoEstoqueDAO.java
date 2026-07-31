package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.MovimentacaoEstoque;

public class MovimentacaoEstoqueDAO {

    public boolean registrarEntrada(MovimentacaoEstoque movimentacao) {

        String sqlMovimentacao = "INSERT INTO movimentacoes_estoque " +
                                 "(id_produto, tipo, quantidade, valor_unitario, observacao) " +
                                 "VALUES (?, ?, ?, ?, ?)";

        String sqlAtualizaProduto = "UPDATE produtos " +
                                    "SET quantidade_estoque = quantidade_estoque + ? " +
                                    "WHERE id_produto = ?";

        Connection con = null;

        try {
            con = ConexaoDAO.conectar();
            con.setAutoCommit(false);

            try (PreparedStatement stmtMov = con.prepareStatement(sqlMovimentacao)) {
                stmtMov.setInt(1, movimentacao.getIdProduto());
                stmtMov.setString(2, "ENTRADA");
                stmtMov.setInt(3, movimentacao.getQuantidade());
                stmtMov.setBigDecimal(4, movimentacao.getValorUnitario());
                stmtMov.setString(5, movimentacao.getObservacao());
                stmtMov.executeUpdate();
            }

            try (PreparedStatement stmtProd = con.prepareStatement(sqlAtualizaProduto)) {
                stmtProd.setInt(1, movimentacao.getQuantidade());
                stmtProd.setInt(2, movimentacao.getIdProduto());
                stmtProd.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException erro) {

            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException e) {
                    System.out.println("Erro ao desfazer entrada: " + e.getMessage());
                }
            }

            System.out.println("Erro ao registrar entrada: " + erro.getMessage());
            return false;

        } finally {

            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }
    }

    public boolean registrarSaida(MovimentacaoEstoque movimentacao) {

        String sqlConsultaEstoque = "SELECT quantidade_estoque FROM produtos WHERE id_produto = ? FOR UPDATE";

        String sqlMovimentacao = "INSERT INTO movimentacoes_estoque " +
                                 "(id_produto, tipo, quantidade, valor_unitario, observacao) " +
                                 "VALUES (?, ?, ?, ?, ?)";

        String sqlAtualizaProduto = "UPDATE produtos " +
                                    "SET quantidade_estoque = quantidade_estoque - ? " +
                                    "WHERE id_produto = ?";

        Connection con = null;

        try {
            con = ConexaoDAO.conectar();
            con.setAutoCommit(false);

            int estoqueAtual = 0;

            try (PreparedStatement stmtConsulta = con.prepareStatement(sqlConsultaEstoque)) {
                stmtConsulta.setInt(1, movimentacao.getIdProduto());

                try (ResultSet rs = stmtConsulta.executeQuery()) {
                    if (rs.next()) {
                        estoqueAtual = rs.getInt("quantidade_estoque");
                    }
                }
            }

            if (movimentacao.getQuantidade() > estoqueAtual) {
                con.rollback();
                return false;
            }

            try (PreparedStatement stmtMov = con.prepareStatement(sqlMovimentacao)) {
                stmtMov.setInt(1, movimentacao.getIdProduto());
                stmtMov.setString(2, "SAIDA");
                stmtMov.setInt(3, movimentacao.getQuantidade());
                stmtMov.setBigDecimal(4, movimentacao.getValorUnitario());
                stmtMov.setString(5, movimentacao.getObservacao());
                stmtMov.executeUpdate();
            }

            try (PreparedStatement stmtProd = con.prepareStatement(sqlAtualizaProduto)) {
                stmtProd.setInt(1, movimentacao.getQuantidade());
                stmtProd.setInt(2, movimentacao.getIdProduto());
                stmtProd.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException erro) {

            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException e) {
                    System.out.println("Erro ao desfazer saída: " + e.getMessage());
                }
            }

            System.out.println("Erro ao registrar saída: " + erro.getMessage());
            return false;

        } finally {

            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }
    }
}
