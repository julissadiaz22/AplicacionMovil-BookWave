package sv.edu.itca.g3_proyectoandroid;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistorialUsuarioActivity extends AppCompatActivity {

    private RecyclerView recyclerViewHistorial;
    private HistorialAdapter historialAdapter;
    private ControladorBD controladorBD;
    private int usuarioLogueadoId;
    private List<ReservaCompleta> reservas; // Original list of reservations
    private List<ReservaCompleta> filteredReservas; // Filtered list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_usuario);

        recyclerViewHistorial = findViewById(R.id.recycler_view_solicitudes);
        recyclerViewHistorial.setLayoutManager(new LinearLayoutManager(this));

        controladorBD = new ControladorBD(this, "bibliotecasa.db", null, 1);

        usuarioLogueadoId = getIntent().getIntExtra("usuario_id", -1);

        Log.d("HistorialUsuarioActivity", "ID de usuario recibido: " + usuarioLogueadoId);

        if (usuarioLogueadoId != -1) {
            reservas = controladorBD.obtenerReservasConEstadoUnoUsuario(usuarioLogueadoId);
            filteredReservas = new ArrayList<>(reservas); // Copy of original list for filtering
            historialAdapter = new HistorialAdapter(this, filteredReservas);
            recyclerViewHistorial.setAdapter(historialAdapter);
        } else {
            Log.e("HistorialUsuarioActivity", "ID de usuario logueado no válido.");
        }

        // Set up SearchView filtering
        SearchView searchView = findViewById(R.id.search_view_aprobadas);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterReservas(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterReservas(newText);
                return true;
            }
        });
    }

    private void filterReservas(String query) {
        filteredReservas.clear();
        if (query.isEmpty()) {
            filteredReservas.addAll(reservas);
        } else {
            for (ReservaCompleta reserva : reservas) {
                if (reserva.getTituloLibro().toLowerCase().contains(query.toLowerCase())) {
                    filteredReservas.add(reserva);
                }
            }
        }
        historialAdapter.notifyDataSetChanged();
    }
}
