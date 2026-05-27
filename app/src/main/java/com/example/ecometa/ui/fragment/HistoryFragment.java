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
import com.example.ecometa.adapter.AtividadeAdapter; // 💡 Ajustado para o nome da sua classe de Adapter
import com.example.ecometa.viewmodel.EcoMetaViewModel;
import com.example.ecometa.viewmodel.ViewModelFactory;

public class HistoryFragment extends Fragment {

    private EcoMetaViewModel viewModel;
    private RecyclerView rvHistory;
    private AutenticacaoRepository repository; //

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        rvHistory = view.findViewById(R.id.rvHistory);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));


        repository = new AutenticacaoRepository();

        ViewModelFactory factory = new ViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(EcoMetaViewModel.class);

        setupObservers();


        String userIdReal = repository.obterIdUsuarioAtual();
        if (userIdReal != null) {
            viewModel.carregarAtividades(userIdReal);
        } else {
            Toast.makeText(getContext(), "Erro: Usuário não autenticado.", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void setupObservers() {
        viewModel.atividades.observe(getViewLifecycleOwner(), atividades -> {
            if (atividades != null) {

                rvHistory.setAdapter(new AtividadeAdapter(atividades));
            }
        });
    }
}