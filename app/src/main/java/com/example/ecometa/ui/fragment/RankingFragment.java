package com.example.ecometa.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecometa.R;
import com.example.ecometa.repository.AutenticacaoRepository;
import com.example.ecometa.ui.adapter.RankingAdapter;
import com.example.ecometa.viewmodel.EcoMetaViewModel;
import com.example.ecometa.viewmodel.ViewModelFactory;

public class RankingFragment extends Fragment {

    private EcoMetaViewModel viewModel;
    private RecyclerView rvRanking;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);

        rvRanking = view.findViewById(R.id.rvRanking);
        rvRanking.setLayoutManager(new LinearLayoutManager(getContext()));

        ViewModelFactory factory = new ViewModelFactory(new AutenticacaoRepository());
        viewModel = new ViewModelProvider(this, factory).get(EcoMetaViewModel.class);

        setupObservers();

        // Dispara a busca dos dados do ranking global
        viewModel.carregarRanking();

        return view;
    }

    private void setupObservers() {
        viewModel.ranking.observe(getViewLifecycleOwner(), usuarios -> {
            if (usuarios != null) {
                rvRanking.setAdapter(new RankingAdapter(usuarios));
            }
        });
    }
}