package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Produto;

public class ProdutoDAO {

    public boolean cadastrar(Produto produto) {

        String sql = "INSERT INTO produtos " +
                     "(nome, descricao, valor_custo, valor_venda, quantidade_estoque, estoque_minimo, id_categoria, id_fornecedor) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql)
            ) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setBigDecimal(3, produto.getValorCusto());
            stmt.setBigDecimal(4, produto.getValorVenda());
            stmt.setInt(5, produto.getQuantidadeEstoque());
            stmt.setInt(6, produto.getEstoqueMinimo());
            stmt.setInt(7, produto.getIdCategoria());
            stmt.setInt(8, produto.getIdFornecedor());

            int linhasAfetadas = stmt.executeUpdate();

            return linhasAfetadas > 0;

        } catch (SQLException erro) {
            System.out.println("Erro ao cadastrar produto: " + erro.getMessage());
            return false;
        }
    }

    public List<Produto> listar() {

        List<Produto> produtos = new ArrayList<>();

        String sql =
                "SELECT " +
                "p.id_produto, " +
                "p.nome, " +
                "p.descricao, " +
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
                produto.setDescricao(rs.getString("descricao"));
                produto.setValorCusto(rs.getBigDecimal("valor_custo"));
                produto.setValorVenda(rs.getBigDecimal("valor_venda"));
                produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
                produto.setEstoqueMinimo(rs.getInt("estoque_minimo"));
                produto.setNomeCategoria(rs.getString("categoria"));
                produto.setNomeFornecedor(rs.getString("fornecedor"));

                produtos.add(produto);
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao listar produtos: " + erro.getMessage());
        }

        return produtos;
    }

    public List<Produto> pesquisarPorNome(String nomePesquisa) {

        List<Produto> produtos = new ArrayList<>();

        String sql =
                "SELECT " +
                "p.id_produto, " +
                "p.nome, " +
                "p.descricao, " +
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
                "AND p.nome LIKE ? " +
                "ORDER BY p.nome";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql)
            ) {

            stmt.setString(1, "%" + nomePesquisa + "%");

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Produto produto = new Produto();

                    produto.setIdProduto(rs.getInt("id_produto"));
                    produto.setNome(rs.getString("nome"));
                    produto.setDescricao(rs.getString("descricao"));
                    produto.setValorCusto(rs.getBigDecimal("valor_custo"));
                    produto.setValorVenda(rs.getBigDecimal("valor_venda"));
                    produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
                    produto.setEstoqueMinimo(rs.getInt("estoque_minimo"));
                    produto.setNomeCategoria(rs.getString("categoria"));
                    produto.setNomeFornecedor(rs.getString("fornecedor"));

                    produtos.add(produto);
                }
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao pesquisar produtos: " + erro.getMessage());
        }

        return produtos;
    }
}
