package sv.edu.itca.g3_proyectoandroid;

public class Inscripcion {
    private int id; // Identificador único de la inscripción
    private String nombreCompleto; // Nombre del solicitante
    private String nombreActividad; // Nombre de la actividad
    private String fechaInscripcion; // Fecha de inscripción

    // Constructor
    public Inscripcion(int id, String nombreCompleto, String nombreActividad, String fechaInscripcion) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.nombreActividad = nombreActividad;
        this.fechaInscripcion = fechaInscripcion;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }

    public String getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(String fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }
}