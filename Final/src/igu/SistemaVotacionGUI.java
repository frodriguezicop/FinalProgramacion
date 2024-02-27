package igu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;

import Persistencia.CandidatoPersistencia;
import Persistencia.VotacionPersistencia;
import Persistencia.VotantePersistencia;
import Excepciones.ExcepcionVotanteNoEncontrado;
import Excepciones.ExcepcionVotanteReincidente;
import Logica.Candidato;
import Logica.PartidoPolitico;
import Logica.SistemaVotacion;
import Logica.Votante;

public class SistemaVotacionGUI {

    private JFrame frame;
    private SistemaVotacion sistema;
    private JTextArea textArea;
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                SistemaVotacionGUI window = new SistemaVotacionGUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public SistemaVotacionGUI() {
    	VotantePersistencia.crearTablaVotante();
    	CandidatoPersistencia.crearTablaCandidato();
        VotacionPersistencia.crearTablaVotacion();
       

    	initialize();
        sistema = new SistemaVotacion();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu menuInicio = new JMenu("Inicio");
        menuBar.add(menuInicio);

        JMenuItem menuItemAltaVotante = new JMenuItem("Alta Votante");
        menuInicio.add(menuItemAltaVotante);

        JMenuItem menuItemAltaCandidato = new JMenuItem("Alta Candidato");
        menuInicio.add(menuItemAltaCandidato);
        
        menuItemAltaCandidato.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirFormularioCandidato();
            }
        });
        
        textArea = new JTextArea();
        textArea.setEditable(false);
        
        frame.getContentPane().add(textArea, BorderLayout.CENTER);

        
        JMenuItem menuItemVotar = new JMenuItem("Votar");
        menuInicio.add(menuItemVotar);
        
        menuItemVotar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirFormularioVotar();
            }
        });
        
        
        JMenu menuInfo = new JMenu("Info");
        menuBar.add(menuInfo);

        JMenuItem menuItemAcercaDe = new JMenuItem("Acerca De");
        menuInfo.add(menuItemAcercaDe);

        JMenuItem menuItemPadron = new JMenuItem("Padrón");
        menuInfo.add(menuItemPadron);

        JMenuItem menuItemCandidatos = new JMenuItem("Candidatos");
        menuInfo.add(menuItemCandidatos);
        
      

        JMenuItem menuItemEscrutinioProvisorio = new JMenuItem("Escrutinio Provisorio");
        menuInfo.add(menuItemEscrutinioProvisorio);

        JMenuItem menuItemEscrutinioDefinitivo = new JMenuItem("Escrutinio Definitivo");
        menuInfo.add(menuItemEscrutinioDefinitivo);

        JMenuItem menuItemSalir = new JMenuItem("Salir");
        menuBar.add(menuItemSalir);
        
        
     
       

        menuItemAltaVotante.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirFormularioVotante();
            }
        });

        menuItemSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menuItemAltaCandidato.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirFormularioCandidato();
            }
        });
        
 
       
        
        menuItemAcercaDe.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarAcercaDe();
            }
        });
        

        menuItemCandidatos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarCandidatos();
            }
        });
        
        menuItemPadron.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirPadron(); 
            }
        });
        
        
        menuItemEscrutinioProvisorio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	abrirEscrutinioProvisorio(); 
            }
        });
        
        menuItemEscrutinioDefinitivo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	abrirEscrutinioDefinitivo();
            }
        });

        
        
    }

    


    private void abrirFormularioVotante() {
        JTextField apellidoField = new JTextField();
        JTextField nombreField = new JTextField();
        JTextField edadField = new JTextField();
        JTextField generoField = new JTextField();
        JTextField domicilioField = new JTextField();
        JTextField identificacionField = new JTextField();

        Object[] message = {
                "Apellido:", apellidoField,
                "Nombre:", nombreField,
                "Edad:", edadField,
                "Género:", generoField,
                "Domicilio:", domicilioField,
                "Identificación:", identificacionField
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Alta Votante", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String apellido = apellidoField.getText();
            String nombre = nombreField.getText();
            String edadStr = edadField.getText();
            String genero = generoField.getText();
            String domicilio = domicilioField.getText();
            String identificacion = identificacionField.getText();

            
            if (apellido.isEmpty() || nombre.isEmpty() || edadStr.isEmpty() || genero.isEmpty() || domicilio.isEmpty() || identificacion.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Todos los campos son obligatorios");
                return; 
            }
            
            if (VotantePersistencia.existeVotante(identificacion) || CandidatoPersistencia.existeCandidato(identificacion)) {
                JOptionPane.showMessageDialog(frame, "El número de identificación ya está en uso. Por favor, elija otro.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int edad = Integer.parseInt(edadStr);
                Votante nuevoVotante = new Votante(apellido, nombre, edad, genero, domicilio, identificacion, false);

                // Guardar al votante en la base de datos
                VotantePersistencia.guardarVotante(nuevoVotante);

                JOptionPane.showMessageDialog(frame, "Votante registrado con éxito.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "La edad debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    
    private void abrirFormularioVotar() {
        JTextField identificacionVotanteField = new JTextField();

        
        int optionVotante = JOptionPane.showConfirmDialog(frame, identificacionVotanteField, "Ingrese el número de identificación del votante", JOptionPane.OK_CANCEL_OPTION);

        if (optionVotante == JOptionPane.OK_OPTION) {
            String identificacionVotante = identificacionVotanteField.getText();

            //valida vacio 
            if (identificacionVotante.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "El número de identificación del votante es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //  verifica el votante ya ha votado (consultando la tabla votacion)
            if (VotacionPersistencia.votanteYaVoto(identificacionVotante)) {
                JOptionPane.showMessageDialog(frame, "El votante ya ha emitido su voto.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // verifica si el votante existe
            if (!VotantePersistencia.existeVotante(identificacionVotante)) {
                JOptionPane.showMessageDialog(frame, "Votante no encontrado. Verifique el número de identificación.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //lista de nombres de candidatos disponibles
            java.util.List<String> candidatosDisponibles = CandidatoPersistencia.getCandidatosDisponiblesNombres();

            
            String[] nombresCandidatos = candidatosDisponibles.toArray(new String[0]);

            // agrega opciones de voto en blanco y voto impugnado al arreglo
            String[] opcionesVoto = Arrays.copyOf(nombresCandidatos, nombresCandidatos.length + 2);
            opcionesVoto[nombresCandidatos.length] = "Voto en Blanco";
            opcionesVoto[nombresCandidatos.length + 1] = "Voto Impugnado";

            // ComboBox con las opciones
            JComboBox<String> comboBox = new JComboBox<>(opcionesVoto);

            // la interfaz para seleccionar candidato o voto en blanco/impugnado
            int optionVoto = JOptionPane.showOptionDialog(frame, comboBox, "Seleccione su opción:", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

            if (optionVoto == JOptionPane.OK_OPTION) {
                
                String opcionSeleccionada = (String) comboBox.getSelectedItem();

                // guarda en mi base
                VotacionPersistencia.guardarVotacion(identificacionVotante, opcionSeleccionada);

                
                JOptionPane.showMessageDialog(frame, "¡Voto registrado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    
    
    

    private void abrirFormularioCandidato() {
        JTextField apellidoField = new JTextField();
        JTextField nombreField = new JTextField();
        JTextField edadField = new JTextField();
        JTextField generoField = new JTextField();
        JTextField domicilioField = new JTextField();
        JTextField identificacionField = new JTextField();
        JTextField partidoField = new JTextField();
        JTextField lemaField = new JTextField();

        Object[] message = {
                "Apellido:", apellidoField,
                "Nombre:", nombreField,
                "Edad:", edadField,
                "Género:", generoField,
                "Domicilio:", domicilioField,
                "Identificación:", identificacionField,
                "Partido Político (LLA, UXP, JXC, FIT):", partidoField,
                "Lema:", lemaField
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Alta Candidato", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String apellido = apellidoField.getText();
            String nombre = nombreField.getText();
            String edadStr = edadField.getText();
            String genero = generoField.getText();
            String domicilio = domicilioField.getText();
            String identificacion = identificacionField.getText();
            String partido = partidoField.getText();
            String lema = lemaField.getText();

            
            if (apellido.isEmpty() || nombre.isEmpty() || edadStr.isEmpty() || genero.isEmpty() || domicilio.isEmpty()
                    || identificacion.isEmpty() || partido.isEmpty() || lema.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Todos los campos son obligatorios");
                return; 
            }

            // verifica si nmum ident esta en uso 
            if (VotantePersistencia.existeVotante(identificacion) || CandidatoPersistencia.existeCandidato(identificacion)) {
                JOptionPane.showMessageDialog(frame, "El número de identificación ya está en uso.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int edad = Integer.parseInt(edadStr);

                
                String partidoSiglas = partido.toUpperCase(); 
                if (!Arrays.asList("LLA", "UXP", "JXC", "FIT").contains(partidoSiglas)) {
                    JOptionPane.showMessageDialog(frame, "Partido Político no válido. Ingrese LLA, UXP, JXC o FIT", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                PartidoPolitico partidoPolitico = PartidoPolitico.valueOf(partidoSiglas); // Asumiendo que PartidoPolitico es una enumeración
                Candidato nuevoCandidato = new Candidato(apellido, nombre, edad, genero, domicilio, identificacion, partidoPolitico, lema, 0);

                // guardar en a base
                CandidatoPersistencia.guardarCandidato(nuevoCandidato);
 
                JOptionPane.showMessageDialog(frame, "Candidato registrado con éxito.");
                
                JOptionPane.getRootFrame().dispose();
           
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "La edad debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, "Partido Político no válido", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void mostrarAcercaDe() {
        String informacionDesarrollador = "Desarrollador: Francisco Rodriguez,  " +
                "Fecha e instancia de examen: 27/02/24";

        JPanel panel = new JPanel();
        panel.add(new JLabel(informacionDesarrollador));

        JOptionPane.showMessageDialog(frame, panel, "Acerca De", JOptionPane.INFORMATION_MESSAGE);
    }

   
    
    //padron
    private void abrirPadron() {
        // Obtener la lista de datos del padrón
        List<String> datosPadron = VotantePersistencia.obtenerDatosPadron();

        // Verificar si hay votantes registrados
        if (datosPadron.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No hay votantes registrados en el padrón.", "Padrón Vacío", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        
        StringBuilder mensajePadron = new StringBuilder("Padrón de Votantes Registrados:\n");
        for (String datosVotante : datosPadron) {
            mensajePadron.append(datosVotante).append("\n");
        }

       
        JTextArea textAreaPadron = new JTextArea(mensajePadron.toString());
        textAreaPadron.setEditable(false); 
        textAreaPadron.setBorder(null); 

       
        JScrollPane scrollPanePadron = new JScrollPane(textAreaPadron);
        scrollPanePadron.setBorder(null); 

        
        JOptionPane.showOptionDialog(frame, scrollPanePadron, "Padrón de Votantes Registrados", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
    }
    
    
    
    private void mostrarCandidatos() {
        //recibe lista de información de candidatos
        List<String> infoCandidatos = CandidatoPersistencia.getCandidatosDisponibles();

        // verifica si hay candidatos registrados
        if (infoCandidatos.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No hay candidatos registrados.", "Candidatos Vacíos", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        
        StringBuilder candidatosText = new StringBuilder("Candidatos Disponibles:\n");
        for (String infoCandidato : infoCandidatos) {
            candidatosText.append(infoCandidato).append("\n");
        }

       
        JOptionPane.showMessageDialog(frame, candidatosText.toString(), "Candidatos Disponibles", JOptionPane.INFORMATION_MESSAGE);
    }


    private void abrirEscrutinioProvisorio() {
        //recibe lista de datos para el Escrutinio provisorio
        List<String> datosEscrutinio = VotacionPersistencia.obtenerDatosEscrutinio();

        // Verificar si hay datos para mostrar
        if (datosEscrutinio.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No hay datos para mostrar en el Escrutinio Provisorio.", "Escrutinio Vacío", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        //muestra los datos
        JTextArea textArea = new JTextArea();
        for (String dato : datosEscrutinio) {
            textArea.append(dato + "\n");
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(frame, scrollPane, "Escrutinio Provisorio", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void abrirEscrutinioDefinitivo() {
        List<String> datosEscrutinioDefinitivo = VotacionPersistencia.obtenerDatosEscrutinioDefinitivo();

      
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false); 
        textArea.setLineWrap(true); 
        textArea.setWrapStyleWord(true); 
        
        if (datosEscrutinioDefinitivo.isEmpty()) {
            textArea.append("No hay datos para mostrar en el Escrutinio Definitivo.");
        } else {
            
            for (String dato : datosEscrutinioDefinitivo) {
                textArea.append(dato + "\n");
            }
        }

        
        JScrollPane scrollPane = new JScrollPane(textArea);

        
        JOptionPane.showMessageDialog(frame, scrollPane, "Escrutinio Definitivo", JOptionPane.INFORMATION_MESSAGE);
    }

    
    
}




    
