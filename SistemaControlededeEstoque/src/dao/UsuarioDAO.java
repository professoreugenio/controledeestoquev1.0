package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Usuario;

public class UsuarioDAO {

    public Usuario autenticar(String login, String senha) {

        String sql = """
                SELECT id_usuario, nome, login, perfil, ativo
                FROM usuarios
                WHERE login = ?
                AND senha = ?
                AND ativo = TRUE
                """;

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql)
            ) {

            stmt.setString(1, login);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setLogin(rs.getString("login"));
                    usuario.setPerfil(rs.getString("perfil"));
                    usuario.setAtivo(rs.getBoolean("ativo"));

                    return usuario;
                }
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao autenticar usuário: " + erro.getMessage());
        }

        return null;
    }
}
