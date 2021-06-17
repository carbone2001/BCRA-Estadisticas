package com.example.bcraestadisticas.notificacion;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bcraestadisticas.R;

public class NotificacionViewHolder extends RecyclerView.ViewHolder {
    TextView tvNombreNotificacion;
    TextView btnEliminarNotificacion;
    public NotificacionViewHolder(@NonNull View itemView) {
        super(itemView);
        this.tvNombreNotificacion = itemView.findViewById(R.id.tvNombreNotificacion);
        this.btnEliminarNotificacion = itemView.findViewById(R.id.btnEliminarNotificacion);
    }

}
