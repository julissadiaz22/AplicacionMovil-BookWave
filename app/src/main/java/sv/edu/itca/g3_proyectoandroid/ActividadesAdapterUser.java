package sv.edu.itca.g3_proyectoandroid;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ActividadesAdapterUser extends RecyclerView.Adapter<ActividadesAdapterUser.ActividadesViewHolder> {

    private List<Actividades> actividadesList;
    private List<Actividades> actividadesListFull; // Para filtrar
    private Context context;

    // Constructor modificado para recibir el contexto
    public ActividadesAdapterUser(Context context, List<Actividades> actividades) {
        this.context = context; // Inicializa el contexto
        this.actividadesList = actividades;
        this.actividadesListFull = new ArrayList<>(actividades); // Mantiene una copia para filtrar
    }

    @NonNull
    @Override
    public ActividadesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_actividades, parent, false);
        return new ActividadesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActividadesViewHolder holder, int position) {
        Actividades actividad = actividadesList.get(position);
        holder.textViewNombreActividad.setText(actividad.getNombreActividad());
        holder.textViewLugar.setText("Lugar: " + actividad.getLugar());
        holder.textViewFecha.setText("Fecha Inicio: " + actividad.getFechaInicio());

        // Configura el OnClickListener para el botón "VER MÁS"
        holder.buttonViewMore.setOnClickListener(v -> {
            try {
                // Inicia la actividad de detalle
                Intent intent = new Intent(context, DetailActividades_user.class);
                intent.putExtra("actividad", actividad); // Envío del objeto Actividades
                context.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(context, "Error al navegar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return actividadesList.size();
    }

    // Método de filtrado para filtrar la lista según la consulta de búsqueda
    public void filtrar(String query) {
        if (query.isEmpty()) {
            actividadesList = new ArrayList<>(actividadesListFull); // Restablece la lista completa
        } else {
            List<Actividades> filteredList = new ArrayList<>();
            for (Actividades actividad : actividadesListFull) {
                // Verifica si el nombre de la actividad contiene la consulta de búsqueda
                if (actividad.getNombreActividad().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(actividad);
                }
            }
            actividadesList = filteredList; // Actualiza la lista con los resultados filtrados
        }
        notifyDataSetChanged(); // Notifica al adaptador para actualizar la vista
    }

    // Clase ViewHolder para contener las vistas de cada elemento
    public static class ActividadesViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombreActividad;
        TextView textViewLugar;
        TextView textViewFecha;
        Button buttonViewMore;

        public ActividadesViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombreActividad = itemView.findViewById(R.id.text_view_nombreActividad);
            textViewLugar = itemView.findViewById(R.id.text_view_Lugar);
            textViewFecha = itemView.findViewById(R.id.text_view_Fecha);
            buttonViewMore = itemView.findViewById(R.id.button_view_moreAc); // Asegúrate de que este ID es correcto
        }
    }
}