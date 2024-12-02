package sv.edu.itca.g3_proyectoandroid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PerfilFragment extends Fragment {

    private EditText txtNombre, txtUsuario, txtClave, txtCambiarClave;
    private UsuarioDAO usuarioDAO;
    private Usuario usuarioLogueado;
    private SharedPreferences sharedPreferences;

    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuarioDAO = new UsuarioDAO(getContext());
        sharedPreferences = getActivity().getSharedPreferences("sesion_usuario", getContext().MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        txtNombre = view.findViewById(R.id.txtNombre);
        txtUsuario = view.findViewById(R.id.txtUsuario);
        txtClave = view.findViewById(R.id.txtClave);
        txtCambiarClave = view.findViewById(R.id.txtCambiarClave);
        Button btnEditarPerfil = view.findViewById(R.id.btnEditarPerfil);

        cargarDatosUsuario();
        btnEditarPerfil.setOnClickListener(v -> actualizarPerfil());

        return view;
    }

    private void cargarDatosUsuario() {
        int usuarioId = sharedPreferences.getInt("usuario_id", -1);
        String usuarioNombre = sharedPreferences.getString("usuario_nombre", "");
        String nombreCompleto = sharedPreferences.getString("usuario_nombre_completo", "");
        String usuarioClave = sharedPreferences.getString("usuario_clave", "");
        int usuarioRol = sharedPreferences.getInt("usuario_rol", -1);

        if (usuarioId != -1 && !usuarioNombre.isEmpty() && !nombreCompleto.isEmpty()) {
            txtNombre.setText(nombreCompleto);
            txtUsuario.setText(usuarioNombre);
            txtClave.setText(usuarioClave);

            usuarioLogueado = new Usuario();
            usuarioLogueado.setId(usuarioId);
            usuarioLogueado.setNombreUsuario(usuarioNombre);
            usuarioLogueado.setNombreCompleto(nombreCompleto);
            usuarioLogueado.setClave(usuarioClave);
            usuarioLogueado.setRol(usuarioRol);
        } else {
            Toast.makeText(getContext(), "Error al cargar el perfil del usuario.", Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarPerfil() {
        if (usuarioLogueado == null) {
            Toast.makeText(getContext(), "No se pudo cargar el usuario.", Toast.LENGTH_SHORT).show();
            return;
        }

        String nuevoNombre = txtNombre.getText().toString();
        String nuevoUsuario = txtUsuario.getText().toString();
        String nuevaClave = txtCambiarClave.getText().toString().isEmpty() ? txtClave.getText().toString() : txtCambiarClave.getText().toString();

        usuarioLogueado.setNombreCompleto(nuevoNombre);
        usuarioLogueado.setNombreUsuario(nuevoUsuario);
        usuarioLogueado.setClave(nuevaClave);

        boolean exito = usuarioDAO.actualizarUsuario(usuarioLogueado);

        if (exito) {
            Toast.makeText(getContext(), "Perfil actualizado correctamente.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Error al actualizar el perfil.", Toast.LENGTH_SHORT).show(); }
    }
}