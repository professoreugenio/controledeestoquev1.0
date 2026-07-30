package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Produto;

public class DashboardDAO {

    public int contarProdutosAtivos() {

        String sql = "SELECT COUNT(*) AS total FROM produtos WHERE ativo = TRUE";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
            ) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao contar produtos: " + erro.getMessage());
        }

        return 0;
    }

    public int contarClientesAtivos() {

        String sql = "SELECT COUNT(*) AS total FROM clientes WHERE ativo = TRUE";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
            ) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao contar clientes: " + erro.getMessage());
        }

        return 0;
    }

    public int contarProdutosEstoqueBaixo() {

        String sql = "SELECT COUNT(*) AS total " +
                     "FROM produtos " +
                     "WHERE ativo = TRUE " +
                     "AND quantidade_estoque <= estoque_minimo";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
            ) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao contar produtos com estoque baixo: " + erro.getMessage());
        }

        return 0;
    }

    public BigDecimal calcularValorTotalEstoque() {

        String sql = "SELECT SUM(quantidade_estoque * valor_venda) AS total " +
                     "FROM produtos " +
                     "WHERE ativo = TRUE";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
            ) {

            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total");

                if (total != null) {
                    return total;
                }
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao calcular valor total do estoque: " + erro.getMessage());
        }

        return BigDecimal.ZERO;
    }

    public List<Produto> listarProdutosEstoqueBaixo() {

        List<Produto> produtos = new ArrayList<>();

        String sql = "SELECT id_produto, nome, quantidade_estoque, estoque_minimo, valor_venda " +
                     "FROM produtos " +
                     "WHERE ativo = TRUE " +
                     "AND quantidade_estoque <= estoque_minimo " +
                     "ORDER BY nome";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
            ) {

            while (rs.next()) {

                Produto produto = new Produto();

                produto.setIdProduto(rs.getInt("id_produto"));
                produto.setNome(rs.getString("nome"));
                produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
                produto.setEstoqueMinimo(rs.getInt("estoque_minimo"));
                produto.setValorVenda(rs.getBigDecimal("valor_venda"));

                produtos.add(produto);
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao listar produtos com estoque baixo: " + erro.getMessage());
        }

        return produtos;
    }
}
