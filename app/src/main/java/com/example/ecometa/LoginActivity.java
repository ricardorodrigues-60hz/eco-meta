package com.example.ecometa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecometa.repository.AutenticacaoRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.Objects;

/**
 * Camada: View (Activity de Login)
 * Coleta as credenciais da UI e delega as operações de Auth para o AutenticacaoRepository.
 */
public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etSenha;
    private MaterialButton btnEntrar;
    private TextView tvIrParaCadastro, tvEsqueciSenha;
    private ProgressBar progressBar;

    // Centralizador de regras do Firebase
    private AutenticacaoRepository autenticacaoRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicialização do Repositório
        autenticacaoRepository = new AutenticacaoRepository();

        // Inicialização dos componentes da UI
        inicializarComponentes();

        // Configuração dos ouvintes de clique
        configurarCliques();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Verifica se o ID do usuário já existe no escopo de sessão atual
        if (autenticacaoRepository.obterIdUsuarioAtual() != null) {
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

        tvEsqueciSenha.setOnClickListener(v -> recuperarSenha());
    }

    private void validarAutenticacao() {
        String email = Objects.requireNonNull(etEmail.getText()).toString().trim();
        String senha = Objects.requireNonNull(etSenha.getText()).toString().trim();

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

        // Delegação de responsabilidade para o repositório
        autenticacaoRepository.logarUsuario(email, senha, new AutenticacaoRepository.RepositoryCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                progressBar.setVisibility(View.GONE);
                btnEntrar.setEnabled(true);
                Toast.makeText(LoginActivity.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                irParaHome();
            }

            @Override
            public void onError(Exception e) {
                progressBar.setVisibility(View.GONE);
                btnEntrar.setEnabled(true);

                String erroTratado;
                if (e instanceof FirebaseAuthInvalidUserException) {
                    erroTratado = "Este e-mail não está cadastrado.";
                } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    erroTratado = "E-mail ou senha inválidos.";
                } else {
                    erroTratado = "Erro ao autenticar: " + e.getLocalizedMessage();
                }
                Toast.makeText(LoginActivity.this, erroTratado, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void recuperarSenha() {
        String email = Objects.requireNonNull(etEmail.getText()).toString().trim();
        if (email.isEmpty()) {
            etEmail.setError("Informe seu e-mail para recuperar a senha");
            etEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Chamada direta para o método auxiliar que adicionaremos ao repositório
        autenticacaoRepository.recuperarSenhaEmail(email, new AutenticacaoRepository.RepositoryCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "E-mail de recuperação enviado com sucesso!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Erro ao enviar e-mail: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void irParaHome() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}