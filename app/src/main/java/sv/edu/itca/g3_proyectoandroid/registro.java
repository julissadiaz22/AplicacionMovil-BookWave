package sv.edu.itca.g3_proyectoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class registro extends AppCompatActivity {

    private EditText txtNombreCompleto, txtNombreUsuario, txtCorreoElectronico, txtContrasena, txtConfirmarContrasena;
    private ControladorBD controladorBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicializar vistas
        txtNombreCompleto = findViewById(R.id.txtNombreCompleto);
        txtNombreUsuario = findViewById(R.id.txtNombreUsuario);
        txtCorreoElectronico = findViewById(R.id.txtCorreo);
        txtContrasena = findViewById(R.id.txtClave);
        txtConfirmarContrasena = findViewById(R.id.txtConfirmarClave);

        // Inicializar controlador de base de datos
        controladorBD = new ControladorBD(this, "bibliotecasa.db", null, 1);

        // Configurar el botón de registro
        findViewById(R.id.btnRegistro).setOnClickListener(v -> registrarUsuario());

        // Configurar el enlace "Iniciar sesión"
        TextView linkRegistro = findViewById(R.id.linkinicio);
        linkRegistro.setOnClickListener(v -> {
            // Redirigir a la actividad principal (MainActivity)
            Intent intent = new Intent(registro.this, MainActivity.class);
            startActivity(intent);
            finish(); // Finaliza la actividad actual
        });
    }

    private void registrarUsuario() {
        // Obtener los datos del formulario
        String nombreCompleto = txtNombreCompleto.getText().toString().trim();
        String nombreUsuario = txtNombreUsuario.getText().toString().trim();
        String correo = txtCorreoElectronico.getText().toString().trim();
        String contrasena = txtContrasena.getText().toString().trim();
        String confirmarContrasena = txtConfirmarContrasena.getText().toString().trim();

        // Validar campos
        if (nombreCompleto.isEmpty() || nombreUsuario.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || confirmarContrasena.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!contrasena.equals(confirmarContrasena)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Establecer valores predeterminados para el estado y el rol
        int estado = 1;  // Activo por defecto
        int rol = 1;     // Usuario Normal por defecto

        // Crear objeto Usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombreCompleto(nombreCompleto);
        nuevoUsuario.setNombreUsuario(nombreUsuario);
        nuevoUsuario.setCorreo(correo);
        nuevoUsuario.setClave(contrasena);
        nuevoUsuario.setEstado(estado);
        nuevoUsuario.setRol(rol);

        // Guardar el usuario en la base de datos
        boolean resultado = controladorBD.agregarUsuario(nuevoUsuario);

        if (resultado) {
            Toast.makeText(this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
            // Redirigir a MainActivity
            Intent intent = new Intent(registro.this, MainActivity.class);
            startActivity(intent);
            finish(); // Cerrar esta actividad
        } else {
            Toast.makeText(this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
        }
    }
}
