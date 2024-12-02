package sv.edu.itca.g3_proyectoandroid;

public class InscripcionCompleta {

    private String nombreActividad;
    private String lugar;
    private String fechaInicio;

    // Constructor
    public InscripcionCompleta(int id, String nombreActividad, String lugar, String fechaInicio) {
        this.nombreActividad = nombreActividad;
        this.lugar = lugar;
        this.fechaInicio = fechaInicio;
    }

    // Getters y Setters
    public String getNombreActividad() {
        return nombreActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
}
