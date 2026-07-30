package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Categoria;

public class CategoriaDAO {

    public List<Categoria> listar() {

        List<Categoria> categorias = new ArrayList<>();

        String sql = "SELECT id_categoria, nome FROM categorias WHERE ativo = TRUE ORDER BY nome";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
            ) {

            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setIdCategoria(rs.getInt("id_categoria"));
                categoria.setNome(rs.getString("nome"));

                categorias.add(categoria);
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao listar categorias: " + erro.getMessage());
        }

        return categorias;
    }
}
