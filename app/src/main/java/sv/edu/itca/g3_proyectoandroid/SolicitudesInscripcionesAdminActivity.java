package sv.edu.itca.g3_proyectoandroid;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SolicitudesInscripcionesAdminActivity extends AppCompatActivity {

    private RecyclerView recyclerViewInscripciones;
    private ReservasAdapter reservasAdapter;
    private ControladorBD controladorBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_solicitudes_inscripciones_admin);

        recyclerViewInscripciones = findViewById(R.id.recycler_view_incripciones);
        recyclerViewInscripciones.setLayoutManager(new LinearLayoutManager(this));

        controladorBD = new ControladorBD(this, "bibliotecasa.db", null, 1);
        List<Inscripcion> inscripciones = controladorBD.obtenerInscripcionesConEstadoCero();

        if (inscripciones != null && !inscripciones.isEmpty()) {
            Log.d("SolicitudesInscripciones", "Inscripciones obtenidas: " + inscripciones.size());
        } else {
            Log.e("SolicitudesInscripciones", "No se encontraron inscripciones.");
        }

        reservasAdapter = new ReservasAdapter(this, inscripciones);
        recyclerViewInscripciones.setAdapter(reservasAdapter);

        SearchView searchView = findViewById(R.id.search_view_incripciones);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                reservasAdapter.filtrar(newText);
                return true;
            }
        });
    }
}