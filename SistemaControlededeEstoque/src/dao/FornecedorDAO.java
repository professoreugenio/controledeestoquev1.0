package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Fornecedor;

public class FornecedorDAO {

    public List<Fornecedor> listar() {

        List<Fornecedor> fornecedores = new ArrayList<>();

        String sql = "SELECT id_fornecedor, nome FROM fornecedores WHERE ativo = TRUE ORDER BY nome";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
            ) {

            while (rs.next()) {
                Fornecedor fornecedor = new Fornecedor();
                fornecedor.setIdFornecedor(rs.getInt("id_fornecedor"));
                fornecedor.setNome(rs.getString("nome"));

                fornecedores.add(fornecedor);
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao listar fornecedores: " + erro.getMessage());
        }

        return fornecedores;
    }
}
