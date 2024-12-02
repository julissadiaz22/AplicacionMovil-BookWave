package sv.edu.itca.g3_proyectoandroid;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;



public class Principal_user extends AppCompatActivity {
    private static final int STORAGE_PERMISSION_CODE = 100;
    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    NavigationView navigationView; // Declarar el NavigationView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_user);  // Establecer el layout de la actividad

        // Verificar permisos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // Solicitar permiso
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            }
        }



        // Inicializar las vistas usando findViewById
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view); // Inicializar el NavigationView
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Configurar la barra de herramientas (toolbar)
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Comprobamos si no hay estado guardado para cargar el fragmento inicial
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new BooksFragment_user()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        // Configurar el NavigationView (menú lateral)
        setupNavigationView();

        // Configurar el BottomNavigationView
        bottomNavigationView.setBackground(null);  // Eliminar el fondo
        setupBottomNavigation();  // Llamar al método que configura el BottomNavigation
    }

    // Método para configurar la navegación lateral
    private void setupNavigationView() {
        navigationView.setNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // Usamos if para verificar la selección del menú lateral
            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new BooksFragment_user();

            }else if (item.getItemId()==R.id.nav_cultura) {
                    selectedFragment= new ActividadesFragment_user();

            } else if (item.getItemId()==R.id.nav_perfil) {
                selectedFragment= new PerfilFragment();

            } else if (item.getItemId() == R.id.nav_share) {
                selectedFragment = new FragmentCompartir();
            } else if (item.getItemId() == R.id.nav_about) {
                selectedFragment = new FragmentSobreNosotros();
            }else if (item.getItemId() == R.id.nav_logout) {
                // Mostrar un diálogo de confirmación para cerrar sesión
                showLogoutConfirmationDialog();
            }

            // Si un fragmento fue seleccionado, se reemplaza en el frame layout
            if (selectedFragment != null) {
                replaceFragment(selectedFragment);
                drawerLayout.closeDrawers();  // Cerrar el menú lateral tras seleccionar
            }

            return true;  // Retornar true para indicar que la selección fue manejada
        });
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.libros) {
                selectedFragment = new BooksFragment_user();
            } else if (item.getItemId() == R.id.eventos) {
                selectedFragment = new ActividadesFragment_user();
            } else if (item.getItemId() == R.id.prestamos) {
                selectedFragment = new FragmentHistorialUsuario();
            } else if (item.getItemId() == R.id.usuarios) {
                selectedFragment = new UsuariosFragment();
            } else if (item.getItemId() == R.id.solicitudes) {
                selectedFragment = new SolicitudesFragment();
            }

            if (selectedFragment != null) {
                replaceFragment(selectedFragment);
            }
            return true;
        });

        // Configurar el FloatingActionButton (FAB)
        fab.setOnClickListener(view -> showBottomDialog());  // Mostrar un diálogo cuando se presiona el FAB
    }

    // Método para reemplazar fragmentos
    /*private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
    }*/
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null); // Agregar a la pila de retroceso si deseas que el usuario pueda volver
        transaction.commit();
    }



    // Método para mostrar un diálogo en el FAB (debes implementar este método)
    private void showBottomDialog() {
        // Lógica para mostrar el diálogo
    }

    // Método para mostrar el diálogo de confirmación al cerrar sesión
    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    // El usuario confirma, cerramos la sesión
                    logout();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // El usuario cancela, simplemente cerramos el diálogo
                    dialog.dismiss();
                })
                .show();
    }

    // Método para cerrar la sesión del usuario
    private void logout() {
        // Aquí eliminamos cualquier dato de la sesión, por ejemplo, SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();  // Eliminar todos los datos de sesión guardados
        editor.apply();

        // Redirigir al usuario a la pantalla de inicio de sesión
        Intent intent = new Intent(Principal_user.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();  // Finalizamos la actividad actual para evitar volver atrás
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, puedes realizar operaciones relacionadas con el almacenamiento
            } else {
                // Permiso denegado, puedes mostrar un mensaje o manejar el caso
            }
        }
    }



}
