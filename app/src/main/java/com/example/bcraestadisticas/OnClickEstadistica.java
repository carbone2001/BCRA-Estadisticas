package com.example.bcraestadisticas;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class OnClickEstadistica implements View.OnClickListener {
    private Activity activity;
    private String tipoEstadistica;
    public OnClickEstadistica(Activity activity,String tipoEstadistica){
        this.tipoEstadistica = tipoEstadistica;
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this.activity,DatosHistoricos.class);
        intent.putExtra("tipoEstadisticas",this.tipoEstadistica);
        this.activity.startActivity(intent);
    }
}
