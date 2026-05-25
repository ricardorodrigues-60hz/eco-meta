package com.example.ecometa.util;

import android.util.Log;
import com.example.ecometa.model.Atividade;
import com.example.ecometa.model.Conquista;
import com.example.ecometa.model.Desafio;
import com.example.ecometa.model.Usuario;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import java.util.Calendar;
import java.util.Date;

/**
 * Utilitário para popular o Firestore com dados de teste.
 * Essencial para demonstrações e avaliação da banca.
 */
public class EcoMetaMockHelper {
    private static final String TAG = "EcoMetaMockHelper";

    public static void popularBancoParaSimulacao() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        WriteBatch batch = db.batch();
        Timestamp agora = Timestamp.now();

        // 1. MOCK USUÁRIOS
        Usuario joao = new Usuario("user_joao", "João da Silva", "joao@email.com", 1250, 42.5, "Brotinho");
        Usuario maria = new Usuario("user_maria", "Maria Santos", "maria@email.com", 3250, 85.0, "Árvore Anciã");
        Usuario pedro = new Usuario("user_pedro", "Pedro Costa", "pedro@email.com", 2890, 60.0, "Arbusto");

        batch.set(db.collection("usuarios").document(joao.getId_user()), joao);
        batch.set(db.collection("usuarios").document(maria.getId_user()), maria);
        batch.set(db.collection("usuarios").document(pedro.getId_user()), pedro);

        // 2. MOCK ATIVIDADES (Cálculo: E = d * (0.120 - f_escolhido))
        Atividade a1 = new Atividade("at1", "user_joao", "Bicicleta", 5.2, 0.624, agora);
        Atividade a2 = new Atividade("at2", "user_joao", "Caminhada", 2.1, 0.252, agora);
        Atividade a3 = new Atividade("at3", "user_joao", "Ônibus", 8.5, 0.765, agora);
        Atividade a4 = new Atividade("at4", "user_joao", "Metrô", 12.3, 0.984, agora);
        
        batch.set(db.collection("atividades").document(a1.getId_atividade()), a1);
        batch.set(db.collection("atividades").document(a2.getId_atividade()), a2);
        batch.set(db.collection("atividades").document(a3.getId_atividade()), a3);
        batch.set(db.collection("atividades").document(a4.getId_atividade()), a4);

        // 3. MOCK DESAFIOS
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        Date dataLimite = cal.getTime();

        Desafio d1 = new Desafio();
        d1.setId_desafio("des1");
        d1.setTitulo("Semana Sem Carro");
        d1.setDescricao("Complete 5 trajetos sem usar carro");
        d1.setPontos_recompensa(150);
        d1.setData_limite(dataLimite);
        d1.setProgresso_atual(3);
        d1.setMeta_objetivo(5);

        Desafio d2 = new Desafio();
        d2.setId_desafio("des2");
        d2.setTitulo("Economia de CO2");
        d2.setDescricao("Poupe 5kg de CO2 este mês");
        d2.setPontos_recompensa(200);
        d2.setData_limite(dataLimite);
        d2.setProgresso_atual(4.25);
        d2.setMeta_objetivo(5);

        batch.set(db.collection("desafios").document(d1.getId_desafio()), d1);
        batch.set(db.collection("desafios").document(d2.getId_desafio()), d2);

        // 4. MOCK CONQUISTAS
        Conquista c1 = new Conquista();
        c1.setId_conquista("conq1");
        c1.setUser_id("user_joao");
        c1.setTitulo_conquista("Iniciante Verde");
        c1.setData_desbloqueio(new Date());

        Conquista c2 = new Conquista();
        c2.setId_conquista("conq2");
        c2.setUser_id("user_joao");
        c2.setTitulo_conquista("Eco Warrior");
        c2.setData_desbloqueio(new Date());

        batch.set(db.collection("conquistas_usuario").document(c1.getId_conquista()), c1);
        batch.set(db.collection("conquistas_usuario").document(c2.getId_conquista()), c2);

        // EXECUÇÃO DO BATCH
        batch.commit().addOnSuccessListener(aVoid -> Log.d(TAG, "Mock Data injetado com sucesso!"))
                     .addOnFailureListener(e -> Log.e(TAG, "Erro ao injetar Mock Data", e));
    }
}