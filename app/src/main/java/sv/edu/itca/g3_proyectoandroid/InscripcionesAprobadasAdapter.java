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

public class InscripcionesAprobadasAdapter extends RecyclerView.Adapter<InscripcionesAprobadasAdapter.InscripcionesViewHolder> {

    private List<Inscripcion> inscripcionesList;
    private List<Inscripcion> inscripcionesListFull; // Para filtrar
    private Context context;

    // Constructor
    public InscripcionesAprobadasAdapter(Context context, List<Inscripcion> inscripciones) {
        this.context = context;
        this.inscripcionesList = inscripciones != null ? inscripciones : new ArrayList<>(); // Asegúrate de que no sea nula
        this.inscripcionesListFull = new ArrayList<>(this.inscripcionesList); // Copia para filtrar
    }

    @NonNull
    @Override
    public InscripcionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inscripciones_aprobadas, parent, false); // Asegúrate de que este layout exista
        return new InscripcionesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InscripcionesViewHolder holder, int position) {
        Inscripcion inscripcion = inscripcionesList.get(position);
        holder.textViewNombreCompleto.setText("Nombre: " + inscripcion.getNombreCompleto());
        holder.textViewNombreActividad.setText("Actividad: " + inscripcion.getNombreActividad());
        holder.textViewFechaInscripcion.setText("Fecha: " + inscripcion.getFechaInscripcion());
    }

    @Override
    public int getItemCount() {
        return inscripcionesList.size();
    }

    // Método para filtrar inscripciones
    public void filtrar(String query) {
        if (query.isEmpty()) {
            inscripcionesList = new ArrayList<>(inscripcionesListFull); // Restablece la lista completa
        } else {
            List<Inscripcion> filteredList = new ArrayList<>();
            for (Inscripcion inscripcion : inscripcionesListFull) {
                if (inscripcion.getNombreCompleto().toLowerCase().contains(query.toLowerCase()) ||
                        inscripcion.getNombreActividad().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(inscripcion);
                }
            }
            inscripcionesList = filteredList; // Actualiza la lista con los resultados filtrados
        }
        notifyDataSetChanged(); // Notifica al adaptador para actualizar la vista
    }

    // ViewHolder para el RecyclerView
    public static class InscripcionesViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombreCompleto;
        TextView textViewNombreActividad;
        TextView textViewFechaInscripcion;

        public InscripcionesViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombreCompleto = itemView.findViewById(R.id.text_view_nombreCompleto);
            textViewNombreActividad = itemView.findViewById(R.id.text_view_nombreActividad);
            textViewFechaInscripcion = itemView.findViewById(R.id.text_view_fechaInscripcion);
        }
    }
}
