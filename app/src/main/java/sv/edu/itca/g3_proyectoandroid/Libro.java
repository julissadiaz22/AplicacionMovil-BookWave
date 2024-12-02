package sv.edu.itca.g3_proyectoandroid;

import java.io.Serializable;

public class Libro implements Serializable {
    private int id; // ID del libro
    private String titulo; // Título del libro
    private String autor; // Autor del libro
    private String genero; // Género del libro
    private String isbn; // ISBN del libro
    private String fecha_Ingreso; // Fecha de ingreso del libro
    private int cantidad; // Cantidad de libros disponibles
    private String descripcion; // Descripción del libro
    private int estado; // Estado del libro (activo/inactivo)
    private String imagen; // Ruta de la imagen del libro

    public Libro() {
        // Constructor vacío
    }
    // Constructor
    public Libro(int id, String titulo, String autor, String genero, String isbn, String fechaIngreso, int cantidad, String descripcion, int estado, String imagen) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.isbn = isbn;
        this.fecha_Ingreso = fechaIngreso;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.estado = estado;
        this.imagen = imagen;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getFechaIngreso() { return fecha_Ingreso; }
    public void setFechaIngreso(String fechaIngreso) { this.fecha_Ingreso = fechaIngreso; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
}
