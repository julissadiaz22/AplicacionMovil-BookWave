package sv.edu.itca.g3_proyectoandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class FragmentAgregarUsuarios extends Fragment {

    private EditText txtNombreCompleto, txtNombreUsuario, txtCorreoElectronico, txtContrasena, txtConfirmarContrasena;
    private Spinner spinnerEstado, spinnerRol;
    private Button btnAgregarUsuario, btnLimpiar;
    private ControladorBD controladorBD;

    public FragmentAgregarUsuarios() {
        // Requiere un constructor vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout del fragmento
        View view = inflater.inflate(R.layout.fragment_agregar_usuarios, container, false);

        // Inicializar vistas
        txtNombreCompleto = view.findViewById(R.id.txtNombreCompleto);
        txtNombreUsuario = view.findViewById(R.id.txtNombreUsuario);
        txtCorreoElectronico = view.findViewById(R.id.txtCorreoElectronico);
        txtContrasena = view.findViewById(R.id.txtContrasena);
        txtConfirmarContrasena = view.findViewById(R.id.txtConfirmarContrasena);
        spinnerEstado = view.findViewById(R.id.spinnerEstado);
        spinnerRol = view.findViewById(R.id.spinnerRol);
        btnAgregarUsuario = view.findViewById(R.id.btnAgregarUsuario);
        btnLimpiar = view.findViewById(R.id.btnRegresar);

        // Inicializar el controlador de la base de datos
        controladorBD = new ControladorBD(getContext(), "bibliotecasa.db", null, 1);

        // Cargar los roles en el Spinner de Roles
        ArrayAdapter<CharSequence> adapterRol = ArrayAdapter.createFromResource(getContext(),
                R.array.rol_array, android.R.layout.simple_spinner_item);
        adapterRol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRol.setAdapter(adapterRol);

        // Cargar los estados en el Spinner de Estado
        ArrayAdapter<CharSequence> adapterEstado = ArrayAdapter.createFromResource(getContext(),
                R.array.estado_options, android.R.layout.simple_spinner_item);
        adapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapterEstado);

        // Configuración de botones
        btnAgregarUsuario.setOnClickListener(v -> agregarUsuario());
        btnLimpiar.setOnClickListener(v -> limpiarCampos());

        return view;
    }

    private void agregarUsuario() {
        // Validar los campos antes de crear el usuario
        String nombreCompleto = txtNombreCompleto.getText().toString().trim();
        String nombreUsuario = txtNombreUsuario.getText().toString().trim();
        String correo = txtCorreoElectronico.getText().toString().trim();
        String contrasena = txtContrasena.getText().toString().trim();
        String confirmarContrasena = txtConfirmarContrasena.getText().toString().trim();

        // Validar campos
        if (nombreCompleto.isEmpty() || nombreUsuario.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || confirmarContrasena.isEmpty()) {
            Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!contrasena.equals(confirmarContrasena)) {
            Toast.makeText(getContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el estado y el rol desde los spinners
        int estado = spinnerEstado.getSelectedItemPosition(); // Obtiene la posición seleccionada en el spinner de estado
        int rol = spinnerRol.getSelectedItemPosition(); // Obtiene la posición seleccionada en el spinner de rol

        // Convertir la posición del spinner de rol a un valor de rol
        // 0 -> Usuario Normal
        // 1 -> Administrador
        if (rol == 1) {
            rol = 1;  // administrador
        } else if (rol == 0) {
            rol = 0;  // usuario normal
        }

        // Crear el objeto Usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombreCompleto(nombreCompleto);
        nuevoUsuario.setNombreUsuario(nombreUsuario);
        nuevoUsuario.setCorreo(correo);
        nuevoUsuario.setClave(contrasena);
        nuevoUsuario.setEstado(estado);
        nuevoUsuario.setRol(rol);  // Establecer el rol correspondiente

        // Guardar el usuario en la base de datos
        boolean resultado = controladorBD.agregarUsuario(nuevoUsuario);

        if (resultado) {
            Toast.makeText(getContext(), "Usuario agregado exitosamente", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        } else {
            Toast.makeText(getContext(), "Error al agregar el usuario", Toast.LENGTH_SHORT).show();
        }
    }


    private void limpiarCampos() {
        txtNombreCompleto.setText("");
        txtNombreUsuario.setText("");
        txtCorreoElectronico.setText("");
        txtContrasena.setText("");
        txtConfirmarContrasena.setText("");
        spinnerEstado.setSelection(0); // Restablecer el spinner de estado al primer valor
        spinnerRol.setSelection(0); // Restablecer el spinner de rol al primer valor
    }
}
