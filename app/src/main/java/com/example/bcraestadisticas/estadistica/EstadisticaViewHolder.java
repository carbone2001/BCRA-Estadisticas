package com.example.bcraestadisticas.estadistica;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bcraestadisticas.R;

public class EstadisticaViewHolder extends RecyclerView.ViewHolder {
    TextView tvValor;
    TextView tvFecha;
    public EstadisticaViewHolder(@NonNull View itemView) {
        super(itemView);
        this.tvValor = itemView.findViewById(R.id.tvValor);
        this.tvFecha = itemView.findViewById(R.id.tvFecha);
    }

}
