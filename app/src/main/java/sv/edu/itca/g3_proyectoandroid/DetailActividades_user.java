package sv.edu.itca.g3_proyectoandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DetailActividades_user extends AppCompatActivity {

    private ControladorBD controladorBD; // Asegúrate de tener una instancia de tu controlador de base de datos
    private Actividades actividad; // Almacenar la actividad seleccionada
    private int idUsuario; // Variable para almacenar el ID del usuario

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_actividades_user);

        // Inicializar ControladorBD
        controladorBD = new ControladorBD(this, "bibliotecasa.db", null, 1);

        // Obtener el ID del usuario desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("sesion_usuario", MODE_PRIVATE);
        idUsuario = sharedPreferences.getInt("usuario_id", -1); // -1 si no se encuentra el ID

        TextView textViewNombre = findViewById(R.id.text_view_nombre_actividad);
        TextView textViewLugar = findViewById(R.id.text_view_lugar);
        TextView textViewFechaInicio = findViewById(R.id.text_view_fecha_inicio);
        TextView textViewFechaFin = findViewById(R.id.text_view_fecha_fin);
        TextView textViewHoraInicio = findViewById(R.id.text_view_hora_inicio);
        TextView textViewHoraFin = findViewById(R.id.text_view_hora_fin);
        TextView textViewDescripcion = findViewById(R.id.text_view_descripcion);
        TextView textViewCupoMaximo = findViewById(R.id.text_view_cupo_maximo);
        Button buttonInscribirse = findViewById(R.id.button_inscribirse);
        Button buttonRegresar = findViewById(R.id.button_regresar);

        // Obtener la intención y los datos de la actividad seleccionada
        Intent intent = getIntent();
        actividad = (Actividades) intent.getSerializableExtra("actividad");

        // Establecer los datos en los TextViews
        if (actividad != null) {
            textViewNombre.setText(actividad.getNombreActividad());
            textViewLugar.setText("Lugar: " + actividad.getLugar());
            textViewFechaInicio.setText("Fecha Inicio: " + actividad.getFechaInicio());
            textViewFechaFin.setText("Fecha Fin: " + actividad.getFechaFin());
            textViewHoraInicio.setText("Hora Inicio: " + actividad.getHoraInicio());
            textViewHoraFin.setText("Hora Fin: " + actividad.getHoraFin());
            textViewDescripcion.setText(actividad.getDescripcion());
            textViewCupoMaximo.setText("Cupo Máximo: " + actividad.getCupoMaximo());
        } else {
            textViewNombre.setText("Actividad no disponible");
        }

        // Configurar el OnClickListener para el botón "Inscribirse"
        buttonInscribirse.setOnClickListener(v -> {
            if (idUsuario != -1) { // Verificar que el ID del usuario sea válido
                // Obtener la fecha actual
                String fechaActual = obtenerFechaActual();

                // Llamar al método para agregar la inscripción
                boolean exito = controladorBD.agregarInscripcion(actividad.getId(), idUsuario, fechaActual);

                if (exito) {
                    // Si la inscripción fue exitosa
                    Toast.makeText(this, "Inscripción realizada exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    // Si la inscripción no fue exitosa (usuario ya inscrito o error)
                    Toast.makeText(this, "No se puede inscribir a la misma actividad más de una vez", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Error: Usuario no autenticado", Toast.LENGTH_SHORT).show();
            }
        });


        // Configurar el OnClickListener para el botón "Regresar"
        buttonRegresar.setOnClickListener(v -> finish());
    }

    // Método para obtener la fecha actual en formato "yyyy-MM-dd"
    private String obtenerFechaActual() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
}