package sv.edu.itca.g3_proyectoandroid;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SolicitudesAprobadasAdminActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSolicitudes;
    private AprobadasAdapter aprobadasAdapter;
    private ControladorBD controladorBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Habilitar el modo Edge to Edge
        setContentView(R.layout.activity_solicitudes_aprobadas_admin); // Establecer el layout de la actividad

        // Inicializar el RecyclerView
        recyclerViewSolicitudes = findViewById(R.id.recycler_view_solicitudes);
        recyclerViewSolicitudes.setLayoutManager(new LinearLayoutManager(this)); // Usar un LinearLayoutManager

        // Inicializar el controlador de la base de datos
        controladorBD = new ControladorBD(this, "bibliotecasa.db", null, 1);
        List<ReservaCompleta> reservas = controladorBD.obtenerReservasConEstadoUno(); // Obtener reservas con estado cero

        // Inicializar el adaptador y establecerlo en el RecyclerView
        aprobadasAdapter = new AprobadasAdapter(this, reservas);
        recyclerViewSolicitudes.setAdapter(aprobadasAdapter);

        // Inicializar el SearchView para filtrar las solicitudes
        SearchView searchView = findViewById(R.id.search_view_aprobadas);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // No se realiza ninguna acción al enviar el texto
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                aprobadasAdapter.filtrar(newText); // Filtrar la lista según el texto ingresado
                return true;
            }
        });
    }
}