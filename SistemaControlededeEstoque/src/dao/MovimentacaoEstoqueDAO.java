package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.MovimentacaoEstoque;

public class MovimentacaoEstoqueDAO {

    public boolean registrarEntrada(MovimentacaoEstoque movimentacao) {

        String sqlMovimentacao = "INSERT INTO movimentacoes_estoque " +
                                 "(id_produto, tipo, nr_notafiscal, quantidade, valor_unitario, observacao) " +
                                 "VALUES (?, ?, ?, ?, ?, ?)";

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
                stmtMov.setString(3, movimentacao.getNrNotaFiscal());
                stmtMov.setInt(4, movimentacao.getQuantidade());
                stmtMov.setBigDecimal(5, movimentacao.getValorUnitario());
                stmtMov.setString(6, movimentacao.getObservacao());
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
                                 "(id_produto, tipo, nr_notafiscal, quantidade, valor_unitario, observacao) " +
                                 "VALUES (?, ?, ?, ?, ?, ?)";

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
                stmtMov.setString(3, movimentacao.getNrNotaFiscal());
                stmtMov.setInt(4, movimentacao.getQuantidade());
                stmtMov.setBigDecimal(5, movimentacao.getValorUnitario());
                stmtMov.setString(6, movimentacao.getObservacao());
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

    public List<MovimentacaoEstoque> listarMovimentacoes() {

        List<MovimentacaoEstoque> movimentacoes = new ArrayList<>();

        String sql = "SELECT " +
                     "m.id_movimentacao, " +
                     "p.nome AS produto, " +
                     "m.tipo, " +
                     "m.nr_notafiscal, " +
                     "m.quantidade, " +
                     "m.valor_unitario, " +
                     "m.observacao, " +
                     "DATE_FORMAT(m.criado_em, '%d/%m/%Y %H:%i') AS criado_em_formatado " +
                     "FROM movimentacoes_estoque m " +
                     "INNER JOIN produtos p ON m.id_produto = p.id_produto " +
                     "ORDER BY m.criado_em DESC";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
            ) {

            while (rs.next()) {
                MovimentacaoEstoque movimentacao = montarMovimentacao(rs);
                movimentacoes.add(movimentacao);
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao listar movimentações: " + erro.getMessage());
        }

        return movimentacoes;
    }

    public List<MovimentacaoEstoque> pesquisarPorNotaFiscal(String nrNotaFiscal) {

        List<MovimentacaoEstoque> movimentacoes = new ArrayList<>();

        String sql = "SELECT " +
                     "m.id_movimentacao, " +
                     "p.nome AS produto, " +
                     "m.tipo, " +
                     "m.nr_notafiscal, " +
                     "m.quantidade, " +
                     "m.valor_unitario, " +
                     "m.observacao, " +
                     "DATE_FORMAT(m.criado_em, '%d/%m/%Y %H:%i') AS criado_em_formatado " +
                     "FROM movimentacoes_estoque m " +
                     "INNER JOIN produtos p ON m.id_produto = p.id_produto " +
                     "WHERE m.nr_notafiscal LIKE ? " +
                     "ORDER BY m.criado_em DESC";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql)
            ) {

            stmt.setString(1, "%" + nrNotaFiscal + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MovimentacaoEstoque movimentacao = montarMovimentacao(rs);
                    movimentacoes.add(movimentacao);
                }
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao pesquisar por nota fiscal: " + erro.getMessage());
        }

        return movimentacoes;
    }

    public List<MovimentacaoEstoque> pesquisarPorData(Date data) {

        List<MovimentacaoEstoque> movimentacoes = new ArrayList<>();

        String sql = "SELECT " +
                     "m.id_movimentacao, " +
                     "p.nome AS produto, " +
                     "m.tipo, " +
                     "m.nr_notafiscal, " +
                     "m.quantidade, " +
                     "m.valor_unitario, " +
                     "m.observacao, " +
                     "DATE_FORMAT(m.criado_em, '%d/%m/%Y %H:%i') AS criado_em_formatado " +
                     "FROM movimentacoes_estoque m " +
                     "INNER JOIN produtos p ON m.id_produto = p.id_produto " +
                     "WHERE DATE(m.criado_em) = ? " +
                     "ORDER BY m.criado_em DESC";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql)
            ) {

            stmt.setDate(1, data);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MovimentacaoEstoque movimentacao = montarMovimentacao(rs);
                    movimentacoes.add(movimentacao);
                }
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao pesquisar por data: " + erro.getMessage());
        }

        return movimentacoes;
    }

    public List<MovimentacaoEstoque> pesquisarPorNotaFiscalEData(String nrNotaFiscal, Date data) {

        List<MovimentacaoEstoque> movimentacoes = new ArrayList<>();

        String sql = "SELECT " +
                     "m.id_movimentacao, " +
                     "p.nome AS produto, " +
                     "m.tipo, " +
                     "m.nr_notafiscal, " +
                     "m.quantidade, " +
                     "m.valor_unitario, " +
                     "m.observacao, " +
                     "DATE_FORMAT(m.criado_em, '%d/%m/%Y %H:%i') AS criado_em_formatado " +
                     "FROM movimentacoes_estoque m " +
                     "INNER JOIN produtos p ON m.id_produto = p.id_produto " +
                     "WHERE m.nr_notafiscal LIKE ? " +
                     "AND DATE(m.criado_em) = ? " +
                     "ORDER BY m.criado_em DESC";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql)
            ) {

            stmt.setString(1, "%" + nrNotaFiscal + "%");
            stmt.setDate(2, data);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MovimentacaoEstoque movimentacao = montarMovimentacao(rs);
                    movimentacoes.add(movimentacao);
                }
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao pesquisar por nota fiscal e data: " + erro.getMessage());
        }

        return movimentacoes;
    }

    private MovimentacaoEstoque montarMovimentacao(ResultSet rs) throws SQLException {

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();

        movimentacao.setIdMovimentacao(rs.getInt("id_movimentacao"));
        movimentacao.setNomeProduto(rs.getString("produto"));
        movimentacao.setTipo(rs.getString("tipo"));
        movimentacao.setNrNotaFiscal(rs.getString("nr_notafiscal"));
        movimentacao.setQuantidade(rs.getInt("quantidade"));
        movimentacao.setValorUnitario(rs.getBigDecimal("valor_unitario"));
        movimentacao.setObservacao(rs.getString("observacao"));
        movimentacao.setCriadoEmFormatado(rs.getString("criado_em_formatado"));

        return movimentacao;
    }
}
