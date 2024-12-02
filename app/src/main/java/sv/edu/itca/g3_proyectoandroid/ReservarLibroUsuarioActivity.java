package sv.edu.itca.g3_proyectoandroid;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReservarLibroUsuarioActivity extends AppCompatActivity {

    private EditText editTextTituloLibro;
    private TextView idlibroa;
    private EditText editTextFechaReserva;
    private EditText editTextHoraReserva;
    private Button buttonHacerReserva;
    private Button buttonLimpiar;
    private Button buttonRegresar;


    private String fechaReserva = "";
    private String horaReserva = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar_libro_usuario);

        editTextTituloLibro = findViewById(R.id.editTextTituloLibro);
        editTextFechaReserva = findViewById(R.id.editTextFechaReserva);
        editTextHoraReserva = findViewById(R.id.editTextHoraReserva);
        idlibroa = findViewById(R.id.idlibro);
        buttonHacerReserva = findViewById(R.id.buttonHacerReserva);
        buttonLimpiar = findViewById(R.id.buttonLimpiar);
        buttonRegresar = findViewById(R.id.buttonRegresar);


        // Obtener el título, ID del libro y ID del usuario desde el Intent
        String tituloLibro = getIntent().getStringExtra("tituloLibro");
        int idLibro = getIntent().getIntExtra("idLibro", -1);
        int idUsuario = getIntent().getIntExtra("idUsuario", -1);

        SharedPreferences sharedPreferences = getSharedPreferences("sesion_usuario", Context.MODE_PRIVATE);
        int userIda = sharedPreferences.getInt("usuario_id", -1);
        Log.d("USER_ID", "Usuario ID guardado: " + userIda);

        if (tituloLibro != null) {
            editTextTituloLibro.setText(tituloLibro);
        }

        // Configurar el botón "Seleccionar Fecha"
        editTextFechaReserva.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
                selectedMonth++; // Los meses empiezan desde 0
                fechaReserva = selectedDay + "/" + selectedMonth + "/" + selectedYear;
                editTextFechaReserva.setText(fechaReserva);
            }, year, month, day);
            datePickerDialog.show();
        });

        // Configurar el botón "Seleccionar Hora" (AM/PM)
        editTextHoraReserva.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, selectedHour, selectedMinute) -> {
                horaReserva = String.format("%02d:%02d %s", (selectedHour % 12 == 0) ? 12 : selectedHour % 12, selectedMinute, (selectedHour < 12) ? "AM" : "PM");
                editTextHoraReserva.setText(horaReserva);
            }, hour, minute, false); // `false` para formato de 12 horas
            timePickerDialog.show();
        });

        // Configurar el botón "Reservar"
        buttonHacerReserva.setOnClickListener(v -> {
            if (!fechaReserva.isEmpty() && !horaReserva.isEmpty()) {
                if (validarFecha(fechaReserva) && validarHora(horaReserva)) {
                    ControladorBD controladorBD = new ControladorBD(this, "bibliotecasa.db", null, 1);
                    controladorBD.agregarReserva(idLibro, userIda, fechaReserva, horaReserva);
                    Toast.makeText(this, "Reserva realizada correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Toast.makeText(this, "Por favor, selecciona fecha y hora", Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar el botón "Limpiar"
        buttonLimpiar.setOnClickListener(v -> {
            editTextTituloLibro.setText("");
            fechaReserva = "";
            horaReserva = "";
            editTextFechaReserva.setText("Fecha de Reserva (DD/MM/YYYY)");
            editTextHoraReserva.setText("Hora de Reserva (HH:MM AM/PM)");
        });

        // Configurar el botón "Regresar"
        buttonRegresar.setOnClickListener(v -> finish());
    }

    // Método para validar la fecha
    private boolean validarFecha(String fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date fechaReserva = sdf.parse(fecha);
            Date fechaActual = Calendar.getInstance().getTime();
            if (fechaReserva.before(fechaActual)) {
                Toast.makeText(this, "La fecha no puede ser pasada", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Formato de fecha inválido", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Método para validar la hora
    private boolean validarHora(String hora) {
        String[] partes = hora.split(":| ");
        if (partes.length != 3) {
            Toast.makeText(this, "Formato de hora inválido", Toast.LENGTH_SHORT).show();
            return false;
        }

        int horaInt = Integer.parseInt(partes[0]);
        String amPm = partes[2];

        if ((amPm.equals("AM") && (horaInt < 8 || horaInt >= 12)) ||
                (amPm.equals("PM") && (horaInt < 1 || horaInt >= 4))) {
            Toast.makeText(this, "La hora debe estar entre 8:00 AM - 12:00 PM o 1:00 PM - 4:00 PM", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
