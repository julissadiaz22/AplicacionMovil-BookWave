package sv.edu.itca.g3_proyectoandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.Adapter;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControladorBD extends SQLiteOpenHelper {
    private ActividadesAdapter adapter;
    private BookAdapter adapterB;
    private UsuariosAdapter adapterU;
    private SQLiteDatabase db;

    private final Context context;

    public ControladorBD(@Nullable Context context,
                         @Nullable String name,
                         @Nullable SQLiteDatabase.CursorFactory factory,
                         int version) {
        super(context, name, factory, version);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla aglibro con columna para la imagen
        db.execSQL("CREATE TABLE aglibro (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "titulo TEXT, " +
                "autor TEXT, " +
                "genero TEXT, " +
                "isbn TEXT, " +
                "fecha_ingreso TEXT, " +
                "cantidad INTEGER, " +
                "descripcion TEXT, " +
                "estado INTEGER DEFAULT 1, " +
                "imagen TEXT)"); // Nueva columna para la imagen

        // Crear tabla agactividades con columnas para fecha_inicio y fecha_fin
        db.execSQL("CREATE TABLE agactividades (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombreActividad TEXT, " +
                "lugar TEXT, " +
                "fecha_inicio TEXT, " +
                "fecha_fin TEXT, " +
                "horaInicio TEXT, " +
                "horaFin TEXT," +
                "descripcion TEXT, " +
                "cupoMaximo INTEGER, " +
                "estado INTEGER DEFAULT 1)");

        // Crear tabla agusuarios
        db.execSQL("CREATE TABLE agusuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombreCompleto TEXT, " +
                "nombreUsuario TEXT, " +
                "correo TEXT, " +
                "clave TEXT, " +
                "estado INTEGER DEFAULT 1, " +
                "rol INTEGER DEFAULT 1)");

        //Crear tabla reservalibro

        db.execSQL("CREATE TABLE IF NOT EXISTS reservarlibro (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "idlibro INTEGER, " +
                "idusuario INTEGER, " +
                "fecha TEXT, " +
                "hora TEXT, " +
                "estado INTEGER DEFAULT 0, " +
                "FOREIGN KEY (idlibro) REFERENCES aglibro(id), " +
                "FOREIGN KEY (idusuario) REFERENCES agusuarios(id))");


        // Crear tabla inscripciones
        db.execSQL("CREATE TABLE IF NOT EXISTS inscripciones (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "idactividad INTEGER, " +
                "idusuario INTEGER, " +
                "fecha TEXT, " +
                "estado INTEGER DEFAULT 0, " +
                "FOREIGN KEY (idactividad) REFERENCES agactividades(id), " +
                "FOREIGN KEY (idusuario) REFERENCES agusuarios(id))");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int v1, int v2) {
        db.execSQL("DROP TABLE IF EXISTS aglibro");
        db.execSQL("DROP TABLE IF EXISTS agactividades");
        db.execSQL("DROP TABLE IF EXISTS agusuarios");
        db.execSQL("DROP TABLE IF EXISTS reservarlibro");
        db.execSQL("DROP TABLE IF EXISTS inscripciones");
        onCreate(db);
    }

    // Método para agregar un libro con imagen
    public void agregarLibro(String titulo, String autor, String genero, String isbn, String fechaIngreso, int cantidad, String descripcion, String imagen) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("imagen", imagen);
        values.put("autor", autor);
        values.put("genero", genero);
        values.put("isbn", isbn);
        values.put("fecha_ingreso", fechaIngreso);
        values.put("cantidad", cantidad);
        values.put("descripcion", descripcion);
        values.put("estado", 1); // Estado activo por defecto
        db.insert("aglibro", null, values);
        db.close();
    }

    // Método para obtener la ruta de la imagen de un libro por su ID
    public String obtenerImagenLibro(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("aglibro", new String[]{"imagen"}, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        String imagen = null;
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("imagen");
            if (columnIndex != -1) { // Verifica si la columna existe
                imagen = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        db.close();
        return imagen;
    }

    // Método para guardar una imagen en el almacenamiento privado de la aplicación
    public String guardarImagen(Bitmap bitmap, String nombreArchivo) {
        File directorio = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES); // Almacenamiento privado
        if (directorio != null && !directorio.exists()) {
            directorio.mkdirs();
        }

        File archivoImagen = new File(directorio, nombreArchivo + ".jpg");
        try (FileOutputStream fos = new FileOutputStream(archivoImagen)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
            return null; // En caso de error, devuelve null
        }

        return archivoImagen.getAbsolutePath(); // Devuelve la ruta donde se guardó la imagen
    }

    // Método para agregar una nueva actividad
    public void agregarActividad(String nombreActividad, String lugar, String fechaInicio, String fechaFin, String horaInicio, String horaFin, String descripcion, int cupoMaximo, int estado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Validar que los parámetros no sean nulos
        if (nombreActividad != null && lugar != null && fechaInicio != null && fechaFin != null && horaInicio != null && horaFin != null) {
            values.put("nombreActividad", nombreActividad);
            values.put("lugar", lugar);
            values.put("fecha_inicio", fechaInicio);
            values.put("fecha_fin", fechaFin);
            values.put("horaInicio", horaInicio);
            values.put("horaFin", horaFin);
            values.put("descripcion", descripcion);
            values.put("cupoMaximo", cupoMaximo);
            values.put("estado", estado); // Cambiar a la variable estado pasada como parámetro

            long id = db.insert("agactividades", null, values);
            db.close();
        }
    }

    // Método para agregar un nuevo usuario
    public boolean agregarUsuario(Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombreCompleto", usuario.getNombreCompleto());
        values.put("nombreUsuario", usuario.getNombreUsuario());
        values.put("correo", usuario.getCorreo());
        values.put("clave", usuario.getClave());
        values.put("rol", usuario.getRol());
        values.put("estado", 1); // Estado activo por defecto
        long resultado = db.insert("agusuarios", null, values);
        db.close();
        return resultado != -1; // Si insertó correctamente, devuelve true
    }


    // Método para obtener todos los libros
    public List<Libro> obtenerTodosLosLibros() {
        List<Libro> libros = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM aglibro", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Verificar índices antes de acceder a las columnas
                int idIndex = cursor.getColumnIndex("id");
                int tituloIndex = cursor.getColumnIndex("titulo");
                int autorIndex = cursor.getColumnIndex("autor");
                int generoIndex = cursor.getColumnIndex("genero");
                int isbnIndex = cursor.getColumnIndex("isbn");
                int fechaIngresoIndex = cursor.getColumnIndex("fecha_ingreso");
                int cantidadIndex = cursor.getColumnIndex("cantidad");
                int descripcionIndex = cursor.getColumnIndex("descripcion");
                int estadoIndex = cursor.getColumnIndex("estado");
                int imagenIndex = cursor.getColumnIndex("imagen");

                // Solo proceder si los índices son válidos
                if (idIndex != -1 && tituloIndex != -1 && autorIndex != -1 &&
                        generoIndex != -1 && isbnIndex != -1 && fechaIngresoIndex != -1 &&
                        cantidadIndex != -1 && descripcionIndex != -1 && estadoIndex != -1 &&
                        imagenIndex != -1) {

                    // Extraer información del cursor usando los índices
                    int id = cursor.getInt(idIndex);
                    String titulo = cursor.getString(tituloIndex);
                    String autor = cursor.getString(autorIndex);
                    String genero = cursor.getString(generoIndex);
                    String isbn = cursor.getString(isbnIndex);
                    String fechaIngreso = cursor.getString(fechaIngresoIndex);
                    int cantidad = cursor.getInt(cantidadIndex);
                    String descripcion = cursor.getString(descripcionIndex);
                    int estado = cursor.getInt(estadoIndex);
                    String imagen = cursor.getString(imagenIndex);

                    // Crear objeto Libro y añadirlo a la lista
                    Libro libro = new Libro(id, titulo, autor, genero, isbn, fechaIngreso, cantidad, descripcion, estado, imagen);
                    libros.add(libro);
                } else {
                    // Manejo de error en caso de que falte alguna columna
                    System.out.println("Una o más columnas faltan en el cursor.");
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return libros;
    }

    // Método para obtener un libro por ID
    public Libro obtenerLibroPorId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("aglibro", null, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        Libro libro = null;

        if (cursor != null && cursor.moveToFirst()) {
            // Verificar índices antes de acceder a las columnas
            int idIndex = cursor.getColumnIndex("id");
            int tituloIndex = cursor.getColumnIndex("titulo");
            int autorIndex = cursor.getColumnIndex("autor");
            int generoIndex = cursor.getColumnIndex("genero");
            int isbnIndex = cursor.getColumnIndex("isbn");
            int fechaIngresoIndex = cursor.getColumnIndex("fecha_ingreso");
            int cantidadIndex = cursor.getColumnIndex("cantidad");
            int descripcionIndex = cursor.getColumnIndex("descripcion");
            int estadoIndex = cursor.getColumnIndex("estado");
            int imagenIndex = cursor.getColumnIndex("imagen");

            // Solo proceder si los índices son válidos
            if (idIndex != -1 && tituloIndex != -1 && autorIndex != -1 &&
                    generoIndex != -1 && isbnIndex != -1 && fechaIngresoIndex != -1 &&
                    cantidadIndex != -1 && descripcionIndex != -1 && estadoIndex != -1 &&
                    imagenIndex != -1) {

                // Extraer información del cursor usando los índices
                int libroId = cursor.getInt(idIndex);
                String titulo = cursor.getString(tituloIndex);
                String autor = cursor.getString(autorIndex);
                String genero = cursor.getString(generoIndex);
                String isbn = cursor.getString(isbnIndex);
                String fechaIngreso = cursor.getString(fechaIngresoIndex);
                int cantidad = cursor.getInt(cantidadIndex);
                String descripcion = cursor.getString(descripcionIndex);
                int estado = cursor.getInt(estadoIndex);
                String imagen = cursor.getString(imagenIndex);

                // Crear objeto Libro
                libro = new Libro(libroId, titulo, autor, genero, isbn, fechaIngreso, cantidad, descripcion, estado, imagen);
            }
            cursor.close();
        }
        db.close();
        return libro;
    }

    // Método para eliminar un libro por ID
    public boolean eliminarLibro(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            return db.delete("aglibro", "id = ?", new String[]{String.valueOf(id)}) > 0;
        } catch (Exception e) {
            Log.e("ControladorBD", "Error al eliminar libro: ", e);
            return false;
        } finally {
            db.close();
        }
    }

    // Método para recargar la lista de libros
    private void recargarListaLibros() {
        // Aquí obtienes la lista actualizada de libros desde la base de datos
        List<Libro> libros = obtenerTodosLosLibros();

        // Suponiendo que tienes un adaptador para mostrar los libros
        adapterB.setLibros(libros); // Actualiza la lista de libros en el adaptador
        adapterB.notifyDataSetChanged(); // Notifica al adaptador para refrescar la vista
    }

    // Método para eliminar un usuario por ID
    public boolean eliminarUsuario(int idUsuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("agusuarios", "id = ?", new String[]{String.valueOf(idUsuario)});
        db.close();
        return result > 0; // Devuelve true si se eliminó al menos una fila
    }

    // Método para recargar la lista de usuarios
    private void recargarListaUsuarios() {
        // Aquí obtienes la lista actualizada de usuarios desde la base de datos
        List<Usuario> usuarios = obtenerTodosLosUsuarios();

        // Suponiendo que tienes un adaptador para mostrar los usuarios
        adapterU.setUsuarios(usuarios); // Actualiza la lista de usuarios en el adaptador
        adapterU.notifyDataSetChanged(); // Notifica al adaptador para refrescar la vista
    }


    //Metodo para eliminar Actividad

    public boolean eliminarActividad(int idActividad) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("agactividades", "id = ?", new String[]{String.valueOf(idActividad)});
        db.close();
        return result > 0; // Devuelve true si se eliminó al menos una fila
    }

    private void recargarListaActividades() {
        // Aquí recargas la lista de actividades
        // Este es el código de ejemplo para obtener todas las actividades y actualizar el adaptador
        List<Actividades> actividades = obtenerTodasLasActividades();
        // Suponiendo que tienes un adaptador para mostrar las actividades
        adapter.setActividades(actividades);
        adapter.notifyDataSetChanged();
    }


    // Método para actualizar la imagen de un libro
    public void actualizarImagenLibro(int id, String nuevaImagen) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("imagen", nuevaImagen); // Nueva imagen
        db.update("aglibro", values, "id=?", new String[]{String.valueOf(id)});
        db.close();


    }


    public List<Actividades> obtenerTodasLasActividades() {
        List<Actividades> actividadesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase(); // Open database for reading

        // Query to select all records from agactividades
        String query = "SELECT * FROM agactividades";
        Cursor cursor = db.rawQuery(query, null);

        // Log column names for debugging
        StringBuilder columnNames = new StringBuilder("Columns: ");
        for (String name : cursor.getColumnNames()) {
            columnNames.append(name).append(", ");
        }
        Log.d("ControladorBD", columnNames.toString());

        // Check if the cursor has results
        if (cursor.moveToFirst()) {
            do {
                // Use getColumnIndex with error handling
                int idIndex = cursor.getColumnIndex("id");
                int nombreActividadIndex = cursor.getColumnIndex("nombreActividad");
                int lugarIndex = cursor.getColumnIndex("lugar");
                int fechaInicioIndex = cursor.getColumnIndex("fecha_inicio");
                int fechaFinIndex = cursor.getColumnIndex("fecha_fin");
                int horaInicioIndex = cursor.getColumnIndex("horaInicio");
                int horaFinIndex = cursor.getColumnIndex("horaFin");
                int descripcionIndex = cursor.getColumnIndex("descripcion");
                int cupoMaximoIndex = cursor.getColumnIndex("cupoMaximo");
                int estadoIndex = cursor.getColumnIndex("estado");

                // Check if indexes are valid
                if (idIndex == -1 || nombreActividadIndex == -1 || lugarIndex == -1 ||
                        fechaInicioIndex == -1 || fechaFinIndex == -1 ||
                        horaInicioIndex == -1 || horaFinIndex == -1 ||
                        descripcionIndex == -1 || cupoMaximoIndex == -1 || estadoIndex == -1) {
                    Log.e("ControladorBD", "One or more column indexes are invalid.");
                    break; // Exit the loop if any index is invalid
                }

                // Retrieve values from the cursor
                int id = cursor.getInt(idIndex);
                String nombreActividad = cursor.getString(nombreActividadIndex);
                String lugar = cursor.getString(lugarIndex);
                String fechaInicio = cursor.getString(fechaInicioIndex);
                String fechaFin = cursor.getString(fechaFinIndex);
                String horaInicio = cursor.getString(horaInicioIndex);
                String horaFin = cursor.getString(horaFinIndex);
                String descripcion = cursor.getString(descripcionIndex);
                int cupoMaximo = cursor.getInt(cupoMaximoIndex);
                int estado = cursor.getInt(estadoIndex);

                // Create a new Actividades object and add it to the list
                Actividades actividad = new Actividades(id, nombreActividad, lugar, fechaInicio,
                        fechaFin, horaInicio, horaFin,
                        descripcion, cupoMaximo, estado);
                actividadesList.add(actividad);
            } while (cursor.moveToNext()); // Move to the next record
        }

        cursor.close(); // Close the cursor
        db.close(); // Close the database connection
        return actividadesList; // Return the list of activities
    }

    //METODOS PARA USUARIOS
    // Método para obtener todos los usuarios de la tabla agusuarios
    public List<Usuario> obtenerTodosLosUsuarios() {
        List<Usuario> listaUsuarios = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta SQL para obtener todos los usuarios
        Cursor cursor = db.query("agusuarios", null, null, null, null, null, null);

        // Verifica si el cursor tiene datos
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Verifica si las columnas existen antes de obtener sus valores
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String nombreCompleto = cursor.getString(cursor.getColumnIndexOrThrow("nombreCompleto"));
                String nombreUsuario = cursor.getString(cursor.getColumnIndexOrThrow("nombreUsuario"));
                String correo = cursor.getString(cursor.getColumnIndexOrThrow("correo"));
                String clave = cursor.getString(cursor.getColumnIndexOrThrow("clave"));
                int estado = cursor.getInt(cursor.getColumnIndexOrThrow("estado"));
                int rol = cursor.getInt(cursor.getColumnIndexOrThrow("rol"));

                // Crear un objeto Usuario con los datos obtenidos
                Usuario usuario = new Usuario(id, nombreCompleto, nombreUsuario, correo, clave, estado, rol);
                listaUsuarios.add(usuario);
            } while (cursor.moveToNext());
        }

        // Cerrar cursor y base de datos
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return listaUsuarios;
    }

    //METODOS DE RESERVAS

    public boolean existeLibro(int idLibro) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("aglibro", new String[]{"id"}, "id=?", new String[]{String.valueOf(idLibro)}, null, null, null);
        boolean existe = (cursor != null && cursor.moveToFirst());
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return existe;
    }

    public boolean existeUsuario(int idUsuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("agusuarios", new String[]{"id"}, "id=?", new String[]{String.valueOf(idUsuario)}, null, null, null);
        boolean existe = (cursor != null && cursor.moveToFirst());
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return existe;
    }

    // Método para agregar una nueva reserva

    public void agregarReserva(int id, int idUsuario, String fecha, String hora) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Verificar si hay stock disponible para el libro
        Cursor cursor = db.rawQuery("SELECT cantidad FROM aglibro WHERE id = ?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            int cantidadDisponible = cursor.getInt(0);

            if (cantidadDisponible > 0) {
                // Crear la reserva
                ContentValues values = new ContentValues();
                values.put("idlibro", id);
                values.put("idusuario", idUsuario);
                values.put("fecha", fecha);
                values.put("hora", hora);
                values.put("estado", 0); // Estado por defecto (0 = pendiente)

                long resultado = db.insert("reservarlibro", null, values);
                if (resultado == -1) {
                    Log.e("ControladorBD", "Error al insertar la reserva");
                } else {
                    Log.d("ControladorBD", "Reserva insertada con ID: " + resultado);
                }
            } else {
                Log.e("ControladorBD", "No hay stock disponible para el libro con ID: " + id);
            }
        } else {
            Log.e("ControladorBD", "El libro con ID " + id + " no existe.");
        }

        cursor.close();
        db.close();
    }


    // Método para aprobar una reserva
    public void aprobarReserva(int reservaId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Verificar si la reserva existe
        Cursor cursor = db.rawQuery("SELECT idlibro FROM reservarlibro WHERE id = ?", new String[]{String.valueOf(reservaId)});
        if (cursor.moveToFirst()) {
            int idLibro = cursor.getInt(0);

            // Verificar si hay stock disponible para el libro
            Cursor cursorLibro = db.rawQuery("SELECT cantidad FROM aglibro WHERE id = ?", new String[]{String.valueOf(idLibro)});
            if (cursorLibro.moveToFirst()) {
                int cantidadDisponible = cursorLibro.getInt(0);

                if (cantidadDisponible > 0) {
                    // Crear valores para actualizar el estado de la reserva
                    ContentValues values = new ContentValues();
                    values.put("estado", 1); // Estado de 1 significa aprobado

                    // Actualizar el estado de la reserva en la base de datos
                    int rowsAffected = db.update("reservarlibro", values, "id = ?", new String[]{String.valueOf(reservaId)});

                    if (rowsAffected > 0) {
                        // Restar 1 a la cantidad de libros
                        ContentValues updateValues = new ContentValues();
                        updateValues.put("cantidad", cantidadDisponible - 1);
                        db.update("aglibro", updateValues, "id = ?", new String[]{String.valueOf(idLibro)});

                        Log.d("ControladorBD", "Reserva con ID " + reservaId + " aprobada y cantidad del libro actualizada.");
                    } else {
                        Log.e("ControladorBD", "Error al aprobar la reserva o la reserva no existe con ID: " + reservaId);
                    }
                } else {
                    Log.e("ControladorBD", "No hay stock disponible para el libro con ID: " + idLibro);
                }
            } else {
                Log.e("ControladorBD", "El libro con ID " + idLibro + " no existe.");
            }
            cursorLibro.close();
        } else {
            Log.e("ControladorBD", "La reserva con ID " + reservaId + " no existe.");
        }

        cursor.close();
        db.close();
    }




    public List<ReservaCompleta> obtenerReservasConEstadoCero() {
        List<ReservaCompleta> reservasCompletas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT r.*, u.nombreCompleto, l.titulo FROM reservarlibro r " +
                "JOIN agusuarios u ON r.idusuario = u.id " +
                "JOIN aglibro l ON r.idlibro = l.id WHERE r.estado = ?", new String[]{"0"});

        if (cursor != null && cursor.moveToFirst()) {
            // Imprimir nombres de las columnas para depuración
            String[] columnNames = cursor.getColumnNames();
            for (String columnName : columnNames) {
                Log.d("Column Name", columnName);
            }

            do {
                // Obtener índices de las columnas
                int indexId = cursor.getColumnIndex("id"); // Asegúrate de que "id" es el nombre correcto de la columna en la tabla
                int indexNombreCompleto = cursor.getColumnIndex("nombreCompleto");
                int indexTituloLibro = cursor.getColumnIndex("titulo");
                int indexFecha = cursor.getColumnIndex("fecha");
                int indexHora = cursor.getColumnIndex("hora");

                // Verificar si los índices son válidos
                if (indexId != -1 && indexNombreCompleto != -1 && indexTituloLibro != -1 && indexFecha != -1 && indexHora != -1) {
                    int id = cursor.getInt(indexId); // Obtener el ID de la reserva
                    String nombreCompleto = cursor.getString(indexNombreCompleto);
                    String tituloLibro = cursor.getString(indexTituloLibro);
                    String fecha = cursor.getString(indexFecha);
                    String hora = cursor.getString(indexHora);

                    // Crear objeto ReservaCompleta y añadirlo a la lista
                    ReservaCompleta reservaCompleta = new ReservaCompleta(id, nombreCompleto, tituloLibro, fecha, hora);
                    reservasCompletas.add(reservaCompleta);
                } else {
                    Log.e("Error", "Una o más columnas no se encontraron en el cursor.");
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return reservasCompletas;
    }

    //Obtener Reservas con Estado 1
    public List<ReservaCompleta> obtenerReservasConEstadoUno() {
        List<ReservaCompleta> reservasCompletas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT r.*, u.nombreCompleto, l.titulo FROM reservarlibro r " +
                "JOIN agusuarios u ON r.idusuario = u.id " +
                "JOIN aglibro l ON r.idlibro = l.id WHERE r.estado = ?", new String[]{"1"}); // Cambiado a estado 1

        if (cursor != null && cursor.moveToFirst()) {
            // Imprimir nombres de las columnas para depuración
            String[] columnNames = cursor.getColumnNames();
            for (String columnName : columnNames) {
                Log.d("Column Name", columnName);
            }

            do {
                // Obtener índices de las columnas
                int indexId = cursor.getColumnIndex("id"); // Asegúrate de que "id" es el nombre correcto de la columna en la tabla
                int indexNombreCompleto = cursor.getColumnIndex("nombreCompleto");
                int indexTituloLibro = cursor.getColumnIndex("titulo");
                int indexFecha = cursor.getColumnIndex("fecha");
                int indexHora = cursor.getColumnIndex("hora");

                // Verificar si los índices son válidos
                if (indexId != -1 && indexNombreCompleto != -1 && indexTituloLibro != -1 && indexFecha != -1 && indexHora != -1) {
                    int id = cursor.getInt(indexId); // Obtener el ID de la reserva
                    String nombreCompleto = cursor.getString(indexNombreCompleto);
                    String tituloLibro = cursor.getString(indexTituloLibro);
                    String fecha = cursor.getString(indexFecha);
                    String hora = cursor.getString(indexHora);

                    // Crear objeto ReservaCompleta y añadirlo a la lista
                    ReservaCompleta reservaCompleta = new ReservaCompleta(id, nombreCompleto, tituloLibro, fecha, hora);
                    reservasCompletas.add(reservaCompleta);
                } else {
                    Log.e("Error", "Una o más columnas no se encontraron en el cursor.");
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return reservasCompletas;
    }

    //Para mostrar solo para usuario

    public List<ReservaCompleta> obtenerReservasConEstadoUnoUsuario(int usuarioLogueadoId) {
        List<ReservaCompleta> reservasCompletas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta SQL para obtener solo el título, la fecha y la hora de las reservas del usuario logueado
        Cursor cursor = db.rawQuery("SELECT r.id, l.titulo, r.fecha, r.hora FROM reservarlibro r " +
                "JOIN aglibro l ON r.idlibro = l.id " +
                "WHERE r.estado = ? AND r.idusuario = ?", new String[]{"1", String.valueOf(usuarioLogueadoId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Obtener índices de las columnas
                int indexId = cursor.getColumnIndex("id");
                int indexTituloLibro = cursor.getColumnIndex("titulo");
                int indexFecha = cursor.getColumnIndex("fecha");
                int indexHora = cursor.getColumnIndex("hora");

                // Verificar si los índices son válidos
                if (indexId != -1 && indexTituloLibro != -1 && indexFecha != -1 && indexHora != -1) {
                    int id = cursor.getInt(indexId); // Obtener el ID de la reserva
                    String tituloLibro = cursor.getString(indexTituloLibro);
                    String fecha = cursor.getString(indexFecha);
                    String hora = cursor.getString(indexHora);

                    // Crear objeto ReservaCompleta y añadirlo a la lista
                    ReservaCompleta reservaCompleta = new ReservaCompleta(id, null, tituloLibro, fecha, hora);
                    reservasCompletas.add(reservaCompleta);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return reservasCompletas;
    }

    // Método para eliminar una reserva por ID
    public boolean eliminarReserva(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("reservarlibro", "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0; // Devuelve true si se eliminó al menos una fila
    }

    // Método para actualizar el estado de una reserva
    public void actualizarEstadoReserva(int id, int nuevoEstado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("estado", nuevoEstado); // Actualiza el estado
        db.update("reservarlibro", values, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    //METODOS PARA INSCRIPCIONES

//Agregar Inscripcion

    public boolean agregarInscripcion(int idActividad, int idUsuario, String fecha) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Verificar si la actividad existe
        Cursor cursor = db.rawQuery("SELECT id FROM agactividades WHERE id = ?", new String[]{String.valueOf(idActividad)});
        if (cursor.moveToFirst()) {
            // Verificar si el usuario ya está inscrito en la actividad
            Cursor cursorInscripcion = db.rawQuery(
                    "SELECT id FROM inscripciones WHERE idactividad = ? AND idusuario = ?",
                    new String[]{String.valueOf(idActividad), String.valueOf(idUsuario)}
            );

            if (cursorInscripcion.moveToFirst()) {
                // El usuario ya está inscrito en la actividad, mostrar un mensaje y retornar false
                cursorInscripcion.close();
                return false; // Inscripción ya existe
            } else {
                // Crear la inscripción
                ContentValues values = new ContentValues();
                values.put("idactividad", idActividad);
                values.put("idusuario", idUsuario);
                values.put("fecha", fecha);
                values.put("estado", 0); // Estado por defecto (0 = pendiente)

                long resultado = db.insert("inscripciones", null, values);
                cursorInscripcion.close();
                db.close();

                return resultado != -1; // Retorna true si la inserción fue exitosa, false si hubo error
            }
        } else {
            cursor.close();
            db.close();
            return false; // La actividad no existe
        }
    }


    // Método para aprobar una inscripción
    // Método para aprobar una inscripción
    public void aprobarInscripcion(int inscripcionId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Verificar si la inscripción existe y obtener el idactividad
        Cursor cursor = db.rawQuery("SELECT idactividad FROM inscripciones WHERE id = ?", new String[]{String.valueOf(inscripcionId)});
        if (cursor.moveToFirst()) {
            int idActividad = cursor.getInt(0);

            // Verificar si hay cupo disponible para la actividad
            Cursor cursorActividad = db.rawQuery("SELECT cupoMaximo FROM agactividades WHERE id = ?", new String[]{String.valueOf(idActividad)});
            if (cursorActividad.moveToFirst()) {
                int cupoMaximo = cursorActividad.getInt(0);

                if (cupoMaximo > 0) {
                    // Crear valores para actualizar el estado de la inscripción
                    ContentValues values = new ContentValues();
                    values.put("estado", 1); // Estado de 1 significa aprobado

                    // Actualizar el estado de la inscripción en la base de datos
                    int rowsAffected = db.update("inscripciones", values, "id = ?", new String[]{String.valueOf(inscripcionId)});

                    if (rowsAffected > 0) {
                        // Restar 1 al cupoMaximo de la actividad
                        ContentValues updateValues = new ContentValues();
                        updateValues.put("cupoMaximo", cupoMaximo - 1);
                        db.update("agactividades", updateValues, "id = ?", new String[]{String.valueOf(idActividad)});

                        Log.d("ControladorBD", "Inscripción con ID " + inscripcionId + " aprobada y cupo de actividad actualizado.");
                    } else {
                        Log.e("ControladorBD", "Error al aprobar la inscripción o la inscripción no existe con ID: " + inscripcionId);
                    }
                } else {
                    Log.e("ControladorBD", "No hay cupo disponible para la actividad con ID: " + idActividad);
                }
            } else {
                Log.e("ControladorBD", "La actividad con ID " + idActividad + " no existe.");
            }
            cursorActividad.close();
        } else {
            Log.e("ControladorBD", "La inscripción con ID " + inscripcionId + " no existe.");
        }

        cursor.close();
        db.close();
    }

    // Método para eliminar una inscripción por ID
    public boolean eliminarInscripcion(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("inscripciones", "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0; // Devuelve true si se eliminó al menos una fila
    }

    //Obtener inscripciones con estado cero

    public List<Inscripcion> obtenerInscripcionesConEstadoCero() {
        List<Inscripcion> inscripciones = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT i.*, u.nombreCompleto, a.nombreActividad FROM inscripciones i " +
                "JOIN agusuarios u ON i.idusuario = u.id " +
                "JOIN agactividades a ON i.idactividad = a.id WHERE i.estado = ?", new String[]{"0"});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int indexId = cursor.getColumnIndex("id");
                int indexNombreCompleto = cursor.getColumnIndex("nombreCompleto");
                int indexNombreActividad = cursor.getColumnIndex("nombreActividad");
                int indexFechaInscripcion = cursor.getColumnIndex("fecha");

                if (indexId != -1 && indexNombreCompleto != -1 && indexNombreActividad != -1 && indexFechaInscripcion != -1) {
                    int id = cursor.getInt(indexId);
                    String nombreCompleto = cursor.getString(indexNombreCompleto);
                    String nombreActividad = cursor.getString(indexNombreActividad);
                    String fechaInscripcion = cursor.getString(indexFechaInscripcion);

                    Log.d("Inscripcion", "ID: " + id + ", Nombre: " + nombreCompleto + ", Actividad: " + nombreActividad + ", Fecha: " + fechaInscripcion);

                    Inscripcion inscripcion = new Inscripcion(id, nombreCompleto, nombreActividad, fechaInscripcion);
                    inscripciones.add(inscripcion);
                } else {
                    Log.e("Error", "Una o más columnas no se encontraron en el cursor.");
                }
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return inscripciones;
    }

    // Método para obtener las inscripciones con estado 1 (aprobadas)
    public List<Inscripcion> obtenerInscripcionesConEstadoUno() {
        List<Inscripcion> inscripciones = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta SQL para obtener inscripciones con estado 1
        Cursor cursor = db.rawQuery("SELECT i.*, u.nombreCompleto, a.nombreActividad FROM inscripciones i " +
                "JOIN agusuarios u ON i.idusuario = u.id " +
                "JOIN agactividades a ON i.idactividad = a.id WHERE i.estado = ?", new String[]{"1"});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Índices de las columnas
                int indexId = cursor.getColumnIndex("id");
                int indexNombreCompleto = cursor.getColumnIndex("nombreCompleto");
                int indexNombreActividad = cursor.getColumnIndex("nombreActividad");
                int indexFechaInscripcion = cursor.getColumnIndex("fecha");

                // Verifica que las columnas estén presentes
                if (indexId != -1 && indexNombreCompleto != -1 && indexNombreActividad != -1 && indexFechaInscripcion != -1) {
                    int id = cursor.getInt(indexId);
                    String nombreCompleto = cursor.getString(indexNombreCompleto);
                    String nombreActividad = cursor.getString(indexNombreActividad);
                    String fechaInscripcion = cursor.getString(indexFechaInscripcion);

                    // Log para depuración (opcional)
                    Log.d("Inscripcion", "ID: " + id + ", Nombre: " + nombreCompleto + ", Actividad: " + nombreActividad + ", Fecha: " + fechaInscripcion);

                    // Crear un objeto Inscripcion con los datos obtenidos
                    Inscripcion inscripcion = new Inscripcion(id, nombreCompleto, nombreActividad, fechaInscripcion);
                    inscripciones.add(inscripcion);
                } else {
                    Log.e("Error", "Una o más columnas no se encontraron en el cursor.");
                }
            } while (cursor.moveToNext());
        }

        // Cierra el cursor y la base de datos
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return inscripciones;
    }

    // Método para obtener inscripciones aprobadas (estado 1) para un usuario específico
    public List<InscripcionCompleta> obtenerInscripcionesConEstadoUnoUsuario(int usuarioLogueadoId) {
        List<InscripcionCompleta> inscripcionesCompletas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta SQL para obtener las inscripciones aprobadas del usuario logueado
        Cursor cursor = db.rawQuery("SELECT i.id, u.nombreCompleto, a.nombreActividad, i.fecha " +
                "FROM inscripciones i " +
                "JOIN agusuarios u ON i.idusuario = u.id " +
                "JOIN agactividades a ON i.idactividad = a.id " +
                "WHERE i.estado = ? AND i.idusuario = ?", new String[]{"1", String.valueOf(usuarioLogueadoId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Obtener índices de las columnas
                int indexId = cursor.getColumnIndex("id");
                int indexNombreCompleto = cursor.getColumnIndex("nombreCompleto");
                int indexNombreActividad = cursor.getColumnIndex("nombreActividad");
                int indexFechaInscripcion = cursor.getColumnIndex("fecha");

                // Verificar si los índices son válidos
                if (indexId != -1 && indexNombreCompleto != -1 && indexNombreActividad != -1 && indexFechaInscripcion != -1) {
                    int id = cursor.getInt(indexId);
                    String nombreCompleto = cursor.getString(indexNombreCompleto);
                    String nombreActividad = cursor.getString(indexNombreActividad);
                    String fechaInscripcion = cursor.getString(indexFechaInscripcion);

                    // Crear un objeto InscripcionCompleta y añadirlo a la lista
                    InscripcionCompleta inscripcionCompleta = new InscripcionCompleta(id, nombreCompleto, nombreActividad, fechaInscripcion);
                    inscripcionesCompletas.add(inscripcionCompleta);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();

        return inscripcionesCompletas;
    }




}





