package com.example.bcraestadisticas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.bcraestadisticas.estadistica.Estadistica;
import com.example.bcraestadisticas.estadistica.EstadisticaAdapter;
import com.example.bcraestadisticas.notificacion.Notificacion;
import com.example.bcraestadisticas.notificacion.NotificacionAdapter;

public class Notificaciones extends AppCompatActivity implements View.OnClickListener {

    NotificacionAdapter adapter;
    RecyclerView rvNotificaciones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Notificaciones");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Spinner dropdownDivisaNotificacion = findViewById(R.id.spinnerDivisaNotificacion);
        Spinner dropdownLimite = findViewById(R.id.spinnerLimite);
        String[] itemsDivisas = new String[]{"Dolar Oficial","UVA"};
        String[] itemsLimites = new String[]{"máximo","mínimo"};
        ArrayAdapter<String> adapterDivisas = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,itemsDivisas);
        ArrayAdapter<String> adapterLimite = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,itemsLimites);
        adapterDivisas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterLimite.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownDivisaNotificacion.setAdapter(adapterDivisas);
        dropdownLimite.setAdapter(adapterLimite);

        Button btnCrearNotificacion = findViewById(R.id.btnCrearNotificacion);
        btnCrearNotificacion.setOnClickListener(this);


        this.refrescarLista();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvNotificaciones.setLayoutManager(linearLayoutManager);
    }

    public void refrescarLista(){
        this.adapter =  new NotificacionAdapter(this);
        this.rvNotificaciones = super.findViewById(R.id.rvNotificaciones);
        rvNotificaciones.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            super.finish();
        }
        return true;
    }




    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnCrearNotificacion) {
            Spinner spinnerTipoLimite = findViewById(R.id.spinnerLimite);
            Spinner spinnerDivisa = findViewById(R.id.spinnerDivisaNotificacion);
            EditText inputLimite = findViewById(R.id.inputLimite);
            String tipoLimite = spinnerTipoLimite.getSelectedItem().toString();
            String divisa = spinnerDivisa.getSelectedItem().toString();
            String limiteNumerico = inputLimite.getText().toString();
            String titulo = "";
            String mensaje = "";
            String nombreNotificacion;
            titulo = titulo.concat("Se ha superado el limite " + tipoLimite);
            mensaje = mensaje.concat("El valor de " + divisa + " ha superado el limite de $" + limiteNumerico);
            nombreNotificacion = divisa.concat(" - Limite ").concat(tipoLimite).concat(": $").concat(limiteNumerico);
            Notificacion notificacion = new Notificacion(this, MainActivity.class, titulo, mensaje,nombreNotificacion);
            HiloNotificacion hiloNotificacion = new HiloNotificacion(
                    notificacion,
                    spinnerDivisa.getSelectedItem().toString(),
                    spinnerTipoLimite.getSelectedItem().toString(),
                    Double.parseDouble(inputLimite.getText().toString().isEmpty() ? "0" : inputLimite.getText().toString()));
            hiloNotificacion.start();
        }


    }





}