package com.example.ecometa.repository;

import androidx.annotation.NonNull;
import com.example.ecometa.model.Atividade;
import com.example.ecometa.model.ConquistaUsuario;
import com.example.ecometa.model.Desafio;
import com.example.ecometa.model.Estatisticas;
import com.example.ecometa.model.Usuario;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;
import java.util.List;

/**
 * Repositório Central de Dados do EcoMeta.
 * Gerencia todas as operações de leitura e escrita no Firestore relacionadas a atividades,
 * estatísticas, desafios e conquistas.
 */
public class EcoMetaRepository {
    private final FirebaseFirestore db;

    private static final String COLECAO_USUARIOS = "user";
    private static final String COLECAO_ATIVIDADES = "atividades";
    private static final String COLECAO_ESTATISTICAS = "estatisticas";
    private static final String COLECAO_DESAFIOS = "desafios";
    private static final String COLECAO_CONQUISTAS = "conquistas_usuario";

    private static final double FATOR_CARRO = 0.120;
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
                        callback.onError(new Exception("Usuário não encontrado no Firestore."));
                    }
                })
                .addOnFailureListener(callback::onError);
    }

    public void listarAtividades(@NonNull String userId, @NonNull RepositoryCallback<List<Atividade>> callback) {
        db.collection(COLECAO_ATIVIDADES)
                .whereEqualTo("user_id", userId)
                .orderBy("data", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    callback.onSuccess(queryDocumentSnapshots.toObjects(Atividade.class));
                })
                .addOnFailureListener(callback::onError);
    }

    public void registrarAtividade(@NonNull Atividade atividade, @NonNull RepositoryCallback<Void> callback) {
        double co2Evitado = calcularCO2Evitado(atividade.getTipo_transporte(), atividade.getDistancia_km());
        atividade.setCo2_evitado(co2Evitado);

        // Pontos ganhos: Proporcional ao CO2 evitado + bônus fixo
        int pontosGanhos = (int) (co2Evitado * 10) + 10;
        atividade.setPoints_earned(pontosGanhos);

        WriteBatch batch = db.batch();

        // 1. Salvar Atividade
        DocumentReference activityRef = db.collection(COLECAO_ATIVIDADES).document();
        atividade.setId_atividade(activityRef.getId());
        batch.set(activityRef, atividade);

        // 2. Incrementar Estatísticas
        String campoStats = getCampoEstatistica(atividade.getTipo_transporte());
        if (campoStats != null) {
            batch.update(db.collection(COLECAO_ESTATISTICAS).document(atividade.getUser_id()),
                    campoStats, FieldValue.increment(atividade.getDistancia_km()));
        }

        // 3. Atualizar Metricas do Usuário
        DocumentReference userRef = db.collection(COLECAO_USUARIOS).document(atividade.getUser_id());
        batch.update(userRef, "eco_points", FieldValue.increment(pontosGanhos));
        batch.update(userRef, "total_co2_poupado", FieldValue.increment(co2Evitado));

        batch.commit()
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(callback::onError);
    }

    private String getCampoEstatistica(String tipo) {
        if (tipo == null) return null;
        switch (tipo.toLowerCase().trim()) {
            case "caminhada": return "total_caminhada_km";
            case "bicicleta": return "total_bicicleta_km";
            case "ônibus":
            case "onibus": return "total_onibus_km";
            case "metrô":
            case "metro": return "total_metro_km";
            default: return null;
        }
    }

    private double calcularCO2Evitado(String tipo, double distancia) {
        double fatorEscolhido;
        if (tipo == null) return 0.0;

        switch (tipo.toLowerCase().trim()) {
            case "bicicleta":
            case "caminhada":
                fatorEscolhido = 0.0;
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
                fatorEscolhido = FATOR_CARRO;
                break;
        }
        return distancia * (FATOR_CARRO - fatorEscolhido);
    }

    public void obterRankingUsuarios(@NonNull RepositoryCallback<List<Usuario>> callback) {
        db.collection(COLECAO_USUARIOS)
                .orderBy("eco_points", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    callback.onSuccess(queryDocumentSnapshots.toObjects(Usuario.class));
                })
                .addOnFailureListener(callback::onError);
    }

    public void obterTodosDesafios(RepositoryCallback<List<Desafio>> callback) {
        db.collection(COLECAO_DESAFIOS)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> callback.onSuccess(queryDocumentSnapshots.toObjects(Desafio.class)))
                .addOnFailureListener(callback::onError);
    }

    public void obterConquistasDoUsuario(String userId, RepositoryCallback<List<ConquistaUsuario>> callback) {
        db.collection(COLECAO_CONQUISTAS)
                .whereEqualTo("user_id", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> callback.onSuccess(queryDocumentSnapshots.toObjects(ConquistaUsuario.class)))
                .addOnFailureListener(callback::onError);
    }

    public void obterEstatisticas(String userId, RepositoryCallback<Estatisticas> callback) {
        db.collection(COLECAO_ESTATISTICAS).document(userId).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) callback.onSuccess(doc.toObject(Estatisticas.class));
                    else callback.onError(new Exception("Estatísticas não encontradas para o usuário."));
                })
                .addOnFailureListener(callback::onError);
    }

    public void concederPremioDesafio(String userId, Desafio desafio, RepositoryCallback<Void> callback) {
        WriteBatch batch = db.batch();

        batch.update(db.collection(COLECAO_USUARIOS).document(userId),
                "eco_points", FieldValue.increment(desafio.getEco_points_recompensa()));

        DocumentReference conquestRef = db.collection(COLECAO_CONQUISTAS).document();
        ConquistaUsuario conquista = new ConquistaUsuario(conquestRef.getId(), userId, desafio.getId_desafio(), com.google.firebase.Timestamp.now());
        batch.set(conquestRef, conquista);

        batch.commit()
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(callback::onError);
    }
}
