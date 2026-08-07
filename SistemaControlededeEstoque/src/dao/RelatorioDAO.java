package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Produto;

public class RelatorioDAO {

    public List<Produto> listarProdutosCadastrados() {

        List<Produto> produtos = new ArrayList<>();

        String sql =
                "SELECT " +
                "p.id_produto, " +
                "p.nome, " +
                "p.valor_custo, " +
                "p.valor_venda, " +
                "p.quantidade_estoque, " +
                "p.estoque_minimo, " +
                "c.nome AS categoria, " +
                "f.nome AS fornecedor " +
                "FROM produtos p " +
                "INNER JOIN categorias c ON p.id_categoria = c.id_categoria " +
                "LEFT JOIN fornecedores f ON p.id_fornecedor = f.id_fornecedor " +
                "WHERE p.ativo = TRUE " +
                "ORDER BY p.nome";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
            ) {

            while (rs.next()) {

                Produto produto = new Produto();

                produto.setIdProduto(rs.getInt("id_produto"));
                produto.setNome(rs.getString("nome"));
                produto.setValorCusto(rs.getBigDecimal("valor_custo"));
                produto.setValorVenda(rs.getBigDecimal("valor_venda"));
                produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
                produto.setEstoqueMinimo(rs.getInt("estoque_minimo"));
                produto.setNomeCategoria(rs.getString("categoria"));
                produto.setNomeFornecedor(rs.getString("fornecedor"));

                produtos.add(produto);
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao gerar relatório de produtos: " + erro.getMessage());
        }

        return produtos;
    }

    public List<Produto> listarProdutosComEstoqueBaixo() {

        List<Produto> produtos = new ArrayList<>();

        String sql =
                "SELECT " +
                "p.id_produto, " +
                "p.nome, " +
                "p.valor_venda, " +
                "p.quantidade_estoque, " +
                "p.estoque_minimo, " +
                "c.nome AS categoria, " +
                "f.nome AS fornecedor " +
                "FROM produtos p " +
                "INNER JOIN categorias c ON p.id_categoria = c.id_categoria " +
                "LEFT JOIN fornecedores f ON p.id_fornecedor = f.id_fornecedor " +
                "WHERE p.ativo = TRUE " +
                "AND p.quantidade_estoque <= p.estoque_minimo " +
                "ORDER BY p.quantidade_estoque ASC";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
            ) {

            while (rs.next()) {

                Produto produto = new Produto();

                produto.setIdProduto(rs.getInt("id_produto"));
                produto.setNome(rs.getString("nome"));
                produto.setValorVenda(rs.getBigDecimal("valor_venda"));
                produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
                produto.setEstoqueMinimo(rs.getInt("estoque_minimo"));
                produto.setNomeCategoria(rs.getString("categoria"));
                produto.setNomeFornecedor(rs.getString("fornecedor"));

                produtos.add(produto);
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao gerar relatório de estoque baixo: " + erro.getMessage());
        }

        return produtos;
    }
}
