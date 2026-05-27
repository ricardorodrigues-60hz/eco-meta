package com.example.ecometa.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.ecometa.model.Atividade;
import com.example.ecometa.model.Usuario;
import com.example.ecometa.repository.AutenticacaoRepository;

import java.util.List;

/**
 * Camada: ViewModel
 * Gerencia o estado da UI para as telas do EcoMeta.
 */
public class EcoMetaViewModel extends ViewModel {
    private final AutenticacaoRepository repository;

    private final MutableLiveData<Usuario> _usuario = new MutableLiveData<>();
    public final LiveData<Usuario> usuario = _usuario;

    private final MutableLiveData<List<Atividade>> _atividades = new MutableLiveData<>();
    public final LiveData<List<Atividade>> atividades = _atividades;

    private final MutableLiveData<String> _erro = new MutableLiveData<>();
    public final LiveData<String> erro = _erro;

    public EcoMetaViewModel(AutenticacaoRepository repository) {
        this.repository = repository;
    }

    public void carregarDadosUsuario(String userId) {
        repository.buscarUsuario(userId, new AutenticacaoRepository.RepositoryCallback<Usuario>() {
            @Override public void onSuccess(Usuario result) { _usuario.setValue(result); }
            @Override public void onError(Exception e) { _erro.setValue(e.getMessage()); }
        });
    }

    public void carregarAtividades(String userId) {
        repository.listarAtividades(userId, new AutenticacaoRepository.RepositoryCallback<List<Atividade>>() {
            @Override public void onSuccess(List<Atividade> result) { _atividades.setValue(result); }
            @Override public void onError(Exception e) { _erro.setValue(e.getMessage()); }
        });
    }

    public void registrarNovaAtividade(String userId, String tipo, double distancia) {
        Atividade nova = new Atividade();
        nova.setUser_id(userId);
        nova.setTipo_transporte(tipo);
        nova.setDistancia_km(distancia);
        nova.setData(com.google.firebase.Timestamp.now());

        repository.registrarAtividade(nova, new AutenticacaoRepository.RepositoryCallback<Void>() {
            @Override public void onSuccess(Void result) { carregarDadosUsuario(userId); carregarAtividades(userId); }
            @Override public void onError(Exception e) { _erro.setValue(e.getMessage()); }
        });
    }
    private final MutableLiveData<List<Usuario>> _ranking = new MutableLiveData<>();
    public final LiveData<List<Usuario>> ranking = _ranking;

    public void carregarRanking() {
        repository.obterRankingUsuarios(new AutenticacaoRepository.RepositoryCallback<List<Usuario>>() {
            @Override
            public void onSuccess(List<Usuario> result) { _ranking.setValue(result); }
            @Override
            public void onError(Exception e) { _erro.setValue(e.getMessage()); }
        });
    }

    private final MutableLiveData<List<DesafioStatus>> _desafiosStatus = new MutableLiveData<>();
    public final LiveData<List<DesafioStatus>> desafiosStatus = _desafiosStatus;

    public void carregarDesafiosEConquistas(String userId) {
        repository.obterTodosDesafios(new AutenticacaoRepository.RepositoryCallback<List<Desafio>>() {
            @Override
            public void onSuccess(List<Desafio> listaDesafios) {

                repository.obterConquistasDoUsuario(userId, new AutenticacaoRepository.RepositoryCallback<List<ConquistaUsuario>>() {
                    @Override
                    public void onSuccess(List<ConquistaUsuario> listaConquistas) {
                        java.util.List<DesafioStatus> listaFinal = new java.util.ArrayList<>();

                        // Cria um set com os IDs dos desafios já conquistados para busca rápida
                        java.util.Set<String> idsConquistados = new java.util.HashSet<>();
                        for (ConquistaUsuario c : listaConquistas) {
                            idsConquistados.add(c.getId_desafio());
                        }

                        // Cruza os dados
                        for (Desafio d : listaDesafios) {
                            boolean foiConquistado = idsConquistados.contains(d.getId_desafio());
                            listaFinal.add(new DesafioStatus(d, foiConquistado));
                        }

                        _desafiosStatus.setValue(listaFinal);
                    }

                    @Override
                    public void onError(Exception e) { _erro.setValue(e.getMessage()); }
                });
            }

            @Override
            public void onError(Exception e) { _erro.setValue(e.getMessage()); }
        });
    }
}
