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
import com.example.ecometa.repository.EcoMetaRepository;
import com.example.ecometa.ui.adapter.ActivityAdapter;
import com.example.ecometa.viewmodel.EcoMetaViewModel;
import com.example.ecometa.viewmodel.ViewModelFactory;

public class HistoryFragment extends Fragment {

    private EcoMetaViewModel viewModel;
    private RecyclerView rvHistory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        rvHistory = view.findViewById(R.id.rvHistory);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        ViewModelFactory factory = new ViewModelFactory(new EcoMetaRepository());
        viewModel = new ViewModelProvider(this, factory).get(EcoMetaViewModel.class);

        setupObservers();
        viewModel.carregarAtividades("user_123");

        return view;
    }

    private void setupObservers() {
        viewModel.atividades.observe(getViewLifecycleOwner(), atividades -> {
            if (atividades != null) {
                rvHistory.setAdapter(new ActivityAdapter(atividades));
            }
        });
    }
}
