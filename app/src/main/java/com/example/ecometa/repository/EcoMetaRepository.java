package com.example.ecometa.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ecometa.model.Atividade;
import com.example.ecometa.model.Conquista;
import com.example.ecometa.model.Desafio;
import com.example.ecometa.model.Usuario;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repositório responsável pela comunicação com o Firebase Firestore.
 * Implementa o padrão Repository para isolar a lógica de dados da ViewModel.
 */
public class EcoMetaRepository {
    private static final String TAG = "EcoMetaRepository";
    private final FirebaseFirestore db;

    // Constantes para Coleções
    private static final String COLECAO_USUARIOS = "usuarios";
    private static final String COLECAO_ATIVIDADES = "atividades";
    private static final String COLECAO_DESAFIOS = "desafios";
    private static final String COLECAO_CONQUISTAS = "conquistas_usuario";

    // Fatores de Emissão (kg CO2 por km)
    private static final double FATOR_CARRO = 0.120;
    private static final double FATOR_BICICLETA = 0.0;
    private static final double FATOR_CAMINHADA = 0.0;
    private static final double FATOR_ONIBUS = 0.030;
    private static final double FATOR_METRO = 0.040;

    /**
     * Interface de callback para operações assíncronas.
     * @param <T> Tipo de dado retornado em caso de sucesso.
     */
    public interface RepositoryCallback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }

    public EcoMetaRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    /**
     * Salva ou atualiza um usuário no Firestore.
     *
     * @param usuario  Objeto Usuario a ser salvo.
     * @param callback Callback para notificar o resultado.
     */
    public void salvarUsuario(@NonNull Usuario usuario, @NonNull RepositoryCallback<Void> callback) {
        try {
            db.collection(COLECAO_USUARIOS)
                    .document(usuario.getId_user())
                    .set(usuario)
                    .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Erro ao salvar usuário", e);
                        callback.onError(e);
                    });
        } catch (Exception e) {
            Log.e(TAG, "Exceção inesperada ao salvar usuário", e);
            callback.onError(e);
        }
    }

    /**
     * Busca os dados de um usuário pelo ID.
     *
     * @param userId   ID do usuário.
     * @param callback Callback para notificar o resultado.
     */
    public void buscarUsuario(@NonNull String userId, @NonNull RepositoryCallback<Usuario> callback) {
        try {
            db.collection(COLECAO_USUARIOS)
                    .document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Usuario usuario = documentSnapshot.toObject(Usuario.class);
                            callback.onSuccess(usuario);
                        } else {
                            callback.onError(new Exception("Usuário não encontrado"));
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Erro ao buscar usuário", e);
                        callback.onError(e);
                    });
        } catch (Exception e) {
            Log.e(TAG, "Exceção inesperada ao buscar usuário", e);
            callback.onError(e);
        }
    }

    /**
     * Registra uma nova atividade de transporte limpo.
     * Calcula automaticamente o CO2 evitado antes de salvar.
     *
     * @param atividade Objeto Atividade (sem co2_evitado preenchido).
     * @param callback  Callback para notificar o resultado.
     */
    public void registrarAtividade(@NonNull Atividade atividade, @NonNull RepositoryCallback<Void> callback) {
        try {
            // Lógica Core: Cálculo do CO2 Evitado (Isolamento de Regra de Negócio)
            double co2Evitado = calcularCO2Evitado(atividade.getTipo_transporte(), atividade.getDistancia_km());
            atividade.setCo2_evitado(co2Evitado);

            db.collection(COLECAO_ATIVIDADES)
                    .add(atividade)
                    .addOnSuccessListener(documentReference -> {
                        atividade.setId_atividade(documentReference.getId());
                        documentReference.update("id_atividade", documentReference.getId());
                        callback.onSuccess(null);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Erro ao registrar atividade", e);
                        callback.onError(e);
                    });
        } catch (Exception e) {
            Log.e(TAG, "Exceção inesperada ao registrar atividade", e);
            callback.onError(e);
        }
    }

    /**
     * Busca os desafios ativos.
     */
    public void buscarDesafios(@NonNull RepositoryCallback<List<Desafio>> callback) {
        db.collection(COLECAO_DESAFIOS)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Desafio> desafios = queryDocumentSnapshots.toObjects(Desafio.class);
                    callback.onSuccess(desafios);
                })
                .addOnFailureListener(callback::onError);
    }

    /**
     * Busca o ranking de usuários por eco_points.
     */
    public void buscarRanking(@NonNull RepositoryCallback<List<Usuario>> callback) {
        db.collection(COLECAO_USUARIOS)
                .orderBy("eco_points", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Usuario> ranking = queryDocumentSnapshots.toObjects(Usuario.class);
                    callback.onSuccess(ranking);
                })
                .addOnFailureListener(callback::onError);
    }

    /**
     * Busca as conquistas (insígnias) de um usuário.
     */
    public void buscarConquistas(@NonNull String userId, @NonNull RepositoryCallback<List<Conquista>> callback) {
        db.collection(COLECAO_CONQUISTAS)
                .whereEqualTo("user_id", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Conquista> conquistas = queryDocumentSnapshots.toObjects(Conquista.class);
                    callback.onSuccess(conquistas);
                })
                .addOnFailureListener(callback::onError);
    }

    /**
     * Calcula o CO2 evitado baseado na distância e tipo de transporte.
     * Fórmula: E = d * (f_carro - f_escolhido)
     *
     * @param tipo      Tipo de transporte escolhido.
     * @param distancia Distância em KM.
     * @return CO2 evitado em kg.
     */
    private double calcularCO2Evitado(String tipo, double distancia) {
        double fatorEscolhido;

        switch (tipo.toLowerCase()) {
            case "bicicleta":
                fatorEscolhido = FATOR_BICICLETA;
                break;
            case "caminhada":
                fatorEscolhido = FATOR_CAMINHADA;
                break;
            case "ônibus":
            case "onibus":
                fatorEscolhido = FATOR_ONIBUS;
                break;
            case "metrô":
            case "metro":
                fatorEscolhido = FATOR_METRO;
                break;
            default:
                fatorEscolhido = FATOR_CARRO; // Se for carro, a economia é zero
                break;
        }

        return distancia * (FATOR_CARRO - fatorEscolhido);
    }

    public void registrarNovaAtividade() {
        // 1. Criando o objeto com os dados (mapeamento chave-valor)
        Map<String, Object> atividade = new HashMap<>();
        atividade.put("user_id", "user123"); // Simulação de um usuário logado
        atividade.put("tipo_transporte", "Bicicleta");
        atividade.put("distancia_km", 5.2);
        atividade.put("co2_evitado", 0.624); // 5.2 * 0.120

        // 2. Injetando na coleção 'atividades'
        db.collection("atividades")
                .add(atividade)
                .addOnSuccessListener(documentReference -> {
                    // Sucesso! O Firestore gerou um ID automático para o documento
                    System.out.println("Atividade salva com ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    // Falha! Captura de exceção para evitar crash no app
                    System.err.println("Erro ao salvar atividade: " + e.getMessage());
                });
    }

}
