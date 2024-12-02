package sv.edu.itca.g3_proyectoandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentHistorialUsuario extends Fragment {

    private int userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener el ID del usuario desde SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("sesion_usuario", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("usuario_id", -1);

        Log.d("FragmentHistorialUsuario", "ID de usuario recibido en el fragmento: " + userId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historial_usuario, container, false);

        // Configuración del primer botón (Historial de Préstamos)
        Button buttonVerHistorial = view.findViewById(R.id.id_button_ver_historial);
        buttonVerHistorial.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HistorialUsuarioActivity.class);
            intent.putExtra("usuario_id", userId);
            startActivity(intent);
        });

        // Configuración del segundo botón (Historial de Inscripciones)
        Button buttonVerHistorialInscripciones = view.findViewById(R.id.id_button_ver_historialInscripciones);
        buttonVerHistorialInscripciones.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HistorialInscripcionesUsuarioActivity.class);
            intent.putExtra("usuario_id", userId);
            startActivity(intent);
        });

        return view;
    }
}
