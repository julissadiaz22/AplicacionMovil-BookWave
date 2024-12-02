package sv.edu.itca.g3_proyectoandroid;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class BookAdapterUser extends RecyclerView.Adapter<BookAdapterUser.BookViewHolder> {
    private List<Libro> bookList; // Lista original de libros
    private List<Libro> librosFiltrados; // Lista filtrada de libros
    private Context context; // Contexto para iniciar nuevas actividades

    // Constructor modificado para recibir el contexto
    public BookAdapterUser(Context context, List<Libro> bookList) {
        this.context = context; // Inicializa el contexto correctamente
        this.bookList = bookList;
        this.librosFiltrados = new ArrayList<>(bookList); // Inicializa la lista filtrada
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Libro book = librosFiltrados.get(position); // Usar la lista filtrada
        holder.titleTextView.setText(book.getTitulo());
        holder.authorTextView.setText(book.getAutor());
        // Cargar la imagen usando Glide
        Glide.with(holder.itemView.getContext()).load(book.getImagen()).into(holder.imageView);

        // Configurar el OnClickListener para el botón "VER MÁS"
        holder.buttonViewMore.setOnClickListener(v -> {
            try {
                // Inicia la actividad de detalle
                Intent intent = new Intent(context, DetailLibros_user.class);
                intent.putExtra("libro", book); // Enviar el objeto libro
                context.startActivity(intent); // Iniciar la actividad de detalle
            } catch (Exception e) {
                Toast.makeText(context, "Error al navegar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return librosFiltrados.size(); // Retornar el tamaño de la lista filtrada
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView authorTextView;
        ImageView imageView;
        Button buttonViewMore; // Agregar botón aquí

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_view_title);
            authorTextView = itemView.findViewById(R.id.text_view_author);
            imageView = itemView.findViewById(R.id.image_view_book);
            buttonViewMore = itemView.findViewById(R.id.button_view_more_book); // Asegúrate de que este ID es correcto
        }
    }

    // Método para filtrar la lista de libros (opcional)
    public void filtrar(String query) {
        if (query.isEmpty()) {
            librosFiltrados = new ArrayList<>(bookList);
        } else {
            List<Libro> filteredList = new ArrayList<>();
            for (Libro libro : bookList) {
                if (libro.getTitulo().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(libro);
                }
            }
            librosFiltrados = filteredList;
        }
        notifyDataSetChanged(); // Notificar cambios
    }
}