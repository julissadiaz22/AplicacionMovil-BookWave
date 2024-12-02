package sv.edu.itca.g3_proyectoandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnInicio;
    private EditText etUsuario, etClave;
    private UsuarioDAO usuarioDAO;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuarioDAO = new UsuarioDAO(this);
        sharedPreferences = getSharedPreferences("sesion_usuario", MODE_PRIVATE);

        etUsuario = findViewById(R.id.txtUsuario);
        etClave = findViewById(R.id.txtClave);
        btnInicio = findViewById(R.id.btnInicio);

        btnInicio.setOnClickListener(v -> {
            String usuario = etUsuario.getText().toString().trim();
            String clave = etClave.getText().toString().trim();

            if (usuario.isEmpty() || clave.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor ingrese usuario y clave", Toast.LENGTH_SHORT).show();
                return;
            }

            Usuario usuarioAutenticado = usuarioDAO.validarUsuario(usuario, clave);

            if (usuarioAutenticado != null) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("usuario_id", usuarioAutenticado.getId());
                editor.putString("usuario_nombre", usuarioAutenticado.getNombreUsuario());
                editor.putString("usuario_nombre_completo", usuarioAutenticado.getNombreCompleto());
                editor.putString("usuario_clave", usuarioAutenticado.getClave());
                editor.putInt("usuario_rol", usuarioAutenticado.getRol());
                editor.apply();

                int rol = usuarioAutenticado.getRol();
                Intent intent;
                if (rol == 1) {
                    intent = new Intent(MainActivity.this, Principal_user.class);
                } else {
                    intent = new Intent(MainActivity.this, Principal.class);
                }
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(MainActivity.this, "Usuario o clave incorrectos", Toast.LENGTH_SHORT).show();
            }
        });

        TextView linkRegistro = findViewById(R.id.linkinicio);
        linkRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, registro.class);
            startActivity(intent);
        });
    }
}