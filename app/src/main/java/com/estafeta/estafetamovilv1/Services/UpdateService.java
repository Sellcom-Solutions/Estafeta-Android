package com.estafeta.estafetamovilv1.Services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;

import com.estafeta.estafetamovilv1.Activities.MainActivity;
import com.estafeta.estafetamovilv1.Async_Request.METHOD;
import com.estafeta.estafetamovilv1.Async_Request.RequestManager;
import com.estafeta.estafetamovilv1.Async_Request.UIResponseListenerInterface;
import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Utils.DatesHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import database.model.Favorites;
import database.model.History;


/**
 * Created by hugo.figueroa on 22/09/15.
 */
public class UpdateService extends IntentService implements UIResponseListenerInterface{

    private static final    String              TAG         = "UpdateService";
    private ArrayList<Map<String,String>>       listFavorites;
    private ArrayList<Map<String, String>>      codesConfirmed;
    ArrayList<Map<String, String>>              aux;
    private                 int                 j           = 0;
    private                 String              favorite_id;

    private NotificationManager notificationManager;


    public UpdateService(String name) {
        super(name);
    }

    public UpdateService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    /**
     * Initiate service tasks.
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        listFavorites= Favorites.getAllUnconfirmedFavorites(this);

        if(listFavorites != null && listFavorites.size() != 0) {
            if (isNetworkAvailable()) {

                Log.d(TAG, "Actualizando... -------------------------------------");
                codesConfirmed  = new ArrayList<Map<String,String>>();

                checkDateFavorite();

            }else{
                Log.d(TAG, "No hay internet, no se pueden actualizar los favoritos");
                //Toast.makeText(getApplicationContext(),"No hay internet",Toast.LENGTH_SHORT).show();
                scheduleNextUpdate();
            }
        }else {
            Log.d(TAG, "No hay favoritos para actualizar");
            //Toast.makeText(getApplicationContext(),"No hay favoritos",Toast.LENGTH_SHORT).show();
            scheduleNextUpdate();
        }

    }


    /**
     * This method re-schedule the service to run again.
     */
    private void scheduleNextUpdate() {

        Intent intent = new Intent(this, this.getClass());
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // The update frequency should often be user configurable. This is not.

        long currentTimeMillis = System.currentTimeMillis();
        long nextUpdateTimeMillis = currentTimeMillis
                + 1 * DateUtils.HOUR_IN_MILLIS; // Una hora
        //+ 2 * DateUtils.MINUTE_IN_MILLIS; // un minuto
        Time nextUpdateTime = new Time();
        nextUpdateTime.set(nextUpdateTimeMillis);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("alertUp", true);
        editor.commit();

        if(codesConfirmed != null && codesConfirmed.size() != 0) {

            Log.d(TAG, "Si hay notificación");

            for (int i = 0; i < codesConfirmed.size(); i++) {

                String mensaje = "Su envío: ";

                if(codesConfirmed.get(i).get("CI_reference") != null && !codesConfirmed.get(i).get("CI_reference").equals("")){
                    mensaje += codesConfirmed.get(i).get("CI_reference");
                }else if(codesConfirmed.get(i).get("shortWayBillId") != null && !codesConfirmed.get(i).get("shortWayBillId").equals("")){
                    mensaje += codesConfirmed.get(i).get("shortWayBillId");
                }else if(codesConfirmed.get(i).get("wayBillId") != null && codesConfirmed.get(i).get("wayBillId").equals("")){
                    mensaje += codesConfirmed.get(i).get("wayBillId");
                }

                mensaje += ", fue entregado.";

                Intent intentSplash = new Intent(this, MainActivity.class);
                PendingIntent pIntent = PendingIntent.getActivity(this,
                        0, intentSplash, 0);

                Notification noti = new NotificationCompat.Builder(this)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(mensaje)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentIntent(pIntent).build();
                Uri alarmSound = null;
                if (alarmSound == null) {
                    alarmSound = RingtoneManager
                            .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                }

                noti.sound = alarmSound;
                noti.defaults |= Notification.DEFAULT_VIBRATE;
                noti.setLatestEventInfo(this,
                        getText(R.string.app_name), mensaje, pIntent);
                //Para generar un id unico para cada notificación, de esta manera si hay 2 códigos de rastreo que requieran
                //notificación cada uno tendra su espacio en la barra de notificaciones.
                int id_codigo_rastreo = Integer.parseInt(codesConfirmed.get(i).get("shortWayBillId").substring(0, 7));

                notificationManager.notify(id_codigo_rastreo,
                        noti);

            }
        }else{
            Log.d(TAG, "No hay notificación");
            //Toast.makeText(getApplicationContext(),"No hay cambios, no hay notificación",Toast.LENGTH_LONG).show();
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, nextUpdateTimeMillis, pendingIntent);

    }


    /**
     * Checks for Internet access.
     * @return
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


    /**
     * Check that the favorites do not exceed 30 days since they were saved.
     */
    public void checkDateFavorite(){

        listFavorites = Favorites.getAll(this);
        Log.d(TAG, "Tamaño ListFavorites: " + listFavorites.size());

        Log.d(TAG, "Código: "+listFavorites.get(0).get("codigo_rastreo"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String dateInString = "";
        long days = 0;

        for(int i = 0; i<listFavorites.size(); i++){

            dateInString = listFavorites.get(i).get("add_date");
            Log.d(TAG,""+dateInString);

            try {

                Date date = formatter.parse(dateInString);
                days = DatesHelper.daysFromLastUpdate(date);

                if(days > 30){
                    Favorites.delete(this,listFavorites.get(i).get("id_favoritos"));
                    Log.d(TAG,"+ de 30 días, favorito eliminado!! Código: "+listFavorites.get(i).get("codigo_rastreo"));
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        listFavorites = Favorites.getAll(this);

        if(listFavorites == null){

            Log.d(TAG, "No hay favoritos para actualizar, se borraron porque pasaron + de 30 días");
            scheduleNextUpdate();

        }else {

            updateInfoFavorites();
        }

    }

    /**
     * Upgrade codes saved in favorites.
     */
    public void updateInfoFavorites(){

        j               = 0;
        aux = new ArrayList<Map<String, String>>();
        favorite_id = "";

        aux = Favorites.getAllUnconfirmedFavorites(this);

        for (int i = 0; i < aux.size(); i++) {
            Map<String, String> requestData = new HashMap<>();
            Map<String, String> code_item = new HashMap<>();
            code_item = aux.get(i);

            favorite_id = code_item.get("id_favoritos");

            Log.d("Codigo for", code_item.get("codigo_rastreo"));

            requestData.put("initialWaybill", "");
            requestData.put("finalWaybill", "");

            if (code_item.get("codigo_rastreo").length() == 10)
                requestData.put("waybillType", "R");
            else
                requestData.put("waybillType", "G");

            requestData.put("wayBills", code_item.get("codigo_rastreo"));

            requestData.put("type", "L");
            requestData.put("includeDimensions", "true");
            requestData.put("includeWaybillReplaceData", "true");
            requestData.put("includeReturnDocumentData", "true");
            requestData.put("includeMultipleServiceData", "true");
            requestData.put("includeInternationalData", "true");
            requestData.put("includeSignature", "true");
            requestData.put("includeCustomerInfo", "true");
            requestData.put("includeHistory", "true");
            requestData.put("historyType", "ALL");
            requestData.put("filterInformation", "false");
            requestData.put("filterType", "");

            RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_TRACKING_LIST_CODES, requestData,this);

        }



    }

    @Override
    public void prepareRequest(METHOD method, Map<String, String> params, boolean includeCredentials) {

    }

    /**
     * Receives responses from the server.
     * @param stringResponse
     */
    @Override
    public void decodeResponse(String stringResponse) {

        try {

            if (stringResponse == null) {
                Log.d(TAG, "respuesta null");
                j++;
                Log.d(TAG, "contador:" + j);
                if (j == aux.size()) {

                    //Listo
                    Log.d(TAG, "Favoritos actualizados");
                    scheduleNextUpdate();

                }
            } else if(stringResponse.contains("falla_timeOut")) {
                Log.d(TAG, "contador:" + j);
                j++;
                Log.v(TAG, stringResponse);

                if (j == aux.size()) {
                    Log.d(TAG, "Favoritos actualizados - falla_timeOut");
                    scheduleNextUpdate();
                }

            }else {

                Log.d(TAG,"SIZE: "+stringResponse.length());
                Log.v(TAG, stringResponse);
                ArrayList<Map<String, String>> auxResponseList;
                auxResponseList = RequestManager.sharedInstance().getResponseArray();
                if (auxResponseList != null) {


                    //Comprobar si llego una nueva actualización.
                    try {
                        //if (auxResponseList.get(0).get("statusSPA").equals("EN_TRANSITO")) {//Para TEST
                        if (auxResponseList.get(0).get("statusSPA").equals("CONFIRMADO")) {
                            if (auxResponseList.get(0).get("shortWayBillId") != null && !auxResponseList.get(0).get("shortWayBillId").equals("")) {
                                //Se cambia el status.
                                auxResponseList.get(0).put("estatus1","Entregado");
                                //Se actualiza la informacion del favorito.
                                Favorites.insertMap(this, auxResponseList.get(0));
                                //Se agrega a la lista para mandar una notificación local cuando acabe de actualizar todos los códigos.
                                codesConfirmed.add(auxResponseList.get(0));
                            }

                            if (auxResponseList.size() > 0) {
                                for(int i=1; i<auxResponseList.size(); i++){
                                    //Se agrega el id del favorito al que corresponde para reconocerlo.
                                    auxResponseList.get(i).put("favorite_id",favorite_id);
                                    //Se inserta la información de la historia
                                    History.insertMapByFavorite(this, auxResponseList.get(i));

                                }
                            }
                            Log.d(TAG,"El status de este es CONFIRMADO.");
                        }else{
                            Log.d(TAG,"El status de este código aun no se actualiza.");
                        }
                    }catch (NullPointerException npe){
                        Log.d(TAG, "La información de este código llego incompleta");
                    }

                }

                j++;
                Log.d(TAG, "contador:" + j);
                if (j == aux.size()) {
                    //Listo
                    Log.d(TAG, "Favoritos actualizados");
                    scheduleNextUpdate();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            scheduleNextUpdate();
        }

    }



}
