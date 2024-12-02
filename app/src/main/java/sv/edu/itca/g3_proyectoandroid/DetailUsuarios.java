package sv.edu.itca.g3_proyectoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DetailUsuarios extends AppCompatActivity {
    private ControladorBD controladorBD;
    private int idUsuario; // Asegúrate de que este ID se inicialice correctamente

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_usuarios);

        // Referencias a los TextViews y botones
        TextView textViewNombreUsuario = findViewById(R.id.text_view_nombre_usuario);
        TextView textViewNombreCompleto = findViewById(R.id.text_view_nombre_completo);
        TextView textViewCorreo = findViewById(R.id.text_view_correo);
        TextView textViewEstado = findViewById(R.id.text_view_estado);
        TextView textViewRol = findViewById(R.id.text_view_rol);
        Button buttonEliminar = findViewById(R.id.button_eliminar_usuario);
        Button buttonRegresar = findViewById(R.id.button_regresar_usuario);
        Button buttonEditar = findViewById(R.id.button_editar_usuario); // Nuevo botón para editar

        // Inicializar ControladorBD
        controladorBD = new ControladorBD(this, "bibliotecasa.db", null, 1);

        // Obtener la intención y los datos del usuario seleccionado
        Intent intent = getIntent();
        Usuario usuario = (Usuario) intent.getSerializableExtra("usuario");

        // Establecer los datos en los TextViews
        if (usuario != null) {
            idUsuario = usuario.getId(); // Asegúrate de que este método exista en la clase Usuario
            textViewNombreUsuario.setText("Nombre: " + usuario.getNombreUsuario());
            textViewNombreCompleto.setText("Usuario: " + usuario.getNombreCompleto());
            textViewCorreo.setText("Correo: " + usuario.getCorreo());

            // Lógica para mostrar Estado (1: Activo, 0: Inactivo)
            textViewEstado.setText(usuario.getEstado() == 1 ? "Estado: Activo" : "Estado: Inactivo");

            // Lógica para mostrar Rol (0: Administrador, 1: Usuario)
            textViewRol.setText(usuario.getRol() == 0 ? "Rol: Administrador" : "Rol: Usuario");

        } else {
            // Manejo de error si usuario es null
            textViewNombreUsuario.setText("Usuario no disponible");
        }

        // Botón regresar
        buttonRegresar.setOnClickListener(v -> finish());

        // Implementar la lógica para eliminar el usuario
        buttonEliminar.setOnClickListener(v -> {
            // Mostrar diálogo de confirmación
            new AlertDialog.Builder(this)
                    .setTitle("Confirmar Eliminación")
                    .setMessage("¿Estás seguro de que deseas eliminar este usuario?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        // Eliminar el usuario de la base de datos
                        if (controladorBD.eliminarUsuario(idUsuario)) {
                            Toast.makeText(this, "Usuario eliminado exitosamente", Toast.LENGTH_SHORT).show();
                            finish(); // Cierra esta actividad y regresa a la lista de usuarios
                        } else {
                            Toast.makeText(this, "Error al eliminar el usuario", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // Configurar el botón de editar
        buttonEditar.setOnClickListener(v -> {
            Intent editIntent = new Intent(DetailUsuarios.this, EditUsuarioActivity.class);
            editIntent.putExtra("usuario", usuario); // Pasar el usuario a editar
            startActivity(editIntent);
        });
    }
}