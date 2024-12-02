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

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.UsuariosViewHolder> {

    private List<Usuario> usuariosList;
    private List<Usuario> usuariosListFull; // Para filtrar
    private Context context;

    // Constructor modificado para recibir el contexto
    public UsuariosAdapter(Context context, List<Usuario> usuarios) {
        this.context = context; // Inicializa el contexto
        this.usuariosList = usuarios;
        this.usuariosListFull = new ArrayList<>(usuarios); // Mantiene una copia para filtrar
    }

    @NonNull
    @Override
    public UsuariosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_usuario, parent, false);
        return new UsuariosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuariosViewHolder holder, int position) {
        Usuario usuario = usuariosList.get(position);
        holder.textViewNombre.setText("Usuario: " + usuario.getNombreCompleto());
        holder.textViewCorreo.setText("Correo: " + usuario.getCorreo());

        // Modificar el rol para mostrar "Administrador" o "Usuario"
        if (usuario.getRol() == 0) {
            holder.textViewRol.setText("Rol: Administrador");
        } else {
            holder.textViewRol.setText("Rol: Usuario");
        }

        // Configura el OnClickListener para el botón "VER MÁS"
        holder.buttonViewMore.setOnClickListener(v -> {
            try {
                // Inicia la actividad de detalle
                Intent intent = new Intent(context, DetailUsuarios.class);
                intent.putExtra("usuario", usuario); // Envío del objeto Usuario
                context.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(context, "Error al navegar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return usuariosList.size();
    }

    // Método de filtrado para filtrar la lista según la consulta de búsqueda
    public void filtrar(String query) {
        if (query.isEmpty()) {
            usuariosList = new ArrayList<>(usuariosListFull); // Restablece la lista completa
        } else {
            List<Usuario> filteredList = new ArrayList<>();
            for (Usuario usuario : usuariosListFull) {
                // Verifica si el nombre de usuario contiene la consulta de búsqueda
                if (usuario.getNombreCompleto().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(usuario);
                }
            }
            usuariosList = filteredList; // Actualiza la lista con los resultados filtrados
        }
        notifyDataSetChanged(); // Notifica al adaptador para actualizar la vista
    }

    public void setUsuarios(List<Usuario> nuevosUsuarios) {
        this.usuariosList = nuevosUsuarios; // Actualiza la lista de usuarios
        notifyDataSetChanged(); // Notifica al adaptador para que recargue la vista
    }

    // Clase ViewHolder para contener las vistas de cada elemento
    public static class UsuariosViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre;
        TextView textViewCorreo;
        TextView textViewRol;
        Button buttonViewMore;

        public UsuariosViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.text_view_nombreUsuario);
            textViewCorreo = itemView.findViewById(R.id.text_view_Correo);
            textViewRol = itemView.findViewById(R.id.text_view_Rol);
            buttonViewMore = itemView.findViewById(R.id.button_view_moreUsuario); // Asegúrate de que este ID es correcto
        }
    }
}
