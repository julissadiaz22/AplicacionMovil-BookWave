package sv.edu.itca.g3_proyectoandroid;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class PrestamosFragment extends Fragment {

    public PrestamosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prestamos, container, false);

        // Encuentra el botón "VER SOLICITUDES" para reservas
        Button buttonVerSolicitudes = view.findViewById(R.id.id_button_ver_solicitudes); // Asegúrate de que este ID es correcto

        // Configura el OnClickListener para el botón de solicitudes de reservas
        buttonVerSolicitudes.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SolicitudesReservasAdminActivity.class);
            startActivity(intent); // Inicia la actividad
        });

        // Encuentra el botón "VER SOLICITUDES" para inscripciones
        Button buttonVerInscripciones = view.findViewById(R.id.id_button_ver_incripciones); // Asegúrate de que este ID es correcto

        // Configura el OnClickListener para el botón de solicitudes de inscripciones
        buttonVerInscripciones.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SolicitudesInscripcionesAdminActivity.class);
            startActivity(intent); // Inicia la actividad
        });

        return view;
    }
}