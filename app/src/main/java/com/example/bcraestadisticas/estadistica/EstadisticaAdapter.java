package com.example.bcraestadisticas.estadistica;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bcraestadisticas.R;

import java.util.List;


public class EstadisticaAdapter extends RecyclerView.Adapter<EstadisticaViewHolder> {
    List<Estadistica> listaEstadisticas;
    //View.OnClickListener onClickEstadistica;
    public EstadisticaAdapter(List<Estadistica> listaEstadisticas){
        this.listaEstadisticas = listaEstadisticas;
        //this.onClickEstadistica = onClickEstadistica;
    }


    @NonNull
    @Override
    public EstadisticaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_estadistica,parent,false);
        EstadisticaViewHolder estadisticaViewHolder = new EstadisticaViewHolder(v);
        //v.setOnClickListener(this.onClickEstadistica);
        return estadisticaViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EstadisticaViewHolder holder, int position) {
        Estadistica estadistica = this.listaEstadisticas.get(position);
        holder.tvValor.setText(estadistica.getValor().toString());
        holder.tvFecha.setText(estadistica.getFechaString());
    }

    @Override
    public int getItemCount() {
        return this.listaEstadisticas.size();
    }
}
