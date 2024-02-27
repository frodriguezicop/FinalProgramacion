package Persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.Statement;

import Logica.Candidato;
import Logica.PartidoPolitico;

public class CandidatoPersistencia {

    public static void crearTablaCandidato() {
        try (Connection connection = ConexionBase.obtenerConexion();
             PreparedStatement statement = connection.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS Candidato (" +
                             "id SERIAL PRIMARY KEY, " +
                             "apellido VARCHAR(255), " +
                             "nombre VARCHAR(255), " +
                             "edad INT, " +
                             "genero VARCHAR(255), " +
                             "domicilio VARCHAR(255), " +
                             "numeroIdentificacion VARCHAR(255) UNIQUE, " +
                             "partidoPolitico VARCHAR(255), " +
                             "lema VARCHAR(255), " +
                             "cantidadVotos INT DEFAULT 0)")) {
            statement.executeUpdate();
        } catch (SQLException e) {
          
        }
    }

    public static void guardarCandidato(Candidato candidato) {
        try (Connection connection = ConexionBase.obtenerConexion();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO Candidato (apellido, nombre, edad, genero, domicilio, " +
                             "numeroIdentificacion, partidoPolitico, lema, cantidadVotos) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

           
            if (candidato.getApellido() == null || candidato.getNombre() == null || candidato.getGenero() == null ||
                    candidato.getDomicilio() == null || candidato.getNumeroIdentificacion() == null ||
                    candidato.getPartidoPolitico() == null || candidato.getLema() == null) {
                return;
            }

            statement.setString(1, candidato.getApellido());
            statement.setString(2, candidato.getNombre());
            statement.setInt(3, candidato.getEdad());
            statement.setString(4, candidato.getGenero());
            statement.setString(5, candidato.getDomicilio());
            statement.setString(6, candidato.getNumeroIdentificacion());
            statement.setString(7, candidato.getPartidoPolitico().name());
            statement.setString(8, candidato.getLema());
            statement.setInt(9, candidato.getCantidadVotos());

            statement.executeUpdate();
        } catch (SQLException e) {
           
        }
    }

    public static boolean existeCandidato(String numeroIdentificacion) {
        try (Connection connection = ConexionBase.obtenerConexion();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM Candidato WHERE numeroIdentificacion = ?")) {

            statement.setString(1, numeroIdentificacion);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0;

        } catch (SQLException e) {
            
            return false;
        }
    }
    
    public static void actualizarCantidadVotos(String numeroIdentificacion) {
        try (Connection connection = ConexionBase.obtenerConexion();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE Candidato SET cantidadVotos = cantidadVotos + 1 WHERE numeroIdentificacion = ?")) {

            statement.setString(1, numeroIdentificacion);
            statement.executeUpdate();

        } catch (SQLException e) {
           
        }
    }
    
    
    public static List<String> getCandidatosDisponiblesNombres() {
        List<String> nombresCandidatos = new ArrayList<>();

        try (Connection connection = ConexionBase.obtenerConexion();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT nombre, partidoPolitico FROM Candidato")) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                String partidoPolitico = resultSet.getString("partidoPolitico");
                String infoCandidato = String.format("%s - %s", nombre, partidoPolitico);

                nombresCandidatos.add(infoCandidato);
            }

        } catch (SQLException e) {
          
        }

        return nombresCandidatos;
    }
    
    public static List<String> getCandidatosDisponibles() {
        List<String> infoCandidatos = new ArrayList<>();

        try (Connection connection = ConexionBase.obtenerConexion();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM Candidato")) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String apellido = resultSet.getString("apellido");
                String nombre = resultSet.getString("nombre");
                String partidoPolitico = resultSet.getString("partidoPolitico");
                String infoCandidato = String.format("%s - %s - %s", apellido, nombre, partidoPolitico);

                infoCandidatos.add(infoCandidato);
            }

        } catch (SQLException e) {
            
        }

        return infoCandidatos;
    }



}
