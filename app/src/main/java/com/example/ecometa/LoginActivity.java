package com.example.ecometa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Camada: View (Activity de Login)
 * Responsável pela autenticação dos usuários utilizando Firebase Authentication.
 */
public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etSenha;
    private MaterialButton btnEntrar;
    private TextView tvIrParaCadastro, tvEsqueciSenha;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicialização do Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Inicialização dos componentes da UI
        inicializarComponentes();

        // Configuração dos ouvintes de clique
        configurarCliques();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Verifica se o usuário já está logado
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            irParaHome();
        }
    }

    private void inicializarComponentes() {
        etEmail = findViewById(R.id.etEmailLogin);
        etSenha = findViewById(R.id.etSenhaLogin);
        btnEntrar = findViewById(R.id.btnEntrar);
        tvIrParaCadastro = findViewById(R.id.tvIrParaCadastro);
        tvEsqueciSenha = findViewById(R.id.tvEsqueciSenha);
        progressBar = findViewById(R.id.progressBarLogin);
    }

    private void configurarCliques() {
        btnEntrar.setOnClickListener(v -> validarAutenticacao());

        tvIrParaCadastro.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
            startActivity(intent);
        });

        tvEsqueciSenha.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Informe seu e-mail para recuperar a senha", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "E-mail de recuperação enviado!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Erro ao enviar e-mail de recuperação", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void validarAutenticacao() {
        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();

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

        realizarLogin(email, senha);
    }

    private void realizarLogin(String email, String senha) {
        progressBar.setVisibility(View.VISIBLE);
        btnEntrar.setEnabled(false);

        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    btnEntrar.setEnabled(true);

                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        irParaHome();
                    } else {
                        String erro = "Erro ao realizar login";
                        try {
                            throw task.getException();
                        } catch (Exception e) {
                            erro = "E-mail ou senha inválidos";
                        }
                        Toast.makeText(LoginActivity.this, erro, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void irParaHome() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
