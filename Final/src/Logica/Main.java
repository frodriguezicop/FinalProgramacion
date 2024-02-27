package Logica;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import Excepciones.ExcepcionVotanteNoEncontrado;
import Excepciones.ExcepcionVotanteReincidente;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SistemaVotacion sistema = new SistemaVotacion();
        boolean votacionCerrada = false;
       
        int opcion;
        do {
            System.out.println("Seleccione una opcion:");
            System.out.println("1. Registrar un votante");
            System.out.println("2. Registrar un candidato");
            System.out.println("3. Generar padron de votantes ordenado");
            System.out.println("4. Lista de Candidatos");
            System.out.println("5. Registrar Voto");
            System.out.println("6. Listado de Votos");
            System.out.println("7. Validar Votos");
            
            System.out.println("0. Salir");

            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    Votante nuevoVotante = Votante.registrarVotante(scanner);
				try {
					sistema.registrarVotante(nuevoVotante);
				} catch (ExcepcionVotanteReincidente e) {
		
					System.out.println("Error: " + e.getMessage());
				}
                    System.out.println("Votante registrado con exito.");
                    break;
                case 2:
                    Candidato nuevoCandidato = Candidato.registrarCandidato(scanner);
                    if (nuevoCandidato != null) {
                        sistema.registrarCandidato(nuevoCandidato);
                        System.out.println("Candidato registrado con exito.");
                    } else {
                        System.out.println("Error al registrar el candidato.");
                    }
                    break;
                case 3:
                    
                	 List<Votante> padronOrdenado = sistema.generarPadronVotantesOrdenado();
                	    if (padronOrdenado.isEmpty()) {
                	        System.out.println("No hay datos cargados.");
                	    } else {
                	        for (Votante votante : padronOrdenado) {
                	            System.out.println(votante.getApellido() + " " + votante.getNombre() + " - Edad: " + votante.getEdad());
                	        }
                	    }
                	    break;
                case 4:
                
                	  List<Candidato> listaCandidatosOrdenada = sistema.generarListaCandidatosOrdenadaPorPartido();
                	    if (listaCandidatosOrdenada.isEmpty()) {
                	        System.out.println("No hay datos cargados.");
                	    } else {
                	        for (Candidato candidato : listaCandidatosOrdenada) {
                	            String partidoNombreCompleto = "";
                	            if (candidato.getPartidoPolitico() != null) {
                	                partidoNombreCompleto = candidato.getPartidoPolitico().getNombreCompleto();
                	            }
                	            System.out.println(candidato.getApellido() + " " + candidato.getNombre() + " - Partido: " + partidoNombreCompleto);
                	        }
                	    }
                	    break;
                
                case 5:
                	if (votacionCerrada) {
                        System.out.println("La votacion ha sido cerrada.");
                        break;
                    }
                	System.out.print("Ingrese el numero de identificacion del votante: ");
                    String numeroIdentificacionVotante = scanner.nextLine();
                    
                    // el votante esta registrado
                    Votante votante = sistema.buscarVotante(numeroIdentificacionVotante);
                    if (votante == null) {
                        System.out.println("Usted no esta registrado.");
                        break;
                    }
                    //el votante ya voto
                    if (sistema.votanteYaVoto(numeroIdentificacionVotante)) {
                        System.out.println("Usted ya ha votado.");
                        break;
                    }
                    System.out.println("Lista de Candidatos Disponibles:");
                    List<Candidato> listaCandidatos = sistema.getListadoCandidatos();
                    
                    for (int i = 0; i < listaCandidatos.size(); i++) {
                        Candidato candidato = listaCandidatos.get(i);
                        System.out.println((i + 1) + ". " + candidato.getNombre() + " " + candidato.getApellido());
                    }
                    
                    System.out.println("0. Votar en Blanco");
                    System.out.println("-1. Impugnar el Voto");

                    System.out.print("Ingrese el numero correspondiente al candidato al que desea votar (-1 para impugnar, 0 para voto en blanco): ");
                    int numeroCandidatoSeleccionado = Integer.parseInt(scanner.nextLine());

                    try {
                        if (numeroCandidatoSeleccionado >= -1 & numeroCandidatoSeleccionado <= listaCandidatos.size()) {
                            if (numeroCandidatoSeleccionado == 0) {
                                sistema.votosEnBlanco(numeroIdentificacionVotante);
                                System.out.println("Voto en Blanco registrado con exito.");
                            } else if (numeroCandidatoSeleccionado == -1) {
                                sistema.votosImpugnados(numeroIdentificacionVotante);
                                System.out.println("Voto impugnado con exito.");
                            } else {
                                Candidato candidatoElegido = listaCandidatos.get(numeroCandidatoSeleccionado - 1);
                                sistema.votar(numeroIdentificacionVotante, candidatoElegido.getNumeroIdentificacion());
                                System.out.println("Voto registrado con exito.");
                            }
                        } else {
                            System.out.println("Opcion no valida.");
                        }
                    } catch (ExcepcionVotanteNoEncontrado | ExcepcionVotanteReincidente e) {
                        System.out.println("Error al registrar el voto: " + e.getMessage());
                    }
                    break;


                    
                case 6:
             
                    System.out.println("Lista de votos:");
                    HashMap<String, Integer> resultados = sistema.puntoEscrutinio();

                   
                    for (Entry<String, Integer> entry : resultados.entrySet()) {
                        System.out.println(entry.getKey() + ": " + entry.getValue() + " votos");
                    }
                    break;
                               
                case 7:
                    System.out.println("Validar Votos:");
                    HashMap<String, Integer> resultados1 = sistema.puntoEscrutinio();

                    for (Entry<String, Integer> entry : resultados1.entrySet()) {
                        System.out.println(entry.getKey() + ": " + entry.getValue() + " votos");
                    }

                    boolean votosValidados = sistema.validarVotos();
                    if (votosValidados) {
                        System.out.println("Los votos han sido validados. Se cierra la votacion.");
                        votacionCerrada = true;

                        
                        sistema.emitirEscrutinioDefinitivo();
                    } else {
                        System.out.println("Los votos no coinciden con la cantidad validada.");
                    }
                    break;

                    
                
                
                case 0:
                    System.out.println("Saliendo del programa.");
                    break;
                default:
                    System.out.println("Opcion no valida.");
                    break;
            }
        } while (opcion != 0);

        scanner.close();
    }
}
