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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecometa.R;
import com.example.ecometa.repository.AutenticacaoRepository;
import com.example.ecometa.repository.EcoMetaRepository;
import com.example.ecometa.ui.adapter.ChallengesAdapter;
import com.example.ecometa.viewmodel.EcoMetaViewModel;
import com.example.ecometa.viewmodel.ViewModelFactory;
import java.util.ArrayList;

/**
 * Fragment de Desafios.
 */
public class ChallengesFragment extends Fragment {

    private EcoMetaViewModel viewModel;
    private RecyclerView rvChallenges;
    private ChallengesAdapter adapter;
    private TextView tvTotalChallenges, tvCompletedChallenges;
    private AutenticacaoRepository authRepository;
    private EcoMetaRepository dataRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenges, container, false);

        rvChallenges = view.findViewById(R.id.rvChallenges);
        rvChallenges.setLayoutManager(new LinearLayoutManager(getContext()));
        
        tvTotalChallenges = view.findViewById(R.id.tvTotalChallenges);
        tvCompletedChallenges = view.findViewById(R.id.tvCompletedChallenges);

        adapter = new ChallengesAdapter(new ArrayList<>());
        rvChallenges.setAdapter(adapter);

        authRepository = new AutenticacaoRepository();
        dataRepository = new EcoMetaRepository();
        ViewModelFactory factory = new ViewModelFactory(authRepository, dataRepository);
        viewModel = new ViewModelProvider(this, factory).get(EcoMetaViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupObservers();

        String userIdReal = authRepository.obterIdUsuarioAtual();
        if (userIdReal != null) {
            viewModel.carregarDesafiosEConquistas(userIdReal);
        } else {
            Toast.makeText(getContext(), "Erro: Usuário não autenticado.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupObservers() {
        viewModel.desafiosStatus.observe(getViewLifecycleOwner(), desafiosStatusList -> {
            if (desafiosStatusList != null) {
                adapter.setListaDesafios(desafiosStatusList);

                int total = desafiosStatusList.size();
                int concluidos = 0;
                for (com.example.ecometa.model.DesafioStatus ds : desafiosStatusList) {
                    if (ds.isConcluido()) concluidos++;
                }

                if (tvTotalChallenges != null) tvTotalChallenges.setText(String.valueOf(total));
                if (tvCompletedChallenges != null) tvCompletedChallenges.setText(String.valueOf(concluidos));
            }
        });

        viewModel.erro.observe(getViewLifecycleOwner(), msgErro -> {
            if (msgErro != null) {
                Toast.makeText(getContext(), msgErro, Toast.LENGTH_LONG).show();
            }
        });
    }
}
