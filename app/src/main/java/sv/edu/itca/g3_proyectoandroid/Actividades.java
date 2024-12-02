package sv.edu.itca.g3_proyectoandroid;

import java.io.Serializable;

public class Actividades implements Serializable {
    private int id; // To match the primary key
    private String nombreActividad;
    private String lugar;
    private String fechaInicio;
    private String fechaFin;
    private String horaInicio;
    private String horaFin;
    private String descripcion;
    private int cupoMaximo;
    private int estado;

    public Actividades() {
        // Constructor vacío
    }



    // Constructor
    public Actividades(int id, String nombreActividad, String lugar, String fechaInicio,
                       String fechaFin, String horaInicio, String horaFin,
                       String descripcion, int cupoMaximo, int estado) {
        this.id = id;
        this.nombreActividad = nombreActividad;
        this.lugar = lugar;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.descripcion = descripcion;
        this.cupoMaximo = cupoMaximo;
        this.estado = estado;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public String getLugar() {
        return lugar;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public int getEstado() {
        return estado;
    }

}
