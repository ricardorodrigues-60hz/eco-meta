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
import com.example.ecometa.ui.adapter.AtividadeAdapter;
import com.example.ecometa.viewmodel.EcoMetaViewModel;
import com.example.ecometa.viewmodel.ViewModelFactory;

public class HistoryFragment extends Fragment {

    private EcoMetaViewModel viewModel;
    private RecyclerView rvHistory;
    private TextView tvTotalTrajetos, tvSummaryDistancia, tvSummaryCO2;
    private AutenticacaoRepository authRepository;
    private EcoMetaRepository dataRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        rvHistory = view.findViewById(R.id.rvHistory);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        tvTotalTrajetos = view.findViewById(R.id.tvTotalTrajetos);
        tvSummaryDistancia = view.findViewById(R.id.tvSummaryDistancia);
        tvSummaryCO2 = view.findViewById(R.id.tvSummaryCO2);

        authRepository = new AutenticacaoRepository();
        dataRepository = new EcoMetaRepository();

        ViewModelFactory factory = new ViewModelFactory(authRepository, dataRepository);
        viewModel = new ViewModelProvider(this, factory).get(EcoMetaViewModel.class);

        setupObservers();

        String userIdReal = authRepository.obterIdUsuarioAtual();
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

                int totalTrajetos = atividades.size();
                double kmTotal = 0;
                double co2Total = 0;

                for (com.example.ecometa.model.Atividade a : atividades) {
                    kmTotal += a.getDistancia_km();
                    co2Total += a.getCo2_evitado();
                }

                tvTotalTrajetos.setText(String.valueOf(totalTrajetos));
                tvSummaryDistancia.setText(String.format(java.util.Locale.getDefault(), "%.1f", kmTotal));
                tvSummaryCO2.setText(String.format(java.util.Locale.getDefault(), "%.1f", co2Total));
            }
        });

        viewModel.erro.observe(getViewLifecycleOwner(), mensagemErro -> {
            if (mensagemErro != null) {
                Toast.makeText(getContext(), "Erro no banco: " + mensagemErro, Toast.LENGTH_LONG).show();
            }
        });
    }
}
