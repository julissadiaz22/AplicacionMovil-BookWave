package sv.edu.itca.g3_proyectoandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistorialInscripcionesAdapter extends RecyclerView.Adapter<HistorialInscripcionesAdapter.HistorialInscripcionesViewHolder> {

    private List<InscripcionCompleta> inscripcionesList; // Lista de inscripciones completas
    private List<InscripcionCompleta> inscripcionesListFull; // Para filtrar
    private Context context;

    public HistorialInscripcionesAdapter(Context context, List<InscripcionCompleta> inscripciones) {
        this.context = context;
        this.inscripcionesList = inscripciones;
        this.inscripcionesListFull = new ArrayList<>(inscripciones); // Copia para filtrar
    }

    @NonNull
    @Override
    public HistorialInscripcionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflamos el layout de item_historial_inscripciones
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_historial_inscripciones, parent, false);
        return new HistorialInscripcionesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialInscripcionesViewHolder holder, int position) {
        InscripcionCompleta inscripcion = inscripcionesList.get(position);

        // Asignamos los datos al ViewHolder
        holder.textViewNombreActividad.setText("Actividad: " + inscripcion.getNombreActividad());
        holder.textViewLugar.setText("Lugar: " + inscripcion.getLugar());
        holder.textViewFechaInicio.setText("Fecha de Inicio: " + inscripcion.getFechaInicio());
    }

    @Override
    public int getItemCount() {
        return inscripcionesList.size();
    }

    // Método para filtrar las inscripciones
    public void filtrar(String query) {
        if (query.isEmpty()) {
            inscripcionesList = new ArrayList<>(inscripcionesListFull); // Restablece la lista completa
        } else {
            List<InscripcionCompleta> filteredList = new ArrayList<>();
            for (InscripcionCompleta inscripcion : inscripcionesListFull) {
                // Lógica de filtrado por nombre de actividad
                if (inscripcion.getNombreActividad().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(inscripcion);
                }
            }
            inscripcionesList = filteredList; // Actualiza la lista con los resultados filtrados
        }
        notifyDataSetChanged(); // Notifica al adaptador para actualizar la vista
    }

    public static class HistorialInscripcionesViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombreActividad;
        TextView textViewLugar;
        TextView textViewFechaInicio;

        public HistorialInscripcionesViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombreActividad = itemView.findViewById(R.id.text_view_nombreActividad);
            textViewLugar = itemView.findViewById(R.id.text_view_lugar);
            textViewFechaInicio = itemView.findViewById(R.id.text_view_fechaInicio);
        }
    }
}
