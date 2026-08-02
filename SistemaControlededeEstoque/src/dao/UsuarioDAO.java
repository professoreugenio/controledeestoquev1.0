package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Usuario;
import util.SenhaUtil;

public class UsuarioDAO {

    public Usuario autenticar(String login, String senhaDigitada) {

        String sql = "SELECT id_usuario, nome, login, senha, perfil, ativo " +
                     "FROM usuarios " +
                     "WHERE login = ? " +
                     "AND ativo = TRUE";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql)
            ) {

            stmt.setString(1, login);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    String senhaHashBanco = rs.getString("senha");

                    if (SenhaUtil.verificarSenha(senhaDigitada, senhaHashBanco)) {

                        Usuario usuario = new Usuario();
                        usuario.setIdUsuario(rs.getInt("id_usuario"));
                        usuario.setNome(rs.getString("nome"));
                        usuario.setLogin(rs.getString("login"));
                        usuario.setSenhaHash(senhaHashBanco);
                        usuario.setPerfil(rs.getString("perfil"));
                        usuario.setAtivo(rs.getBoolean("ativo"));

                        return usuario;
                    }
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
                     "VALUES (?, ?, ?, ?, ?)";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql)
            ) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getLogin());
            stmt.setString(3, usuario.getSenhaHash());
            stmt.setString(4, usuario.getPerfil());
            stmt.setBoolean(5, usuario.isAtivo());

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

    public List<Usuario> pesquisarPorNome(String nomePesquisa) {

        List<Usuario> usuarios = new ArrayList<>();

        String sql = "SELECT id_usuario, nome, login, perfil, ativo " +
                     "FROM usuarios " +
                     "WHERE nome LIKE ? " +
                     "ORDER BY nome";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql)
            ) {

            stmt.setString(1, "%" + nomePesquisa + "%");

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

    public Usuario buscarPorId(int idUsuario) {

        String sql = "SELECT id_usuario, nome, login, senha, perfil, ativo " +
                     "FROM usuarios " +
                     "WHERE id_usuario = ?";

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql)
            ) {

            stmt.setInt(1, idUsuario);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    Usuario usuario = new Usuario();

                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setLogin(rs.getString("login"));
                    usuario.setSenhaHash(rs.getString("senha"));
                    usuario.setPerfil(rs.getString("perfil"));
                    usuario.setAtivo(rs.getBoolean("ativo"));

                    return usuario;
                }
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao buscar usuário: " + erro.getMessage());
        }

        return null;
    }

    public boolean atualizar(Usuario usuario, String novaSenha) {

        boolean alterarSenha = novaSenha != null && !novaSenha.trim().isEmpty();

        String sql;

        if (alterarSenha) {
            sql = "UPDATE usuarios SET nome = ?, login = ?, senha = ?, perfil = ?, ativo = ? " +
                  "WHERE id_usuario = ?";
        } else {
            sql = "UPDATE usuarios SET nome = ?, login = ?, perfil = ?, ativo = ? " +
                  "WHERE id_usuario = ?";
        }

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql)
            ) {

            if (alterarSenha) {

                stmt.setString(1, usuario.getNome());
                stmt.setString(2, usuario.getLogin());
                stmt.setString(3, SenhaUtil.gerarHash(novaSenha));
                stmt.setString(4, usuario.getPerfil());
                stmt.setBoolean(5, usuario.isAtivo());
                stmt.setInt(6, usuario.getIdUsuario());

            } else {

                stmt.setString(1, usuario.getNome());
                stmt.setString(2, usuario.getLogin());
                stmt.setString(3, usuario.getPerfil());
                stmt.setBoolean(4, usuario.isAtivo());
                stmt.setInt(5, usuario.getIdUsuario());
            }

            int linhasAfetadas = stmt.executeUpdate();

            return linhasAfetadas > 0;

        } catch (SQLException erro) {
            System.out.println("Erro ao atualizar usuário: " + erro.getMessage());
            return false;
        }
    }

    public boolean atualizarPerfil(Usuario usuario, String novaSenha) {

        boolean alterarSenha = novaSenha != null && !novaSenha.trim().isEmpty();

        String sql;

        if (alterarSenha) {
            sql = "UPDATE usuarios SET nome = ?, login = ?, senha = ? " +
                  "WHERE id_usuario = ?";
        } else {
            sql = "UPDATE usuarios SET nome = ?, login = ? " +
                  "WHERE id_usuario = ?";
        }

        try (
                Connection con = ConexaoDAO.conectar();
                PreparedStatement stmt = con.prepareStatement(sql)
            ) {

            if (alterarSenha) {

                stmt.setString(1, usuario.getNome());
                stmt.setString(2, usuario.getLogin());
                stmt.setString(3, SenhaUtil.gerarHash(novaSenha));
                stmt.setInt(4, usuario.getIdUsuario());

            } else {

                stmt.setString(1, usuario.getNome());
                stmt.setString(2, usuario.getLogin());
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
