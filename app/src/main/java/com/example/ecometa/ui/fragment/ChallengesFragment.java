package com.example.ecometa.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecometa.R;
import com.example.ecometa.repository.AutenticacaoRepository;
import com.example.ecometa.ui.adapter.ChallengesAdapter;
import com.example.ecometa.viewmodel.EcoMetaViewModel;
import com.example.ecometa.viewmodel.ViewModelFactory;

public class ChallengesFragment extends Fragment {

    private EcoMetaViewModel viewModel;
    private RecyclerView rvChallenges;
    private AutenticacaoRepository repository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenges, container, false);

        rvChallenges = view.findViewById(R.id.rvChallenges);
        rvChallenges.setLayoutManager(new LinearLayoutManager(getContext()));

        repository = new AutenticacaoRepository();
        ViewModelFactory factory = new ViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(EcoMetaViewModel.class);

        setupObservers();

        String userIdReal = repository.obterIdUsuarioAtual();
        if (userIdReal != null) {
            viewModel.carregarDesafiosEConquistas(userIdReal);
        } else {
            Toast.makeText(getContext(), "Erro: Usuário não autenticado.", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void setupObservers() {
        viewModel.desafiosStatus.observe(getViewLifecycleOwner(), desafiosStatusList -> {
            if (desafiosStatusList != null) {
                rvChallenges.setAdapter(new ChallengesAdapter(desafiosStatusList));
            }
        });
    }
}