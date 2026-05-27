package com.example.ecometa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecometa.R;
import com.example.ecometa.model.Atividade;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AtividadeAdapter extends RecyclerView.Adapter<AtividadeAdapter.AtividadeViewHolder> {

    private final List<Atividade> listaAtividades;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public AtividadeAdapter(List<Atividade> listaAtividades) {
        this.listaAtividades = listaAtividades;
    }

    @NonNull
    @Override
    public AtividadeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_atividade, parent, false);
        return new AtividadeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AtividadeViewHolder holder, int position) {
        Atividade atividade = listaAtividades.get(position);

        holder.tvTipo.setText(atividade.getTipo_transporte());
        holder.tvDistancia.setText(String.format(Locale.getDefault(), "Distância: %.1f km", atividade.getDistancia_km()));
        holder.tvCo2.setText(String.format(Locale.getDefault(), "CO₂ Evitado: %.2f kg", atividade.getCo2_evitado()));
        holder.tvPontos.setText(String.format(Locale.getDefault(), "+%d EcoPoints", atividade.getPoints_earned()));

        if (atividade.getData() != null) {
            holder.tvData.setText(dateFormat.format(atividade.getData().toDate()));
        }
    }

    @Override
    public int getItemCount() {
        return listaAtividades.size();
    }

    static class AtividadeViewHolder extends RecyclerView.ViewHolder {
        TextView tvTipo, tvData, tvDistancia, tvCo2, tvPontos;

        public AtividadeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTipo = itemView.findViewById(R.id.tvTipoTransporte);
            tvData = itemView.findViewById(R.id.tvDataAtividade);
            tvDistancia = itemView.findViewById(R.id.tvDistancia);
            tvCo2 = itemView.findViewById(R.id.tvCo2Poupado);
            tvPontos = itemView.findViewById(R.id.tvPontosGanhos);
        }
    }
}