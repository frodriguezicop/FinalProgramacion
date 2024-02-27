package Persistencia;

import java.sql.Connection;
import java.sql.SQLException;

public class PruebaConexion {
    public static void main(String[] args) {
        try {
            Connection conexion = ConexionBase.obtenerConexion();
            if (conexion != null) {
                System.out.println("Se ha conectado a la base de datos");
                
                conexion.close();
            } else {
                System.out.println("No se pudo establecer la conexion a la base de datos");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
