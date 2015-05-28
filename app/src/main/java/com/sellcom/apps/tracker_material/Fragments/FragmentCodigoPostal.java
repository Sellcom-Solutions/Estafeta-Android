package com.sellcom.apps.tracker_material.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.sellcom.apps.tracker_material.Adapters.SpinnerAdapter;
import com.sellcom.apps.tracker_material.Async_Request.METHOD;
import com.sellcom.apps.tracker_material.Async_Request.RequestManager;
import com.sellcom.apps.tracker_material.Async_Request.UIResponseListenerInterface;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;
import com.sellcom.apps.tracker_material.R;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jose Luis 26/05/2015
 */
public class FragmentCodigoPostal extends TrackerFragment implements OnClickListener, UIResponseListenerInterface{

    private Spinner spinner_state;
    private String[] array_states;
    private SpinnerAdapter spinnerAdapter;
    private Button validateZipCode;
    private UIResponseListenerInterface listener;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_codigo_postal, container, false);

        if(view != null){
            spinner_state = (Spinner) view.findViewById(R.id.spinner_state);
            validateZipCode = (Button) view.findViewById(R.id.btn_validar_zipcode);

            //setStates to Spinner
            setStatesToSpinner(spinner_state);

            validateZipCode.setOnClickListener(this);
        }

        return view;
    }

    @Override
    public void onClick(View view) {

        //MapString Params...
        //Query from Zip Code
        Map<String, String> requestData =  new HashMap<>();
        requestData.put("suscriberId", "1");
        requestData.put("login", "AdminUser");
        requestData.put("password", "zi+Eybk8");
        requestData.put("pais", "Mexico");
        //requestData.put("estado", "Hidalgo");
        //requestData.put("codigoPostal", "43000");
        //requestData.put("ciudad", "Huejutla de Reyes");
        //requestData.put("localidad", "Huejutla de Reyes");

        //Send params to RequestManager
        RequestManager.sharedInstance().setListener(this);
        RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_EXCEPTION_CODES ,requestData);

    }

    @Override
    public void decodeResponse(String response){
        if(response != null){
            Log.v("FragmentCodigoPostal", response);
        }else{
            Log.v("FragmentCodigoPostal", "El servidor devolvio null");
        }

    }


}
