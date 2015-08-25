package com.sellcom.apps.tracker_material.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;
import android.content.Context;

import com.daimajia.swipe.SwipeLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sellcom.apps.tracker_material.Activities.MainActivity;
import com.sellcom.apps.tracker_material.Adapters.FavoriteListAdapter;
import com.sellcom.apps.tracker_material.Adapters.RastreoListAdapter;
import com.sellcom.apps.tracker_material.Async_Request.METHOD;
import com.sellcom.apps.tracker_material.Async_Request.RequestManager;
import com.sellcom.apps.tracker_material.Async_Request.UIResponseListenerInterface;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.DatesHelper;
import com.sellcom.apps.tracker_material.Utils.DialogManager;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;
import com.sellcom.apps.tracker_material.Utils.Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.model.Favorites;
import database.model.History;

/**
 * Created by rebecalopezmartinez on 21/05/15.
 */
public class FragmentRastreo extends TrackerFragment implements View.OnClickListener, AdapterView.OnItemClickListener,RastreoListAdapter.setNumCodes {

    String TAG= "FRAG_RASTREO";

    private FragmentManager         fragmentManager;
    private FragmentTransaction     fragmentTransaction;
    private TrackerFragment         fragment;

    Button                     info;
    Button                          rastreo;
    Button                          escanear;
    Button                     agregar;
    EditText                        codigo;
    TextView    txv_num_sends,
                footer;

    FragmentDialogHelp fdh;

    private int restantes = 10;

    MenuItem favorite;



    ListView lst_rastreo;
    RastreoListAdapter lstAdapter;
    static ArrayList<Map<String,String>> codes_array = new ArrayList<Map<String,String>>();

    Context context;

    private ArrayList<Map<String,String>> listFavorites;



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = getActivity();
       // codes_array = Rastreo_tmp.getAllInMaps(context);
        //lstAdapter = new RastreoAdapter(getActivity(),codes_array);
        //lstAdapter.setCodesNumbers(this);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_rastreo, container, false);
        context = getActivity();


        TrackerFragment.section_index = 0;

        info        = (Button)view.findViewById(R.id.btn_help);
        rastreo     = (Button)view.findViewById(R.id.btn_rastrear);
        escanear    = (Button) view.findViewById(R.id.btn_escanear);
        agregar     = (Button) view.findViewById(R.id.btn_agregar);
        lst_rastreo = (ListView)view.findViewById(R.id.liv_rastreo);
        lst_rastreo.setOnItemClickListener(this);
        codigo      = (EditText)view.findViewById(R.id.edt_codigo);
        txv_num_sends = (TextView)view.findViewById(R.id.txv_num_sends);

        footer      = (TextView)view.findViewById(R.id.footer);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String currentYear = formatter.format(new Date());
        footer.setText("©2012-"+currentYear+" "+getString(R.string.footer));

        Utilities.hideKeyboard(context,codigo);
        //codes_array = Rastreo_tmp.getAllInMaps(context);

        if( codes_array != null){
            Log.v(TAG,"aux size"+codes_array.size());
            if(codes_array.size() == 10){
                agregar.setEnabled(false);
                codigo.setText("");
                codigo.setEnabled(false);
                escanear.setEnabled(false);
            }
            lstAdapter = new RastreoListAdapter(getActivity(),codes_array);
            lstAdapter.setCodesNumbers(this);
            lst_rastreo.setAdapter(lstAdapter);
            lstAdapter.notifyDataSetChanged();
        }

        try {
            DialogManager.sharedInstance().dismissDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }

        info.setOnClickListener(this);
        escanear.setOnClickListener(this);
        rastreo.setOnClickListener(this);
        agregar.setOnClickListener(this);



        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            codes_array = (ArrayList<Map<String,String>>) getArguments().getSerializable("codes");
        }
        setHasOptionsMenu(true);
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("codes", codes_array);
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

                ArrayList<Map<String,String>> listFavorites= Favorites.getAll(context);
                if(listFavorites==null){
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "No hay registro de favoritos.", 3000);

                }else {
                    favorite.setEnabled(false);
                    //Log.d(TAG,"item selected");
                    // Bundle bundle= new Bundle();
                    // bundle.putSerializable("codes_info", codes_info);
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING,"Actualizando favoritos...",0);

                    new CheckDateFavorite().execute();
                }
                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_help:

                fragmentManager = getActivity().getSupportFragmentManager();
                fdh = new FragmentDialogHelp();
                fdh.show(fragmentManager,"FRAG_DIALOG_HELP");


                break;

            //15000
            case R.id.btn_escanear:
                //Toast.makeText(context,"Módulo en Desarrollo",Toast.LENGTH_SHORT).show();
                List<String> oDesiredFormats = Arrays.asList("PDF_417,CODE_128,QR_CODE".split(","));
                IntentIntegrator integrator= IntentIntegrator.forSupportFragment(this);
                integrator.initiateScan(oDesiredFormats);
                codigo.setText("");
                break;

            case R.id.btn_rastrear:
                if(codes_array == null || codes_array.size()<=0){
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_lista_vacia),3000);
                }
                else {

                    new GetCodesInfo().execute();
                }

                break;

            case R.id.btn_agregar:
                Utilities.hideKeyboard(context,codigo);
                addCode();
                break;
        }

    }

    public void addCode(){


        String codigoStr = codigo.getText().toString().trim();
        //codes_array = Rastreo_tmp.getAllInMaps(context);

        if(codigoStr.length() == 10){

            boolean correctCode = false;

            for(int i = 0; i<codigoStr.length(); i++){
                correctCode = Utilities.validatedigits("" + codigoStr.charAt(i));

                if(!correctCode){
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR,getString(R.string.code_invalid),3000);
                    return;
                }
            }



            if(!compareCodeRepeat(codigoStr)){
                return;
            }
            setCodeRatreo(codigoStr);

        }else if(codigoStr.length() == 22){

            boolean correctCode = false;

            for(int i = 0; i<codigoStr.length(); i++){

                if(i == 0){
                    correctCode = Utilities.validatetext(""+codigoStr.charAt(i));
                }else if(i > 0 && i < 3){
                    correctCode = Utilities.validatetext(""+codigoStr.charAt(i));
                }else if(i > 2 && i < 10){
                    correctCode = Utilities.validatedigits("" + codigoStr.charAt(i));
                }else if(i > 9 && i < 13){
                    correctCode = Utilities.validatetext("" + codigoStr.charAt(i));
                }else if(i > 12 && i < 15){
                    correctCode = Utilities.validatetext(""+codigoStr.charAt(i));
                }else if(i > 14 && i < 22){
                    correctCode = Utilities.validatedigits(""+codigoStr.charAt(i));
                }

                if(!correctCode){
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR,getString(R.string.code_invalid),3000);
                    return;
                }

            }



            if(!compareCodeRepeat(codigoStr)){
                return;
            }

            setCodeRatreo(codigoStr);

        } else {
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR,getString(R.string.error_tamaño_codigo),3000);

        }


    }

    public boolean compareCodeRepeat(String code){
        if(codes_array != null) {
            for (int j = 0; j < codes_array.size(); j++) {
                Map<String, String> aux = new HashMap<>();
                aux = codes_array.get(j);
                String c = aux.get("codigo");
                if (c.equals(code)) {
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_cod_repetido),3000);
                    return false;
                }

            }
            return true;

        }
        return false;
    }

    public void setCodeRatreo(String codeRatreo){//Agrega el código a la lista de rastreo una vez que ya se validó.

        Map<String,String> item = new HashMap<>();
        item.put("codigo", codeRatreo);
        item.put("favorito", "false");
        //Rastreo_tmp.insert(context, item);
        codes_array.add(item);
        //codes_array = Rastreo_tmp.getAllInMaps(context);
        lstAdapter.notifyDataSetChanged();
        /*
        lstAdapter = new RastreoListAdapter(getActivity(),codes_array);
        lstAdapter.setCodesNumbers(this);
        lst_rastreo.setAdapter(lstAdapter);*/
        //lst_rastreo.setOnItemClickListener(this);

        if(codes_array.size() == 10) {
            agregar.setEnabled(false);
            codigo.setText("");
            codigo.setEnabled(false);
            escanear.setEnabled(false);
        }
        codigo.setText("");

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void setCodes(int restantes) {

            txv_num_sends.setText("Has ingresado " + restantes + " de 10 guías para rastrear.");

    }

    @Override
    public void removeCode(int position) {

        codes_array.remove(position);

        if(codes_array.size() != 10) {
            agregar.setEnabled(true);
            codigo.setText("");
            codigo.setEnabled(true);
            escanear.setEnabled(true);
        }

        setCodes(codes_array.size());
        lstAdapter.stateDelete[position] = 0;
        lstAdapter.notifyDataSetChanged();
        /*
        lstAdapter = new RastreoListAdapter(getActivity(),codes_array);
        lstAdapter.setCodesNumbers(this);
        lst_rastreo.setAdapter(lstAdapter);
        */
    }


    public class GetCodesInfo extends AsyncTask<Void, Void, String> implements UIResponseListenerInterface {

        int j = 0;
        ArrayList<ArrayList<Map<String, String>>> aux = new ArrayList<ArrayList<Map<String,String>>>();
        boolean flag = false;

        protected void onPreExecute() {
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING, getString(R.string.cargando), 0);
        }

        @Override
        protected String doInBackground(Void... params) {

            for (int i = 0; i < codes_array.size(); i++) {
                Map<String, String> requestData = new HashMap<>();
                Map<String, String> code_item = new HashMap<>();
                code_item = codes_array.get(i);

                Log.d("Codigo for", code_item.get("codigo"));

                requestData.put("initialWaybill", "");
                requestData.put("finalWaybill", "");

                if (code_item.get("codigo").length() == 10)
                    requestData.put("waybillType", "R");
                else
                    requestData.put("waybillType", "G");

                requestData.put("wayBills", code_item.get("codigo"));

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

                RequestManager.sharedInstance().setListener(this);
                RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_TRACKING_LIST_CODES, requestData);

            }

            return null;

        }

        @Override
        public void prepareRequest(METHOD method, Map<String, String> params, boolean includeCredentials) {

        }

        @Override
        public void decodeResponse(String stringResponse) {

            try {
                if (stringResponse == null) {
                    Log.d(TAG, "respuesta null");
                    j++;
                    Log.d(TAG, "contador:" + j);
                    if (j == codes_array.size()) {
                        DialogManager.sharedInstance().dismissDialog();
                        favorite.setEnabled(true);
                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_servicio1), 3000);

                    }
                } else {
                    if (stringResponse.equals("0")) {
                        Log.v(TAG, "El servidor devolvió null o 0");
                        String cod_aux = codes_array.get(j).get("codigo");
                        ArrayList<Map<String, String>> auxResponseList = new ArrayList<Map<String, String>>();
                        Map<String, String> auxResponse = new HashMap<>();
                        if (cod_aux.length() == 10) {
                            auxResponse.put("shortWayBillId", cod_aux);
                            auxResponse.put("wayBill", "Sin información");
                            auxResponse.put("statusSPA", "Sin información");
                        } else {
                            auxResponse.put("shortWayBillId", "Sin información");
                            auxResponse.put("wayBill", cod_aux);
                            auxResponse.put("statusSPA", "Sin información");
                        }
                        auxResponseList.add(auxResponse);
                        aux.add(auxResponseList);
                        Log.v(TAG + "aux", "" + aux.size());

                    } else {
                        Log.v(TAG, stringResponse);
                        ArrayList<Map<String, String>> auxResponseList;
                        auxResponseList = RequestManager.sharedInstance().getResponseArray();
                        if (auxResponseList != null) {
                            // Log.v(TAG + "auxResponse", "" + auxResponse.size());

                            if (auxResponseList.size() > 0) {
                                aux.add(auxResponseList);
                            }




                            //   Log.v(TAG + "aux", "" + aux.size());
                        }

                    }
                    j++;
                    Log.d(TAG, "contador:" + j);
                    if (j == codes_array.size()) {
                        /*ArrayList<ArrayList<Map<String, String>>> codes_ver;
                        codes_ver = verifyFavorites(aux);*/

                        for (int i = 0; i < aux.size(); i++) {
                            if(!aux.get(i).get(0).get("wayBill").equals("Sin información") || !aux.get(i).get(0).get("shortWayBillId").equals("Sin información")) {
                                for (int j = 0; j < aux.size(); j++) {
                                    j = i + j;

                                    if (i != j) {
                                        if( j < aux.size()) {
                                            if (aux.get(i).get(0).get("wayBill").equals(aux.get(j).get(0).get("wayBill"))) {
                                                aux.remove(i);
                                                i++;
                                                break;
                                            }

                                        }
                                    }

                                }
                            }
                        }


                        Bundle bundle = new Bundle();
                        bundle.putSerializable("codes_info", aux);

                        fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragment = new FragmentRastreoEfectuado();
                        fragment.addFragmentToStack(getActivity());
                        fragment.setArguments(bundle);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.replace(R.id.container, fragment, TAG);
                        fragmentTransaction.commit();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode,resultCode,data);

        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == IntentIntegrator.REQUEST_CODE) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                scanResult(scanningResult);
            }
        }
    }

    public void scanResult(IntentResult scanResult) {
        if (scanResult != null) {
            String preCod = scanResult.getContents().trim();
            Log.d("Longitud", String.valueOf(preCod.length()));
            Log.d("Codigo", preCod);

            if(preCod.length() >= 22) {
                if (Utilities.validateCode(preCod.substring(0, 22))) {
                    if (!compareCodeRepeat(scanResult.getContents().trim())) {
                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_cod_repetido), 3000);
                    } else {
                        setCodeRatreo(preCod.substring(0, 22));
                    }
                } else
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.code_invalid), 3000);
            }else
                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.code_invalid), 3000);
        }
    }

    public class CheckDateFavorite extends AsyncTask<Void, Void, Void>{


        @Override
        protected Void doInBackground(Void... params) {

            listFavorites = Favorites.getAll(context);
            Log.d(TAG,"Tamaño ListFavorites: "+listFavorites.size());


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
                        Favorites.delete(context,listFavorites.get(i).get("id_favoritos"));
                        Log.d(TAG,"+ de 30 días, favorito eliminado!! Código: "+listFavorites.get(i).get("codigo_rastreo"));
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }


/*
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
*/
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            listFavorites = Favorites.getAll(context);

            if(listFavorites == null){
                DialogManager.sharedInstance().dismissDialog();
                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "No hay registro de favoritos.", 3000);
            }else {

                new UpdateInfoFavorites().execute();
            }

        }
    }


    public class UpdateInfoFavorites extends AsyncTask<Void, Void, Void> implements UIResponseListenerInterface{

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

                RequestManager.sharedInstance().setListener(this);
                RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_TRACKING_LIST_CODES, requestData);

            }

            return null;
        }


        @Override
        public void prepareRequest(METHOD method, Map<String, String> params, boolean includeCredentials) {

        }

        @Override
        public void decodeResponse(String stringResponse) {


            try {
                if (stringResponse == null) {
                    Log.d(TAG, "respuesta null");
                    j++;
                    Log.d(TAG, "contador:" + j);
                    if (j == aux.size()) {

                        fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragment = new FragmentFavorites();
                        fragment.addFragmentToStack(getActivity());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.replace(R.id.container, fragment, FragmentFavorites.TAG);
                        fragmentTransaction.commit();

                    }
                } else {
                    {
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
                            if (auxResponseList.size() > 0) {
                                for(int i=1; i<auxResponseList.size(); i++){

                                    auxResponseList.get(i).put("favorite_id",favorite_id);
                                    History.insertMapByFavorite(context, auxResponseList.get(i));

                                }
                            }

                            //   Log.v(TAG + "aux", "" + aux.size());
                        }

                    }
                    j++;
                    Log.d(TAG, "contador:" + j);
                    if (j == aux.size()) {

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



}

