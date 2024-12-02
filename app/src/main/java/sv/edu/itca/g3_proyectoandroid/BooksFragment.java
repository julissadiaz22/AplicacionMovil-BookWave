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

public class BooksFragment extends Fragment {

    private static final int EDIT_BOOK_REQUEST_CODE = 1; // Código de solicitud para editar libros
    private ControladorBD controladorBD;
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private SearchView searchView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books, container, false);

        // Inicializa las vistas
        searchView = view.findViewById(R.id.search_view_books);
        recyclerView = view.findViewById(R.id.recycler_view_books);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializa el controlador de la base de datos
        controladorBD = new ControladorBD(getContext(), "bibliotecasa.db", null, 1);

        // Cargar libros
        cargarLibros();

        // Configura el SearchView para filtrar libros
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // No se necesita realizar nada al enviar el texto
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filtra los libros al escribir en el SearchView
                if (bookAdapter != null) {
                    bookAdapter.filtrar(newText); // Llama al método de filtrado en el adaptador
                }
                return true;
            }
        });

        return view;
    }

    private void cargarLibros() {
        List<Libro> libros = controladorBD.obtenerTodosLosLibros();
        Log.d("BooksFragment", "Número de libros: " + libros.size()); // Log para verificar la cantidad de libros
        // Asegúrate de pasar el contexto correctamente al adaptador
        bookAdapter = new BookAdapter(getContext(), libros); // Modificado para pasar el contexto
        recyclerView.setAdapter(bookAdapter);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_BOOK_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            cargarLibros(); // Recargar libros si se ha editado o eliminado uno
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarLibros(); // Recargar libros cada vez que el fragmento se muestra
    }
}