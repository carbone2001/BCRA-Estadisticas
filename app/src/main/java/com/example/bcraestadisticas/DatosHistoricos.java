package com.example.bcraestadisticas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.bcraestadisticas.estadistica.Estadistica;
import com.example.bcraestadisticas.estadistica.EstadisticaAdapter;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DatosHistoricos extends AppCompatActivity implements SearchView.OnQueryTextListener  {

    private List<Estadistica> listaEstadisticas;
    private static String tipoEstadistica;
    EstadisticaAdapter adapter;
    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_historicos);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Bundle extras = super.getIntent().getExtras();
        String titulo = extras.getString("tipoEstadisticas");
        actionBar.setTitle(titulo);
        SearchView searchFecha = this.findViewById(R.id.searchFecha);
        searchFecha.setOnQueryTextListener(this);

        if(Estadistica.estadisticaUva != null && Estadistica.estadisticaDolarOficial != null)
        {
            if(titulo.equals("Dolar Oficial")){
                this.listaEstadisticas = Estadistica.estadisticaDolarOficial;
            }
            else if(titulo.equals("UVA")){
                this.listaEstadisticas = Estadistica.estadisticaUva;
            }
            this.listaEstadisticas.sort(new Estadistica());
            this.adapter =  new EstadisticaAdapter(this.listaEstadisticas);
            this.rv = super.findViewById(R.id.rvEstadisticas);
            rv.setAdapter(adapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            rv.setLayoutManager(linearLayoutManager);
        }


    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if(s.isEmpty())
        {
            this.adapter =  new EstadisticaAdapter(this.listaEstadisticas);
            this.rv = super.findViewById(R.id.rvEstadisticas);
            rv.setAdapter(adapter);
        }
        else{
            Predicate<Estadistica> byDate = estadistica -> estadistica.getFechaString().contains(s);
            this.adapter =  new EstadisticaAdapter(this.listaEstadisticas.stream().filter(byDate).collect(Collectors.toList()));
            this.rv = super.findViewById(R.id.rvEstadisticas);
            rv.setAdapter(adapter);
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            super.finish();
        }
        return true;
    }
}