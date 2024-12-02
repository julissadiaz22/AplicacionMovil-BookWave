package sv.edu.itca.g3_proyectoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailLibros extends AppCompatActivity {

    private static final int EDIT_BOOK_REQUEST_CODE = 1; // Agrega esta línea
    private TextView textViewTitulo;
    private TextView textViewAutor;
    private TextView textViewGenero;
    private TextView textViewISBN;
    private TextView textViewFechaIngreso;
    private TextView textViewCantidad;
    private TextView textViewDescripcion;
    private Button buttonEditar;
    private Button buttonEliminar;
    private Button buttonRegresar;
    private ImageView imageViewLibro;
    private ControladorBD controladorBD;
    private int idLibro;
    private Libro libro; // Variable para almacenar el objeto libro

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_libros);

        // Inicializar vistas
        textViewTitulo = findViewById(R.id.text_view_titulo_libro);
        textViewAutor = findViewById(R.id.text_view_autor);
        textViewGenero = findViewById(R.id.text_view_genero);
        textViewISBN = findViewById(R.id.text_view_isbn);
        textViewFechaIngreso = findViewById(R.id.text_view_fecha_ingreso);
        textViewCantidad = findViewById(R.id.text_view_cantidad);
        textViewDescripcion = findViewById(R.id.text_view_descripcion_libro);
        buttonEditar = findViewById(R.id.button_editar);
        buttonEliminar = findViewById(R.id.button_eliminar);
        buttonRegresar = findViewById(R.id.button_regresar);
        imageViewLibro = findViewById(R.id.image_view_libro);

        // Inicializar ControladorBD
        controladorBD = new ControladorBD(this, "bibliotecasa.db", null, 1);

        // Obtener el libro de la intención
        libro = (Libro) getIntent().getSerializableExtra("libro");
        if (libro != null) {
            // Configurar las vistas con la información del libro
            idLibro = libro.getId(); // Obtener el ID del libro
            textViewTitulo.setText(libro.getTitulo());
            textViewAutor.setText("Autor: " + libro.getAutor());
            textViewGenero.setText("Género: " + libro.getGenero());
            textViewISBN.setText("ISBN: " + libro.getIsbn());
            textViewFechaIngreso.setText("Fecha Ingreso: " + libro.getFechaIngreso());
            textViewCantidad.setText("Cantidad: " + libro.getCantidad());
            textViewDescripcion.setText(libro.getDescripcion());

            // Cargar la imagen del libro usando Glide
            Glide.with(this).load(libro.getImagen()).into(imageViewLibro);
        }

        buttonRegresar.setOnClickListener(v -> finish());

        // Implementar la lógica para eliminar el libro
        buttonEliminar.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmar Eliminación")
                    .setMessage("¿Estás seguro de que deseas eliminar este libro?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        try {
                            // Eliminar el libro de la base de datos
                            if (controladorBD.eliminarLibro(idLibro)) {
                                Toast.makeText(this, "Libro eliminado exitosamente", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK); // Establece un resultado OK
                                finish(); // Cierra esta actividad y regresa a la lista de libros
                            } else {
                                Toast.makeText(this, "Error al eliminar el libro", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e("DetailLibros", "Error al eliminar el libro: ", e);
                            Toast.makeText(this, "Ocurrió un error al intentar eliminar el libro", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // Lógica para editar el libro
        buttonEditar.setOnClickListener(v -> {
            Intent intent = new Intent(DetailLibros.this, EditBookActivity.class);
            // Pasar datos del libro a EditBookActivity
            intent.putExtra ("titulo", libro.getTitulo());
            intent.putExtra("autor", libro.getAutor());
            intent.putExtra("genero", libro.getGenero());
            intent.putExtra("isbn", libro.getIsbn());
            intent.putExtra("fechaIngreso", libro.getFechaIngreso());
            intent.putExtra("cantidad", libro.getCantidad());
            intent.putExtra("descripcion", libro.getDescripcion());
            intent.putExtra("imagenUrl", libro.getImagen());
            startActivityForResult(intent, EDIT_BOOK_REQUEST_CODE);
        });
    }
}