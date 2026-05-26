package com.example.ecometa.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.ecometa.repository.EcoMetaRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final EcoMetaRepository repository;

    public ViewModelFactory(EcoMetaRepository repository) {
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
