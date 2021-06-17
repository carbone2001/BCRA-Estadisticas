package com.example.bcraestadisticas.notificacion;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.bcraestadisticas.MainActivity;
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

    public Notificacion(Notificaciones activityOrigen, Class<MainActivity> activityDestino,String titulo,String mensaje,String nombreNotificacion){
        this.activityOrigen = activityOrigen;
        this.activityDestino = activityDestino;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.nombreNotificacion = nombreNotificacion;
        this.emitida = false;
        Boolean coincidencias = false;
        for(int i=0;i<Notificacion.listaNotificaciones.length();i++){
            JSONObject current = null;
            try {
                current = Notificacion.listaNotificaciones.getJSONObject(i);
                if(current.getInt("id") == this.hashCode()){
                    coincidencias = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(coincidencias == false){
            this.mBuilder = new NotificationCompat.Builder(this.activityOrigen.getApplicationContext(), "notificacionLimites");
            Intent intent = new Intent(this.activityOrigen.getApplicationContext(), this.activityDestino);
            intent.putExtra("nombreNotificacion",this.nombreNotificacion);
            PendingIntent pendingIntent = PendingIntent.getActivity(this.activityOrigen.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            bigText.bigText(this.mensaje);
            bigText.setBigContentTitle(this.titulo);
            bigText.setSummaryText("Alerta de Limites");

            mBuilder.setContentIntent(pendingIntent);
            //mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            mBuilder.setSmallIcon(R.drawable.ic_launcher_foreground);
            mBuilder.setContentTitle(titulo);
            mBuilder.setContentText(mensaje);
            //mBuilder.setPriority(Notification.PRIORITY_HIGH);
            mBuilder.setStyle(bigText);
            mBuilder.setAutoCancel(true);
            mNotificationManager = (NotificationManager) this.activityOrigen.getSystemService(Context.NOTIFICATION_SERVICE);
            this.guardarNotificacion();
            Toast.makeText(this.activityOrigen,"Se ha creado una nueva notificación",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this.activityOrigen,"Ya existe una notificación con esas características",Toast.LENGTH_SHORT).show();
        }
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

    private void guardarNotificacion(){
        Notificacion.listaNotificaciones.put(this.toJSON());
        this.guardarListaNotificaciones(this.activityOrigen);
        this.activityOrigen.refrescarLista();
    }

    public static JSONArray obtenerNotificacionesGuardadas(Activity activity){
        if(Notificacion.listaNotificaciones == null){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
            String jsonArrayString = prefs.getString("notificaciones",(new JSONObject()).toString());
            Log.d("String shared preference----------------------------------------------------------------",jsonArrayString);
            JSONObject objectMain = null;
            JSONArray jsonArray = null;
            try {
                objectMain = new JSONObject(jsonArrayString);
                jsonArray = objectMain.getJSONArray("notificaciones");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Notificacion.listaNotificaciones = jsonArray;
        }
        return Notificacion.listaNotificaciones;
    }


    public static void eliminarNotificacion(Activity activity,int id){
        NotificationManager notificationManager = (NotificationManager)activity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
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
        Notificacion.guardarListaNotificaciones(activity);
        Toast.makeText(activity,"La notificacion ha sido eliminada",Toast.LENGTH_SHORT).show();
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
        return Objects.hash(nombreNotificacion);
    }

    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nombre",this.nombreNotificacion);
            jsonObject.put("id",this.hashCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    private static void guardarListaNotificaciones(Activity activity){
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


    private static JSONArray getListaNotificaciones() {
        return listaNotificaciones;
    }

    public static void setListaNotificaciones(JSONArray listaNotificaciones) {
        Notificacion.listaNotificaciones = listaNotificaciones;
    }

    public String getNombreNotificacion() {
        return nombreNotificacion;
    }

    public Boolean notificacionVigente(){
        Boolean existeEnLaLista = false;
        for(int i=0;i<Notificacion.listaNotificaciones.length();i++){
            JSONObject current = null;
            try {
                current = Notificacion.listaNotificaciones.getJSONObject(i);
                if(current.getInt("id") == this.hashCode()){
                    existeEnLaLista = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return (existeEnLaLista && !emitida);
    }
}
