package com.example.ecometa;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.example.ecometa.databinding.ActivityMainBinding;
import com.example.ecometa.ui.fragment.DesafiosFragment;
import com.example.ecometa.ui.fragment.HistoricoFragment;
import com.example.ecometa.ui.fragment.HomeFragment;
import com.example.ecometa.ui.fragment.RankingFragment;

/**
 * MainActivity atua como o Router Principal da aplicação.
 * Implementa navegação via BottomNavigationView com preservação de estado (hide/show).
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final FragmentManager fragmentManager = getSupportFragmentManager();

    // Fragments das abas
    private final Fragment homeFragment = new HomeFragment();
    private final Fragment historicoFragment = new HistoricoFragment();
    private final Fragment desafiosFragment = new DesafiosFragment();
    private final Fragment rankingFragment = new RankingFragment();

    private Fragment activeFragment = homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupNavigation();

    }

    /**
     * Configura a BottomNavigationView e inicializa os Fragments no container.
     */
    private void setupNavigation() {
        // Adiciona todos os fragments ao container, mas esconde os que não são a Home
        fragmentManager.beginTransaction().add(R.id.main, rankingFragment, "4").hide(rankingFragment).commit();
        fragmentManager.beginTransaction().add(R.id.main, desafiosFragment, "3").hide(desafiosFragment).commit();
        fragmentManager.beginTransaction().add(R.id.main, historicoFragment, "2").hide(historicoFragment).commit();
        fragmentManager.beginTransaction().add(R.id.main, homeFragment, "1").commit();

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                switchFragment(homeFragment);
                return true;
            } else if (itemId == R.id.nav_historico) {
                switchFragment(historicoFragment);
                return true;
            } else if (itemId == R.id.nav_desafios) {
                switchFragment(desafiosFragment);
                return true;
            } else if (itemId == R.id.nav_ranking) {
                switchFragment(rankingFragment);
                return true;
            }
            return false;
        });
    }

    /**
     * Realiza a troca de fragments utilizando hide() e show() para preservar o estado.
     * @param target O fragment que deve ser exibido.
     */
    private void switchFragment(Fragment target) {
        if (activeFragment != target) {
            fragmentManager.beginTransaction()
                    .hide(activeFragment)
                    .show(target)
                    .commit();
            activeFragment = target;
        }
    }
}