package sv.edu.itca.g3_proyectoandroid;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SolicitudesReservasAdminActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSolicitudes;
    private SolicitudesAdapter solicitudesAdapter;
    private ControladorBD controladorBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Habilitar el modo Edge to Edge
        setContentView(R.layout.activity_solicitudes_reservas_admin); // Establecer el layout de la actividad

        // Inicializar el RecyclerView
        recyclerViewSolicitudes = findViewById(R.id.recycler_view_solicitudes);
        recyclerViewSolicitudes.setLayoutManager(new LinearLayoutManager(this)); // Usar un LinearLayoutManager

        // Inicializar el controlador de la base de datos
        controladorBD = new ControladorBD(this, "bibliotecasa.db", null, 1);
        List<ReservaCompleta> reservas = controladorBD.obtenerReservasConEstadoCero(); // Obtener reservas con estado cero

        // Inicializar el adaptador y establecerlo en el RecyclerView
        solicitudesAdapter = new SolicitudesAdapter(this, reservas);
        recyclerViewSolicitudes.setAdapter(solicitudesAdapter);

        // Inicializar el SearchView para filtrar las solicitudes
        SearchView searchView = findViewById(R.id.search_view_solicitudes);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // No se realiza ninguna acción al enviar el texto
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                solicitudesAdapter.filtrar(newText); // Filtrar la lista según el texto ingresado
                return true;
            }
        });
    }
}