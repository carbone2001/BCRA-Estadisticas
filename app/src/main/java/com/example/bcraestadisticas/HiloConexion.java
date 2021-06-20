package com.example.bcraestadisticas;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.bcraestadisticas.estadistica.Estadistica;
import com.example.bcraestadisticas.notificacion.Notificacion;

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
        if ("Dolar Oficial".equals(this.tipoEstadistica)) {
            List<Estadistica> lista = ConexionAPI.obtenerEstadisticas("usd_of");
            msg.arg1 = 1;
            Estadistica.estadisticaDolarOficial = lista;
            Estadistica.estadisticaDolarOficial.sort(new Estadistica());
        } else if ("UVA".equals(this.tipoEstadistica)) {
            List<Estadistica> lista = ConexionAPI.obtenerEstadisticas("uva");
            Estadistica.estadisticaUva = lista;
            msg.arg1 = 2;
            Estadistica.estadisticaUva.sort(new Estadistica());
        }
        else{
            //Log.d("No se ha elegido ningun tipo de estadistica","Puede que haya un error en el nombre");
        }

        this.handler.sendMessage(msg);
    }
}
