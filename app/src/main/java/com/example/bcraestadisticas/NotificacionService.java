package com.example.bcraestadisticas;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class NotificacionService extends Service {
    //Thread thread;
    //public static Activity activity;
    //public static Handler handlerConexion;
    public NotificacionService(){ }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        HiloNotificacion thread = new HiloNotificacion(this);
        thread.start();
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        String channelId = "Servicio de actualizacion de cotizaciones"; //getString(R.string.app_name);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(notificationManager.getNotificationChannel(channelId) == null)
        {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(channelId);
            notificationChannel.setSound(null, null);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Notification notification = new Notification.Builder(this, channelId)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(channelId)
                .setSmallIcon(R.mipmap.ic_bcra_estadisticas_foreground)
                .build();
        startForeground(21, notification);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
