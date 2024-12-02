package sv.edu.itca.g3_proyectoandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class LibroDAO {

    private ControladorBD controladorBD;
    private Context context;

    public LibroDAO(Context context) {
        // Inicializa el controlador de base de datos
        this.context = context;
        controladorBD = new ControladorBD(context, "bibliotecasa.db", null, 1);
    }

    public boolean actualizarLibro(String titulo, String autor, String genero, String isbn, String fechaIngreso, int cantidad, String descripcion, String imagenUrl) {
        SQLiteDatabase db = controladorBD.getWritableDatabase();

        // Log para depurar
        Log.d("LibroDAO", "Actualizando libro ISBN: " + isbn);

        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("autor", autor);
        values.put("genero", genero);
        values.put("isbn", isbn);
        values.put("fecha_ingreso", fechaIngreso);
        values.put("cantidad", cantidad);
        values.put("descripcion", descripcion);
        values.put("imagen", imagenUrl);

        // Actualizar el libro por su ISBN (considerando que ISBN es único)
        int rowsAffected = db.update("aglibro", values, "isbn = ?", new String[]{isbn});

        db.close();

        // Verifica si se actualizaron filas
        return rowsAffected > 0;
    }

    // Método para guardar una imagen en el almacenamiento privado de la aplicación
    public String guardarImagen(Bitmap bitmap, String nombreArchivo) {
        // Directorio privado de almacenamiento de imágenes
        File directorio = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (directorio != null && !directorio.exists()) {
            directorio.mkdirs();
        }

        File archivoImagen = new File(directorio, nombreArchivo + ".png");
        try (FileOutputStream fos = new FileOutputStream(archivoImagen)) {
            // Comprimir y guardar la imagen en formato PNG
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } catch (IOException e) {
            Log.e("LibroDAO", "Error al guardar la imagen: " + e.getMessage());
            return null;
        }

        // Retorna la ruta absoluta de la imagen guardada
        return archivoImagen.getAbsolutePath();
    }
}
