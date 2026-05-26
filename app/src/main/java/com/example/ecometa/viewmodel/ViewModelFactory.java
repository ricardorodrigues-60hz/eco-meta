package com.example.ecometa.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.ecometa.repository.AutenticacaoRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final AutenticacaoRepository repository;

    public ViewModelFactory(AutenticacaoRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EcoMetaViewModel.class)) {
            return (T) new EcoMetaViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
