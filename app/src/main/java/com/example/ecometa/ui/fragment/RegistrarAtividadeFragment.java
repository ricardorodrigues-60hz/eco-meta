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
import com.example.ecometa.repository.EcoMetaRepository;
import com.example.ecometa.viewmodel.EcoMetaViewModel;
import com.example.ecometa.viewmodel.ViewModelFactory;

public class RegistrarAtividadeFragment extends Fragment {

    private EcoMetaViewModel viewModel;
    private AutenticacaoRepository authRepository;
    private EcoMetaRepository dataRepository;

    private Spinner spinnerTransporte;
    private EditText etDistancia;
    private Button btnRegistrar;
    private com.google.android.material.card.MaterialCardView cardWalk, cardBike, cardBus, cardMetro;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registrar_atividade, container, false);

        spinnerTransporte = view.findViewById(R.id.spinnerTransporte);
        etDistancia = view.findViewById(R.id.etDistancia);
        btnRegistrar = view.findViewById(R.id.btnRegistrarAtividade);

        cardWalk = view.findViewById(R.id.cardWalk);
        cardBike = view.findViewById(R.id.cardBike);
        cardBus = view.findViewById(R.id.cardBus);
        cardMetro = view.findViewById(R.id.cardMetro);

        setupTransportSelection();

        authRepository = new AutenticacaoRepository();
        dataRepository = new EcoMetaRepository();
        ViewModelFactory factory = new ViewModelFactory(authRepository, dataRepository);
        viewModel = new ViewModelProvider(this, factory).get(EcoMetaViewModel.class);

        setupObservers();

        btnRegistrar.setOnClickListener(v -> executarRegistro());

        view.findViewById(R.id.btnClose).setOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        return view;
    }

    private void setupObservers() {
        viewModel.sucessoOperacao.observe(getViewLifecycleOwner(), sucesso -> {
            if (Boolean.TRUE.equals(sucesso)) {
                Toast.makeText(getContext(), "Atividade registrada com sucesso!", Toast.LENGTH_SHORT).show();
                etDistancia.setText("");
                if (getActivity() != null) getActivity().onBackPressed();
            }
        });

        viewModel.erro.observe(getViewLifecycleOwner(), erro -> {
            if (erro != null) {
                Toast.makeText(getContext(), "Erro ao registrar: " + erro, Toast.LENGTH_LONG).show();
                btnRegistrar.setEnabled(true);
            }
        });
    }

    private void setupTransportSelection() {
        View.OnClickListener listener = v -> {
            cardWalk.setSelected(false);
            cardBike.setSelected(false);
            cardBus.setSelected(false);
            cardMetro.setSelected(false);

            v.setSelected(true);
            
            if (v.getId() == R.id.cardBike) spinnerTransporte.setSelection(0);
            else if (v.getId() == R.id.cardWalk) spinnerTransporte.setSelection(1);
            else if (v.getId() == R.id.cardBus) spinnerTransporte.setSelection(2);
            else if (v.getId() == R.id.cardMetro) spinnerTransporte.setSelection(3);

            btnRegistrar.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                androidx.core.content.ContextCompat.getColor(getContext(), R.color.eco_green_primary)
            ));
        };

        cardWalk.setOnClickListener(listener);
        cardBike.setOnClickListener(listener);
        cardBus.setOnClickListener(listener);
        cardMetro.setOnClickListener(listener);
    }

    private void executarRegistro() {
        String distanciaTexto = etDistancia.getText().toString().trim();

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
        String userId = authRepository.obterIdUsuarioAtual();

        if (userId != null) {
            btnRegistrar.setEnabled(false);
            viewModel.registrarNovaAtividade(userId, transporteSelecionado, distancia);
        } else {
            Toast.makeText(getContext(), "Erro: Usuário não autenticado.", Toast.LENGTH_SHORT).show();
        }
    }
}
