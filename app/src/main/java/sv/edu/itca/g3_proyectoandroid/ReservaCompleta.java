package sv.edu.itca.g3_proyectoandroid;

public class ReservaCompleta {
    private int id; // Asegúrate de que el ID sea un campo de la clase
    private String nombreCompleto;
    private String tituloLibro;
    private String fecha;
    private String hora;

    // Constructor
    public ReservaCompleta(int id, String nombreCompleto, String tituloLibro, String fecha, String hora) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.tituloLibro = tituloLibro;
        this.fecha = fecha;
        this.hora = hora;
    }

    // Métodos getters
    public int getId() {
        return id; // Devuelve el ID de la reserva
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getTituloLibro() {
        return tituloLibro;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }
}
