package Persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;


import Logica.Votante;

public class VotantePersistencia {

	public static void crearTablaVotante() {
        try (Connection connection = ConexionBase.obtenerConexion();
             PreparedStatement statement = connection.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS Votante (" +
                             "id SERIAL PRIMARY KEY, " +
                             "apellido VARCHAR(255), " +
                             "nombre VARCHAR(255), " +
                             "edad INT, " +
                             "genero VARCHAR(255), " +
                             "domicilio VARCHAR(255), " +
                             "numeroIdentificacion VARCHAR(255) UNIQUE)")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            
        }
    }

    public static void votar(String numeroIdentificacion, String voto) {
        try (Connection connection = ConexionBase.obtenerConexion();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE Votante SET votoEmitido = true, candidatoVoto = ? WHERE numeroIdentificacion = ?")) {

            statement.setString(1, voto);
            statement.setString(2, numeroIdentificacion);

            statement.executeUpdate();

            VotacionPersistencia.guardarVotacion(numeroIdentificacion, voto);

        } catch (SQLException e) {
            
            e.printStackTrace();
        }
    }
    
    
    
    
    
    public static void guardarVotante(Votante votante) {
        try (Connection connection = ConexionBase.obtenerConexion()) {
            //verifica si el votante ya existe
            if (existeVotante(votante.getNumeroIdentificacion())) {
                
                return;
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO Votante (apellido, nombre, edad, genero, domicilio, " +
                            "numeroIdentificacion) " +
                            "VALUES (?, ?, ?, ?, ?, ?)")) {

                statement.setString(1, votante.getApellido());
                statement.setString(2, votante.getNombre());

                // verifica si la edad es especificada o no
                if (votante.getEdad() != 0) {
                    statement.setInt(3, votante.getEdad());
                } else {
                    statement.setNull(3, java.sql.Types.INTEGER);
                }

                statement.setString(4, votante.getGenero());
                statement.setString(5, votante.getDomicilio());
                statement.setString(6, votante.getNumeroIdentificacion());

                
                statement.executeUpdate();

            } 
        } catch (SQLException e) {
            
        }
    }

    public static boolean existeVotante(String numeroIdentificacion) {
        try (Connection connection = ConexionBase.obtenerConexion();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM Votante WHERE numeroIdentificacion = ?")) {

            statement.setString(1, numeroIdentificacion);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0;

        } catch (SQLException e) {
            
            return false;
        }
    }
    
    
   
    
    public static void actualizarVotoEmitido(String numeroIdentificacion) {
        try (Connection connection = ConexionBase.obtenerConexion();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE Votante SET votoEmitido = true WHERE numeroIdentificacion = ?")) {

            statement.setString(1, numeroIdentificacion);
            statement.executeUpdate();

        } catch (SQLException e) {
            
        }
    }
    
    
    public static List<String> obtenerDatosPadron() {
        List<String> datosPadron = new ArrayList<>();

        try (Connection connection = ConexionBase.obtenerConexion();
                PreparedStatement statement = connection.prepareStatement("SELECT apellido, nombre, edad FROM Votante");
                ResultSet resultSet = statement.executeQuery()) {
        	
            while (resultSet.next()) {
                String apellido = resultSet.getString("apellido");
                String nombre = resultSet.getString("nombre");
                int edad = resultSet.getInt("edad");

                String datosVotante = String.format("%s, %s - Edad: %d", apellido, nombre, edad);
                datosPadron.add(datosVotante);
            }

        } catch (SQLException e) {
            
        }

        return datosPadron;
    }
}
    
 
