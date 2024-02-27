package Persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBase {
    private static final String URL = "jdbc:postgresql://localhost:5432/Final";
    private static final String USUARIO = "postgres";
    private static final String CONTRASENA = "icop2023";

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}

