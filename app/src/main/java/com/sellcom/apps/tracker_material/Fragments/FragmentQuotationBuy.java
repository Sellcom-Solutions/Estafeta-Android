package com.sellcom.apps.tracker_material.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    private Map<String,String> origin,destiny;
    Map<String,String> requestData;
    private FragmentQuotationBuyFields fragment;
    private int current;
    private String estadoO,municipioO,coloniaO,
                   estadoD,municipioD,coloniaD;



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
        chargeFragment(FragmentQuotationBuyFields.ORIGIN, origin);

        return view;
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
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING, getString(R.string.cargando),0);
            RequestManager.sharedInstance().setListener(this);
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
        fragment.setArguments(bundle);
        transaction.replace(R.id.quotation_buy_container,fragment).commit();
    }


    public int getCurrent(){
        return current;
    }

    @Override
    public void decodeResponse(String response){

        try {
            DialogManager.sharedInstance().dismissDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(response != null && response.length() > 0){

            ArrayList<Map<String,String>> resp = new ArrayList<>();
            resp = RequestManager.sharedInstance().getResponseArray();
            Log.d("Respuesta: ",response);
            if(resp != null && resp.size()>0 )
                showDialogCP(resp);
            else {
                DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_servicio),3000);
            }

        }else{
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_servicio1),3000);
            Log.v("FragmentCodigoPostal", "El servidor devolvio null");
        }

    }

    public void showDialogCP(ArrayList<Map<String, String>> values){
        /*Bundle bundle= new Bundle();

        bundle.putSerializable("col", values);
        bundle.putString("tipo",tipo);

        fragmentManager = getActivity().getSupportFragmentManager();
        FragmentDialogCP fdh = new FragmentDialogCP();
        fdh.setArguments(bundle);
        fdh.show(fragmentManager,"FRAG_DIALOG_CP");*/
    }


}
