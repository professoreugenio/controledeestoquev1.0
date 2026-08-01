package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Usuario;
import util.CriptografiaSenha;

public class UsuarioDAO {

    public Usuario autenticar(String login, String senha) {

        String senhaCriptografada = CriptografiaSenha.gerarHash(senha);

        String sql = "SELECT id_usuario, nome, login, perfil, ativo " +
                     "FROM usuarios " +
                     "WHERE login = ? " +
                     "AND senha = ? " +
                     "AND ativo = TRUE";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql)
            ) {

            stmt.setString(1, login);
            stmt.setString(2, senhaCriptografada);

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

    public boolean cadastrar(Usuario usuario) {

        String sql = "INSERT INTO usuarios " +
                     "(nome, login, senha, perfil, ativo) " +
                     "VALUES (?, ?, ?, ?, TRUE)";

        String senhaCriptografada = CriptografiaSenha.gerarHash(usuario.getSenha());

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql)
            ) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getLogin());
            stmt.setString(3, senhaCriptografada);
            stmt.setString(4, usuario.getPerfil());

            int linhasAfetadas = stmt.executeUpdate();

            return linhasAfetadas > 0;

        } catch (SQLException erro) {
            System.out.println("Erro ao cadastrar usuário: " + erro.getMessage());
            return false;
        }
    }

    public List<Usuario> listar() {

        List<Usuario> usuarios = new ArrayList<>();

        String sql = "SELECT id_usuario, nome, login, perfil, ativo " +
                     "FROM usuarios " +
                     "WHERE ativo = TRUE " +
                     "ORDER BY nome";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
            ) {

            while (rs.next()) {

                Usuario usuario = new Usuario();

                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNome(rs.getString("nome"));
                usuario.setLogin(rs.getString("login"));
                usuario.setPerfil(rs.getString("perfil"));
                usuario.setAtivo(rs.getBoolean("ativo"));

                usuarios.add(usuario);
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao listar usuários: " + erro.getMessage());
        }

        return usuarios;
    }

    public List<Usuario> pesquisar(String texto) {

        List<Usuario> usuarios = new ArrayList<>();

        String sql = "SELECT id_usuario, nome, login, perfil, ativo " +
                     "FROM usuarios " +
                     "WHERE ativo = TRUE " +
                     "AND (nome LIKE ? OR login LIKE ?) " +
                     "ORDER BY nome";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql)
            ) {

            stmt.setString(1, "%" + texto + "%");
            stmt.setString(2, "%" + texto + "%");

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Usuario usuario = new Usuario();

                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setLogin(rs.getString("login"));
                    usuario.setPerfil(rs.getString("perfil"));
                    usuario.setAtivo(rs.getBoolean("ativo"));

                    usuarios.add(usuario);
                }
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao pesquisar usuários: " + erro.getMessage());
        }

        return usuarios;
    }

    public boolean atualizarPerfil(Usuario usuario, boolean alterarSenha) {

        String sql;

        if (alterarSenha) {
            sql = "UPDATE usuarios " +
                  "SET nome = ?, login = ?, senha = ? " +
                  "WHERE id_usuario = ?";
        } else {
            sql = "UPDATE usuarios " +
                  "SET nome = ?, login = ? " +
                  "WHERE id_usuario = ?";
        }

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql)
            ) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getLogin());

            if (alterarSenha) {
                String senhaCriptografada = CriptografiaSenha.gerarHash(usuario.getSenha());
                stmt.setString(3, senhaCriptografada);
                stmt.setInt(4, usuario.getIdUsuario());
            } else {
                stmt.setInt(3, usuario.getIdUsuario());
            }

            int linhasAfetadas = stmt.executeUpdate();

            return linhasAfetadas > 0;

        } catch (SQLException erro) {
            System.out.println("Erro ao atualizar perfil: " + erro.getMessage());
            return false;
        }
    }

    public boolean excluirLogico(int idUsuario) {

        String sql = "UPDATE usuarios SET ativo = FALSE WHERE id_usuario = ?";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql)
            ) {

            stmt.setInt(1, idUsuario);

            int linhasAfetadas = stmt.executeUpdate();

            return linhasAfetadas > 0;

        } catch (SQLException erro) {
            System.out.println("Erro ao excluir usuário: " + erro.getMessage());
            return false;
        }
    }
}
