package com.example.ecometa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecometa.model.Usuario;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

/**
 * ctivity de Cadastro
 * Responsável pela criação de novos usuários no Firebase Auth e Firestore.
 */
public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText etNome, etEmail, etSenha;
    private MaterialButton btnCriarConta;
    private TextView tvVoltarLogin;
    private ProgressBar progressBar;
    
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Inicialização do Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Inicialização dos componentes da UI
        inicializarComponentes();

        // Configuração dos ouvintes de clique
        configurarCliques();
    }

    private void inicializarComponentes() {
        etNome = findViewById(R.id.etNomeCadastro);
        etEmail = findViewById(R.id.etEmailCadastro);
        etSenha = findViewById(R.id.etSenhaCadastro);
        btnCriarConta = findViewById(R.id.btnCriarConta);
        tvVoltarLogin = findViewById(R.id.tvVoltarLogin);
        progressBar = findViewById(R.id.progressBarCadastro);
    }

    private void configurarCliques() {
        btnCriarConta.setOnClickListener(v -> validarDados());

        tvVoltarLogin.setOnClickListener(v -> finish());
    }

    private void validarDados() {
        String nome = Objects.requireNonNull(etNome.getText()).toString().trim();
        String email = Objects.requireNonNull(etEmail.getText()).toString().trim();
        String senha = Objects.requireNonNull(etSenha.getText()).toString().trim();

        if (nome.isEmpty()) {
            etNome.setError("O nome é obrigatório");
            etNome.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            etEmail.setError("O e-mail é obrigatório");
            etEmail.requestFocus();
            return;
        }

        if (senha.isEmpty()) {
            etSenha.setError("A senha é obrigatória");
            etSenha.requestFocus();
            return;
        }

        if (senha.length() < 6) {
            etSenha.setError("A senha deve ter no mínimo 6 caracteres");
            etSenha.requestFocus();
            return;
        }

        criarUsuarioAuth(nome, email, senha);
    }

    private void criarUsuarioAuth(String nome, String email, String senha) {
        progressBar.setVisibility(View.VISIBLE);
        btnCriarConta.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                        salvarUsuarioFirestore(userId, nome, email);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        btnCriarConta.setEnabled(true);

                        String erro;
                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch (FirebaseAuthWeakPasswordException e) {
                            erro = "Digite uma senha mais forte";
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            erro = "E-mail inválido";
                        } catch (FirebaseAuthUserCollisionException e) {
                            erro = "Esta conta já existe";
                        } catch (Exception e) {
                            erro = "Erro ao cadastrar usuário: " + e.getMessage();
                        }
                        Toast.makeText(CadastroActivity.this, erro, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void salvarUsuarioFirestore(String userId, String nome, String email) {
        Usuario usuario = new Usuario(userId, nome, email);

        db.collection("usuarios")
                .document(userId)
                .set(usuario)
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CadastroActivity.this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();
                    irParaHome();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnCriarConta.setEnabled(true);
                    Toast.makeText(CadastroActivity.this, "Erro ao salvar dados: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void irParaHome() {
        Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
