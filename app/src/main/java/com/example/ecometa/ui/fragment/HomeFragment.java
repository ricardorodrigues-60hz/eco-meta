package com.example.ecometa.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.ecometa.R;
import com.example.ecometa.repository.AutenticacaoRepository;
import com.example.ecometa.viewmodel.EcoMetaViewModel;
import com.example.ecometa.viewmodel.ViewModelFactory;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private EcoMetaViewModel viewModel;
    private TextView tvNivel, tvEcoPoints, tvCO2;
    private LinearProgressIndicator progressLevel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvNivel = view.findViewById(R.id.tvNivel);
        tvEcoPoints = view.findViewById(R.id.tvEcoPoints);
        tvCO2 = view.findViewById(R.id.tvCO2);
        progressLevel = view.findViewById(R.id.progressLevel);

        ViewModelFactory factory = new ViewModelFactory(new AutenticacaoRepository());
        viewModel = new ViewModelProvider(this, factory).get(EcoMetaViewModel.class);

        setupObservers();
        
        // Simulação de usuário logado
        viewModel.carregarDadosUsuario("user_123");

        return view;
    }

    private void setupObservers() {
        viewModel.usuario.observe(getViewLifecycleOwner(), usuario -> {
            if (usuario != null) {
                tvNivel.setText(usuario.getNivel());
                tvEcoPoints.setText(String.valueOf(usuario.getEco_points()));
                tvCO2.setText(String.format(Locale.getDefault(), "%.1f kg", usuario.getTotal_co2_poupado()));
                progressLevel.setProgress(usuario.getEco_points() % 1000 / 10, true);
            }
        });
    }
}
