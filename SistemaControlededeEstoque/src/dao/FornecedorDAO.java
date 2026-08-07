package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import model.Fornecedor;

public class FornecedorDAO {

    public boolean cadastrar(Fornecedor fornecedor) {

        String sql = "INSERT INTO fornecedores " +
                     "(nome, cnpj, telefone, email, cidade) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql)
            ) {

            stmt.setString(1, fornecedor.getNome());
            stmt.setString(2, fornecedor.getCnpj());
            stmt.setString(3, fornecedor.getTelefone());
            stmt.setString(4, fornecedor.getEmail());
            stmt.setString(5, fornecedor.getCidade());

            int linhasAfetadas = stmt.executeUpdate();

            return linhasAfetadas > 0;

        } catch (SQLIntegrityConstraintViolationException erro) {
            System.out.println("Erro: já existe um fornecedor cadastrado com este CNPJ.");
            return false;

        } catch (SQLException erro) {
            System.out.println("Erro ao cadastrar fornecedor: " + erro.getMessage());
            return false;
        }
    }

    public List<Fornecedor> listar() {

        List<Fornecedor> fornecedores = new ArrayList<>();

        String sql = "SELECT id_fornecedor, nome, cnpj, telefone, email, cidade, ativo " +
                     "FROM fornecedores " +
                     "WHERE ativo = TRUE " +
                     "ORDER BY nome";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
            ) {

            while (rs.next()) {

                Fornecedor fornecedor = new Fornecedor();

                fornecedor.setIdFornecedor(rs.getInt("id_fornecedor"));
                fornecedor.setNome(rs.getString("nome"));
                fornecedor.setCnpj(rs.getString("cnpj"));
                fornecedor.setTelefone(rs.getString("telefone"));
                fornecedor.setEmail(rs.getString("email"));
                fornecedor.setCidade(rs.getString("cidade"));
                fornecedor.setAtivo(rs.getBoolean("ativo"));

                fornecedores.add(fornecedor);
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao listar fornecedores: " + erro.getMessage());
        }

        return fornecedores;
    }

    public List<Fornecedor> pesquisarPorNome(String nomePesquisa) {

        List<Fornecedor> fornecedores = new ArrayList<>();

        String sql = "SELECT id_fornecedor, nome, cnpj, telefone, email, cidade, ativo " +
                     "FROM fornecedores " +
                     "WHERE ativo = TRUE " +
                     "AND nome LIKE ? " +
                     "ORDER BY nome";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql)
            ) {

            stmt.setString(1, "%" + nomePesquisa + "%");

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Fornecedor fornecedor = new Fornecedor();

                    fornecedor.setIdFornecedor(rs.getInt("id_fornecedor"));
                    fornecedor.setNome(rs.getString("nome"));
                    fornecedor.setCnpj(rs.getString("cnpj"));
                    fornecedor.setTelefone(rs.getString("telefone"));
                    fornecedor.setEmail(rs.getString("email"));
                    fornecedor.setCidade(rs.getString("cidade"));
                    fornecedor.setAtivo(rs.getBoolean("ativo"));

                    fornecedores.add(fornecedor);
                }
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao pesquisar fornecedores: " + erro.getMessage());
        }

        return fornecedores;
    }

    public Fornecedor buscarPorId(int idFornecedor) {

        String sql = "SELECT id_fornecedor, nome, cnpj, telefone, email, cidade, ativo " +
                     "FROM fornecedores " +
                     "WHERE id_fornecedor = ? " +
                     "AND ativo = TRUE";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql)
            ) {

            stmt.setInt(1, idFornecedor);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    Fornecedor fornecedor = new Fornecedor();

                    fornecedor.setIdFornecedor(rs.getInt("id_fornecedor"));
                    fornecedor.setNome(rs.getString("nome"));
                    fornecedor.setCnpj(rs.getString("cnpj"));
                    fornecedor.setTelefone(rs.getString("telefone"));
                    fornecedor.setEmail(rs.getString("email"));
                    fornecedor.setCidade(rs.getString("cidade"));
                    fornecedor.setAtivo(rs.getBoolean("ativo"));

                    return fornecedor;
                }
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao buscar fornecedor por ID: " + erro.getMessage());
        }

        return null;
    }

    public boolean atualizar(Fornecedor fornecedor) {

        String sql = "UPDATE fornecedores SET " +
                     "nome = ?, " +
                     "cnpj = ?, " +
                     "telefone = ?, " +
                     "email = ?, " +
                     "cidade = ? " +
                     "WHERE id_fornecedor = ?";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql)
            ) {

            stmt.setString(1, fornecedor.getNome());
            stmt.setString(2, fornecedor.getCnpj());
            stmt.setString(3, fornecedor.getTelefone());
            stmt.setString(4, fornecedor.getEmail());
            stmt.setString(5, fornecedor.getCidade());
            stmt.setInt(6, fornecedor.getIdFornecedor());

            int linhasAfetadas = stmt.executeUpdate();

            return linhasAfetadas > 0;

        } catch (SQLIntegrityConstraintViolationException erro) {
            System.out.println("Erro: já existe outro fornecedor cadastrado com este CNPJ.");
            return false;

        } catch (SQLException erro) {
            System.out.println("Erro ao atualizar fornecedor: " + erro.getMessage());
            return false;
        }
    }

    public boolean excluirLogico(int idFornecedor) {

        String sql = "UPDATE fornecedores SET ativo = FALSE WHERE id_fornecedor = ?";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql)
            ) {

            stmt.setInt(1, idFornecedor);

            int linhasAfetadas = stmt.executeUpdate();

            return linhasAfetadas > 0;

        } catch (SQLException erro) {
            System.out.println("Erro ao excluir fornecedor: " + erro.getMessage());
            return false;
        }
    }
}