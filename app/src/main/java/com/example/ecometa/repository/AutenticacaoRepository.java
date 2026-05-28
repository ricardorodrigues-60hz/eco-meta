package com.example.ecometa.repository;

import androidx.annotation.NonNull;
import com.example.ecometa.model.Atividade;
import com.example.ecometa.model.ConquistaUsuario;
import com.example.ecometa.model.Desafio;
import com.example.ecometa.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository Geral do Projeto EcoMeta
 * Gerencia centralizadamente a Autenticação (Firebase Auth) e a Persistência (Firestore).
 */
public class AutenticacaoRepository {
    private static final String TAG = "AutenticacaoRepository";

    private final FirebaseAuth mAuth;
    private final FirebaseFirestore db;

    // 1. ALTERADO AQUI: De "usuarios" para "user"
    private static final String COLECAO_USUARIOS = "user";
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

    public AutenticacaoRepository() {
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
    }

    //  Realiza o cadastro do usuário no Firebase Auth e sincroniza criando o perfil no Firestore.
    public void cadastrarUsuario(@NonNull String nome, @NonNull String email, @NonNull String senha, @NonNull RepositoryCallback<Void> callback) {
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String uid = firebaseUser.getUid();

                            Usuario novoUsuario = new Usuario(uid, nome, 0, 0.0);
                            novoUsuario.setEmail(email);

                            // Usa a constante dinâmica que agora vale "user"
                            db.collection(COLECAO_USUARIOS).document(uid).set(novoUsuario)
                                    .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                                    .addOnFailureListener(callback::onError);
                        } else {
                            callback.onError(new Exception("Usuário nulo após criação bem-sucedida."));
                        }
                    } else {
                        if (task.getException() != null) {
                            callback.onError(task.getException());
                        } else {
                            callback.onError(new Exception("Falha desconhecida ao cadastrar no Auth."));
                        }
                    }
                });
    }

    // Realiza a autenticação via E-mail e Senha no Firebase Auth.
    public void logarUsuario(@NonNull String email, @NonNull String senha, @NonNull RepositoryCallback<Void> callback) {
        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(null);
                    } else {
                        if (task.getException() != null) {
                            callback.onError(task.getException());
                        } else {
                            callback.onError(new Exception("Falha desconhecida ao realizar login."));
                        }
                    }
                });
    }

    // Retorna o ID único do usuário atualmente autenticado no aplicativo.
    public String obterIdUsuarioAtual() {
        FirebaseUser user = mAuth.getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    // Puxa os dados de perfil de um usuário específico do Firestore.
    public void buscarUsuario(@NonNull String userId, @NonNull RepositoryCallback<Usuario> callback) {
        db.collection(COLECAO_USUARIOS).document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        callback.onSuccess(documentSnapshot.toObject(Usuario.class));
                    } else {
                        callback.onError(new Exception("Usuário não encontrado no banco de dados."));
                    }
                })
                .addOnFailureListener(callback::onError);
    }


    public void registrarAtividade(@NonNull Atividade atividade, @NonNull RepositoryCallback<Void> callback) {
        double co2Evitado = calcularCO2Evitado(atividade.getTipo_transporte(), atividade.getDistancia_km());
        atividade.setCo2_evitado(co2Evitado);

        int pontosGanhos = (int) (co2Evitado * 10) + 10;

        db.collection(COLECAO_ATIVIDADES).add(atividade)
                .addOnSuccessListener(documentReference -> {
                    String idGerado = documentReference.getId();
                    documentReference.update("id_atividade", idGerado);

                    atualizarMetricasUsuario(atividade.getUser_id(), pontosGanhos, co2Evitado, callback);
                })
                .addOnFailureListener(callback::onError);
    }

    /**
     * Lista todas as atividades registradas por um usuário específico em ordem decrescente de data.
     */
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

    // Executa o incremento atômico dos pontos e do co2 acumulado para evitar inconsistências.
    private void atualizarMetricasUsuario(String userId, int pontos, double co2, @NonNull RepositoryCallback<Void> callback) {

        DocumentReference userRef = db.collection(COLECAO_USUARIOS).document(userId);

        db.runTransaction(transaction -> {
                    transaction.update(userRef, "eco_points", FieldValue.increment(pontos));
                    transaction.update(userRef, "total_co2_poupado", FieldValue.increment(co2));
                    return null;
                })
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(callback::onError);
    }

    // Lógica básica para cálculo de impacto ecológico
    private double calcularCO2Evitado(String tipo, double distancia) {
        double fatorEscolhido;
        if (tipo == null) return FATOR_BICICLETA;

        switch (tipo.toLowerCase().trim()) {
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
                fatorEscolhido = FATOR_CARRO;
                break;
        }

        return distancia * (FATOR_CARRO - fatorEscolhido);
    }

    /**
     * Envia um e-mail de redefinição de senha para o endereço informado.
     */
    public void recuperarSenhaEmail(@NonNull String email, @NonNull RepositoryCallback<Void> callback) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(null);
                    } else {
                        if (task.getException() != null) {
                            callback.onError(task.getException());
                        } else {
                            callback.onError(new Exception("Falha desconhecida ao enviar e-mail de recuperação."));
                        }
                    }
                });
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

    // 1. Busca todos os desafios existentes no sistema
    public void obterTodosDesafios(RepositoryCallback<List<Desafio>> callback) {
        db.collection("desafios")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> callback.onSuccess(queryDocumentSnapshots.toObjects(Desafio.class)))
                .addOnFailureListener(callback::onError);
    }

    // 2. Busca apenas as conquistas que o usuário atual já liberou
    public void obterConquistasDoUsuario(String userId, RepositoryCallback<List<ConquistaUsuario>> callback) {
        db.collection("conquistas_usuario")
                .whereEqualTo("user_id", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> callback.onSuccess(queryDocumentSnapshots.toObjects(ConquistaUsuario.class)))
                .addOnFailureListener(callback::onError);
    }


}