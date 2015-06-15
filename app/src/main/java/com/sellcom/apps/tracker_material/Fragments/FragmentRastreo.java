package com.sellcom.apps.tracker_material.Fragments;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.content.Context;

import com.sellcom.apps.tracker_material.Activities.MainActivity;
import com.sellcom.apps.tracker_material.Adapters.RastreoListAdapter;
import com.sellcom.apps.tracker_material.Async_Request.METHOD;
import com.sellcom.apps.tracker_material.Async_Request.RequestManager;
import com.sellcom.apps.tracker_material.Async_Request.ResponseManager;
import com.sellcom.apps.tracker_material.Async_Request.UIResponseListenerInterface;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.DialogManager;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.model.Rastreo_tmp;

/**
 * Created by rebecalopezmartinez on 21/05/15.
 */
public class FragmentRastreo extends TrackerFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    String TAG= "FRAG_RASTREO";

    private FragmentManager         fragmentManager;
    private FragmentTransaction     fragmentTransaction;
    private TrackerFragment         fragment;

    ImageButton                     info;
    Button                          rastreo;
    Button                          escanear;
    ImageButton                     agregar;
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

        info        = (ImageButton)view.findViewById(R.id.btn_help);
        rastreo     = (Button)view.findViewById(R.id.btn_rastrear);
        escanear    = (Button) view.findViewById(R.id.btn_escanear);
        agregar     = (ImageButton) view.findViewById(R.id.btn_agregar);
        lst_rastreo = (ListView)view.findViewById(R.id.liv_rastreo);
        codigo      = (EditText)view.findViewById(R.id.edt_codigo);


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
                Log.d(TAG,"item selected");
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

            case R.id.btn_escanear:
                break;

            case R.id.btn_rastrear:
                if(codes_array == null || codes_array.size()<=0){
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_lista_vacia),1000);
                }
                else {
                    new GetCodesInfo().execute();
                }

                break;

            case R.id.btn_agregar:
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
                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_cod_repetido),1000);
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
           // lst_rastreo.setOnItemClickListener(this);

             if(codes_array.size() == 10) {
                  agregar.setEnabled(false);
                  codigo.setText("");
                  codigo.setEnabled(false);
                  escanear.setEnabled(false);
              }

        }
        else {
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR,getString(R.string.error_tamaño_codigo),2000);

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }



    public class GetCodesInfo extends AsyncTask<Void, Void, String> implements UIResponseListenerInterface {

        int j=0;
        ArrayList<Map<String, String>> aux = new ArrayList<>();
        boolean flag = false;

        protected void onPreExecute() {
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING,getString(R.string.cargando),0);
        }

        @Override
        protected String doInBackground(Void... params) {

            for(int i = 0; i< codes_array.size();i++){
                Map<String, String> requestData = new HashMap<>();
                Map<String, String> code_item =new HashMap<>();
                code_item = codes_array.get(i);

                Log.d("Codigo for",code_item.get("codigo"));

                /*if(i == (codes_array.size()-1)) {
                    flag = true;
                    Log.d(TAG,"size codes array: "+codes_array.size());
                    Log.d(TAG,"flag "+flag+" i "+i);
                }*/

                requestData.put("initialWaybill","");
                requestData.put("finalWaybill","");

                if(code_item.get("codigo").length() == 10)
                    requestData.put("waybillType","R");
                else
                    requestData.put("waybillType","G");

                requestData.put("wayBills",code_item.get("codigo"));

                requestData.put("type","L");
                requestData.put("includeDimensions", "true");
                requestData.put("includeWaybillReplaceData","true");
                requestData.put("includeReturnDocumentData","true");
                requestData.put("includeMultipleServiceData","true");
                requestData.put("includeInternationalData","true");
                requestData.put("includeSignature","true");
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
                if (stringResponse == null ||stringResponse.equals("0") ) {
                    Log.v(TAG, "El servidor devolvió null o 0");
                    String cod_aux= codes_array.get(j).get("codigo");
                    Map<String, String> auxResponse = new HashMap<>();
                    if(cod_aux.length() == 10) {
                        auxResponse.put("shortWayBillId", cod_aux);
                        auxResponse.put("wayBill","Sin información");
                        auxResponse.put("statusSPA","Sin información");
                    }
                    else{
                        auxResponse.put("shortWayBillId","Sin información");
                        auxResponse.put("wayBill", cod_aux);
                        auxResponse.put("statusSPA","Sin información");
                    }
                    aux.add(auxResponse);
                    Log.v(TAG + "aux", "" + aux.size());
                } else {
                    Log.v(TAG, stringResponse);
                    Map<String, String> auxResponse = new HashMap<>();
                    auxResponse = RequestManager.sharedInstance().getResponseArray().get(0);
                    if (auxResponse != null) {
                        Log.v(TAG + "auxResponse", "" + auxResponse.size());
                        if (auxResponse.size() > 0)
                            aux.add(auxResponse);
                        Log.v(TAG + "aux", "" + aux.size());
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            j++;
            Log.d(TAG,"contador:"+j);
            if (j == codes_array.size()){
                Log.d(TAG,"flag "+flag+" j "+j);
                Bundle bundle= new Bundle();
                //bundle.putSerializable("codes",codes_array);
                bundle.putSerializable("codes_info",aux);

                fragmentManager =getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragment = new FragmentRastreoEfectuado();
                fragment.addFragmentToStack(getActivity());
                fragment.setArguments(bundle);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, fragment, TAG);
                fragmentTransaction.commit();
                //DialogManager.sharedInstance().dismissDialog();
            }

        }

        protected void onPostExecute(String result) {



        }

    }
}

