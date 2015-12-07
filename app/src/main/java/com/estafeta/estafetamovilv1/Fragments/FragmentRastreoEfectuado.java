package com.estafeta.estafetamovilv1.Fragments;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;

import com.estafeta.estafetamovilv1.Activities.MainActivity;
import com.estafeta.estafetamovilv1.Adapters.RastreoEfectuadoAdapter;
import com.estafeta.estafetamovilv1.Async_Request.METHOD;
import com.estafeta.estafetamovilv1.Async_Request.RequestManager;
import com.estafeta.estafetamovilv1.Async_Request.UIResponseListenerInterface;
import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Utils.DatesHelper;
import com.estafeta.estafetamovilv1.Utils.DialogManager;
import com.estafeta.estafetamovilv1.Utils.TrackerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import database.model.Favorites;
import database.model.History;

/**
 * This class shows the result of tracking codes entered.
 */
public class FragmentRastreoEfectuado extends TrackerFragment implements AdapterView.OnItemClickListener {


    TrackerFragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private TextView footer;

    String TAG = "FRAG_RASTREO_EFECTUADO";
    Context context;
    ListView lst_rastreo_efectuado;

    RastreoEfectuadoAdapter efectuadoAdapter;
    ArrayList<ArrayList<Map<String, String>>> codes_info = new ArrayList<>();
    private ArrayList<Map<String,String>> listFavorites;

    MenuItem favorite;
    ArrayList<ArrayList<Map<String, String>>> codes;

    //Para las notificaciones.
    private ArrayList<Map<String, String>>      codesConfirmed;
    private NotificationManager notificationManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_rastreo_efectuado, container, false);
        TrackerFragment.section_index = 0;
        context = getActivity();
        lst_rastreo_efectuado = (ListView)view.findViewById(R.id.lst_rastreo_efectuado);

        footer      = (TextView)view.findViewById(R.id.footer);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String currentYear = formatter.format(new Date());
        footer.setText("©2012-"+currentYear+" "+getString(R.string.footer));

        codes_info = (ArrayList<ArrayList<Map<String,String>>>) getArguments().getSerializable("codes_info");


                        codes = verifyFavorites(codes_info);



        efectuadoAdapter = new RastreoEfectuadoAdapter(getActivity(),context ,codes,getActivity().getSupportFragmentManager());
        lst_rastreo_efectuado.setAdapter(efectuadoAdapter);

        try {
            DialogManager.sharedInstance().dismissDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
    }

    /**
     * Check if the code is already on the favorite list.
     * @param values
     * @return
     */
    public ArrayList<ArrayList<Map<String, String>>> verifyFavorites(ArrayList<ArrayList<Map<String, String>>> values){
        Log.d(TAG, "verifyFavorites");
        ArrayList<ArrayList<Map<String, String>>> resp = new ArrayList<ArrayList<Map<String, String>>>();
        for (int j = 0; j<values.size(); j++) {

            ArrayList<Map<String, String>> respList = new ArrayList<>();
            Map<String, String> item = new HashMap<>();
            item = values.get(j).get(0);

            if (item.get("wayBill") != null) {
                String auxFavId = Favorites.getIdByWayBill(context, item.get("wayBill"));
                //Log.d(TAG, "DB aux = "+auxFavId);
                if (auxFavId != null) {
                    item.put("favorites", "true");
                    //Log.d(TAG, "item true");
                } else {
                    item.put("favorites", "false");
                    //Log.d(TAG, "item false");
                }
            } else {
                item.put("favorites", "false");
                //Log.d(TAG, "item false");
            }
            respList.add(item);

            for (int k = 1; k < values.get(j).size(); k++) {
                item = new HashMap<>();
                item = values.get(j).get(k);

                respList.add(item);
            }
            resp.add(respList);

        }
        return resp;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (!((MainActivity) getActivity()).isDrawerOpen) {
            menu.clear();
            inflater.inflate(R.menu.rastreo, menu);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        favorite = menu.findItem(R.id.add_favorite);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d(TAG, "Selected:  " + item.getItemId());

        switch (item.getItemId()) {

            case R.id.add_favorite:
                favorite.setEnabled(true);
                ArrayList<Map<String,String>> listFavorites= Favorites.getAll(context);
                if(listFavorites==null){
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "No hay registro de favoritos.", 3000);

                }else {
                    favorite.setEnabled(false);
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING,"Actualizando favoritos...",0);

                    new CheckDateFavorite().execute();
                }
                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.v(TAG, "item id" + id);
        Toast.makeText(context,
                "Click ListItem Number " + position, Toast.LENGTH_LONG)
                .show();

    }


    /**
     * Check that the favorites do not exceed 30 days since they were saved.
     */
    public class CheckDateFavorite extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {


            listFavorites = Favorites.getAll(context);
            Log.d(TAG, "Tamaño ListFavorites: " + listFavorites.size());


            Log.d(TAG, "Código: " + listFavorites.get(0).get("codigo_rastreo"));


            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String dateInString = "";
            long days = 0;

            for (int i = 0; i < listFavorites.size(); i++) {

                dateInString = listFavorites.get(i).get("add_date");
                Log.d(TAG, "" + dateInString);

                try {

                    Date date = formatter.parse(dateInString);
                    days = DatesHelper.daysFromLastUpdate(date);

                    if (days > 30) {
                        Favorites.delete(context, listFavorites.get(i).get("id_favoritos"));
                        Log.d(TAG, "+ de 30 días, favorito eliminado!! Código: " + listFavorites.get(i).get("codigo_rastreo"));
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            listFavorites = Favorites.getAll(context);

            if(listFavorites == null){
                favorite.setEnabled(true);
                DialogManager.sharedInstance().dismissDialog();
                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "No hay registro de favoritos.", 3000);
            }else {

                new UpdateInfoFavorites().execute();
            }
        }
    }


    /**
     * Upgrade codes saved in favorites.
     */
    public class UpdateInfoFavorites extends AsyncTask<Void, Void, Void> implements UIResponseListenerInterface {

        int j = 0;
        ArrayList<Map<String,String>> aux = new ArrayList<Map<String,String>>();
        String favorite_id = "";

        @Override
        protected Void doInBackground(Void... params) {

            aux = Favorites.getAll(context);

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

            return null;
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

                        checkForNotifications();

                        fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragment = new FragmentFavorites();
                        fragment.addFragmentToStack(getActivity());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.replace(R.id.container, fragment, FragmentFavorites.TAG);
                        fragmentTransaction.commit();

                    }
                }else if(stringResponse.contains("falla_timeOut")) {
                    Log.d(TAG, "contador:" + j);
                    j++;
                    Log.v(TAG, stringResponse);

                    if (j == aux.size()) {
                        Log.d(TAG, "Favoritos actualizados - falla_timeOut");

                        checkForNotifications();

                        fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragment = new FragmentFavorites();
                        fragment.addFragmentToStack(getActivity());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.replace(R.id.container, fragment, FragmentFavorites.TAG);
                        fragmentTransaction.commit();
                    }

                } else {
                        Log.v(TAG, stringResponse);
                        ArrayList<Map<String, String>> auxResponseList;
                        auxResponseList = RequestManager.sharedInstance().getResponseArray();
                        if (auxResponseList != null) {
                            // Log.v(TAG + "auxResponse", "" + auxResponse.size());
/*
                            Map<String,String> map = new HashMap<String,String>();

                            map.put("H_eventDateTime","7/8/2015 2:32:00 PM");
                            map.put("H_eventDescriptionSPA","Llegada al centro de distribución X");
                            map.put("H_eventPlaceName","Morelos");
                            auxResponseList.add(map);
*/

                            try {

                                if (auxResponseList.get(0).get("statusSPA").equals("CONFIRMADO")) {
                                    if (Favorites.getUnconfirmedByShortWayBill(context,auxResponseList.get(0).get("shortWayBillId"))) {
                                        auxResponseList.get(0).put("estatus1", "Entregado");
                                        Favorites.insertMap(context, auxResponseList.get(0));
                                        codesConfirmed.add(auxResponseList.get(0));
                                        Log.d(TAG, "¡Hay una actualización para un código con notificación!");
                                    }else{
                                        Log.d(TAG, "No hay notificación");
                                    }
                                }else{
                                    Log.d(TAG, "Código PENDIENTE");
                                }
                            }catch (Exception e){
                                Log.w(TAG,"Ocurrio un error al comparar los códigos de rastreo");
                            }

                            if (auxResponseList.size() > 0) {
                                for(int i=1; i<auxResponseList.size(); i++){

                                    auxResponseList.get(i).put("favorite_id",favorite_id);
                                    History.insertMapByFavorite(context, auxResponseList.get(i));

                                }
                            }

                            //   Log.v(TAG + "aux", "" + aux.size());
                        }

                    j++;
                    Log.d(TAG, "contador:" + j);
                    if (j == aux.size()) {

                        checkForNotifications();

                        fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragment = new FragmentFavorites();
                        fragment.addFragmentToStack(getActivity());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.replace(R.id.container, fragment, FragmentFavorites.TAG);
                        fragmentTransaction.commit();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


    /**
     * Check if there is a change in a favorite and send a local notification.
     */
    private void checkForNotifications(){

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

                Intent intentSplash = new Intent(context, MainActivity.class);
                PendingIntent pIntent = PendingIntent.getActivity(context,
                        0, intentSplash, 0);

                Notification noti = new NotificationCompat.Builder(context)
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
                noti.setLatestEventInfo(context,
                        getText(R.string.app_name), mensaje, pIntent);

                //Para generar un id unico para cada notificación, de esta manera si hay 2 códigos de rastreo que requieran
                //notificación cada uno tendra su espacio en la barra de notificaciones.
                int id_codigo_rastreo = Integer.parseInt(codesConfirmed.get(i).get("shortWayBillId").substring(0, 7));

                notificationManager.notify(id_codigo_rastreo,
                        noti);

            }
        }else{
            Log.d(TAG, "No hay notificación");
        }

    }

}
