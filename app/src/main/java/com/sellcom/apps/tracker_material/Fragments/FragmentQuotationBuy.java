package com.sellcom.apps.tracker_material.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.client.android.Contents;
import com.sellcom.apps.tracker_material.Activities.MainActivity;
import com.sellcom.apps.tracker_material.Async_Request.METHOD;
import com.sellcom.apps.tracker_material.Async_Request.Parameter;
import com.sellcom.apps.tracker_material.Async_Request.RequestManager;
import com.sellcom.apps.tracker_material.Async_Request.UIResponseListenerInterface;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.DialogManager;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;
import com.sellcom.apps.tracker_material.Utils.Utilities;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import database.model.Buys;

/**
 * Created by juan.guerra on 07/07/2015.
 */
public class FragmentQuotationBuy extends TrackerFragment implements View.OnClickListener,
        UIResponseListenerInterface{

    private View view;

    Context context;

    private String CPO;
    private String CPD;
    private String COSTO;

    private TextView cp_origen;
    private TextView cp_destino;
    private TextView costo;

    private Map<String,String> origin,destiny;
    Map<String,String> requestData;
    private FragmentQuotationBuyFields fragment;
    private int current;
    private String estadoO,municipioO,coloniaO,
                   estadoD,municipioD,coloniaD;
    private FragmentManager fragmentManager;

    private String lastMethod;

    private Map<String,Map<String,String>> zipCodesLocationValues;

    public static final String ESTADO_ORIGEN = "edo";
    public static final String ESTADO_DESTINO = "edd";
    public static final String MUNICIPIO_ORIGEN = "mo";
    public static final String MUNICIPIO_DESTINO = "md";
    public static final String COLONIA_ORIGEN = "co";
    public static final String COLONIA_DESTINO = "cd";



    public static enum EXTRAS{
        CP ("cp"),
        CP_ORIGEN ("cp_origen"),
        CP_DESTINO ("cp_destino"),
        COSTO ("costo"),
        DATOS ("datos");
        private final String name;
        private EXTRAS(String s) {
            name = s;
        }
        public boolean equalsName(String otherName){ return (otherName == null)? false:name.equals(otherName); }
        public String toString(){ return name; }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_quotation_buy, container, false);
        context = getActivity();
        Bundle b = getArguments();
        CPO = b.getString(EXTRAS.CP_ORIGEN.toString());
        CPD = b.getString(EXTRAS.CP_DESTINO.toString());
        COSTO = b.getString(EXTRAS.COSTO.toString());


        zipCodesLocationValues = new HashMap<>();
        costo = (TextView)view.findViewById(R.id.txt_costo);
        cp_origen  = (TextView)view.findViewById(R.id.txt_cp_origen);
        cp_destino = (TextView)view.findViewById(R.id.txt_cp_destino);


        costo.setText(COSTO);

        //Get state, district and colonies for origin and destiny
        initOriginData();




        return view;
    }

    private void initOriginData(){
        requestData = new HashMap<>();
        requestData.put("pais", "Mexico");
        requestData.put("codigoPostal", CPO);
        lastMethod = METHOD.REQUEST_ZIPCODE_ADDRESSES.toString();
        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING, getString(R.string.cargando),0);
        RequestManager.sharedInstance().setListener(this);
        RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_ZIPCODE_ADDRESSES, requestData);


    }

    private void initDestinyData(){
        requestData = new HashMap<>();
        requestData.put("pais", "Mexico");
        requestData.put("codigoPostal", CPD);
        lastMethod = METHOD.REQUEST_ZIPCODE_ADDRESSES.toString();
        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING, getString(R.string.cargando), 0);
        RequestManager.sharedInstance().setListener(this);
        RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_ZIPCODE_ADDRESSES, requestData);
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        if ( ((Button)v).getText().toString().equals(getResources().getString(R.string.next)) ){
            if (!fragment.validateData())
                return;
            origin = fragment.getData();
            //destiny = fragment.getData();
            chargeFragment(FragmentQuotationBuyFields.DESTINY,destiny);
        }
        else if ( ((Button)v).getText().toString().equals(getResources().getString(R.string.continue_buy)) ){
            if (!fragment.validateData())
                return;

            destiny = fragment.getData();

            
            estadoO = origin.get(FragmentQuotationBuyFields.EXTRAS.STATE.toString());
            municipioO = origin.get(FragmentQuotationBuyFields.EXTRAS.DISTRICT.toString());
            coloniaO = origin.get(FragmentQuotationBuyFields.EXTRAS.COLONY.toString());
            estadoD = destiny.get(FragmentQuotationBuyFields.EXTRAS.STATE.toString());
            municipioD = destiny.get(FragmentQuotationBuyFields.EXTRAS.DISTRICT.toString());
            coloniaD = destiny.get(FragmentQuotationBuyFields.EXTRAS.COLONY.toString());

            if (estadoO.equalsIgnoreCase("Baja California Norte")) {
                estadoO = "BAJA CALIFORNIA";
            }
            else if (estadoO.equalsIgnoreCase("Estado de México")) {
                estadoO = "EDO. DE MEXICO";
            }
            else if (estadoO.equalsIgnoreCase("México, D.F.")) {
                estadoO = "DISTRITO FEDERAL";
            }

            if (estadoD.equalsIgnoreCase("Baja California Norte")) {
                estadoD = "BAJA CALIFORNIA";
            }
            else if (estadoD.equalsIgnoreCase("Estado de México")) {
                estadoD = "EDO. DE MEXICO";
            }
            else if (estadoD.equalsIgnoreCase("México, D.F.")) {
                estadoD = "DISTRITO FEDERAL";
            }

            setParametersToWebService();
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING, getString(R.string.cargando), 0);
            RequestManager.sharedInstance().setListener(this);
            lastMethod = METHOD.TEST.toString();
            RequestManager.sharedInstance().makeRequest(METHOD.TEST, requestData);
        }

    }

    private void setParametersToWebService(){
        requestData = new HashMap<>();

        requestData.put(Parameter.BUY_REQUEST.COSTO.toString(), COSTO);
        requestData.put(Parameter.BUY_REQUEST.NOMBRE_REMITENTE.toString(),origin.get(FragmentQuotationBuyFields.EXTRAS.NAME));
        requestData.put(Parameter.BUY_REQUEST.TELEFONO_REMITENTE.toString(),origin.get(FragmentQuotationBuyFields.EXTRAS.PHONE));
        requestData.put(Parameter.BUY_REQUEST.EMAIL_REMITENTE.toString(),origin.get(FragmentQuotationBuyFields.EXTRAS.EMAIL));
        requestData.put(Parameter.BUY_REQUEST.RAZON_REMITENTE.toString(),origin.get(FragmentQuotationBuyFields.EXTRAS.BUSSINESS_NAME));
        requestData.put(Parameter.BUY_REQUEST.CP_ORIGEN.toString(),CPO);
        requestData.put(Parameter.BUY_REQUEST.ESTADO_ORIGEN.toString(),convertNonAscii(estadoO).toUpperCase());
        requestData.put(Parameter.BUY_REQUEST.CIUDAD_ORIGEN.toString(),convertNonAscii(municipioO).toUpperCase());
        requestData.put(Parameter.BUY_REQUEST.COLONIA_ORIGEN.toString(),convertNonAscii(coloniaO).toUpperCase());
        requestData.put(Parameter.BUY_REQUEST.CALLE_ORIGEN.toString(), origin.get(FragmentQuotationBuyFields.EXTRAS.STREET));
        requestData.put(Parameter.BUY_REQUEST.NO_ORIGEN.toString(), origin.get(FragmentQuotationBuyFields.EXTRAS.STREET_NUMBER));
        requestData.put(Parameter.BUY_REQUEST.NOMBRE_DESTINATARIO.toString(),destiny.get(FragmentQuotationBuyFields.EXTRAS.NAME));
        requestData.put(Parameter.BUY_REQUEST.TELEFONO_DESTINATARIO.toString(), destiny.get(FragmentQuotationBuyFields.EXTRAS.PHONE));
        requestData.put(Parameter.BUY_REQUEST.EMAIL_DESTINATARIO.toString(),destiny.get(FragmentQuotationBuyFields.EXTRAS.EMAIL));
        requestData.put(Parameter.BUY_REQUEST.RAZON_DESTINATARIO.toString(),destiny.get(FragmentQuotationBuyFields.EXTRAS.BUSSINESS_NAME));
        requestData.put(Parameter.BUY_REQUEST.CP_DESTINO.toString(), CPD);
        requestData.put(Parameter.BUY_REQUEST.ESTADO_DESTINO.toString(),convertNonAscii(estadoD).toUpperCase());
        requestData.put(Parameter.BUY_REQUEST.CIUDAD_DESTINO.toString(),convertNonAscii(municipioD).toUpperCase());
        requestData.put(Parameter.BUY_REQUEST.COLONIA_DESTINO.toString(),convertNonAscii(coloniaD).toUpperCase());
        requestData.put(Parameter.BUY_REQUEST.CALLE_DESTINO.toString(),destiny.get(FragmentQuotationBuyFields.EXTRAS.STREET));
        requestData.put(Parameter.BUY_REQUEST.NO_DESTINO.toString(),destiny.get(FragmentQuotationBuyFields.EXTRAS.STREET_NUMBER));
    }

    public void handleBackPressed(){
        switch (fragment.getVersion()){
            case FragmentQuotationBuyFields.DESTINY:
                destiny = fragment.getData();
                chargeFragment(FragmentQuotationBuyFields.ORIGIN,origin);
                break;
            case FragmentQuotationBuyFields.ORIGIN:
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.remove(this).commit();
                break;
        }
    }

    private void chargeFragment(int version,Map<String,String> previous_data){
        current = version;
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        fragment = new FragmentQuotationBuyFields();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRAS.CP.toString(),CPD);
        bundle.putInt(FragmentQuotationBuyFields.EXTRAS.VERSION.toString(), version);
        bundle.putSerializable(EXTRAS.DATOS.toString(), (Serializable) previous_data);
        bundle.putSerializable(FragmentQuotationBuyFields.EXTRAS.DATA.toString(),
                               (Serializable)zipCodesLocationValues);
        fragment.setArguments(bundle);
        transaction.replace(R.id.quotation_buy_container,fragment).commit();
    }


    public int getCurrent(){
        return current;
    }

    @Override
    public void decodeResponse(String response){


        if (lastMethod.toString().equals(METHOD.REQUEST_ZIPCODE_ADDRESSES.toString())){
            try {
                DialogManager.sharedInstance().dismissDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(response != null && response.length() > 0){
                Log.v("FragmentCodigoPostal", response);
                ArrayList<Map<String,String>> resp = new ArrayList<>();
                resp =RequestManager.sharedInstance().getResponseArray();
                if(resp != null && resp.size()>0 ){
                    getDataFromWS(resp);
                }
                else {
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_servicio),3000);
                }

            }else{
                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_servicio1),3000);
                Log.v("FragmentCodigoPostal", "El servidor devolvio null");
            }
        }
        else {
            try {
                DialogManager.sharedInstance().dismissDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(response != null && response.length() > 0){

                ArrayList<Map<String,String>> resp = new ArrayList<>();
                resp = RequestManager.sharedInstance().getResponseArray();
                Log.d("Respuesta: ",response);
                if(resp != null && resp.size()>0 ){

                    showDialogCP(saveInDataBase(resp.get(0)));
                    Activity a = getActivity();
                    a.onBackPressed();
                    a.onBackPressed();
                    a.onBackPressed();
                    ((MainActivity)a).depthCounter = 0;
                }

                else {
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_servicio), 3000);
                }

            }else{
                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_servicio1),3000);
                Log.v("FragmentCodigoPostal", "El servidor devolvio null");
            }
        }


    }

    public void showDialogCP(String parameter){
        fragmentManager = getActivity().getSupportFragmentManager();
        FragmentComprobanteCompra fcc = new FragmentComprobanteCompra();
        Bundle b = new Bundle();
        b.putString(Buys.REFERENCIA,parameter);
        fcc.setArguments(b);
        fcc.show(fragmentManager,"FRAG_DIALOG_CP");

    }


    public void getDataFromWS(ArrayList<Map<String,String>> values){
        try {
            if (zipCodesLocationValues.get(ESTADO_ORIGEN) == null){
                Map<String,String> aux = new HashMap<>();
                aux.put("0",values.get(0).get("estado"));
                zipCodesLocationValues.put(ESTADO_ORIGEN, aux);
                aux = new HashMap<>();
                aux.put("0", values.get(0).get("ciudad"));
                zipCodesLocationValues.put(MUNICIPIO_ORIGEN,aux);
                aux = new HashMap<>();
                for (int index = 0; index < values.size(); index++){
                    aux.put(String.valueOf(index),values.get(index).get("colonia"));

                }
                zipCodesLocationValues.put(COLONIA_ORIGEN,aux);
                initDestinyData();
            }

            else {
                Map<String,String> aux = new HashMap<>();
                aux.put("0",values.get(0).get("estado"));
                zipCodesLocationValues.put(ESTADO_DESTINO, aux);
                aux = new HashMap<>();
                aux.put("0", values.get(0).get("ciudad"));
                zipCodesLocationValues.put(MUNICIPIO_DESTINO,aux);
                aux = new HashMap<>();
                for (int index = 0; index < values.size(); index++){
                    aux.put(String.valueOf(index),values.get(index).get("colonia"));
                }
                zipCodesLocationValues.put(COLONIA_DESTINO, aux);
                costo.setText(COSTO);
                cp_origen.setText(CPO + ". " +zipCodesLocationValues.get(FragmentQuotationBuy.MUNICIPIO_ORIGEN).get("0")
                + ", " + zipCodesLocationValues.get(FragmentQuotationBuy.ESTADO_ORIGEN).get("0"));
                cp_destino.setText(CPD + ". " +zipCodesLocationValues.get(FragmentQuotationBuy.MUNICIPIO_DESTINO).get("0")
                + ", " + zipCodesLocationValues.get(FragmentQuotationBuy.ESTADO_DESTINO).get("0"));

                chargeFragment(FragmentQuotationBuyFields.ORIGIN, origin);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String saveInDataBase(Map<String,String> arguments){
        Map<String,String> values = new HashMap<>();
        values.put(Buys.NOMBRE_REMITENTE,arguments.get(Parameter.BUY_RESPONSE.REMITENTE.toString()));
        values.put(Buys.ORIGEN,arguments.get(Parameter.BUY_RESPONSE.ORIGEN.toString()));
        values.put(Buys.CPO,arguments.get(Parameter.BUY_RESPONSE.CP_ORIGEN.toString()));
        values.put(Buys.NOMBRE_DESTINATARIO,arguments.get(Parameter.BUY_RESPONSE.DESTINATARIO.toString()));
        values.put(Buys.DESTINO,arguments.get(Parameter.BUY_RESPONSE.DESTINO.toString()));
        values.put(Buys.CPD,arguments.get(Parameter.BUY_RESPONSE.CP_DESTINO.toString()));
        values.put(Buys.TIPO_SERVICIO,arguments.get(Parameter.BUY_RESPONSE.TIPO_SERVICIO.toString()));
        values.put(Buys.GARANTIA,arguments.get(Parameter.BUY_RESPONSE.GARANTIA.toString()));
        values.put(Buys.COSTO,arguments.get(Parameter.BUY_RESPONSE.COSTO.toString()));
        values.put(Buys.REFERENCIA, arguments.get(Parameter.BUY_RESPONSE.REFERENCIA.toString()));
        Log.d("vbeifhadjnk", values.get(Buys.REFERENCIA));
        Log.d("efeujntrfdjnef", Parameter.BUY_RESPONSE.COSTO.toString());

        Buys.insert(context, values);

        return values.get(Buys.REFERENCIA);
    }

}
