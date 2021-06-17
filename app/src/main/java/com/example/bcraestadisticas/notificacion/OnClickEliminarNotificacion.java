package com.example.bcraestadisticas.notificacion;

import android.app.Activity;
import android.view.View;

import com.example.bcraestadisticas.Notificaciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OnClickEliminarNotificacion implements View.OnClickListener {

    private int notificaionId;
    private Notificaciones activity;

    public OnClickEliminarNotificacion(Notificaciones activity, int notificaionId){
        this.notificaionId = notificaionId;
        this.activity = activity;
    }


    @Override
    public void onClick(View view) {
        Notificacion.eliminarNotificacion(this.activity,notificaionId);
        this.activity.refrescarLista();
    }
}
