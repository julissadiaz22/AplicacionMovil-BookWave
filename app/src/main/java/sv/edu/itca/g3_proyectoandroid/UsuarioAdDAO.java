package sv.edu.itca.g3_proyectoandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class UsuarioAdDAO {
    private ControladorBD controladorBD;

    public UsuarioAdDAO(Context context) {
        controladorBD = new ControladorBD(context, "bibliotecasa.db", null, 1);
    }

    public boolean actualizarUsuario(int id, String nombreCompleto, String nombreUsuario, String correo, String clave, int estado, int rol) {
        SQLiteDatabase db = controladorBD.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombreCompleto", nombreCompleto);
        values.put("nombreUsuario", nombreUsuario);
        values.put("correo", correo);
        values.put("clave", clave); // Asegúrate de que la clave se incluya en la actualización
        values.put("estado", estado);
        values.put("rol", rol);

        int rowsAffected = db.update("agusuarios", values, "id = ?", new String[]{String.valueOf(id)});
        db.close();

        return rowsAffected > 0;
    }
}