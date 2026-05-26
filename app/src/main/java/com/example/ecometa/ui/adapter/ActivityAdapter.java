package com.example.ecometa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecometa.R;
import com.example.ecometa.model.Atividade;
import java.util.List;
import java.util.Locale;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private final List<Atividade> activities;

    public ActivityAdapter(List<Atividade> activities) {
        this.activities = activities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Atividade activity = activities.get(position);
        holder.tvType.setText(activity.getTipo_transporte());
        holder.tvDistance.setText(String.format(Locale.getDefault(), "%.1f km", activity.getDistancia_km()));
        holder.tvCO2.setText(String.format(Locale.getDefault(), "%.2f kg", activity.getCo2_evitado()));
        holder.tvDate.setText("Registro"); // Placeholder simplificado
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvType, tvDistance, tvCO2, tvDate;
        ImageView imgIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tvType);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvCO2 = itemView.findViewById(R.id.tvCO2);
            tvDate = itemView.findViewById(R.id.tvDate);
            imgIcon = itemView.findViewById(R.id.imgIcon);
        }
    }
}
