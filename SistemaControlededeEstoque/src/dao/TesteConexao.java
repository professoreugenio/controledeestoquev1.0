//testedeconexao
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TesteConexao {

    private static final String URL =
            "jdbc:mysql://localhost:3306/controle_estoque_db";

    private static final String USUARIO = "root";

    private static final String SENHA = "Aluno";


    public static Connection conectar() throws SQLException {

        return DriverManager.getConnection(
                URL,
                USUARIO,
                SENHA
        );
    }

    public static void main(String[] args) {

        try (Connection conexao = TesteConexao.conectar()) {

            System.out.println("Conexão realizada com sucesso!");

            System.out.println(
                "Banco conectado: " + conexao.getCatalog()
            );

        } catch (SQLException erro) {

            System.out.println("Erro ao conectar com o banco de dados.");

            System.out.println(
                "Mensagem: " + erro.getMessage()
            );

            System.out.println(
                "Código do erro: " + erro.getErrorCode()
            );
        }
    }
}
//