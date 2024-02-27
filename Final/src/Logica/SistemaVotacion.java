
package Logica;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Excepciones.ExcepcionVotanteNoEncontrado;
import Excepciones.ExcepcionVotanteReincidente;

public class SistemaVotacion {
    public SistemaVotacion(ArrayList<Votante> votantes, ArrayList<Candidato> candidatos, int votosEmitidos,
			int votosEnBlanco, int votosImpugnados) {
		super();
		this.votantes = votantes;
		this.candidatos = candidatos;
		this.votosEmitidos = votosEmitidos;
		this.votosEnBlanco = votosEnBlanco;
		this.votosImpugnados = votosImpugnados;
	}

	private ArrayList<Votante> votantes;
    private ArrayList<Candidato> candidatos;
    private int votosEmitidos;
    
    public SistemaVotacion() {
        votantes = new ArrayList<>();
        candidatos = new ArrayList<>();
    }
    
    
    public int getVotosEmitidos() {
		return votosEmitidos;
	}

	public void setVotosEmitidos(int votosEmitidos) {
		this.votosEmitidos = votosEmitidos;
	}

	public int getVotosEnBlanco() {
		return votosEnBlanco;
	}

	public void setVotosEnBlanco(int votosEnBlanco) {
		this.votosEnBlanco = votosEnBlanco;
	}

	public int getVotosImpugnados() {
		return votosImpugnados;
	}

	public void setVotosImpugnados(int votosImpugnados) {
		this.votosImpugnados = votosImpugnados;
	}

	private int votosEnBlanco;
    private int votosImpugnados;

    
    public boolean esCandidato(String numeroIdentificacion) {
        for (Candidato candidato : candidatos) {
            if (candidato.getNumeroIdentificacion().equals(numeroIdentificacion)) {
                return true; 
            }
        }
        return false; 
    }
    
    
    public void registrarVotante(Votante votante) throws ExcepcionVotanteReincidente {
        if (esCandidato(votante.getNumeroIdentificacion())) {
            throw new ExcepcionVotanteReincidente("El ciudadano ya esta registrado como candidato.");
        }

        if (buscarVotante(votante.getNumeroIdentificacion()) != null) {
            throw new ExcepcionVotanteReincidente("El ciudadano ya esta registrado como votante.");
        }

        votantes.add(votante);
        
    }


    public void registrarCandidato(Candidato candidato) {
        candidatos.add(candidato);
    }

    public Votante buscarVotante(String numeroIdentificacion) {
        for (Votante votante : votantes) {
            if (votante.getNumeroIdentificacion().equals(numeroIdentificacion)) {
                return votante;
            }
        }
        return null;
    }

    public void votar(String numeroIdentificacion, String candidatoVoto) throws ExcepcionVotanteNoEncontrado, ExcepcionVotanteReincidente {
        Votante votante = buscarVotante(numeroIdentificacion);
        if (votante == null) {
            throw new ExcepcionVotanteNoEncontrado("Votante no encontrado.");
        }

        if (votante.isVotoEmitido()) {
            throw new ExcepcionVotanteReincidente("El votante ya ha emitido su voto.");
        }

        votante.setVotoEmitido(true);
        votante.setCandidatoVoto(candidatoVoto);
        incrementarVotosCandidato(candidatoVoto);
        votosEmitidos++; 
    }
    
    
    
    
    
    
    

    //  metodo para verificar ya ha emitido su voto
    public boolean votanteYaVoto(String numeroIdentificacion) {
        Votante votante = buscarVotante(numeroIdentificacion);
        return votante != null && votante.isVotoEmitido();
    }


    private void incrementarVotosCandidato(String candidatoVoto) {
        for (Candidato candidato : candidatos) {
            if (candidato.getNumeroIdentificacion().equals(candidatoVoto)) {
                candidato.incrementarVotos();
                break;
            }
        }
    }
    //lista votantes
    public List<Votante> generarPadronVotantesOrdenado() {
        List<Votante> padronOrdenado = new ArrayList<>(votantes);

      
        //modificacionLAMBDA
        Collections.sort(padronOrdenado, new Comparator<Votante>() {
            @Override
            public int compare(Votante v1, Votante v2) {
                int comparacionApellido = v1.getApellido().compareTo(v2.getApellido());
                if (comparacionApellido != 0) {
                    return comparacionApellido;
                }
                int comparacionNombre = v1.getNombre().compareTo(v2.getNombre());
                if (comparacionNombre != 0) {
                    return comparacionNombre;
                }
                return Integer.compare(v1.getEdad(), v2.getEdad());
            }
        });

        return padronOrdenado;
    }
    
    //lista candidatos
    
    public List<Candidato> generarListaCandidatosOrdenadaPorPartido() {
        List<Candidato> listaCandidatosOrdenada = new ArrayList<>(candidatos);

        Collections.sort(listaCandidatosOrdenada, new Comparator<Candidato>() {
            @Override
            public int compare(Candidato c1, Candidato c2) {
                return c1.getPartidoPolitico().compareTo(c2.getPartidoPolitico());
            }
        });

        return listaCandidatosOrdenada;
    }

    
    
    public HashMap<String, Integer> puntoEscrutinio() {
        HashMap<String, Integer> resultados = new HashMap<>();
        for (Candidato candidato : candidatos) {
            resultados.put(candidato.getNombre(), candidato.getCantidadVotos());
        }
        resultados.put("Votos en Blanco", votosEnBlanco);
        resultados.put("Votos Impugnados", votosImpugnados);
        
        return resultados;
    }

    public List<Votante> getListadoVotantes() {
        return new ArrayList<>(votantes);
    }

    public List<Candidato> getListadoCandidatos() {
        return new ArrayList<>(candidatos);
    }
    
   
  //votos en blanco
    public void votosEnBlanco(String numeroIdentificacionVotante) {
        for (Votante votante : votantes) {
            if (votante.getNumeroIdentificacion().equals(numeroIdentificacionVotante)) {
                if (!votante.isVotoEmitido()) {
                    votante.setVotoEmitido(true);
                    votosEnBlanco++;
                    // votosEmitidos++; // No incrementar votosEmitidos aquí
                }
                return;
            }
        }
    }

    //votos impugnados
    public void votosImpugnados(String numeroIdentificacionVotante) {
        for (Votante votante : votantes) {
            if (votante.getNumeroIdentificacion().equals(numeroIdentificacionVotante)) {
                if (!votante.isVotoEmitido()) {
                    votante.setVotoEmitido(true);
                    votosImpugnados++;
                    // votosEmitidos++; // No incrementar votosEmitidos aquí
                }
                return;
            }
        }
    }

    //validacion de los votos emitidos

    public boolean validarVotos() {
        int totalVotosEmitidos = getVotosEmitidos() + getVotosEnBlanco() + getVotosImpugnados();

        return totalVotosEmitidos == votantes.size();
    }



    
    public void emitirEscrutinioDefinitivo() {
        HashMap<String, Integer> votosPorPartido = new HashMap<>();
        HashMap<String, Integer> votosPorCandidato = new HashMap<>();

        int votosTotales = 0;

        // contar los votos por partido y por candidato
        for (Candidato candidato : candidatos) {
            String partido = candidato.getPartidoPolitico().getNombreCompleto();
            votosPorCandidato.put(candidato.getNombre(), candidato.getCantidadVotos());

            votosPorPartido.put(partido, votosPorPartido.getOrDefault(partido, 0) + candidato.getCantidadVotos());
            votosTotales += candidato.getCantidadVotos();
        }

        // contar votos en blanco e impugnados
        votosTotales += getVotosEnBlanco() + getVotosImpugnados();

        List<Map.Entry<String, Integer>> listaCandidatosOrdenados =
                new ArrayList<>(votosPorCandidato.entrySet());
        listaCandidatosOrdenados.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

        System.out.println("Terna de Ganadores:");
        for (int i = 0; i < Math.min(3, listaCandidatosOrdenados.size()); i++) {
            Map.Entry<String, Integer> candidatoEntry = listaCandidatosOrdenados.get(i);
            System.out.println(
                candidatoEntry.getKey() + ": " + candidatoEntry.getValue() + " votos"
            );
        }

        // cantidad de votos totales por partido
        System.out.println("Votos totales por partido:");
        for (Map.Entry<String, Integer> entry : votosPorPartido.entrySet()) {
            System.out.println(
                entry.getKey() + ": " + entry.getValue() + " votos"
            );
        }

        // mostrar votos totales
        System.out.println("Votos totales: " + votosTotales);
    }


    
}
