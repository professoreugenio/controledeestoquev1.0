package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDAO {


    private static final String URL = "jdbc:mysql://localhost:3306/controle_estoque_db";
    private static final String USUARIO = "root";
    private static final String SENHA = "Aluno";

    /*private static final String URL = "jdbc:mysql://localhost:3307/controle_estoque_db";
    private static final String USUARIO = "root";
    private static final String SENHA = "&ugML$2834&2026";*/

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
