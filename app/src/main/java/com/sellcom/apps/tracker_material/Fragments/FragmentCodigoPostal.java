package com.sellcom.apps.tracker_material.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.sellcom.apps.tracker_material.Adapters.SpinnerAdapter;
import com.sellcom.apps.tracker_material.Async_Request.METHOD;
import com.sellcom.apps.tracker_material.Async_Request.RequestManager;
import com.sellcom.apps.tracker_material.Async_Request.UIResponseListenerInterface;
import com.sellcom.apps.tracker_material.Utils.DialogManager;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;
import com.sellcom.apps.tracker_material.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jose Luis 26/05/2015
 */
public class FragmentCodigoPostal extends TrackerFragment implements OnClickListener, UIResponseListenerInterface{

    Context context;
    private Spinner spinner_state;
    private String[] array_states;
    private SpinnerAdapter spinnerAdapter;

    private Button validateZipCode;
    private Button buscarZipCode;
    private EditText zipCode;
    private EditText ciudad;
    private EditText colonia;

    private String zipCodeString;
    private String ciudadString;
    private String coloniaString;
    private String estadoString;

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
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR,getString(R.string.error_codigo));
                }
                else {
                    //MapString Params...
                    //Query from Zip Code
                    Map<String, String> requestData = new HashMap<>();
                    requestData.put("pais", "Mexico");
                    requestData.put("codigoPostal", zipCodeString);

                    //Send params to RequestManager
                    RequestManager.sharedInstance().setListener(this);
                    RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_ZIPCODE_ADDRESSES, requestData);

                }
                break;

            case R.id.btn_buscar_zipcode:
                ciudadString = ciudad.getText().toString();
                coloniaString = colonia.getText().toString();
                estadoString = spinner_state.getSelectedItem().toString();

                Map<String, String> requestData = new HashMap<>();
                requestData = new HashMap<>();
                requestData.put("pais","MEXICO");
                requestData.put("estado", estadoString);
                requestData.put("ciudad",ciudadString);
                requestData.put("localidad",coloniaString);

                RequestManager.sharedInstance().setListener(this);
                RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_ZIPCODE,requestData);
                break;
        }


    }

    @Override
    public void decodeResponse(String response){
        if(response != null){
            Log.v("FragmentCodigoPostal", response);
            ArrayList<Map<String,String>> resp = RequestManager.sharedInstance().responseArray;
            Log.v("FCP, array", "tam:"+resp.size());
        }else{
            Log.v("FragmentCodigoPostal", "El servidor devolvio null");
        }

    }


}
