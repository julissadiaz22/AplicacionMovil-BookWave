package sv.edu.itca.g3_proyectoandroid;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class FragmentAgregarActividades extends Fragment {

    private EditText txtNombreActividad, txtLugar, txtFecha, txtFechaFin, txtHora, txtHoraFin, txtDescripcion, txtCupoMaximo;
    private Spinner spinnerEstado;
    private Button btnAgregarActividad, btnLimpiar;

    private ControladorBD controladorBD;

    public FragmentAgregarActividades() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        controladorBD = new ControladorBD(context, "bibliotecasa.db", null, 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar_actividades, container, false);

        // Inicializa las vistas
        txtNombreActividad = view.findViewById(R.id.txtNombreActividad);
        txtLugar = view.findViewById(R.id.txtLugar);
        txtFecha = view.findViewById(R.id.txtFechaInicio);
        txtFechaFin = view.findViewById(R.id.txtFechaFin); // Nuevo campo para la fecha final
        txtHora = view.findViewById(R.id.txtHoraInicio);
        txtHoraFin = view.findViewById(R.id.txtHoraFin); // Nuevo campo para la hora de fin
        txtDescripcion = view.findViewById(R.id.txtDescripcion);
        txtCupoMaximo = view.findViewById(R.id.txtCupoMaximo);
        spinnerEstado = view.findViewById(R.id.spinnerEstado);
        btnAgregarActividad = view.findViewById(R.id.btnAgregarActividad);
        btnLimpiar = view.findViewById(R.id.btnRegresar);

        // Configurar el Spinner para el estado
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.estado_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapter);

        // Configurar el DatePickerDialog para los campos de fecha
        txtFecha.setOnClickListener(v -> showDatePickerDialog(txtFecha));
        txtFechaFin.setOnClickListener(v -> showDatePickerDialog(txtFechaFin)); // Para fecha de fin

        // Configurar el TimePickerDialog para los campos de hora
        txtHora.setOnClickListener(v -> showTimePickerDialog(txtHora));
        txtHoraFin.setOnClickListener(v -> showTimePickerDialog(txtHoraFin)); // Para hora de fin

        // Configurar el botón para agregar actividad
        btnAgregarActividad.setOnClickListener(v -> agregarActividad());

        // Configurar el botón para limpiar campos
        btnLimpiar.setOnClickListener(v -> limpiarCampos());

        return view;
    }

    private void showDatePickerDialog(EditText editText) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view1, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    editText.setText(date);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog(EditText editText) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (view12, selectedHour, selectedMinute) -> {
                    String amPm;
                    if (selectedHour >= 12) {
                        amPm = "PM";
                        if (selectedHour > 12) selectedHour -= 12;
                    } else {
                        amPm = "AM";
                        if (selectedHour == 0) selectedHour = 12;
                    }

                    String time = String.format("%02d:%02d %s", selectedHour, selectedMinute, amPm);
                    editText.setText(time);
                }, hour, minute, false);
        timePickerDialog.show();
    }

    private void agregarActividad() {
        String nombreActividad = txtNombreActividad.getText().toString();
        String lugar = txtLugar.getText().toString();
        String fecha = txtFecha.getText().toString();
        String fechaFin = txtFechaFin.getText().toString();
        String hora = txtHora.getText().toString();
        String horaFin = txtHoraFin.getText().toString();
        String descripcion = txtDescripcion.getText().toString();
        String cupoMaximoStr = txtCupoMaximo.getText().toString();

        // Validar campos
        if (nombreActividad.isEmpty() || lugar.isEmpty() || fecha.isEmpty() || fechaFin.isEmpty() || hora.isEmpty() || horaFin.isEmpty() || descripcion.isEmpty() || cupoMaximoStr.isEmpty()) {
            Toast.makeText(getActivity(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar que la fecha de fin no sea anterior a la fecha de inicio
        if (!validarFechas(fecha, fechaFin)) {
            Toast.makeText(getActivity(), "La fecha de fin no puede ser anterior a la fecha de inicio", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar que la hora de fin no sea anterior a la hora de inicio
        if (!validarHoras(hora, horaFin)) {
            Toast.makeText(getActivity(), "La hora de fin no puede ser anterior a la hora de inicio", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar que el cupo máximo sea un número positivo
        try {
            int cupoMaximo = Integer.parseInt(cupoMaximoStr);
            if (cupoMaximo <= 0) {
                Toast.makeText(getActivity(), "Cupo máximo debe ser un número positivo", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Cupo máximo debe ser un número", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el estado como un int, basado en la posición seleccionada
        int estado = spinnerEstado.getSelectedItemPosition(); // Esto devuelve el índice seleccionado

        // Guardar en la base de datos
        controladorBD.agregarActividad(nombreActividad, lugar, fecha, fechaFin, hora, horaFin, descripcion, Integer.parseInt(cupoMaximoStr), estado);
        Toast.makeText(getActivity(), "Actividad agregada con éxito", Toast.LENGTH_SHORT).show();
        limpiarCampos();
    }

    private boolean validarFechas(String fechaInicio, String fechaFin) {
        // Puedes mejorar esta función utilizando SimpleDateFormat para comparar las fechas
        return fechaFin.compareTo(fechaInicio) >= 0; // Devuelve true si la fechaFin es igual o posterior a fechaInicio
    }

    private boolean validarHoras(String horaInicio, String horaFin) {
        // Similar a la validación de fechas, compara las horas
        return horaFin.compareTo(horaInicio) >= 0;
    }

    private void limpiarCampos() {
        txtNombreActividad.setText("");
        txtLugar.setText("");
        txtFecha.setText("");
        txtFechaFin.setText("");
        txtHora.setText("");
        txtHoraFin.setText("");
        txtDescripcion.setText("");
        txtCupoMaximo.setText("");
        spinnerEstado.setSelection(0);
    }
}
