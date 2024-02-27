package Persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


import java.sql.SQLException;
import java.util.ArrayList;

public class VotacionPersistencia {
    public static void crearTablaVotacion() {
        try (Connection connection = ConexionBase.obtenerConexion();
             Statement statement = connection.createStatement()) {

            
            String sql = "CREATE TABLE IF NOT EXISTS votacion (" +
                    "ID SERIAL PRIMARY KEY," +
                    "NUMEROIDENTIFICACIONVOTANTE VARCHAR(255) REFERENCES Votante(numeroIdentificacion) NOT NULL," +
                    "VOTO VARCHAR(255) NOT NULL)"; 

            //ejecuta sql
            statement.executeUpdate(sql);

        } catch (SQLException e) {
            
        }
    }

    public static void guardarVotacion(String numeroIdentificacionVotante, String voto) {
        try (Connection connection = ConexionBase.obtenerConexion();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO Votacion (NUMEROIDENTIFICACIONVOTANTE, VOTO) " +
                             "VALUES (?, ?)")) {
            statement.setString(1, numeroIdentificacionVotante);
            statement.setString(2, voto);

            statement.executeUpdate();
        } catch (SQLException e) {
            
        }
    }
    
    public static boolean votanteYaVoto(String numeroIdentificacionVotante) {
        try (Connection connection = ConexionBase.obtenerConexion();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM votacion WHERE NUMEROIDENTIFICACIONVOTANTE = ?")) {

            statement.setString(1, numeroIdentificacionVotante);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0;

        } catch (SQLException e) {
           
            return false;
        }
    }
    
    
    public static List<String> obtenerDatosEscrutinio() {
        List<String> datosEscrutinio = new ArrayList<>();

        try (Connection connection = ConexionBase.obtenerConexion();
             Statement statement = connection.createStatement()) {

            String sql = "SELECT VOTACION.VOTO, COUNT(*) AS CANTIDADVOTOS " +
                         "FROM VOTACION " +
                         "GROUP BY VOTACION.VOTO";

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String voto = resultSet.getString("VOTO");
                int cantidadVotos = resultSet.getInt("CANTIDADVOTOS");

                String infoCandidato = String.format("%s: %d votos", voto, cantidadVotos);
                datosEscrutinio.add(infoCandidato);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return datosEscrutinio;
    }

    
    public static List<String> obtenerDatosEscrutinioDefinitivo() {
        List<String> datosEscrutinio = new ArrayList<>();

        try (Connection connection = ConexionBase.obtenerConexion();
             Statement statement = connection.createStatement()) {

            // terna de ganadores
        	String ternaQuery = "SELECT VOTO, COUNT(*) AS cantidadVotos FROM Votacion " +
                    "WHERE VOTO NOT LIKE 'Voto en Blanco' AND VOTO NOT LIKE 'Voto Impugnado' " +
                    "GROUP BY VOTO ORDER BY cantidadVotos DESC LIMIT 3";
        	
        	ResultSet ternaResultSet = statement.executeQuery(ternaQuery);
            // verifica si hay ganadores
            if (!ternaResultSet.isBeforeFirst()) {
                datosEscrutinio.add("No hay ganadores aún.");
            } else {
                datosEscrutinio.add("Terna de Ganadores:");
                while (ternaResultSet.next()) {
                    String voto = ternaResultSet.getString("VOTO");
                    int cantidadVotos = ternaResultSet.getInt("cantidadVotos");
                    datosEscrutinio.add(voto + ": " + cantidadVotos + " votos");
                }
            }

            //obtiene cantidad de votos por partido (incluyendo blanco e impugnado)
            String votosPorPartidoQuery = "SELECT VOTO, COUNT(*) AS totalVotos FROM Votacion GROUP BY VOTO";
            ResultSet votosPorPartidoResultSet = statement.executeQuery(votosPorPartidoQuery);

            // agrega la cantidad de votos por partido
            datosEscrutinio.add("\nCantidad de Votos por Partido:");
            Map<String, Integer> cantidadVotosPorPartido = new HashMap<>();
            while (votosPorPartidoResultSet.next()) {
                String partidoPolitico = votosPorPartidoResultSet.getString("VOTO");
                int totalVotos = votosPorPartidoResultSet.getInt("totalVotos");
                cantidadVotosPorPartido.put(partidoPolitico, totalVotos);
                datosEscrutinio.add(partidoPolitico + " - " + totalVotos + " votos");
            }

            // calcula el total de votos recibidos (incluyendo blanco e impugnado)
            String totalVotosQuery = "SELECT COUNT(*) AS totalVotos FROM Votacion";
            ResultSet totalVotosResultSet = statement.executeQuery(totalVotosQuery);
            if (totalVotosResultSet.next()) {
                int totalVotosRecibidos = totalVotosResultSet.getInt("totalVotos");
                datosEscrutinio.add("\nTotal de Votos Recibidos: " + totalVotosRecibidos + " votos");
            }

        } catch (SQLException e) {
            e.printStackTrace(); 
        }

        return datosEscrutinio;
    }



    
}