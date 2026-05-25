package com.example.ecometa.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.ecometa.repository.EcoMetaRepository;

/**
 * Factory para criar instâncias da HomeViewModel com injeção de dependência do repositório.
 */
public class HomeViewModelFactory implements ViewModelProvider.Factory {
    private final EcoMetaRepository repository;

    public HomeViewModelFactory(EcoMetaRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
