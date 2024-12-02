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

public class SolicitudesAdapter extends RecyclerView.Adapter<SolicitudesAdapter.SolicitudesViewHolder> {

    private List<ReservaCompleta> reservasList;
    private List<ReservaCompleta> reservasListFull; // Para filtrar
    private Context context;

    public SolicitudesAdapter(Context context, List<ReservaCompleta> reservas) {
        this.context = context;
        this.reservasList = reservas;
        this.reservasListFull = new ArrayList<>(reservas); // Copia para filtrar
    }

    @NonNull
    @Override
    public SolicitudesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reservas, parent, false);
        return new SolicitudesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitudesViewHolder holder, int position) {
        ReservaCompleta reserva = reservasList.get(position);

        holder.textViewNombreCompleto.setText("Nombre Solicitante: " + reserva.getNombreCompleto());
        holder.textViewTituloLibro.setText("Titulo Libro: " + reserva.getTituloLibro());
        holder.textViewFechaReserva.setText("Fecha: " + reserva.getFecha());
        holder.textViewHoraReserva.setText("Hora: " + reserva.getHora());

        // Configura el OnClickListener para el botón "Aprobar"
        holder.buttonAprobar.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Confirmar Aprobación")
                    .setMessage("¿Está seguro de que desea aprobar la reserva para " + reserva.getNombreCompleto() + "?")
                    .setPositiveButton("Aceptar", (dialog, which) -> {
                        ControladorBD controladorBD = new ControladorBD(context, "bibliotecasa.db", null, 1);
                        controladorBD.aprobarReserva(reserva.getId());
                        Toast.makeText(context, "Reserva aprobada para " + reserva.getNombreCompleto(), Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        // Configura el OnClickListener para el botón "Eliminar"
        holder.buttonEliminar.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Confirmar Eliminación")
                    .setMessage("¿Está seguro de que desea rechazar esta solicitud para " + reserva.getNombreCompleto() + "?")
                    .setPositiveButton("Aceptar", (dialog, which) -> {
                        ControladorBD controladorBD = new ControladorBD(context, "bibliotecasa.db", null, 1);
                        if (controladorBD.eliminarReserva(reserva.getId())) {
                            Toast.makeText(context, "Reserva rechazada para " + reserva.getNombreCompleto(), Toast.LENGTH_SHORT).show();
                            // Elimina la reserva de la lista y notifica al adaptador
                            reservasList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, reservasList.size());
                        } else {
                            Toast.makeText(context, "Error al eliminar la reserva", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return reservasList.size();
    }

    public void filtrar(String query) {
        if (query.isEmpty()) {
            reservasList = new ArrayList<>(reservasListFull); // Restablece la lista completa
        } else {
            List<ReservaCompleta> filteredList = new ArrayList<>();
            for (ReservaCompleta reserva : reservasListFull) {
                if (reserva.getNombreCompleto().toLowerCase().contains(query.toLowerCase()) ||
                        reserva.getTituloLibro().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(reserva);
                }
            }
            reservasList = filteredList; // Actualiza la lista con los resultados filtrados
        }
        notifyDataSetChanged(); // Notifica al adaptador para actualizar la vista
    }

    public static class SolicitudesViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombreCompleto;
        TextView textViewTituloLibro;
        TextView textViewFechaReserva;
        TextView textViewHoraReserva;
        Button buttonAprobar;
        Button buttonEliminar; // Añadido el botón de eliminar

        public SolicitudesViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombreCompleto = itemView.findViewById(R.id.text_view_nombreCompleto);
            textViewTituloLibro = itemView.findViewById(R.id.text_view_tituloLibro);
            textViewFechaReserva = itemView.findViewById(R.id.text_view_fechaReserva);
            textViewHoraReserva = itemView.findViewById(R.id.text_view_horaReserva);
            buttonAprobar = itemView.findViewById(R.id.button_aprobar);
            buttonEliminar = itemView.findViewById(R.id.button_eliminar); // Inicializa el botón de eliminar
        }
    }
}