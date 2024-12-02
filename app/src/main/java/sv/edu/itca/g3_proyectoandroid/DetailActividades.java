package sv.edu.itca.g3_proyectoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActividades extends AppCompatActivity {
    private ControladorBD controladorBD;
    private int idActividad; // Asegúrate de que este ID se inicialice correctamente

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_actividades);

        TextView textViewNombre = findViewById(R.id.text_view_nombre_actividad);
        TextView textViewLugar = findViewById(R.id.text_view_lugar);
        TextView textViewFechaInicio = findViewById(R.id.text_view_fecha_inicio);
        TextView textViewFechaFin = findViewById(R.id.text_view_fecha_fin);
        TextView textViewHoraInicio = findViewById(R.id.text_view_hora_inicio);
        TextView textViewHoraFin = findViewById(R.id.text_view_hora_fin);
        TextView textViewDescripcion = findViewById(R.id.text_view_descripcion);
        TextView textViewCupoMaximo = findViewById(R.id.text_view_cupo_maximo);
        Button buttonEliminar = findViewById(R.id.button_eliminar);
        Button buttonRegresar = findViewById(R.id.button_regresar);
        Button buttonEditar = findViewById(R.id.button_editar); // Nuevo botón para editar

        // Inicializar ControladorBD
        controladorBD = new ControladorBD(this, "bibliotecasa.db", null, 1);

        // Obtener la intención y los datos de la actividad seleccionada
        Intent intent = getIntent();
        Actividades actividad = (Actividades) intent.getSerializableExtra("actividad");

        // Establecer los datos en los TextViews
        if (actividad != null) {
            idActividad = actividad.getId(); // Asegúrate de que este método exista en la clase Actividades
            textViewNombre.setText(actividad.getNombreActividad());
            textViewLugar.setText("Lugar: " + actividad.getLugar());
            textViewFechaInicio.setText("Fecha Inicio: " + actividad.getFechaInicio());
            textViewFechaFin.setText("Fecha Fin: " + actividad.getFechaFin());
            textViewHoraInicio.setText("Hora Inicio: " + actividad.getHoraInicio());
            textViewHoraFin.setText("Hora Fin: " + actividad.getHoraFin());
            textViewDescripcion.setText(actividad.getDescripcion());
            textViewCupoMaximo.setText("Cupo Máximo: " + actividad.getCupoMaximo());
        } else {
            // Manejo de error si actividad es null
            textViewNombre.setText("Actividad no disponible");
        }

        buttonRegresar.setOnClickListener(v -> finish());

        // Implementar la lógica para eliminar la actividad ```java
        buttonEliminar.setOnClickListener(v -> {
            // Mostrar diálogo de confirmación
            new AlertDialog.Builder(this)
                    .setTitle("Confirmar Eliminación")
                    .setMessage("¿Estás seguro de que deseas eliminar esta actividad?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        // Eliminar la actividad de la base de datos
                        if (controladorBD.eliminarActividad(idActividad)) {
                            Toast.makeText(this, "Actividad eliminada exitosamente", Toast.LENGTH_SHORT).show();
                            finish(); // Cierra esta actividad y regresa a la lista de actividades
                        } else {
                            Toast.makeText(this, "Error al eliminar la actividad", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // Configurar el botón de editar
        buttonEditar.setOnClickListener(v -> {
            Intent editIntent = new Intent(DetailActividades.this, EditActividadesActivity.class);
            editIntent.putExtra("actividad", actividad); // Pasar la actividad a editar
            startActivity(editIntent);
        });
    }
}