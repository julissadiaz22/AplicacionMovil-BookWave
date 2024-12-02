package sv.edu.itca.g3_proyectoandroid;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SolicitudesFragment extends Fragment {

    public SolicitudesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_solicitudes, container, false);

        // Encuentra el botón "VER SOLICITUDES APROBADAS"
        Button buttonVerSolicitudesAprobadas = view.findViewById(R.id.id_button_ver_solicitudes_aprobadas);
        buttonVerSolicitudesAprobadas.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SolicitudesAprobadasAdminActivity.class);
            startActivity(intent); // Inicia la actividad
        });

        // Encuentra el botón "VER ACTIVIDADES APROBADAS"
        Button buttonVerActividadesAprobadas = view.findViewById(R.id.id_button_ver_actividades_aprobadas);
        buttonVerActividadesAprobadas.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), InscripcionesAprobadasActivity.class);
            startActivity(intent); // Inicia la actividad
        });

        return view;
    }
}