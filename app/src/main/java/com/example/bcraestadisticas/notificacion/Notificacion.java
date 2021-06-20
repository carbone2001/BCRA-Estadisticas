package com.example.bcraestadisticas.notificacion;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.bcraestadisticas.MainActivity;
import com.example.bcraestadisticas.NotificacionService;
import com.example.bcraestadisticas.Notificaciones;
import com.example.bcraestadisticas.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Objects;

public class Notificacion implements Serializable {
    private Notificaciones activityOrigen;
    private Class<MainActivity> activityDestino;
    private static JSONArray listaNotificaciones = null;
    private String mensaje;
    private String titulo;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private String nombreNotificacion;
    private Boolean emitida;
    private String tipoLimite;
    private String divisa;
    private double limiteNumerico;

    public Notificacion(Notificaciones activityOrigen, Class<MainActivity> activityDestino,String tipoLimite,String divisa,double limiteNumerico){
        this.activityOrigen = activityOrigen;
        this.activityDestino = activityDestino;

        Log.d("TIPO LIMITE --------------",tipoLimite);
        this.titulo = "".concat("Se ha superado el limite ").concat(tipoLimite);
        this.mensaje = "".concat("El valor de " + divisa + " ha superado el limite de $" + limiteNumerico);
        this.nombreNotificacion = divisa.concat(" - Limite ").concat(tipoLimite).concat(": $").concat(Double.toString(limiteNumerico));
        this.tipoLimite = tipoLimite;
        this.divisa = divisa;
        this.limiteNumerico = limiteNumerico;
        this.emitida = false;
    }

    public void enviarNotificacion(){
        try{
            this.emitida = true;
            mNotificationManager.notify( this.hashCode(), mBuilder.build());
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    public static StatusBarNotification[] getNotificacionesActivas(Activity activity){
        NotificationManager notificationManager = (NotificationManager)activity.getSystemService(Context.NOTIFICATION_SERVICE);
        StatusBarNotification[] statusBarNotifications = notificationManager.getActiveNotifications();
        for(StatusBarNotification notification:statusBarNotifications){
            Notification notification1 = notification.getNotification();
            Log.d("ID DE LA NOTIFICACION",Integer.toString(notification.getId()));
        }
        return statusBarNotifications;
    }

    public static boolean guardarNotificacion(Context context,Notificacion notificacion){
        Boolean coincidencias = false;
        for(int i=0;i<Notificacion.listaNotificaciones.length();i++){
            JSONObject current = null;
            try {
                current = Notificacion.listaNotificaciones.getJSONObject(i);
                if(current.getInt("id") == notificacion.hashCode()){
                    coincidencias = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!coincidencias){
            Notificacion.listaNotificaciones.put(notificacion.toJSON());
            guardarListaNotificaciones(context);
            if(context.getClass() == Notificaciones.class){
                ((Notificaciones)context).refrescarLista();
            }
            return true;
        }
        else{
            return false;
        }

    }

    public static JSONArray obtenerNotificacionesGuardadas(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonArrayString = prefs.getString("notificaciones",(new JSONObject()).toString());
        JSONObject objectMain = null;
        JSONArray jsonArray = null;
        try {
            objectMain = new JSONObject(jsonArrayString);
            jsonArray = objectMain.getJSONArray("notificaciones");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(jsonArray == null){
            jsonArray = new JSONArray();
        }
        Notificacion.listaNotificaciones = jsonArray;
        return Notificacion.listaNotificaciones;
    }


    public static void eliminarNotificacionDeLista(Context context,int id){
        cancelarNotificacion(context,id);
        try {
            for(int i=0; i<Notificacion.listaNotificaciones.length();i++){
                JSONObject current = Notificacion.listaNotificaciones.getJSONObject(i);
                if(current.getInt("id") == id){
                    Notificacion.listaNotificaciones.remove(i);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Notificacion.guardarListaNotificaciones(context);
    }

    public static void cancelarNotificacion(Context context,int id){
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notificacion)) return false;
        Notificacion that = (Notificacion) o;
        return nombreNotificacion.equals(that.nombreNotificacion);
    }

    @Override
    public int hashCode() {
        Long longNumber = new Long(Math.round(Math.pow(Objects.hash(nombreNotificacion),2)));
        return longNumber.intValue();
    }

    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nombre",this.nombreNotificacion);
            jsonObject.put("divisa",this.divisa);
            jsonObject.put("id",this.hashCode());
            jsonObject.put("limite",this.limiteNumerico);
            jsonObject.put("tipoLimite",this.tipoLimite);
            jsonObject.put("emitida",this.emitida);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    private static void guardarListaNotificaciones(Context activity){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("notificaciones",Notificacion.listaNotificaciones);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        editor.putString("notificaciones",jsonObject.toString());
        editor.apply();
    }


    public static void registrarNotificacionEmitida(Context context, int id){

        for(int i=0;i<Notificacion.listaNotificaciones.length();i++){
            JSONObject current = null;
            try {
                current = Notificacion.listaNotificaciones.getJSONObject(i);
                if(current.getInt("id") == id){
                    current.put("emitida",true);
                    Notificacion.listaNotificaciones.remove(i);
                    Notificacion.listaNotificaciones.put(current);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        guardarListaNotificaciones(context);
    }

}
