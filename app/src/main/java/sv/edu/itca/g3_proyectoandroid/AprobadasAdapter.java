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

public class AprobadasAdapter extends RecyclerView.Adapter<AprobadasAdapter.AprobadasViewHolder> {

    private List<ReservaCompleta> reservasList;
    private List<ReservaCompleta> reservasListFull; // Para filtrar
    private Context context;

    public AprobadasAdapter(Context context, List<ReservaCompleta> reservas) {
        this.context = context;
        this.reservasList = reservas;
        this.reservasListFull = new ArrayList<>(reservas); // Copia para filtrar
    }

    @NonNull
    @Override
    public AprobadasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_aprobadas, parent, false); // Asegúrate de que este layout exista
        return new AprobadasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AprobadasViewHolder holder, int position) {
        ReservaCompleta reserva = reservasList.get(position);

        holder.textViewNombreCompleto.setText("Nombre: " + reserva.getNombreCompleto());
        holder.textViewTituloLibro.setText("Título Libro: " + reserva.getTituloLibro());
        holder.textViewFechaReserva.setText("Fecha: " + reserva.getFecha());
        holder.textViewHoraReserva.setText("Hora: " + reserva.getHora());
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
                // Aquí puedes agregar lógica para filtrar por nombre o título
                if (reserva.getNombreCompleto().toLowerCase().contains(query.toLowerCase()) ||
                        reserva.getTituloLibro().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(reserva);
                }
            }
            reservasList = filteredList; // Actualiza la lista con los resultados filtrados
        }
        notifyDataSetChanged(); // Notifica al adaptador para actualizar la vista
    }

    public static class AprobadasViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombreCompleto;
        TextView textViewTituloLibro;
        TextView textViewFechaReserva;
        TextView textViewHoraReserva;

        public AprobadasViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombreCompleto = itemView.findViewById(R.id.text_view_nombreCompleto);
            textViewTituloLibro = itemView.findViewById(R.id.text_view_tituloLibro);
            textViewFechaReserva = itemView.findViewById(R.id.text_view_fechaReserva);
            textViewHoraReserva = itemView.findViewById(R.id.text_view_horaReserva);
        }
    }
}