package com.example.bcraestadisticas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bcraestadisticas.estadistica.Estadistica;

public class Calculadora extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Calculadora");
        actionBar.setDisplayHomeAsUpEnabled(true);
        Spinner dropdownDivisa1 = findViewById(R.id.spinnerDivisa1);
        Spinner dropdownDivisa2 = findViewById(R.id.spinnerDivisa2);
        String[] items = new String[]{"Dolar Oficial","UVA"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownDivisa1.setAdapter(adapter);
        dropdownDivisa2.setAdapter(adapter);
        Button btnCalcular = findViewById(R.id.btnCalcular);
        btnCalcular.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnCalcular){
            Spinner spinner1 = findViewById(R.id.spinnerDivisa1);
            Spinner spinner2 = findViewById(R.id.spinnerDivisa2);
            double valorDivisa1 =  Estadistica.getUltimoValorDeUnaEstadistica(Estadistica.DOLAR_OFICIAL);
            double valorDivisa2 = Estadistica.getUltimoValorDeUnaEstadistica(Estadistica.UVA);
            EditText inputValor = findViewById(R.id.inputValor);
            String inputValorString = inputValor.getText().toString();
            TextView tvResultado = findViewById(R.id.tvResultado);
            double valor = Double.parseDouble((inputValorString.isEmpty()) ? "0" : inputValorString);
            String divisa1 = spinner1.getSelectedItem().toString();
            String divisa2 = spinner2.getSelectedItem().toString();

            if(divisa1 == Estadistica.DOLAR_OFICIAL){
                valorDivisa1 = Estadistica.getUltimoValorDeUnaEstadistica(Estadistica.DOLAR_OFICIAL)*valor;
            }
            else if(divisa1 == Estadistica.UVA){
                valorDivisa1 = Estadistica.getUltimoValorDeUnaEstadistica(Estadistica.UVA)*valor;
            }

            if(divisa2 == Estadistica.DOLAR_OFICIAL){
                valorDivisa2 = Estadistica.getUltimoValorDeUnaEstadistica(Estadistica.DOLAR_OFICIAL);
            }
            else if(divisa2 == Estadistica.UVA){
                valorDivisa2 = Estadistica.getUltimoValorDeUnaEstadistica(Estadistica.UVA);
            }
            //DecimalFormat formatoDecimales = new DecimalFormat("#.00");
            tvResultado.setText(Double.toString(valorDivisa1/valorDivisa2));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            super.finish();
        }
        return true;
    }
}