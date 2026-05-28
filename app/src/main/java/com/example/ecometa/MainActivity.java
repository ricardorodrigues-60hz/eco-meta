package com.example.ecometa;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.example.ecometa.repository.AutenticacaoRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;


 // configura o Navigation Component e o BottomNavigationView

public class MainActivity extends AppCompatActivity {

    private AutenticacaoRepository autenticacaoRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Força o modo claro programaticamente
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);

        // Inicializa o repositório que centraliza as checagens do Firebase
        autenticacaoRepository = new AutenticacaoRepository();

        // 💡 Utiliza o repositório para verificar a sessão ativa de forma desacoplada
        if (autenticacaoRepository.obterIdUsuarioAtual() == null) {
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