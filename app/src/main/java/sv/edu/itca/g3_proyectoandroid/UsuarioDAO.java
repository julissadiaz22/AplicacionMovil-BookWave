package sv.edu.itca.g3_proyectoandroid;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class UsuarioDAO {
    private ControladorBD controladorBD;

    public UsuarioDAO(Context context) {
        // Inicializa el controlador de base de datos
        controladorBD = new ControladorBD(context, "bibliotecasa.db", null, 1);
    }

    // Método para validar usuario y devolver un objeto Usuario si las credenciales son correctas
    public Usuario validarUsuario(String nombreUsuario, String clave) {
        SQLiteDatabase db = controladorBD.getReadableDatabase();
        Usuario usuarioAutenticado = null;

        try {
            // Consulta para verificar usuario y clave en la base de datos
            String query = "SELECT id, nombreUsuario, nombreCompleto, correo, clave, estado, rol " +
                    "FROM agusuarios WHERE nombreUsuario = ? AND clave = ?";
            Cursor cursor = db.rawQuery(query, new String[]{nombreUsuario, clave});

            if (cursor.moveToFirst()) {
                // Verificación segura para obtener índices de las columnas
                int idIndex = cursor.getColumnIndex("id");
                int nombreUsuarioIndex = cursor.getColumnIndex("nombreUsuario");
                int nombreCompletoIndex = cursor.getColumnIndex("nombreCompleto");
                int correoIndex = cursor.getColumnIndex("correo");
                int claveIndex = cursor.getColumnIndex("clave");
                int estadoIndex = cursor.getColumnIndex("estado");
                int rolIndex = cursor.getColumnIndex("rol");

                // Verifica que todas las columnas existen antes de extraer los datos
                if (idIndex != -1 && nombreUsuarioIndex != -1 && nombreCompletoIndex != -1 &&
                        correoIndex != -1 && claveIndex != -1 && estadoIndex != -1 && rolIndex != -1) {

                    // Extrae los datos de la base de datos
                    int id = cursor.getInt(idIndex);
                    String usuario = cursor.getString(nombreUsuarioIndex);
                    String nombreCompleto = cursor.getString(nombreCompletoIndex);
                    String correo = cursor.getString(correoIndex);
                    String claveUsuario = cursor.getString(claveIndex);
                    int estado = cursor.getInt(estadoIndex);
                    int rol = cursor.getInt(rolIndex);

                    // Crea el objeto Usuario con los datos extraídos
                    usuarioAutenticado = new Usuario(id, usuario, nombreCompleto, correo, claveUsuario, estado, rol);
                } else {
                    Log.e("DB_ERROR", "Algunas columnas no existen en la base de datos.");
                }
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error al validar usuario: ", e);
        } finally {
            db.close();
        }

        return usuarioAutenticado;
    }

    public boolean actualizarUsuario(Usuario usuarioLogueado) {
        SQLiteDatabase db = controladorBD.getWritableDatabase(); // Obtén la base de datos en modo escritura

        // Consulta SQL con parámetros
        String query = "UPDATE agusuarios SET nombreCompleto = ?, nombreUsuario = ?, clave = ? WHERE id = ?";

        // Usa SQLiteStatement para vincular los parámetros
        SQLiteStatement statement = db.compileStatement(query);

        try {
            // Vincula los parámetros a la consulta
            statement.bindString(1, usuarioLogueado.getNombreCompleto()); // Primer parámetro: nombreCompleto
            statement.bindString(2, usuarioLogueado.getNombreUsuario()); // Segundo parámetro: nombreUsuario
            statement.bindString(3, usuarioLogueado.getClave()); // Tercer parámetro: clave
            statement.bindLong(4, usuarioLogueado.getId()); // Cuarto parámetro: id

            // Ejecuta la consulta
            statement.executeUpdateDelete();

            return true; // Indica que la actualización fue exitosa
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error al actualizar usuario: ", e);
            return false; // Indica que ocurrió un error
        } finally {
            db.close(); // Cierra la conexión a la base de datos
        }
    }
}