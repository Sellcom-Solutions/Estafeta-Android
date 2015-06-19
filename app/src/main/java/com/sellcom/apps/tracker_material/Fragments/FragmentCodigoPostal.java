package com.sellcom.apps.tracker_material.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.sellcom.apps.tracker_material.Async_Request.METHOD;
import com.sellcom.apps.tracker_material.Async_Request.RequestManager;
import com.sellcom.apps.tracker_material.Async_Request.UIResponseListenerInterface;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.DialogManager;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Jose Luis 26/05/2015
 */
public class FragmentCodigoPostal extends TrackerFragment implements OnClickListener, UIResponseListenerInterface{

    Context context;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private TrackerFragment         fragment;

    private Spinner spinner_state;

    private Button validateZipCode;
    private Button buscarZipCode;
    private EditText zipCode;
    private EditText ciudad;
    private EditText colonia;

    private String zipCodeString;
    private String ciudadString;
    private String coloniaString;
    private String estadoString;
    private String tipo;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_codigo_postal, container, false);

        if(view != null){
            spinner_state   = (Spinner) view.findViewById(R.id.spinner_state);
            validateZipCode = (Button) view.findViewById(R.id.btn_validar_zipcode);
            buscarZipCode   = (Button) view.findViewById(R.id.btn_buscar_zipcode);
            zipCode         = (EditText) view.findViewById(R.id.verify_zip_code);
            ciudad          = (EditText) view.findViewById(R.id.ciudad);
            colonia         = (EditText) view.findViewById(R.id.colonia);

            //setStates to Spinner
            setStatesToSpinner(spinner_state,context);


            validateZipCode.setOnClickListener(this);
            buscarZipCode.setOnClickListener(this);
        }

        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_validar_zipcode:
                zipCodeString = zipCode.getText().toString();
                if(zipCodeString == null || zipCodeString.equals("")){
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR,getString(R.string.error_codigo),3000);
                }
                else {
                    //MapString Params...
                    //Query from Zip Code
                    Map<String, String> requestData = new HashMap<>();
                    requestData.put("pais", "Mexico");
                    requestData.put("codigoPostal", zipCodeString);
                    tipo = "0";
                    //Send params to RequestManager
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING, getString(R.string.cargando),0);
                    RequestManager.sharedInstance().setListener(this);
                    RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_ZIPCODE_ADDRESSES, requestData);

                }
                break;

            case R.id.btn_buscar_zipcode:
                ciudadString = ciudad.getText().toString();

                if(spinner_state.getSelectedItemPosition() == 0){

                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_estado), 3000);
                    return;
                }else if(ciudadString== null || ciudadString.equals("")){
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR,getString(R.string.error_ciudad),3000);
                }
                else {
                    zipCode.setText("");
                    tipo = "1";
                    ciudadString = ciudad.getText().toString();
                    coloniaString = colonia.getText().toString();
                    estadoString = spinner_state.getSelectedItem().toString();


                    if (estadoString.equalsIgnoreCase("Baja California Norte")) {
                        estadoString = "BAJA CALIFORNIA";
                    }
                    else if (estadoString.equalsIgnoreCase("Estado de México")) {
                        estadoString = "EDO. DE MEXICO";
                    }
                    else if (estadoString.equalsIgnoreCase("México, D.F.")) {
                        estadoString = "DISTRITO FEDERAL";
                    }


                    Map<String, String> requestData = new HashMap<>();
                    requestData = new HashMap<>();
                    requestData.put("pais", "MEXICO");



                    requestData.put("estado", convertNonAscii(estadoString).toUpperCase());
                    Log.d("Estado",estadoString);

                    requestData.put("ciudad", convertNonAscii(ciudadString).trim());
                    requestData.put("localidad", convertNonAscii(ciudadString).trim());

                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING, getString(R.string.cargando),0);
                    RequestManager.sharedInstance().setListener(this);
                    RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_ZIPCODE, requestData);
                }
                break;
        }


    }

    @Override
    public void decodeResponse(String response){

        try {
            DialogManager.sharedInstance().dismissDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(response != null && response.length() > 0){
            Log.v("FragmentCodigoPostal", response);
            ArrayList<Map<String,String>> resp = new ArrayList<>();
                    resp =RequestManager.sharedInstance().getResponseArray();
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
        Bundle bundle= new Bundle();
        Log.d("Frag CP, colonias","size"+values.size());
        bundle.putSerializable("col", values);
        bundle.putString("tipo",tipo);

        fragmentManager = getActivity().getSupportFragmentManager();
        FragmentDialogCP fdh = new FragmentDialogCP();
        fdh.setArguments(bundle);
        fdh.show(fragmentManager,"FRAG_DIALOG_CP");
    }


}
