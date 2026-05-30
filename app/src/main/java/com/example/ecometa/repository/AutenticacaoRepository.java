package com.example.ecometa.repository;

import androidx.annotation.NonNull;
import com.example.ecometa.model.Estatisticas;
import com.example.ecometa.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

/**
 * Repository focado em Autenticação e Gerenciamento de Sessão.
 */
public class AutenticacaoRepository {
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore db;

    private static final String COLECAO_USUARIOS = "user";
    private static final String COLECAO_ESTATISTICAS = "estatisticas";

    public interface RepositoryCallback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }

    public AutenticacaoRepository() {
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
    }

    /**
     * Realiza o cadastro do usuário no Firebase Auth e sincroniza criando o perfil no Firestore.
     * Inclui lógica de rollback (deletar usuário do Auth) caso a escrita no Firestore falhe.
     */
    public void cadastrarUsuario(@NonNull String nome, @NonNull String email, @NonNull String senha, @NonNull RepositoryCallback<Void> callback) {
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String uid = firebaseUser.getUid();

                            Usuario novoUsuario = new Usuario(uid, nome, 0, 0.0);
                            novoUsuario.setEmail(email);

                            WriteBatch batch = db.batch();
                            batch.set(db.collection(COLECAO_USUARIOS).document(uid), novoUsuario);
                            batch.set(db.collection(COLECAO_ESTATISTICAS).document(uid), new Estatisticas(uid));

                            batch.commit()
                                    .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                                    .addOnFailureListener(e -> {
                                        // ROLLBACK: Se falhar ao criar no Firestore, deletamos o usuário do Auth
                                        // para que ele possa tentar cadastrar novamente com o mesmo e-mail.
                                        firebaseUser.delete().addOnCompleteListener(deleteTask -> {
                                            callback.onError(new Exception("Erro ao salvar dados no Firestore. Usuário removido para nova tentativa. " + e.getMessage()));
                                        });
                                    });
                        } else {
                            callback.onError(new Exception("Usuário nulo após criação bem-sucedida."));
                        }
                    } else {
                        callback.onError(task.getException() != null ? task.getException() : new Exception("Falha ao cadastrar no Auth."));
                    }
                });
    }

    public void logarUsuario(@NonNull String email, @NonNull String senha, @NonNull RepositoryCallback<Void> callback) {
        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(null);
                    } else {
                        callback.onError(task.getException() != null ? task.getException() : new Exception("Falha ao realizar login."));
                    }
                });
    }

    public String obterIdUsuarioAtual() {
        FirebaseUser user = mAuth.getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    public void recuperarSenhaEmail(@NonNull String email, @NonNull RepositoryCallback<Void> callback) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(null);
                    } else {
                        callback.onError(task.getException() != null ? task.getException() : new Exception("Falha ao enviar e-mail de recuperação."));
                    }
                });
    }

    public void deslogar() {
        mAuth.signOut();
    }
}
