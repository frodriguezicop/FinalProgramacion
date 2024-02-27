package Logica;

public enum PartidoPolitico {
    LLA("La Libertad Avanza", "LlA"),
    UXP("Union por la Patria", "UxP"),
    JXC("Juntos por el Cambio", "JxC"),
    FIT("Hacemos Pais", "FiT");

    private final String nombreCompleto;
    private final String lema;

   
    PartidoPolitico(String nombreCompleto, String lema) {
        this.nombreCompleto = nombreCompleto;
        this.lema = lema;
    }

   
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getLema() {
        return lema;
    }
}
