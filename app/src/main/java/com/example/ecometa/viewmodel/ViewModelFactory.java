package com.example.ecometa.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.ecometa.repository.AutenticacaoRepository;
import com.example.ecometa.repository.EcoMetaRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final AutenticacaoRepository authRepository;
    private final EcoMetaRepository dataRepository;

    public ViewModelFactory(AutenticacaoRepository authRepository, EcoMetaRepository dataRepository) {
        this.authRepository = authRepository;
        this.dataRepository = dataRepository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EcoMetaViewModel.class)) {
            return (T) new EcoMetaViewModel(authRepository, dataRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
