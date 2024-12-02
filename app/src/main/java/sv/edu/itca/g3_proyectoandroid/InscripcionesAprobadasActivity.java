package sv.edu.itca.g3_proyectoandroid;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InscripcionesAprobadasActivity extends AppCompatActivity {

    private RecyclerView recyclerViewInscripciones;
    private InscripcionesAprobadasAdapter inscripcionesAdapter;
    private ControladorBD controladorBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscripciones_aprobadas);

        // Inicializar el RecyclerView
        recyclerViewInscripciones = findViewById(R.id.recycler_view_solicitudes);
        recyclerViewInscripciones.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar el controlador de la base de datos
        controladorBD = new ControladorBD(this, "bibliotecasa.db", null, 1);
        List<Inscripcion> inscripciones = controladorBD.obtenerInscripcionesConEstadoUno(); // Obtener inscripciones aprobadas
        Log.d("InscripcionesAprobadasActivity", "Número de inscripciones obtenidas: " + (inscripciones != null ? inscripciones.size() : 0));

        // Inicializar el adaptador y establecerlo en el RecyclerView
        inscripcionesAdapter = new InscripcionesAprobadasAdapter(this, inscripciones);
        recyclerViewInscripciones.setAdapter(inscripcionesAdapter);

        // Inicializar el SearchView para filtrar las inscripciones
        SearchView searchView = findViewById(R.id.search_view_aprobadas);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // No se realiza ninguna acción al enviar el texto
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                inscripcionesAdapter.filtrar(newText); // Filtrar la lista según el texto ingresado
                return true;
            }
        });
    }
}
