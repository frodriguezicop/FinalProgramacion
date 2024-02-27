package Logica;

import java.util.Scanner;

public class Candidato extends Votante {
    private PartidoPolitico partidoPolitico;
    private String lema;
    private int cantidadVotos;

    public Candidato(String apellido, String nombre, int edad, String genero, String domicilio,
                     String numeroIdentificacion, PartidoPolitico partidoPolitico, String lema, int cantidadVotos) {
        super(apellido, nombre, edad, genero, domicilio, numeroIdentificacion, false);
        this.partidoPolitico = partidoPolitico;
        this.lema = lema;
        this.cantidadVotos = cantidadVotos;
    }

    public PartidoPolitico getPartidoPolitico() {
        return partidoPolitico;
    }

    public void setPartidoPolitico(PartidoPolitico partidoPolitico) {
        this.partidoPolitico = partidoPolitico;
    }

    public String getLema() {
        return lema;
    }

    public void setLema(String lema) {
        this.lema = lema;
    }

    public int getCantidadVotos() {
        return cantidadVotos;
    }

    public void setCantidadVotos(int cantidadVotos) {
        this.cantidadVotos = cantidadVotos;
    }

    public void incrementarVotos() {
        this.cantidadVotos++;
    }

    public static Candidato registrarCandidato(Scanner scanner) {
        System.out.println("Ingrese los datos del Candidato:");

        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Edad: ");
        int edad = Integer.parseInt(scanner.nextLine());

        System.out.print("Genero: ");
        String genero = scanner.nextLine();

        System.out.print("Domicilio: ");
        String domicilio = scanner.nextLine();

        System.out.print("Numero Identificación: ");
        String numeroIdentificacion = scanner.nextLine();

        System.out.print("Partido Politico: (Digitar: LLA, UXP, JXC, FIT) ");
        String nombrePartido = scanner.nextLine();

        PartidoPolitico partido = null;
        for (PartidoPolitico p : PartidoPolitico.values()) {
            if (p.name().equalsIgnoreCase(nombrePartido)) {
                partido = p;
                break;
            }
        }

        if (partido != null) {
           

            System.out.print("Lema: ");
            String lema = scanner.nextLine();

            return new Candidato(apellido, nombre, edad, genero, domicilio, numeroIdentificacion, partido, lema, 0);
        } else {
            
            System.out.println("Partido politico ingresado no es valido.");
            return null; 
        }
    }
}
