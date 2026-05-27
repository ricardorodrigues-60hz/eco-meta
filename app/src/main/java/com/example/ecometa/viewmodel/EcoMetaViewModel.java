package com.example.ecometa.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.ecometa.model.Atividade;
import com.example.ecometa.model.ConquistaUsuario;
import com.example.ecometa.model.Desafio;
import com.example.ecometa.model.DesafioStatus;
import com.example.ecometa.model.Usuario;
import com.example.ecometa.repository.AutenticacaoRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        // Calculo do pontos
        int pontosPorKm;
        switch (tipo.toLowerCase()) {
            case "caminhada":
                pontosPorKm = 15;
                break;
            case "bicicleta":
                pontosPorKm = 10;
                break;
            case "metrô":
            case "ônibus":
                pontosPorKm = 5;
                break;
            default:
                pontosPorKm = 0;
                break;
        }

        // Calcula o total de pontos da atividade (arredondando para número inteiro)
        int pontosGanhos = (int) (distancia * pontosPorKm);
        nova.setPoints_earned(pontosGanhos);

        // Envia para o repositório salvar no banco com os pontos calculados
        repository.registrarAtividade(nova, new AutenticacaoRepository.RepositoryCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                carregarDadosUsuario(userId);
                carregarAtividades(userId);
            }
            @Override
            public void onError(Exception e) {
                _erro.setValue(e.getMessage());
            }
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
                        List<DesafioStatus> listaFinal = new ArrayList<>();

                        // Cria um set com os IDs dos desafios já conquistados para busca rápida
                        Set<String> idsConquistados = new HashSet<>();
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
