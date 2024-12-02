package sv.edu.itca.g3_proyectoandroid;

public class Reservar {
    private int id;
    private int idlibro;
    private int idusuario;
    private String fecha;
    private String hora;
    private int estado;


    public Reservar() {
    }

    public Reservar(int id, int idlibro, int idusuario, String fecha, String hora, int estado) {
        this.id = id;
        this.idlibro = idlibro;
        this.idusuario = idusuario;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdlibro() {
        return idlibro;
    }

    public void setIdlibro(int idlibro) {
        this.idlibro = idlibro;
    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
