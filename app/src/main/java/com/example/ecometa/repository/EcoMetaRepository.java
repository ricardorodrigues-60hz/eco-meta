package com.example.ecometa.repository;

import android.util.Log;
import androidx.annotation.NonNull;
import com.example.ecometa.model.Atividade;
import com.example.ecometa.model.Usuario;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * Repository
 * Gerencia a comunicação com o Firebase Firestore usando Listeners padrão.
 */
public class EcoMetaRepository {
    private static final String TAG = "EcoMetaRepository";
    private final FirebaseFirestore db;

    private static final String COLECAO_USUARIOS = "usuarios";
    private static final String COLECAO_ATIVIDADES = "atividades";

    private static final double FATOR_CARRO = 0.120;
    private static final double FATOR_BICICLETA = 0.0;
    private static final double FATOR_CAMINHADA = 0.0;
    private static final double FATOR_ONIBUS = 0.030;
    private static final double FATOR_METRO = 0.040;

    public interface RepositoryCallback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }

    public EcoMetaRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void buscarUsuario(@NonNull String userId, @NonNull RepositoryCallback<Usuario> callback) {
        db.collection(COLECAO_USUARIOS).document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        callback.onSuccess(documentSnapshot.toObject(Usuario.class));
                    } else {
                        callback.onError(new Exception("Usuário não encontrado"));
                    }
                })
                .addOnFailureListener(callback::onError);
    }

    public void registrarAtividade(@NonNull Atividade atividade, @NonNull RepositoryCallback<Void> callback) {
        double co2Evitado = calcularCO2Evitado(atividade.getTipo_transporte(), atividade.getDistancia_km());
        atividade.setCo2_evitado(co2Evitado);

        db.collection(COLECAO_ATIVIDADES).add(atividade)
                .addOnSuccessListener(documentReference -> {
                    documentReference.update("id_atividade", documentReference.getId());
                    callback.onSuccess(null);
                })
                .addOnFailureListener(callback::onError);
    }

    public void listarAtividades(@NonNull String userId, @NonNull RepositoryCallback<java.util.List<Atividade>> callback) {
        db.collection(COLECAO_ATIVIDADES)
                .whereEqualTo("user_id", userId)
                .orderBy("data", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    callback.onSuccess(queryDocumentSnapshots.toObjects(Atividade.class));
                })
                .addOnFailureListener(callback::onError);
    }

    private double calcularCO2Evitado(String tipo, double distancia) {
        double fatorEscolhido;
        switch (tipo.toLowerCase()) {
            case "bicicleta": fatorEscolhido = FATOR_BICICLETA; break;
            case "caminhada": fatorEscolhido = FATOR_CAMINHADA; break;
            case "ônibus": case "onibus": fatorEscolhido = FATOR_ONIBUS; break;
            case "metrô": case "metro": fatorEscolhido = FATOR_METRO; break;
            default: fatorEscolhido = FATOR_CARRO; break;
        }
        return distancia * (FATOR_CARRO - fatorEscolhido);
    }
}
