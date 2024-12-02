package sv.edu.itca.g3_proyectoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditActividadesActivity extends AppCompatActivity {

    private EditText editTextID, editTextNombreActividad, editTextLugar, editTextFechaInicio, editTextFechaFin, editTextHoraInicio, editTextHoraFin, editTextDescripcion, editTextCupoMaximo;
    private Button btnActualizar, btnRegresar;
    private ActividadesDAO actividadesDAO;
    private Actividades actividad; // Objeto de actividad a editar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_actividades);

        // Inicialización de vistas
        editTextID = findViewById(R.id.txtID); // Asegúrate de que este ID esté en tu XML
        editTextNombreActividad = findViewById(R.id.txtNombreActividad);
        editTextLugar = findViewById(R.id.txtLugar);
        editTextFechaInicio = findViewById(R.id.txtFechaInicio);
        editTextFechaFin = findViewById(R.id.txtFechaFin);
        editTextHoraInicio = findViewById(R.id.txtHoraInicio);
        editTextHoraFin = findViewById(R.id.txtHoraFin);
        editTextDescripcion = findViewById(R.id.txtDescripcion);
        editTextCupoMaximo = findViewById(R.id.txtCupoMaximo);
        btnActualizar = findViewById(R.id.btnAgregarActividad);
        btnRegresar = findViewById(R.id.btnRegresar);

        // Inicializar ActividadesDAO
        actividadesDAO = new ActividadesDAO(this);

        // Obtener la intención y los datos de la actividad seleccionada
        Intent intent = getIntent();
        actividad = (Actividades) intent.getSerializableExtra("actividad");

        // Cargar los datos en los EditText
        if (actividad != null) {
            // Mostrar ID de la actividad
            editTextID.setText(String.valueOf(actividad.getId())); // Mostrar ID
            editTextNombreActividad.setText(actividad.getNombreActividad());
            editTextLugar.setText(actividad.getLugar());
            editTextFechaInicio.setText(actividad.getFechaInicio());
            editTextFechaFin.setText(actividad.getFechaFin());
            editTextHoraInicio.setText(actividad.getHoraInicio());
            editTextHoraFin.setText(actividad.getHoraFin());
            editTextDescripcion.setText(actividad.getDescripcion());
            editTextCupoMaximo.setText(String.valueOf(actividad.getCupoMaximo()));
        }

        // Configurar el botón de actualización
        btnActualizar.setOnClickListener(v -> actualizarActividad());

        // Configurar el botón de regresar
        btnRegresar.setOnClickListener(v -> finish());
    }

    private void actualizarActividad() {
        String nombreActividad = editTextNombreActividad.getText().toString().trim();
        String lugar = editTextLugar.getText().toString().trim();
        String fechaInicio = editTextFechaInicio.getText().toString().trim();
        String fechaFin = editTextFechaFin.getText().toString().trim();
        String horaInicio = editTextHoraInicio.getText().toString().trim();
        String horaFin = editTextHoraFin.getText().toString().trim();
        String descripcion = editTextDescripcion.getText().toString().trim();
        String cupoMaximoStr = editTextCupoMaximo.getText().toString().trim();

        // Validar que los campos no estén vacíos
        if (nombreActividad.isEmpty() || lugar.isEmpty() || fechaInicio.isEmpty() || fechaFin.isEmpty() || horaInicio.isEmpty() || horaFin.isEmpty() || descripcion.isEmpty() || cupoMaximoStr.isEmpty()) {
            Toast.makeText(this , "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        int cupoMaximo = Integer.parseInt(cupoMaximoStr);

        // Actualizar la actividad en la base de datos
        boolean exito = actividadesDAO.actualizarActividad(actividad.getId(), nombreActividad, lugar, fechaInicio, fechaFin, horaInicio, horaFin, descripcion, cupoMaximo);

        if (exito) {
            Toast.makeText(this, "Actividad actualizada correctamente.", Toast.LENGTH_SHORT).show();
            finish(); // Cerrar la actividad y regresar
        } else {
            Toast.makeText(this, "Error al actualizar la actividad.", Toast.LENGTH_SHORT).show();
        }
    }
}