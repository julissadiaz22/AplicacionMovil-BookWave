package sv.edu.itca.g3_proyectoandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class ActividadesDAO {

    private ControladorBD controladorBD;

    public ActividadesDAO(Context context) {
        controladorBD = new ControladorBD(context, "bibliotecasa.db", null, 1);
    }

    public boolean actualizarActividad(int id, String nombreActividad, String lugar, String fechaInicio, String fechaFin, String horaInicio, String horaFin, String descripcion, int cupoMaximo) {
        SQLiteDatabase db = controladorBD.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombreActividad", nombreActividad);
        values.put("lugar", lugar);
        values.put("fecha_inicio", fechaInicio);
        values.put("fecha_fin", fechaFin);
        values.put("horaInicio", horaInicio);
        values.put("horaFin", horaFin);
        values.put("descripcion", descripcion);
        values.put("cupoMaximo", cupoMaximo);

        int rowsAffected = db.update("agactividades", values, "id = ?", new String[]{String.valueOf(id)});
        db.close();

        return rowsAffected > 0;
    }


}