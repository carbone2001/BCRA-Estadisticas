package com.example.bcraestadisticas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bcraestadisticas.estadistica.Estadistica;
import com.example.bcraestadisticas.notificacion.Notificacion;

import org.json.JSONArray;

import java.util.List;

public class MainActivity extends AppCompatActivity implements Handler.Callback{
    //List<Estadistica> listaEstadisticas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View btnDolarOficial = this.findViewById(R.id.btnDolarOficial);
        View btnUva = this.findViewById(R.id.btnUva);
        btnDolarOficial.setOnClickListener(new OnClickEstadistica(this,"Dolar Oficial"));
        btnUva.setOnClickListener(new OnClickEstadistica(this,"UVA"));
        JSONArray jsonArray = Notificacion.obtenerNotificacionesGuardadas(this);
        //Log.d("LONGITUD DEL ARRAY DE NOTIFICACIONES -----------------", String.valueOf(jsonArray.length()));

        Handler handler = new Handler(this);
        HiloConexion hiloDolar = new HiloConexion(handler,"Dolar Oficial");
        HiloConexion hiloUVA = new HiloConexion(handler,"UVA");
        hiloDolar.start();
        hiloUVA.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal,menu);
        //MenuItem menuItem = menu.findItem(R.id.buscadorEstadistica);
        //SearchView searchView = (SearchView) menuItem.getActionView();
        //searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.btnCalculadora){
            if(Estadistica.estadisticaDolarOficial == null || Estadistica.UVA == null){
                Toast.makeText(this,"Espere... aún no se han cargado las cotizaciones.",Toast.LENGTH_LONG).show();
            }
            else{
                Intent intent = new Intent(this,Calculadora.class);
                startActivity(intent);
            }
        }
        else if(item.getItemId() == R.id.btnNotificaciones){
            Intent intent = new Intent(this,Notificaciones.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean handleMessage(@NonNull Message message) {
        //Dolar Oficial
        if(message.arg1 == 1){
            Estadistica.estadisticaDolarOficial = (List<Estadistica>) message.obj;
            Estadistica.estadisticaDolarOficial.sort(new Estadistica());
            TextView tvValorActualDolarOficial = findViewById(R.id.tvValorActualDolarOficial);
            tvValorActualDolarOficial.setText( Estadistica.getUltimoValorDeUnaEstadistica(Estadistica.DOLAR_OFICIAL).toString());
        }
        //UVA
        else if(message.arg1 == 2){
            Estadistica.estadisticaUva = (List<Estadistica>) message.obj;
            Estadistica.estadisticaUva.sort(new Estadistica());
            TextView tvValorActualUva = findViewById(R.id.tvValorActualUva);
            tvValorActualUva.setText(Estadistica.getUltimoValorDeUnaEstadistica(Estadistica.UVA).toString());
        }
        return false;
    }
}