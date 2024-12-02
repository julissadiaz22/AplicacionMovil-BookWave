package sv.edu.itca.g3_proyectoandroid;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;

public class FragmentAgregarLibros extends Fragment {

    private EditText txtTitulo, txtAutor, txtGenero, txtISBN, txtFechaIngreso, txtCantidad, txtDescripcion;
    private ImageView imageView;
    private Button btnCargarImagen, btnAgregarLibro, btnLimpiar;
    private static final int PICK_IMAGE = 1; // Código para seleccionar imagen
    private Uri selectedImageUri = null; // Para almacenar la URI de la imagen seleccionada

    private ControladorBD controladorBD;

    public FragmentAgregarLibros() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controladorBD = new ControladorBD(getActivity(), "bibliotecasa.db", null, 1); // Instanciar el controlador de la BD
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar_libros, container, false);

        // Referencias a los campos de texto
        txtTitulo = view.findViewById(R.id.txtTitulo);
        txtAutor = view.findViewById(R.id.txtAutor);
        txtGenero = view.findViewById(R.id.txtGenero);
        txtISBN = view.findViewById(R.id.txtISBN);
        txtFechaIngreso = view.findViewById(R.id.txtFechaIngreso);
        txtCantidad = view.findViewById(R.id.txtCantidad);
        txtDescripcion = view.findViewById(R.id.txtDescripcion);
        imageView = view.findViewById(R.id.imageView);

        btnCargarImagen = view.findViewById(R.id.btnCargarImagen);
        btnAgregarLibro = view.findViewById(R.id.btnActualizar);
        btnLimpiar = view.findViewById(R.id.btnRegresar);

        // Configurar el DatePickerDialog
        txtFechaIngreso.setOnClickListener(v -> showDatePickerDialog());

        // Configurar el botón para seleccionar la imagen
        btnCargarImagen.setOnClickListener(v -> openGallery());

        // Configurar el botón agregar libro
        btnAgregarLibro.setOnClickListener(v -> agregarLibro());

        // Configurar el botón limpiar campos
        btnLimpiar.setOnClickListener(v -> limpiarCampos());

        return view;
    }

    // Método para abrir el DatePickerDialog
    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view1, selectedYear, selectedMonth, selectedDay) -> {
                    // Formatear la fecha en formato dd/MM/yyyy
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    txtFechaIngreso.setText(date);
                }, year, month, day);
        datePickerDialog.show();
    }

    // Método para abrir la galería
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Verificar si se seleccionó una imagen
        if (requestCode == PICK_IMAGE && resultCode == getActivity().RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                imageView.setVisibility(View.VISIBLE); // Hacer visible el ImageView
                imageView.setImageURI(selectedImageUri); // Establecer la imagen seleccionada
            }
        } else {
            Toast.makeText(getActivity(), "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para agregar un libro a la base de datos
    private void agregarLibro() {
        String titulo = txtTitulo.getText().toString();
        String autor = txtAutor.getText().toString();
        String genero = txtGenero.getText().toString();
        String isbn = txtISBN.getText().toString();
        String fechaIngreso = txtFechaIngreso.getText().toString();
        String cantidadStr = txtCantidad.getText().toString();
        String descripcion = txtDescripcion.getText().toString();

        if (titulo.isEmpty() || autor.isEmpty() || genero.isEmpty() || isbn.isEmpty() || fechaIngreso.isEmpty() || cantidadStr.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(getActivity(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int cantidad = Integer.parseInt(cantidadStr);

        // Guardar la imagen
        String imagenPath = null;
        if (selectedImageUri != null) {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            imagenPath = controladorBD.guardarImagen(bitmap, "libro_" + System.currentTimeMillis());
        }

        // Guardar en la base de datos
        controladorBD.agregarLibro(titulo, autor, genero, isbn, fechaIngreso, cantidad, descripcion, imagenPath);
        Toast.makeText(getActivity(), "Libro agregado con éxito", Toast.LENGTH_SHORT).show();
        limpiarCampos();
    }

    // Método para limpiar los campos de entrada
    private void limpiarCampos() {
        txtTitulo.setText("");
        txtAutor.setText("");
        txtGenero.setText("");
        txtISBN.setText("");
        txtFechaIngreso.setText("");
        txtCantidad.setText("");
        txtDescripcion.setText("");
        imageView.setImageURI(null); // Limpiar la imagen
        selectedImageUri = null; // Resetear la URI de la imagen
        imageView.setVisibility(View.GONE); // Ocultar el ImageView
    }
}
