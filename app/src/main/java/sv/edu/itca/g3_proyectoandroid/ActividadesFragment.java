package sv.edu.itca.g3_proyectoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.util.List;

public class ActividadesFragment extends Fragment {

    private static final int EDIT_ACTIVITY_REQUEST_CODE = 1; // Código de solicitud para editar actividades
    private ControladorBD controladorBD;
    private RecyclerView recyclerView;
    private ActividadesAdapter actividadesAdapter;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actividades, container, false);

        // Inicializa las vistas
        searchView = view.findViewById(R.id.search_view_actividades);
        recyclerView = view.findViewById(R.id.recycler_view_actividades);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializa el controlador de la base de datos
        controladorBD = new ControladorBD(getContext(), "bibliotecasa.db", null, 1);

        // Cargar actividades
        cargarActividades();

        // Configura el SearchView para filtrar actividades
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // No se necesita realizar nada al enviar el texto
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filtra las actividades al escribir en el SearchView
                if (actividadesAdapter != null) {
                    actividadesAdapter.filtrar(newText); // Llama al método de filtrado en el adaptador
                }
                return true;
            }
        });

        return view;
    }

    private void cargarActividades() {
        List<Actividades> actividades = controladorBD.obtenerTodasLasActividades();
        Log.d("ActividadesFragment", "Número de actividades: " + actividades.size()); // Log para verificar la cantidad de actividades
        // Asegúrate de pasar el contexto correctamente al adaptador
        actividadesAdapter = new ActividadesAdapter(getContext(), actividades);
        recyclerView.setAdapter(actividadesAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_ACTIVITY_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            cargarActividades(); // Recargar actividades si se ha editado o eliminado una
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarActividades(); // Recargar actividades cada vez que el fragmento se muestra
    }
}