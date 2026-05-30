package com.example.ecometa.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecometa.R;
import com.example.ecometa.model.Desafio;
import com.example.ecometa.model.DesafioStatus;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import java.util.List;
import java.util.Locale;

/**
 * Camada: View (Adapter)
 * Gerencia a exibição da lista de desafios com progresso em tempo real.
 */
public class ChallengesAdapter extends RecyclerView.Adapter<ChallengesAdapter.ChallengeViewHolder> {

    private List<DesafioStatus> listaDesafios;

    public ChallengesAdapter(List<DesafioStatus> listaDesafios) {
        this.listaDesafios = listaDesafios;
    }

    /**
     * Atualiza a lista de dados do adapter e notifica a mudança.
     */
    public void setListaDesafios(List<DesafioStatus> novaLista) {
        this.listaDesafios = novaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_desafio, parent, false);
        return new ChallengeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeViewHolder holder, int position) {
        DesafioStatus status = listaDesafios.get(position);
        Desafio desafio = status.getDesafio();

        // 1. Dados básicos
        holder.tvTitulo.setText(desafio.getTitulo());
        holder.tvDescricao.setText(desafio.getDescricao());
        holder.tvRecompensa.setText(String.format(Locale.getDefault(), "+%d EcoPoints", desafio.getEco_points_recompensa()));

        // 2. Cálculo e exibição do Progresso (km_atual / meta_km)
        int porcentagem = status.getPorcentagemProgresso();
        holder.progress.setProgress(porcentagem, true);
        
        holder.tvProgressoTexto.setText(String.format(Locale.getDefault(), "%.1f / %.1f km", 
                status.getKm_atual(), desafio.getMeta_km()));

        // 3. Status Visual (CONCLUÍDO vs EM PROGRESSO/BLOQUEADO)
        if (status.isConcluido()) {
            holder.tvSelo.setText("CONCLUÍDO");
            holder.tvSelo.setTextColor(Color.parseColor("#27AE60")); // Verde
            holder.tvSelo.setBackgroundResource(R.drawable.bg_badge_unlocked);
            
            // Troca ícone para Check
            holder.ivIcon.setImageResource(R.drawable.ic_check);
            holder.ivIcon.setColorFilter(Color.parseColor("#27AE60"));
            holder.ivIcon.setBackgroundColor(Color.parseColor("#E8F5E9"));
        } else {
            // Se tiver algum progresso mas não concluído
            if (status.getKm_atual() > 0) {
                holder.tvSelo.setText("EM PROGRESSO");
                holder.tvSelo.setTextColor(Color.parseColor("#00B8D9")); // Azul
                holder.tvSelo.setBackgroundResource(R.drawable.bg_badge_locked); // Pode usar um neutro aqui
            } else {
                holder.tvSelo.setText("BLOQUEADO");
                holder.tvSelo.setTextColor(Color.parseColor("#636E72")); // Cinza
                holder.tvSelo.setBackgroundResource(R.drawable.bg_badge_locked);
            }
            
            // Ícone padrão de estrela/desafio
            holder.ivIcon.setImageResource(R.drawable.ic_stars);
            holder.ivIcon.setColorFilter(Color.parseColor("#D1D1D1"));
            holder.ivIcon.setBackgroundColor(Color.parseColor("#F5F6F7"));
        }
    }

    @Override
    public int getItemCount() { 
        return listaDesafios != null ? listaDesafios.size() : 0; 
    }

    static class ChallengeViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvDescricao, tvRecompensa, tvSelo, tvProgressoTexto;
        LinearProgressIndicator progress;
        android.widget.ImageView ivIcon;

        public ChallengeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTituloDesafio);
            tvDescricao = itemView.findViewById(R.id.tvDescricaoDesafio);
            tvRecompensa = itemView.findViewById(R.id.tvRecompensaDesafio);
            tvSelo = itemView.findViewById(R.id.tvStatusSelo);
            tvProgressoTexto = itemView.findViewById(R.id.tvProgressoTexto);
            progress = itemView.findViewById(R.id.progressDesafio);
            ivIcon = itemView.findViewById(R.id.ivChallengeIcon);
        }
    }
}
