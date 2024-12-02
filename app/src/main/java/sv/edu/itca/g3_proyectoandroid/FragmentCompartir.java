package sv.edu.itca.g3_proyectoandroid;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCompartir#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCompartir extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FragmentCompartir() {
        // Required empty public constructor
    }

    public static FragmentCompartir newInstance(String param1, String param2) {
        FragmentCompartir fragment = new FragmentCompartir();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_compartir, container, false);

        // Configurar botones
        Button btnCompartir = view.findViewById(R.id.btnCompartir);
        Button btnCopiarEnlace = view.findViewById(R.id.btnCopiarEnlace);

        // Configurar acción para el botón de compartir
        btnCompartir.setOnClickListener(v -> compartirAplicacion());

        // Configurar acción para el botón de copiar enlace
        btnCopiarEnlace.setOnClickListener(v -> copiarEnlace());

        return view;
    }

    private void compartirAplicacion() {
        String link = "https://tuapp.com"; // Cambia esto al enlace de tu aplicación
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "¡Descubre esta increíble aplicación! " + link);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Compartir a través de"));
    }

    private void copiarEnlace() {
        String link = "https://tuapp.com"; // Cambia esto al enlace de tu aplicación
        ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Enlace de la Aplicación", link);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), "Enlace copiado al portapapeles", Toast.LENGTH_SHORT).show();
    }
}
