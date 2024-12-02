package sv.edu.itca.g3_proyectoandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ReservasAdapter extends RecyclerView.Adapter<ReservasAdapter.ReservasViewHolder> {

    private List<Inscripcion> inscripcionesList;
    private List<Inscripcion> inscripcionesListFull;
    private Context context;

    public ReservasAdapter(Context context, List<Inscripcion> inscripciones) {
        this.context = context;
        this.inscripcionesList = inscripciones;
        this.inscripcionesListFull = new ArrayList<>(inscripciones);
    }

    @NonNull
    @Override
    public ReservasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inscripciones, parent, false);
        return new ReservasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservasViewHolder holder, int position) {
        Inscripcion inscripcion = inscripcionesList.get(position);

        holder.textViewNombreCompleto.setText("Nombre Solicitante: " + inscripcion.getNombreCompleto());
        holder.textViewNombreActividad.setText("Nombre Actividad: " + inscripcion.getNombreActividad());
        holder.textViewFechaInscripcion.setText("Fecha Inscripción: " + inscripcion.getFechaInscripcion());

        holder.buttonAprobar.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Confirmar Aprobación")
                    .setMessage("¿Está seguro de que desea aprobar la inscripción de " + inscripcion.getNombreCompleto() + "?")
                    .setPositiveButton("Aceptar", (dialog, which) -> {
                        ControladorBD controladorBD = new ControladorBD(context, "bibliotecasa.db", null, 1);
                        controladorBD.aprobarInscripcion(inscripcion.getId());
                        Toast.makeText(context, "Inscripción aprobada para " + inscripcion.getNombreCompleto(), Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        holder.buttonRechazar.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Confirmar Eliminación")
                    .setMessage("¿Está seguro de que desea rechazar esta inscripción de " + inscripcion.getNombreCompleto() + "?")
                    .setPositiveButton("Aceptar", (dialog, which) -> {
                        ControladorBD controladorBD = new ControladorBD(context, "bibliotecasa.db", null, 1);
                        if (controladorBD.eliminarInscripcion(inscripcion.getId())) {
                            Toast.makeText(context, "Inscripción rechazada para " + inscripcion.getNombreCompleto(), Toast.LENGTH_SHORT).show();
                            inscripcionesList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, inscripcionesList.size());
                        } else {
                            Toast.makeText(context, "Error al eliminar la inscripción", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return inscripcionesList.size();
    }

    public void filtrar(String query) {
        if (query.isEmpty()) {
            inscripcionesList = new ArrayList<>(inscripcionesListFull);
        } else {
            List<Inscripcion> filteredList = new ArrayList<>();
            for (Inscripcion inscripcion : inscripcionesListFull) {
                if (inscripcion.getNombreCompleto().toLowerCase().contains(query.toLowerCase()) ||
                        inscripcion.getNombreActividad().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(inscripcion);
                }
            }
            inscripcionesList = filteredList;
        }
        notifyDataSetChanged();
    }

    public static class ReservasViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombreCompleto;
        TextView textViewNombreActividad;
        TextView textViewFechaInscripcion;
        Button buttonAprobar;
        Button buttonRechazar;

        public ReservasViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombreCompleto = itemView.findViewById(R.id.text_view_nombreCompleto);
            textViewNombreActividad = itemView.findViewById(R.id.text_view_nombreActividad);
            textViewFechaInscripcion = itemView.findViewById(R.id.text_view_fechaInscripcion);
            buttonAprobar = itemView.findViewById(R.id.button_aprobar);
            buttonRechazar = itemView.findViewById(R.id.button_rechazar);
        }
    }
}