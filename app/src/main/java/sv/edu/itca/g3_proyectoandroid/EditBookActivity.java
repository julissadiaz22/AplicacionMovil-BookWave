package sv.edu.itca.g3_proyectoandroid;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;

public class EditBookActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;  // Código para seleccionar imagen
    private EditText editTextTitulo, editTextAutor, editTextGenero, editTextISBN, editTextFechaIngreso, editTextCantidad, editTextDescripcion;
    private ImageView imageViewLibro;
    private Button btnActualizar, btnSeleccionarImagen, btnFinalizar;
    private Uri imagenUri;  // URI para la imagen seleccionada

    // Lanzador para la selección de imagen
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<androidx.activity.result.ActivityResult>() {
                @Override
                public void onActivityResult(androidx.activity.result.ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            imagenUri = selectedImageUri;
                            Glide.with(EditBookActivity.this)
                                    .load(imagenUri)  // Carga la imagen usando Glide
                                    .into(imageViewLibro);
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        // Inicialización de vistas
        editTextTitulo = findViewById(R.id.txtTitulo);
        editTextAutor = findViewById(R.id.txtAutor);
        editTextGenero = findViewById(R.id.txtGenero);
        editTextISBN = findViewById(R.id.txtISBN);
        editTextFechaIngreso = findViewById(R.id.txtFechaIngreso);
        editTextCantidad = findViewById(R.id.txtCantidad);
        editTextDescripcion = findViewById(R.id.txtDescripcion);
        imageViewLibro = findViewById(R.id.imageView);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnSeleccionarImagen = findViewById(R.id.btnCargarImagen);
        btnFinalizar=findViewById(R.id.btnRegresar);

        // Obtener los datos enviados desde la actividad anterior
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            editTextTitulo.setText(extras.getString("titulo", ""));
            editTextAutor.setText(extras.getString("autor", ""));
            editTextGenero.setText(extras.getString("genero", ""));
            editTextISBN.setText(extras.getString("isbn", ""));
            editTextFechaIngreso.setText(extras.getString("fechaIngreso", ""));
            int cantidad = extras.getInt("cantidad", 0);
            editTextCantidad.setText(String.valueOf(cantidad));
            editTextDescripcion.setText(extras.getString("descripcion", ""));
            String imagenUrl = extras.getString("imagenUrl");

            // Si la imagenUrl no es vacía, se carga la imagen en el ImageView
            if (imagenUrl != null && !imagenUrl.isEmpty()) {
                Glide.with(this).load(imagenUrl).into(imageViewLibro);
                imagenUri = Uri.parse(imagenUrl);  // Guardar la URI para usarla al actualizar
            }
        }

        // Configurar el botón de actualización
        btnActualizar.setOnClickListener(v -> actualizarLibro());

        // Configurar el DatePickerDialog para la fecha de ingreso
        editTextFechaIngreso.setOnClickListener(v -> mostrarDatePicker());

        // Configurar el botón para seleccionar la imagen
        btnSeleccionarImagen.setOnClickListener(v -> seleccionarImagen());

        //Configurar el boton de regresar
        btnFinalizar.setOnClickListener(v -> {
            finish();
        });


    }

    private void mostrarDatePicker() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int year = calendar.get(java.util.Calendar.YEAR);
        int month = calendar.get(java.util.Calendar.MONTH);
        int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                EditBookActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Formato de la fecha: YYYY-MM-DD
                    String fechaSeleccionada = selectedYear + "-" +
                            String.format("%02d", selectedMonth + 1) + "-" +
                            String.format("%02d", selectedDay);
                    editTextFechaIngreso.setText(fechaSeleccionada);
                },
                year, month, day
        );
        datePickerDialog.show();
    }
    private void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);  // Usamos el launcher para abrir la galería
    }

    private void actualizarLibro() {
        String titulo = editTextTitulo.getText().toString().trim();
        String autor = editTextAutor.getText().toString().trim();
        String genero = editTextGenero.getText().toString().trim();
        String isbn = editTextISBN.getText().toString().trim();
        String fechaIngreso = editTextFechaIngreso.getText().toString().trim();
        String cantidadStr = editTextCantidad.getText().toString().trim();
        String descripcion = editTextDescripcion.getText().toString().trim();

        // Validar que los campos no estén vacíos
        if (titulo.isEmpty() || autor.isEmpty() || genero.isEmpty() || isbn.isEmpty() || fechaIngreso.isEmpty() || cantidadStr.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(EditBookActivity.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        int cantidad = Integer.parseInt(cantidadStr);

        // Si el usuario ha seleccionado una imagen, obtenemos la URI
        String imagenUrl = imagenUri != null ? imagenUri.toString() : "";

        // Llamada al DAO para actualizar el libro
        LibroDAO libroDAO = new LibroDAO(EditBookActivity.this);

        boolean exito = libroDAO.actualizarLibro(titulo, autor, genero, isbn, fechaIngreso, cantidad, descripcion, imagenUrl);

        if (exito) {
            Toast.makeText(EditBookActivity.this, "Libro actualizado correctamente.", Toast.LENGTH_SHORT).show();
            finish(); // Cerrar la actividad y regresar
        } else {
            Toast.makeText(EditBookActivity.this, "Error al actualizar el libro.", Toast.LENGTH_SHORT).show();
        }
    }
}