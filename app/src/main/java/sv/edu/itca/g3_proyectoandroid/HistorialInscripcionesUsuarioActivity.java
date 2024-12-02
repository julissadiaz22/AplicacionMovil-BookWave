package sv.edu.itca.g3_proyectoandroid;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HistorialInscripcionesUsuarioActivity extends AppCompatActivity {

    private RecyclerView recyclerViewHistorialInscripciones;
    private HistorialInscripcionesAdapter historialInscripcionesAdapter;
    private ControladorBD controladorBD;
    private int usuarioLogueadoId;
    private List<InscripcionCompleta> inscripciones; // Lista original de inscripciones
    private List<InscripcionCompleta> filteredInscripciones; // Lista filtrada

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_inscripciones_usuario);

        recyclerViewHistorialInscripciones = findViewById(R.id.recycler_view_solicitudes);
        recyclerViewHistorialInscripciones.setLayoutManager(new LinearLayoutManager(this));

        controladorBD = new ControladorBD(this, "bibliotecasa.db", null, 1);

        usuarioLogueadoId = getIntent().getIntExtra("usuario_id", -1);
        Log.d("HistorialInscripcionesUsuarioActivity", "ID de usuario recibido: " + usuarioLogueadoId);

        if (usuarioLogueadoId != -1) {
            // Obtener las inscripciones con estado 1 para el usuario
            inscripciones = controladorBD.obtenerInscripcionesConEstadoUnoUsuario(usuarioLogueadoId);
            Log.d("HistorialInscripcionesUsuarioActivity", "Inscripciones obtenidas: " + inscripciones.size());

            if (inscripciones.isEmpty()) {
                Log.d("HistorialInscripcionesUsuarioActivity", "No se encontraron inscripciones para el usuario.");
            }

            filteredInscripciones = new ArrayList<>(inscripciones); // Copia de la lista original para filtrar
            historialInscripcionesAdapter = new HistorialInscripcionesAdapter(this, filteredInscripciones);
            recyclerViewHistorialInscripciones.setAdapter(historialInscripcionesAdapter);
        } else {
            Log.e("HistorialInscripcionesUsuarioActivity", "ID de usuario logueado no válido.");
        }

        // Configuración del SearchView para filtrar las inscripciones
        SearchView searchView = findViewById(R.id.search_view_aprobadas);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("HistorialInscripcionesUsuarioActivity", "Filtrando por: " + query);
                filterInscripciones(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("HistorialInscripcionesUsuarioActivity", "Filtrando por: " + newText);
                filterInscripciones(newText);
                return true;
            }
        });
    }

    // Método para filtrar las inscripciones
    private void filterInscripciones(String query) {
        Log.d("HistorialInscripcionesUsuarioActivity", "Filtrando inscripciones con el texto: " + query);
        filteredInscripciones.clear();
        if (query.isEmpty()) {
            filteredInscripciones.addAll(inscripciones);
        } else {
            for (InscripcionCompleta inscripcion : inscripciones) {
                // Lógica de filtrado por nombre de actividad
                if (inscripcion.getNombreActividad().toLowerCase().contains(query.toLowerCase())) {
                    filteredInscripciones.add(inscripcion);
                }
            }
        }

        Log.d("HistorialInscripcionesUsuarioActivity", "Inscripciones después del filtro: " + filteredInscripciones.size());
        historialInscripcionesAdapter.notifyDataSetChanged();
    }
}
