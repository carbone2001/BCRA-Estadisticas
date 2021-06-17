package com.example.bcraestadisticas;

import android.util.Log;

import com.example.bcraestadisticas.estadistica.Estadistica;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConexionAPI {
    final private static String urlBase = "https://api.estadisticasbcra.com/";
    final private static String bearerToken = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2NTI2MzM5NjcsInR5cGUiOiJleHRlcm5hbCIsInVzZXIiOiJiZW1peDQ2MjA1QGxhYmVieC5jb20ifQ.i_hO1GmqTtD_cb06aGXkv2emL32ZjdLikPeM7J57OD_BEjqpEfjCI9fikt1sYwvzC5gKisVyEn4g_wdvw4gYFA";
    public static List<Estadistica> obtenerEstadisticas(String urlEstadistica){
        List<Estadistica> listaEstadisticas = new ArrayList<>();
        Log.d("Se ha enviado una peticion a la API","Cuidado que no es infinita");
        try {
            //"usd_of"
            URL url = new URL(urlBase.concat(urlEstadistica));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");//Get es el valor por defecto
            urlConnection.setRequestProperty("Authorization","Bearer "+bearerToken);
            urlConnection.connect();
            int respuesta = urlConnection.getResponseCode();
            Log.d("Conexion","Respuesta del servidor: "+respuesta);
            if(respuesta == 200){
                InputStream is = urlConnection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int cant = 0;
                while((cant = is.read(buffer)) != -1){
                    baos.write(buffer,0,cant);
                }
                is.close();
                JSONArray jsonArray = new JSONArray(new String(baos.toString()));
                for(int i = 0; i<jsonArray.length();i++){
                    JSONObject jsonActual = jsonArray.getJSONObject(i);
                    LocalDate localDate = LocalDate.parse(jsonActual.getString("d"));
                    listaEstadisticas.add(new Estadistica(localDate, jsonActual.getDouble("v")));
                }
                return listaEstadisticas;
            }
            else{
                throw new RuntimeException("Error en la conexion con el servidor: "+respuesta);
            }
        } catch (MalformedURLException e) {
            //Log.d("Mensaje de error","MalformedURLException")
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
