package com.example.bcraestadisticas;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.SoundEffectConstants;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.bcraestadisticas.estadistica.Estadistica;
import com.example.bcraestadisticas.notificacion.Notificacion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HiloNotificacion extends Thread {
//public class HiloNotificacion implements Runnable {
    public static MainActivity activity;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    NotificacionService service;
//    private Notificacion notification;
//    private String divisa;
//    private String tipoLimite;
//    private double limite;
//    public HiloNotificacion(Notificacion notificacion,String divisa,String tipoLimite,double limite){
//        this.notification = notificacion;
//        this.divisa = divisa;
//        this.tipoLimite = tipoLimite;
//        this.limite = limite;
//    }

    public HiloNotificacion(NotificacionService service){
        this.service = service;
    }


    @Override
    public void run() {
        try {
            while (true){
                this.enviarNotificacionesPendientes();
                Thread.sleep(21600000);//6hs
                //Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //super.run();
    }

    private void enviarNotificacionesPendientes(){
        JSONArray notificacionesArray = Notificacion.obtenerNotificacionesGuardadas(service);

        for(int i = 0;i<notificacionesArray.length();i++){
            try {
                JSONObject currentNotif = notificacionesArray.getJSONObject(i);
                boolean emitida = currentNotif.getBoolean("emitida");

                if(!emitida){
                    Estadistica.estadisticaDolarOficial = ConexionAPI.obtenerEstadisticas("usd_of");
                    Estadistica.estadisticaDolarOficial.sort(new Estadistica());
                    Estadistica.estadisticaUva = ConexionAPI.obtenerEstadisticas("uva");
                    Estadistica.estadisticaUva.sort(new Estadistica());

                    String tipoLimite = currentNotif.getString("tipoLimite");
                    String divisa =  currentNotif.getString("divisa");
                    double limite = currentNotif.getDouble("limite");
                    double ultimoValor = Estadistica.getUltimoValorDeUnaEstadistica(divisa);

                    if(("máximo".equals(tipoLimite) && ultimoValor > limite) || ("mínimo".equals(tipoLimite) && ultimoValor < limite)){
                        String mensaje = currentNotif.getString("divisa")
                                .concat(" ha superado el limite ")
                                .concat( currentNotif.getString("tipoLimite"))
                                .concat(" de $")
                                .concat(Double.toString(currentNotif.getDouble("limite")));
                        String titulo = "Limite de cotización superado";
                        int id = currentNotif.getInt("id");

                        String channelId = "Aviso de limite superado";
                        mNotificationManager = (NotificationManager) service.getSystemService(service.NOTIFICATION_SERVICE);

                        if(mNotificationManager.getNotificationChannel(channelId) == null)
                        {
                            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT);
                            notificationChannel.setDescription(channelId);
                            mNotificationManager.createNotificationChannel(notificationChannel);
                        }

                        this.mBuilder = new NotificationCompat.Builder(service.getApplicationContext(), channelId);

                        Intent intent = new Intent(service.getApplicationContext(), activity.getClass());
                        intent.putExtra("idNotificacion",id);
                        PendingIntent pendingIntent = PendingIntent.getActivity(service.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
//                        bigText.bigText(titulo);
//                        bigText.setBigContentTitle(mensaje);
                        bigText.setSummaryText("Alerta de Limites");

                        mBuilder.setContentIntent(pendingIntent);
                        //mBuilder.setSmallIcon(R.drawable.ic_launcher_foreground);
                        mBuilder.setSmallIcon(R.mipmap.ic_bcra_estadisticas_foreground);
                        mBuilder.setContentTitle(titulo);
                        mBuilder.setContentText(mensaje);
                        mBuilder.setStyle(bigText);
                        mBuilder.setAutoCancel(true);

                        mNotificationManager.notify( id, mBuilder.build());
                        Notificacion.registrarNotificacionEmitida(service,id);
                    }
                }

            } catch (JSONException e) {
                this.service.onDestroy();
                e.printStackTrace();
            }
        }
    }

}
