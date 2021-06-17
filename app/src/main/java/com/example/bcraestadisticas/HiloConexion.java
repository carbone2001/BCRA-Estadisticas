package com.example.bcraestadisticas;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.bcraestadisticas.estadistica.Estadistica;

import java.util.List;

public class HiloConexion extends Thread {

    private Handler handler;
    private String tipoEstadistica;

    public HiloConexion(Handler handler, String tipoEstadistica) {
        this.handler = handler;
        this.tipoEstadistica = tipoEstadistica;
    }


    @Override
    public void run() {
        Message msg = new Message();
        //Log.d("Tipo de estadistica:",(this.tipoEstadistica.is));
        if ("Dolar Oficial".equals(this.tipoEstadistica)) {
            List<Estadistica> listaDolarOficial = ConexionAPI.obtenerEstadisticas("usd_of");
            Log.d("tamaño de la lista", Integer.toString(listaDolarOficial.size()));
            msg.obj = listaDolarOficial;
            msg.arg1 = 1;
        } else if ("UVA".equals(this.tipoEstadistica)) {
            List<Estadistica> lista = ConexionAPI.obtenerEstadisticas("uva");
            Log.d("tamaño de la lista", Integer.toString(lista.size()));
            msg.obj = lista;
            msg.arg1 = 2;
        }
        else{
            Log.d("No se ha elegido ningun tipo de estadistica","Puede que haya un error en el nombre");
        }

        this.handler.sendMessage(msg);
    }
}
