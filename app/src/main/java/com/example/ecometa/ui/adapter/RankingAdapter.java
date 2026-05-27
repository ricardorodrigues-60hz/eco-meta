package com.example.ecometa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecometa.R;
import com.example.ecometa.model.Usuario;
import java.util.List;
import java.util.Locale;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.RankingViewHolder> {

    private final List<Usuario> listaUsuarios;

    public RankingAdapter(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    @NonNull
    @Override
    public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ranking, parent, false);
        return new RankingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingViewHolder holder, int position) {
        Usuario usuario = listaUsuarios.get(position);

        holder.tvPosicao.setText(String.valueOf(position + 1));
        holder.tvNome.setText(usuario.getNome());
        holder.tvNivel.setText(String.format(Locale.getDefault(), "%d EcoPoints", usuario.getEco_points()));
        
        // Lógica de Medalhas
        if (position == 0) {
            holder.ivMedal.setVisibility(View.VISIBLE);
            holder.ivMedal.setImageResource(R.drawable.ic_medal_gold);
        } else if (position == 1) {
            holder.ivMedal.setVisibility(View.VISIBLE);
            holder.ivMedal.setImageResource(R.drawable.ic_medal_silver);
        } else if (position == 2) {
            holder.ivMedal.setVisibility(View.VISIBLE);
            holder.ivMedal.setImageResource(R.drawable.ic_medal_bronze);
        } else {
            holder.ivMedal.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    static class RankingViewHolder extends RecyclerView.ViewHolder {
        TextView tvPosicao, tvNome, tvNivel, tvPontos;
        android.widget.ImageView ivMedal;

        public RankingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPosicao = itemView.findViewById(R.id.tvPosicao);
            tvNome = itemView.findViewById(R.id.tvNomeUsuario);
            tvNivel = itemView.findViewById(R.id.tvNivelUsuario);
            tvPontos = itemView.findViewById(R.id.tvPontosRanking);
            ivMedal = itemView.findViewById(R.id.ivMedal);
        }
    }
}