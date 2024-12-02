package sv.edu.itca.g3_proyectoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailLibros_user extends AppCompatActivity {

    private TextView textViewTitulo;
    private TextView textViewAutor;
    private TextView textViewGenero;
    private TextView textViewISBN;
    private TextView textViewFechaIngreso;
    private TextView textViewCantidad;
    private TextView textViewDescripcion;
    private Button buttonReservar;
    private Button buttonRegresar;
    private ImageView imageViewLibro;
    private TextView id;
    private int idlibro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_libros_user);

        textViewTitulo = findViewById(R.id.text_view_titulo_libro);
        textViewAutor = findViewById(R.id.text_view_autor);
        textViewGenero = findViewById(R.id.text_view_genero);
        textViewISBN = findViewById(R.id.text_view_isbn);
        textViewFechaIngreso = findViewById(R.id.text_view_fecha_ingreso);
        textViewCantidad = findViewById(R.id.text_view_cantidad);
        textViewDescripcion = findViewById(R.id.text_view_descripcion_libro);
        buttonReservar = findViewById(R.id.button_reservar);
        buttonRegresar = findViewById(R.id.button_regresar);
        imageViewLibro = findViewById(R.id.image_view_libro);
        id = findViewById(R.id.idl);

        Libro libro = (Libro) getIntent().getSerializableExtra("libro");
        if (libro != null) {
            textViewTitulo.setText(libro.getTitulo());
            textViewAutor.setText("Autor: " + libro.getAutor());
            textViewGenero.setText("Género: " + libro.getGenero());
            textViewISBN.setText("ISBN: " + libro.getIsbn());
            textViewFechaIngreso.setText("Fecha Ingreso: " + libro.getFechaIngreso());
            textViewFechaIngreso.setText("Cantidad: " + libro.getCantidad());
            textViewDescripcion.setText(libro.getDescripcion());
            Glide.with(this).load(libro.getImagen()).into(imageViewLibro);
        }
        idlibro = libro.getId();

        buttonRegresar.setOnClickListener(v -> finish());

        // Pasar el título del libro a ReservarLibroUsuarioActivity
        buttonReservar.setOnClickListener(v -> {
            Intent intent = new Intent(DetailLibros_user.this, ReservarLibroUsuarioActivity.class);
            intent.putExtra("tituloLibro", libro.getTitulo());
            intent.putExtra("idLibro", idlibro); // Asegúrate de tener el id del libro
            startActivity(intent);
        });
    }
}
