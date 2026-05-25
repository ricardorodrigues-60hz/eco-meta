package com.example.ecometa.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecometa.model.Atividade;
import com.example.ecometa.model.Usuario;
import com.example.ecometa.repository.EcoMetaRepository;

import java.util.Date;
import com.google.firebase.Timestamp;

/**
 * ViewModel responsável pela lógica da tela principal (Home).
 * Expõe dados do usuário e permite o registro de novas atividades de sustentabilidade.
 */
public class HomeViewModel extends ViewModel {
    private final EcoMetaRepository repository;

    private final MutableLiveData<Usuario> _usuario = new MutableLiveData<>();
    public final LiveData<Usuario> usuario = _usuario;

    private final MutableLiveData<String> _mensagemErro = new MutableLiveData<>();
    public final LiveData<String> mensagemErro = _mensagemErro;

    private final MutableLiveData<Boolean> _operacaoSucesso = new MutableLiveData<>();
    public final LiveData<Boolean> operacaoSucesso = _operacaoSucesso;

    /**
     * Construtor da ViewModel.
     * @param repository Instância do repositório (Injeção de dependência manual).
     */
    public HomeViewModel(EcoMetaRepository repository) {
        this.repository = repository;
    }

    /**
     * Carrega os dados do usuário atual do Firestore.
     * @param userId ID do usuário logado.
     */
    public void carregarDadosUsuario(String userId) {
        repository.buscarUsuario(userId, new EcoMetaRepository.RepositoryCallback<Usuario>() {
            @Override
            public void onSuccess(Usuario result) {
                _usuario.setValue(result);
            }

            @Override
            public void onError(Exception e) {
                _mensagemErro.setValue("Erro ao carregar perfil: " + e.getMessage());
            }
        });
    }

    /**
     * Registra uma nova atividade de deslocamento sustentável.
     *
     * @param userId         ID do usuário.
     * @param tipoTransporte Tipo escolhido (Bicicleta, Caminhada, Ônibus, Metrô).
     * @param distanciaKm    Distância percorrida em quilômetros.
     */
    public void registrarNovaAtividade(String userId, String tipoTransporte, double distanciaKm) {
        Atividade novaAtividade = new Atividade();
        novaAtividade.setUser_id(userId);
        novaAtividade.setTipo_transporte(tipoTransporte);
        novaAtividade.setDistancia_km(distanciaKm);
        novaAtividade.setData(new Timestamp(new Date()));

        repository.registrarAtividade(novaAtividade, new EcoMetaRepository.RepositoryCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                _operacaoSucesso.setValue(true);
                // Após registrar, recarregamos os dados do usuário para atualizar os pontos na UI
                carregarDadosUsuario(userId);
            }

            @Override
            public void onError(Exception e) {
                _mensagemErro.setValue("Erro ao registrar atividade: " + e.getMessage());
                _operacaoSucesso.setValue(false);
            }
        });
    }

    /**
     * Limpa o estado de sucesso da operação.
     */
    public void resetarStatusOperacao() {
        _operacaoSucesso.setValue(null);
    }
}
