package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Cliente;

public class ClienteDAO {

    public boolean cadastrar(Cliente cliente) {

        String sql = "INSERT INTO clientes " +
                     "(nome, cpf, telefone, email, cidade) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql)
            ) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getEmail());
            stmt.setString(5, cliente.getCidade());

            int linhasAfetadas = stmt.executeUpdate();

            return linhasAfetadas > 0;

        } catch (SQLException erro) {
            System.out.println("Erro ao cadastrar cliente: " + erro.getMessage());
            return false;
        }
    }

    public List<Cliente> listar() {

        List<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT id_cliente, nome, cpf, telefone, email, cidade, ativo " +
                     "FROM clientes " +
                     "WHERE ativo = TRUE " +
                     "ORDER BY nome";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
            ) {

            while (rs.next()) {

                Cliente cliente = new Cliente();

                cliente.setIdCliente(rs.getInt("id_cliente"));
                cliente.setNome(rs.getString("nome"));
                cliente.setCpf(rs.getString("cpf"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setEmail(rs.getString("email"));
                cliente.setCidade(rs.getString("cidade"));
                cliente.setAtivo(rs.getBoolean("ativo"));

                clientes.add(cliente);
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao listar clientes: " + erro.getMessage());
        }

        return clientes;
    }

    public List<Cliente> pesquisarPorNome(String nomePesquisa) {

        List<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT id_cliente, nome, cpf, telefone, email, cidade, ativo " +
                     "FROM clientes " +
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

                    Cliente cliente = new Cliente();

                    cliente.setIdCliente(rs.getInt("id_cliente"));
                    cliente.setNome(rs.getString("nome"));
                    cliente.setCpf(rs.getString("cpf"));
                    cliente.setTelefone(rs.getString("telefone"));
                    cliente.setEmail(rs.getString("email"));
                    cliente.setCidade(rs.getString("cidade"));
                    cliente.setAtivo(rs.getBoolean("ativo"));

                    clientes.add(cliente);
                }
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao pesquisar clientes: " + erro.getMessage());
        }

        return clientes;
    }
}
