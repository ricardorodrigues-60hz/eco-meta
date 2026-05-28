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
import com.example.ecometa.ui.adapter.AtividadeAdapter;
import com.example.ecometa.viewmodel.EcoMetaViewModel;
import com.example.ecometa.viewmodel.ViewModelFactory;

public class HistoryFragment extends Fragment {

    private EcoMetaViewModel viewModel;
    private RecyclerView rvHistory;
    private TextView tvTotalTrajetos, tvSummaryDistancia, tvSummaryCO2;
    private AutenticacaoRepository repository; //

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        rvHistory = view.findViewById(R.id.rvHistory);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        tvTotalTrajetos = view.findViewById(R.id.tvTotalTrajetos);
        tvSummaryDistancia = view.findViewById(R.id.tvSummaryDistancia);
        tvSummaryCO2 = view.findViewById(R.id.tvSummaryCO2);


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
                android.util.Log.d("ECO_META_TESTE", "Total de atividades trazidas do banco: " + atividades.size());
                rvHistory.setAdapter(new AtividadeAdapter(atividades));

                // Cálculo do Resumo Dinâmico
                int totalTrajetos = atividades.size();
                double kmTotal = 0;
                double co2Total = 0;

                for (com.example.ecometa.model.Atividade a : atividades) {
                    kmTotal += a.getDistancia_km();
                    co2Total += a.getCo2_evitado();
                }

                // Atualização da UI do Cabeçalho
                tvTotalTrajetos.setText(String.valueOf(totalTrajetos));
                tvSummaryDistancia.setText(String.format(java.util.Locale.getDefault(), "%.1f", kmTotal));
                tvSummaryCO2.setText(String.format(java.util.Locale.getDefault(), "%.1f", co2Total));
            }
        });

        // Adicione a observação de erro para ver se o Firebase está rejeitando algo
        viewModel.erro.observe(getViewLifecycleOwner(), mensagemErro -> {
            if (mensagemErro != null) {
                android.util.Log.e("ECO_META_ERRO", "Erro na ViewModel: " + mensagemErro);
                Toast.makeText(getContext(), "Erro no banco: " + mensagemErro, Toast.LENGTH_LONG).show();
            }
        });
    }

}