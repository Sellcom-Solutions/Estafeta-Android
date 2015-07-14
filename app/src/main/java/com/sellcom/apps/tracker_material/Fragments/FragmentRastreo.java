package com.sellcom.apps.tracker_material.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
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
import android.content.Context;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sellcom.apps.tracker_material.Activities.MainActivity;
import com.sellcom.apps.tracker_material.Adapters.RastreoListAdapter;
import com.sellcom.apps.tracker_material.Async_Request.METHOD;
import com.sellcom.apps.tracker_material.Async_Request.RequestManager;
import com.sellcom.apps.tracker_material.Async_Request.UIResponseListenerInterface;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.DialogManager;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;
import com.sellcom.apps.tracker_material.Utils.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.model.Favorites;

/**
 * Created by rebecalopezmartinez on 21/05/15.
 */
public class FragmentRastreo extends TrackerFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    String TAG= "FRAG_RASTREO";

    private FragmentManager         fragmentManager;
    private FragmentTransaction     fragmentTransaction;
    private TrackerFragment         fragment;

    Button                     info;
    Button                          rastreo;
    Button                          escanear;
    Button                     agregar;
    EditText                        codigo;

    ListView lst_rastreo;
    RastreoListAdapter lstAdapter;
    static ArrayList<Map<String,String>> codes_array = new ArrayList<Map<String,String>>();

    Context context;

    public FragmentRastreo() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = getActivity();
       // codes_array = Rastreo_tmp.getAllInMaps(context);
        lstAdapter = new RastreoListAdapter(getActivity(),codes_array);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_rastreo, container, false);
        context = getActivity();

        info        = (Button)view.findViewById(R.id.btn_help);
        rastreo     = (Button)view.findViewById(R.id.btn_rastrear);
        escanear    = (Button) view.findViewById(R.id.btn_escanear);
        agregar     = (Button) view.findViewById(R.id.btn_agregar);
        lst_rastreo = (ListView)view.findViewById(R.id.liv_rastreo);
        codigo      = (EditText)view.findViewById(R.id.edt_codigo);

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
        outState.putSerializable("codes",codes_array);
    }

        @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (!((MainActivity) getActivity()).isDrawerOpen) {
            menu.clear();
            inflater.inflate(R.menu.rastreo, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "Selected:  " + item.getItemId());
        switch (item.getItemId()) {

            case R.id.add_favorite:
                //Log.d(TAG,"item selected");
               // Bundle bundle= new Bundle();
               // bundle.putSerializable("codes_info", codes_info);
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragment = new FragmentFavorites();
                fragment.addFragmentToStack(getActivity());
             //   fragment.setArguments(bundle);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, fragment, TAG);
                fragmentTransaction.commit();
                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_help:
                fragmentManager = getActivity().getSupportFragmentManager();
                FragmentDialogHelp fdh = new FragmentDialogHelp();
                fdh.show(fragmentManager,"FRAG_DIALOG_HELP");
                break;

            //15000
            case R.id.btn_escanear:
                //Toast.makeText(context,"Módulo en Desarrollo",Toast.LENGTH_SHORT).show();
                List<String> oDesiredFormats = Arrays.asList("PDF_417".split(","));
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
        String codigoStr = codigo.getText().toString();
        //codes_array = Rastreo_tmp.getAllInMaps(context);

        if(codigoStr.length()== 10 || codigoStr.length()== 22){
            if(codes_array != null) {
                for (int i = 0; i < codes_array.size(); i++) {
                    Map<String, String> aux = new HashMap<>();
                    aux = codes_array.get(i);
                    String c = aux.get("codigo");
                    if (c.equals(codigoStr)) {
                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_cod_repetido),3000);
                        return;
                    }

                }
            }

            Map<String,String> item = new HashMap<>();
            item.put("codigo",codigoStr);
            item.put("favorito","false");
            //Rastreo_tmp.insert(context, item);
            codes_array.add(item);
            //codes_array = Rastreo_tmp.getAllInMaps(context);

            lstAdapter = new RastreoListAdapter(getActivity(),codes_array);
            lst_rastreo.setAdapter(lstAdapter);
            //lst_rastreo.setOnItemClickListener(this);

             if(codes_array.size() == 10) {
                  agregar.setEnabled(false);
                  codigo.setText("");
                  codigo.setEnabled(false);
                  escanear.setEnabled(false);
              }
            codigo.setText("");
        }
        else {
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR,getString(R.string.error_tamaño_codigo),3000);

        }


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
                if(stringResponse == null){
                    Log.d(TAG,"respuesta null");
                    j++;
                    Log.d(TAG, "contador:" + j);
                    if (j == codes_array.size()) {
                        DialogManager.sharedInstance().dismissDialog();
                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_servicio1), 3000);
                    }
                }
                else {
                    if (stringResponse.equals("0")) {
                        Log.v(TAG, "El servidor devolvió null o 0");
                        String cod_aux = codes_array.get(j).get("codigo");
                        ArrayList<Map<String,String>> auxResponseList  = new ArrayList<Map<String, String>>();
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
                        ArrayList<Map<String,String>> auxResponseList;
                        auxResponseList = RequestManager.sharedInstance().getResponseArray();
                        if (auxResponseList != null) {
                            // Log.v(TAG + "auxResponse", "" + auxResponse.size());
                            if (auxResponseList.size() > 0){
                                aux.add(auxResponseList);
                            }

                            //   Log.v(TAG + "aux", "" + aux.size());
                        }

                    }
                    j++;
                    Log.d(TAG, "contador:" + j);
                    if (j == codes_array.size()) {
                        Log.d(TAG, "flag " + flag + " j " + j);
                        ArrayList<ArrayList<Map<String, String>>> codes_ver;
                        codes_ver = verifyFavorites(aux);

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("codes_info", codes_ver);

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
        public ArrayList<ArrayList<Map<String, String>>> verifyFavorites(ArrayList<ArrayList<Map<String, String>>> values){
            Log.d(TAG, "verifyFavorites");
            ArrayList<ArrayList<Map<String, String>>> resp = new ArrayList<ArrayList<Map<String, String>>>();
            for (int j = 0; j<values.size(); j++) {
                Log.d(TAG, "verifyFavorites111111");
                //for (int i = 0; i < values.get(j).size(); i++) {
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
                //}
            }
            return resp;
        }

        protected void onPostExecute(String result) {


        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode,resultCode,data);

        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == IntentIntegrator.REQUEST_CODE) {
                IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                scanResult(scanningResult);
            }
        }
    }

    public void scanResult(IntentResult scanResult) {
        if (scanResult != null) {
            String preCod = scanResult.getContents().trim();
            Log.d("Longitud",String.valueOf(preCod.length()));
            Log.d("Codigo",preCod);
            if ( Utilities.validateCode(preCod,context) )
                codigo.setText(scanResult.getContents());
            else
                Toast.makeText(context,context.getResources().getString(R.string.code_incorrect),Toast.LENGTH_SHORT).show();
        }
    }

}

