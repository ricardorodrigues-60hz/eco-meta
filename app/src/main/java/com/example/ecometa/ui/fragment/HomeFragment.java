package com.example.ecometa.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
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
    private AutenticacaoRepository repository; //  Criado para gerenciar a sessão ativa do Firebase

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvNivel = view.findViewById(R.id.tvNivelUsuario);
        tvEcoPoints = view.findViewById(R.id.tvEcoPoints);
        tvCO2 = view.findViewById(R.id.tvCO2);
        progressLevel = view.findViewById(R.id.progressLevel);

        view.findViewById(R.id.btnRegisterActivity).setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.registrarAtividadeFragment);
        });

        // Inicializa o repositório
        repository = new AutenticacaoRepository();

        ViewModelFactory factory = new ViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(EcoMetaViewModel.class);

        setupObservers();

        // Busca dinamicamente o UID do usuário que está logado no app
        String userIdReal = repository.obterIdUsuarioAtual();
        if (userIdReal != null) {
            viewModel.carregarDadosUsuario(userIdReal);
        } else {
            Toast.makeText(getContext(), "Erro: Usuário não autenticado.", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void setupObservers() {
        viewModel.usuario.observe(getViewLifecycleOwner(), usuario -> {
            if (usuario != null) {
                tvNivel.setText(usuario.getNivel());
                tvEcoPoints.setText(String.valueOf(usuario.getEco_points()));
                tvCO2.setText(String.format(Locale.getDefault(), "%.1f kg", usuario.getTotal_co2_poupado()));

                // Mantém a sua lógica de cálculo de progresso para a barra baseada nos pontos
                progressLevel.setProgress(usuario.getEco_points() % 1000 / 10, true);
            }
        });
    }
}