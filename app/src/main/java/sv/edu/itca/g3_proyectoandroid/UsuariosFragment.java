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

public class UsuariosFragment extends Fragment {

    private static final int EDIT_USER_REQUEST_CODE = 1; // Código de solicitud para editar usuarios
    private ControladorBD controladorBD;
    private RecyclerView recyclerView;
    private UsuariosAdapter usuariosAdapter;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_usuarios, container, false);

        // Inicializa las vistas
        searchView = view.findViewById(R.id.search_view_usuarios);
        recyclerView = view.findViewById(R.id.recycler_view_usuarios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializa el controlador de la base de datos
        controladorBD = new ControladorBD(getContext(), "bibliotecasa.db", null, 1);

        // Cargar usuarios
        cargarUsuarios();

        // Configura el SearchView para filtrar usuarios
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // No se necesita realizar nada al enviar el texto
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filtra los usuarios al escribir en el SearchView
                if (usuariosAdapter != null) {
                    usuariosAdapter.filtrar(newText); // Llama al método de filtrado en el adaptador
                }
                return true;
            }
        });

        return view;
    }

    private void cargarUsuarios() {
        List<Usuario> usuarios = controladorBD.obtenerTodosLosUsuarios();
        Log.d("UsuariosFragment", "Número de usuarios: " + usuarios.size()); // Log para verificar la cantidad de usuarios
        // Asegúrate de pasar el contexto correctamente al adaptador
        usuariosAdapter = new UsuariosAdapter(getContext(), usuarios);
        recyclerView.setAdapter(usuariosAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_USER_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            cargarUsuarios(); // Recargar usuarios si se ha editado o eliminado uno
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarUsuarios(); // Recargar usuarios cada vez que el fragmento se muestra
    }
}