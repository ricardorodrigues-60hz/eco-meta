package com.example.ecometa.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecometa.R;
import com.example.ecometa.model.Desafio;
import com.example.ecometa.model.DesafioStatus;
import java.util.List;
import java.util.Locale;

public class ChallengesAdapter extends RecyclerView.Adapter<ChallengesAdapter.ChallengeViewHolder> {

    private final List<DesafioStatus> listaDesafios;

    public ChallengesAdapter(List<DesafioStatus> listaDesafios) {
        this.listaDesafios = listaDesafios;
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

        holder.tvTitulo.setText(desafio.getTitulo());
        holder.tvDescricao.setText(desafio.getDescricao());
        holder.tvRecompensa.setText(String.format(Locale.getDefault(), "+%d EcoPoints", desafio.getEco_points_recompensa()));

        if (status.isConquistado()) {
            holder.tvSelo.setText("CONCLUÍDO");
            holder.tvSelo.setTextColor(Color.parseColor("#27AE60"));
            holder.tvSelo.setBackgroundResource(R.drawable.bg_badge_unlocked);
            holder.ivIcon.setBackgroundColor(Color.parseColor("#E8F5E9"));
            holder.ivIcon.setColorFilter(Color.parseColor("#27AE60"));
        } else {
            holder.tvSelo.setText("DISPONÍVEL");
            holder.tvSelo.setTextColor(Color.parseColor("#27AE60"));
            holder.tvSelo.setBackgroundResource(R.drawable.bg_badge_unlocked);
            holder.ivIcon.setBackgroundColor(Color.parseColor("#E8F5E9"));
            holder.ivIcon.setColorFilter(Color.parseColor("#27AE60"));
        }
    }

    @Override
    public int getItemCount() { return listaDesafios.size(); }

    static class ChallengeViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvDescricao, tvRecompensa, tvSelo;
        CardView cardDesafio;
        android.widget.ImageView ivIcon;

        public ChallengeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTituloDesafio);
            tvDescricao = itemView.findViewById(R.id.tvDescricaoDesafio);
            tvRecompensa = itemView.findViewById(R.id.tvRecompensaDesafio);
            tvSelo = itemView.findViewById(R.id.tvStatusSelo);
            cardDesafio = itemView.findViewById(R.id.cardDesafio);
            ivIcon = itemView.findViewById(R.id.ivChallengeIcon);
        }
    }
}