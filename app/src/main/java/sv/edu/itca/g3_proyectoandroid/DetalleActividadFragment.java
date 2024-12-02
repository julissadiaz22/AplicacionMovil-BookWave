package sv.edu.itca.g3_proyectoandroid;

import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DetalleActividadFragment extends Fragment {
    private TextView titleTextView, lugarTextView, fechaInicioTextView, horaInicioTextView,
            horaFinTextView, descripcionTextView;
    private Button editarButton, eliminarButton;

    public static DetalleActividadFragment newInstance(Actividades actividad) {
        DetalleActividadFragment fragment = new DetalleActividadFragment();
        Bundle args = new Bundle();
        args.putSerializable("actividad", actividad);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_actividad, container, false);

        titleTextView = view.findViewById(R.id.text_view_titulo);
        lugarTextView = view.findViewById(R.id.text_view_lugar);
        fechaInicioTextView = view.findViewById(R.id.text_view_fecha_inicio);
        horaInicioTextView = view.findViewById(R.id.text_view_hora_inicio);
        horaFinTextView = view.findViewById(R.id.text_view_hora_fin);
        descripcionTextView = view.findViewById(R.id.text_view_descripcion);
        editarButton = view.findViewById(R.id.button_editar);
        eliminarButton = view.findViewById(R.id.button_eliminar);

        if (getArguments() != null) {
            Actividades actividad = (Actividades) getArguments().getSerializable("actividad");
            if (actividad != null) {
                titleTextView.setText(actividad.getNombreActividad());
                lugarTextView.setText("Lugar: " + actividad.getLugar());
                fechaInicioTextView.setText("Fecha Inicio: " + actividad.getFechaInicio());
                horaInicioTextView.setText("Hora Inicio: " + actividad.getHoraInicio());
                horaFinTextView.setText("Hora Fin: " + actividad.getHoraFin());
                descripcionTextView.setText("Descripción: " + actividad.getDescripcion());
            } else {
                Log.e("DetalleActividadFragment", "Actividad es null");
                titleTextView.setText("Actividad no encontrada");
            }
        }

        // Configurar listeners para los botones
        editarButton.setOnClickListener(v -> {
            // Manejar acción de editar
        });

        eliminarButton.setOnClickListener(v -> {
            // Manejar acción de eliminar
        });

        return view;
    }
}
