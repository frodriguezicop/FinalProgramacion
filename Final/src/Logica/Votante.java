package Logica;

import java.util.Scanner;

public class Votante extends Ciudadano implements Comparable<Votante> {
    private boolean votoEmitido;
    private String candidatoVoto;
    public Votante(String apellido, String nombre, int edad, String genero, String domicilio,
                   String numeroIdentificacion, boolean votoEmitido) {
        super(apellido, nombre, edad, genero, domicilio, numeroIdentificacion);
        this.votoEmitido = votoEmitido;
    }

    public boolean isVotoEmitido() {
        return votoEmitido;
    }

    public void setVotoEmitido(boolean votoEmitido) {
        this.votoEmitido = votoEmitido;
    }

	public String getCandidatoVoto() {
		return candidatoVoto;
	}

	public void setCandidatoVoto(String candidatoVoto) {
		this.candidatoVoto = candidatoVoto;
	}
	
	
    public int compareTo(Votante otroVotante) {
        int comparacionApellido = this.getApellido().compareTo(otroVotante.getApellido());
        if (comparacionApellido == 0) {
            return this.getNombre().compareTo(otroVotante.getNombre());
        } else {
            return comparacionApellido;
        }
    }
	
	
	 public static Votante registrarVotante(Scanner scanner) {
	        System.out.println("Ingrese los datos del votante:");

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

	        System.out.print("Numero Identificacion: ");
	        String numeroIdentificacion = scanner.nextLine();

	        return new Votante(apellido, nombre, edad, genero, domicilio, numeroIdentificacion, false);
	    }
}
