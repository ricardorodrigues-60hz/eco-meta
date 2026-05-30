package com.example.ecometa.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.ecometa.model.Atividade;
import com.example.ecometa.model.ConquistaUsuario;
import com.example.ecometa.model.Desafio;
import com.example.ecometa.model.DesafioStatus;
import com.example.ecometa.model.Estatisticas;
import com.example.ecometa.model.Usuario;
import com.example.ecometa.repository.AutenticacaoRepository;
import com.example.ecometa.repository.EcoMetaRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ViewModel principal do EcoMeta.
 * Centraliza a lógica de observação de dados para as telas Home, Desafios, Ranking e Histórico.
 */
public class EcoMetaViewModel extends ViewModel {
    private final AutenticacaoRepository authRepository;
    private final EcoMetaRepository dataRepository;

    private final MutableLiveData<Usuario> _usuario = new MutableLiveData<>();
    public final LiveData<Usuario> usuario = _usuario;

    private final MutableLiveData<List<Atividade>> _atividades = new MutableLiveData<>();
    public final LiveData<List<Atividade>> atividades = _atividades;

    private final MutableLiveData<String> _erro = new MutableLiveData<>();
    public final LiveData<String> erro = _erro;

    private final MutableLiveData<Boolean> _sucessoOperacao = new MutableLiveData<>();
    public final LiveData<Boolean> sucessoOperacao = _sucessoOperacao;

    private final MutableLiveData<List<Usuario>> _ranking = new MutableLiveData<>();
    public final LiveData<List<Usuario>> ranking = _ranking;

    private final MutableLiveData<List<DesafioStatus>> _desafiosStatus = new MutableLiveData<>();
    public final LiveData<List<DesafioStatus>> desafiosStatus = _desafiosStatus;

    public EcoMetaViewModel(AutenticacaoRepository authRepository, EcoMetaRepository dataRepository) {
        this.authRepository = authRepository;
        this.dataRepository = dataRepository;
    }

    public void carregarDadosUsuario(String userId) {
        dataRepository.buscarUsuario(userId, new EcoMetaRepository.RepositoryCallback<Usuario>() {
            @Override public void onSuccess(Usuario result) { _usuario.setValue(result); }
            @Override public void onError(Exception e) { _erro.setValue(e.getMessage()); }
        });
    }

    public void carregarAtividades(String userId) {
        dataRepository.listarAtividades(userId, new EcoMetaRepository.RepositoryCallback<List<Atividade>>() {
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

        dataRepository.registrarAtividade(nova, new EcoMetaRepository.RepositoryCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                _sucessoOperacao.setValue(true);
                carregarDadosUsuario(userId);
                carregarAtividades(userId);
                verificarConclusaoDesafios(userId);
            }
            @Override public void onError(Exception e) { _erro.setValue(e.getMessage()); }
        });
    }

    public void verificarConclusaoDesafios(String userId) {
        dataRepository.obterEstatisticas(userId, new EcoMetaRepository.RepositoryCallback<Estatisticas>() {
            @Override
            public void onSuccess(Estatisticas stats) {
                dataRepository.obterTodosDesafios(new EcoMetaRepository.RepositoryCallback<List<Desafio>>() {
                    @Override
                    public void onSuccess(List<Desafio> desafios) {
                        dataRepository.obterConquistasDoUsuario(userId, new EcoMetaRepository.RepositoryCallback<List<ConquistaUsuario>>() {
                            @Override
                            public void onSuccess(List<ConquistaUsuario> conquistas) {
                                Set<String> concluidos = new HashSet<>();
                                for (ConquistaUsuario c : conquistas) concluidos.add(c.getId_desafio());

                                for (Desafio d : desafios) {
                                    if (concluidos.contains(d.getId_desafio())) continue;

                                    double totalKm = getDistanciaPorTipo(stats, d.getTipo_transporte());
                                    if (totalKm >= d.getMeta_km()) {
                                        dataRepository.concederPremioDesafio(userId, d, new EcoMetaRepository.RepositoryCallback<Void>() {
                                            @Override public void onSuccess(Void r) { carregarDadosUsuario(userId); carregarDesafiosEConquistas(userId); }
                                            @Override public void onError(Exception e) { _erro.setValue(e.getMessage()); }
                                        });
                                    }
                                }
                            }
                            @Override public void onError(Exception e) { _erro.setValue(e.getMessage()); }
                        });
                    }
                    @Override public void onError(Exception e) { _erro.setValue(e.getMessage()); }
                });
            }
            @Override public void onError(Exception e) { _erro.setValue(e.getMessage()); }
        });
    }

    private double getDistanciaPorTipo(Estatisticas stats, String tipo) {
        if (tipo == null) return 0;
        switch (tipo.toLowerCase().trim()) {
            case "caminhada": return stats.getTotal_caminhada_km();
            case "bicicleta": return stats.getTotal_bicicleta_km();
            case "ônibus":
            case "onibus": return stats.getTotal_onibus_km();
            case "metrô":
            case "metro": return stats.getTotal_metro_km();
            default: return 0;
        }
    }

    public void carregarRanking() {
        dataRepository.obterRankingUsuarios(new EcoMetaRepository.RepositoryCallback<List<Usuario>>() {
            @Override
            public void onSuccess(List<Usuario> result) { _ranking.setValue(result); }
            @Override
            public void onError(Exception e) { _erro.setValue(e.getMessage()); }
        });
    }

    public void carregarDesafiosEConquistas(String userId) {
        dataRepository.obterTodosDesafios(new EcoMetaRepository.RepositoryCallback<List<Desafio>>() {
            @Override
            public void onSuccess(List<Desafio> listaDesafios) {
                dataRepository.obterEstatisticas(userId, new EcoMetaRepository.RepositoryCallback<Estatisticas>() {
                    @Override
                    public void onSuccess(Estatisticas stats) {
                        dataRepository.obterConquistasDoUsuario(userId, new EcoMetaRepository.RepositoryCallback<List<ConquistaUsuario>>() {
                            @Override
                            public void onSuccess(List<ConquistaUsuario> listaConquistas) {
                                List<DesafioStatus> listaFinal = new ArrayList<>();
                                Set<String> idsConquistados = new HashSet<>();
                                for (ConquistaUsuario c : listaConquistas) idsConquistados.add(c.getId_desafio());

                                for (Desafio d : listaDesafios) {
                                    boolean foiConquistado = idsConquistados.contains(d.getId_desafio());
                                    double kmAcumulado = getDistanciaPorTipo(stats, d.getTipo_transporte());
                                    listaFinal.add(new DesafioStatus(d, kmAcumulado, foiConquistado));
                                }
                                _desafiosStatus.setValue(listaFinal);
                            }
                            @Override public void onError(Exception e) { _erro.setValue(e.getMessage()); }
                        });
                    }
                    @Override public void onError(Exception e) { _erro.setValue(e.getMessage()); }
                });
            }
            @Override public void onError(Exception e) { _erro.setValue(e.getMessage()); }
        });
    }
}
