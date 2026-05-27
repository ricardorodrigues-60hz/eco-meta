package com.example.ecometa.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.ecometa.R;
import com.example.ecometa.repository.AutenticacaoRepository;
import com.example.ecometa.viewmodel.EcoMetaViewModel;
import com.example.ecometa.viewmodel.ViewModelFactory;

public class RegistrarAtividadeFragment extends Fragment {

    private EcoMetaViewModel viewModel;
    private AutenticacaoRepository repository;

    private Spinner spinnerTransporte;
    private EditText etDistancia;
    private Button btnRegistrar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registrar_atividade, container, false);

        // Vincula as views do XML
        spinnerTransporte = view.findViewById(R.id.spinnerTransporte);
        etDistancia = view.findViewById(R.id.etDistancia);
        btnRegistrar = view.findViewById(R.id.btnRegistrarAtividade);

        // Inicializa arquitetura MVVM idêntica aos seus outros fragments
        repository = new AutenticacaoRepository();
        ViewModelFactory factory = new ViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(EcoMetaViewModel.class);

        btnRegistrar.setOnClickListener(v -> executarRegistro());

        return view;
    }

    private void executarRegistro() {
        String distanciaTexto = etDistancia.getText().toString().trim();

        // Validações básicas de entrada
        if (distanciaTexto.isEmpty()) {
            etDistancia.setError("Informe a distância percorrida");
            return;
        }

        double distancia = Double.parseDouble(distanciaTexto);
        if (distancia <= 0) {
            etDistancia.setError("A distância deve ser maior que zero");
            return;
        }

        String transporteSelecionado = spinnerTransporte.getSelectedItem().toString();
        String userId = repository.obterIdUsuarioAtual();

        // 2. Dispara o método exato da sua ViewModel
        if (userId != null) {
            viewModel.registrarNovaAtividade(userId, transporteSelecionado, distancia);

            Toast.makeText(getContext(), "Atividade registrada com sucesso!", Toast.LENGTH_SHORT).show();

            // Limpa o campo de texto para um próximo registro
            etDistancia.setText("");
        } else {
            Toast.makeText(getContext(), "Erro: Usuário não autenticado.", Toast.LENGTH_SHORT).show();
        }
    }
}