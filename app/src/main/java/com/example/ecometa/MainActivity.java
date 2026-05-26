package com.example.ecometa;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Camada: View (Activity Principal)
 * Responsável por configurar o Navigation Component e o BottomNavigationView em Java.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verifica se o usuário está autenticado antes de carregar o layout
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Garante que o layout XML está definido antes de buscar as views
        setContentView(R.layout.activity_main);

        // 1. Instancia o BottomNavigationView do XML
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);

        // 2. Localiza o NavHostFragment usando o FragmentManager (Método mais estável em Java)
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            // 3. Obtém o NavController
            NavController navController = navHostFragment.getNavController();

            // 4. Conecta o NavController ao BottomNavigationView
            NavigationUI.setupWithNavController(navView, navController);
        }
    }
}
