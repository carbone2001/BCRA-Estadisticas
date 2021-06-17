package com.example.bcraestadisticas;

import android.util.Log;

import com.example.bcraestadisticas.estadistica.Estadistica;
import com.example.bcraestadisticas.notificacion.Notificacion;

public class HiloNotificacion extends Thread {

    private Notificacion notification;
    private String divisa;
    private String tipoLimite;
    private double limite;
    public HiloNotificacion(Notificacion notificacion,String divisa,String tipoLimite,double limite){
        this.notification = notificacion;
        this.divisa = divisa;
        this.tipoLimite = tipoLimite;
        this.limite = limite;
    }



    @Override
    public void run() {
        try {
            while(this.notification.notificacionVigente()){
                //Thread.sleep(720000);
                Thread.sleep(5000);
                Log.d("Interacion-------------------","Bucle vivo");
                double valorActual = Estadistica.getUltimoValorDeUnaEstadistica(this.divisa);
                if(tipoLimite.equals("máximo") && valorActual > this.limite){
                    this.notification.enviarNotificacion();
                    Log.d("Interacion-------------------","SE HA ENVIADO LA NOTIFICAICON");
                    break;
                }
                else if(tipoLimite.equals("mínimo") && valorActual < this.limite){
                    this.notification.enviarNotificacion();
                    Log.d("Interacion-------------------","SE HA ENVIADO LA NOTIFICAICON");
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //super.run();
    }
}
