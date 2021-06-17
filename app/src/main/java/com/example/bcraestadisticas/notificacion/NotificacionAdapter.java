package com.example.bcraestadisticas.notificacion;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bcraestadisticas.Notificaciones;
import com.example.bcraestadisticas.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class NotificacionAdapter extends RecyclerView.Adapter<NotificacionViewHolder> {
    private Notificaciones activity;
    public NotificacionAdapter(Notificaciones activity){
        this.activity = activity;
    }


    @NonNull
    @Override
    public NotificacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notificacion,parent,false);
        NotificacionViewHolder notificacionViewHolder = new NotificacionViewHolder(v);
        return notificacionViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificacionViewHolder holder, int position) {
        JSONArray notificaciones = Notificacion.obtenerNotificacionesGuardadas(this.activity);

        try {
            JSONObject notificacion = notificaciones.getJSONObject(position);
            holder.tvNombreNotificacion.setText(notificacion.getString("nombre"));
            holder.btnEliminarNotificacion.setOnClickListener(new OnClickEliminarNotificacion(this.activity,notificacion.getInt("id")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return Notificacion.obtenerNotificacionesGuardadas(activity).length();
    }
}
