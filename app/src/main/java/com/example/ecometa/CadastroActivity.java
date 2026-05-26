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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Objects;

/**
 * Activity de Cadastro
 * Coleta os dados da UI e delega a criação de conta para o AutenticacaoRepository.
 */
public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText etNome, etEmail, etSenha;
    private MaterialButton btnCriarConta;
    private TextView tvVoltarLogin;
    private ProgressBar progressBar;
    private AutenticacaoRepository autenticacaoRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Inicialização do Repositório Central de Autenticação
        autenticacaoRepository = new AutenticacaoRepository();

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

        if (email.contains(" ")) {
            etEmail.setError("O e-mail não pode conter espaços");
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

        // Executa o fluxo enviando os dados encapsulados para o repositório
        realizarCadastroNoRepositorio(nome, email, senha);
    }

    private void realizarCadastroNoRepositorio(String nome, String email, String senha) {
        progressBar.setVisibility(View.VISIBLE);
        btnCriarConta.setEnabled(false);

        // Chamamos o método síncrono/atômico que criamos no repositório refatorado
        autenticacaoRepository.cadastrarUsuario(nome, email, senha, new AutenticacaoRepository.RepositoryCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CadastroActivity.this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();
                irParaHome();
            }

            @Override
            public void onError(Exception e) {
                progressBar.setVisibility(View.GONE);
                btnCriarConta.setEnabled(true);

                String erroTratado;
                // Tratamento detalhado de exceções vindas do Firebase Auth
                if (e instanceof FirebaseAuthWeakPasswordException) {
                    erroTratado = "Digite uma senha mais forte (mínimo 6 caracteres).";
                } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    erroTratado = "O formato do e-mail inserido é inválido.";
                } else if (e instanceof FirebaseAuthUserCollisionException) {
                    erroTratado = "Este endereço de e-mail já está cadastrado.";
                } else {
                    erroTratado = "Erro no cadastro: " + e.getLocalizedMessage();
                }

                Toast.makeText(CadastroActivity.this, erroTratado, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void irParaHome() {
        Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}