package sv.edu.itca.g3_proyectoandroid;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditUsuarioActivity extends AppCompatActivity {
    private EditText editTextID, editTextNombreCompleto, editTextNombreUsuario, editTextCorreo, editTextContrasena, editTextConfirmarContrasena;
    private Spinner spinnerEstado, spinnerRol;
    private Button btnActualizar, btnRegresar;
    private UsuarioAdDAO usuarioAdDAO;
    private Usuario usuario; // Objeto de usuario a editar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_usuario);

        // Inicialización de vistas
        editTextID = findViewById(R.id.txtID);
        editTextNombreCompleto = findViewById(R.id.txtNombreCompleto);
        editTextNombreUsuario = findViewById(R.id.txtNombreUsuario);
        editTextCorreo = findViewById(R.id.txtCorreoElectronico);
        editTextContrasena = findViewById(R.id.txtContrasena);
        editTextConfirmarContrasena = findViewById(R.id.txtConfirmarContrasena);
        spinnerEstado = findViewById(R.id.spinnerEstado);
        spinnerRol = findViewById(R.id.spinnerRol);
        btnActualizar = findViewById(R.id.btnAgregarUsuario);
        btnRegresar = findViewById(R.id.btnRegresar);

        // Inicializar UsuarioAdDAO
        usuarioAdDAO = new UsuarioAdDAO(this);

        // Obtener la intención y los datos del usuario seleccionado
        Intent intent = getIntent();
        usuario = (Usuario) intent.getSerializableExtra("usuario");

        // Cargar los datos en los EditText y Spinner
        if (usuario != null) {
            editTextID.setText(String.valueOf(usuario.getId()));
            editTextNombreCompleto.setText(usuario.getNombreUsuario());
            editTextNombreUsuario.setText(usuario.getNombreCompleto());
            editTextCorreo.setText(usuario.getCorreo());
            // Asignar valores a los spinners
            spinnerEstado.setSelection(usuario.getEstado() == 1 ? 0 : 1); // 0: Activo, 1: Inactivo
            spinnerRol.setSelection(usuario.getRol() == 0 ? 0 : 1); // 0: Administrador, 1: Usuario
        }

        // Cargar los arrays en los spinners

        ArrayAdapter<CharSequence> adapterEstado = ArrayAdapter.createFromResource(this, R.array.estado_array, android.R.layout.simple_spinner_item);
        adapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapterEstado);


        ArrayAdapter<CharSequence> adapterRol = ArrayAdapter.createFromResource(this, R.array.rol_array, android.R.layout.simple_spinner_item);
        adapterRol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerRol.setAdapter(adapterRol);

        // Configurar el botón de actualización
        btnActualizar.setOnClickListener(v -> actualizarUsuario());

        // Configurar el botón de regresar
        btnRegresar.setOnClickListener(v -> finish());
    }

    private void actualizarUsuario() {
        String nombreCompleto = editTextNombreCompleto.getText().toString().trim();
        String nombreUsuario = editTextNombreUsuario.getText().toString().trim();
        String correo = editTextCorreo.getText().toString().trim();
        String contrasena = editTextContrasena.getText ().toString().trim();
        String confirmarContrasena = editTextConfirmarContrasena.getText().toString().trim();
        int estado = spinnerEstado.getSelectedItemPosition(); // 0: Activo, 1: Inactivo
        int rol = spinnerRol.getSelectedItemPosition(); // 0: Administrador, 1: Usuario

        // Validar que los campos no estén vacíos
        if (nombreCompleto.isEmpty() || nombreUsuario.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || confirmarContrasena.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!contrasena.equals(confirmarContrasena)) {
            Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Actualizar el usuario en la base de datos
        boolean exito = usuarioAdDAO.actualizarUsuario(usuario.getId(), nombreCompleto, nombreUsuario, correo, contrasena, estado, rol);

        if (exito) {
            Toast.makeText(this, "Usuario actualizado correctamente.", Toast.LENGTH_SHORT).show();
            finish(); // Cerrar la actividad y regresar
        } else {
            Toast.makeText(this, "Error al actualizar el usuario.", Toast.LENGTH_SHORT).show();
        }
    }
}




