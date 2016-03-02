package com.estafeta.estafetamovilv1.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.estafeta.estafetamovilv1.Async_Request.METHOD;
import com.estafeta.estafetamovilv1.Async_Request.RequestManager;
import com.estafeta.estafetamovilv1.Async_Request.UIResponseListenerInterface;
import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Utils.DialogManager;
import com.estafeta.estafetamovilv1.Utils.MyApp;
import com.estafeta.estafetamovilv1.Utils.TrackerFragment;
import com.estafeta.estafetamovilv1.Utils.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Jose Luis 26/05/2015
 * This is the main module class zip_code.
 */
public class  FragmentCodigoPostal extends TrackerFragment implements OnClickListener, UIResponseListenerInterface, EditText.OnEditorActionListener{

    Context                         context;
    private FragmentManager         fragmentManager;
    private FragmentTransaction     fragmentTransaction;
    private TrackerFragment         fragment;


    private Spinner                 spinner_state;

    private Button                  validateZipCode;
    private Button                  buscarZipCode;

    private EditText                zipCode;
    private EditText                ciudad;
    private EditText                colonia;

    private TextView                footer;

    private String                  zipCodeString;
    private String                  ciudadString;
    private String                  coloniaString;
    private String                  estadoString;
    private String                  tipo;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        context = getActivity();

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_codigo_postal, container, false);

        if(view != null){
            spinner_state   = (Spinner) view.findViewById(R.id.spinner_state);
            spinner_state.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus) {
                        zipCode.setText("");
                    }
                }
            });
            validateZipCode = (Button) view.findViewById(R.id.btn_validar_zipcode);
            validateZipCode.requestFocus();
            buscarZipCode   = (Button) view.findViewById(R.id.btn_buscar_zipcode);
            zipCode         = (EditText) view.findViewById(R.id.verify_zip_code);
            zipCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        spinner_state.setSelection(0);
                        ciudad.setText("");
                        colonia.setText("");
                    }
                }
            });
            ciudad          = (EditText) view.findViewById(R.id.ciudad);
            ciudad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        zipCode.setText("");
                    }
                }
            });
            colonia         = (EditText) view.findViewById(R.id.colonia);
            colonia.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        zipCode.setText("");
                    }
                }
            });

            footer      = (TextView)view.findViewById(R.id.footer);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
            String currentYear = formatter.format(new Date());
            footer.setText("©2012-"+currentYear+" "+getString(R.string.footer));


            //Set Hints
            Utilities.setCustomHint(getActivity(), getString(R.string.search_by_zip_code), zipCode);
            Utilities.setCustomHint(getActivity(), getString(R.string.city), ciudad);

            //setStates to Spinner
            setStatesToSpinner(spinner_state,context);
            zipCode.setOnEditorActionListener(this);
            colonia.setOnEditorActionListener(this);

            validateZipCode.setOnClickListener(this);
            buscarZipCode.setOnClickListener(this);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        footer.requestFocus();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_validar_zipcode:

                //Google Analytics
                MyApp.tracker().send(new HitBuilders.EventBuilder("CodigosPostales", "TapBoton")
                        .setLabel("Boton_Validar")
                        .build());

                zipCodeString = zipCode.getText().toString();
                if(zipCodeString == null || zipCodeString.equals("")){
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR,getString(R.string.error_codigo),3000);
                }else if(zipCodeString.length() < 5) {

                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR,getString(R.string.error_codigo),3000);

                }else {
                    //MapString Params...
                    //Query from Zip Code
                    Map<String, String> requestData = new HashMap<>();
                    requestData.put("pais", "Mexico");
                    requestData.put("codigoPostal", zipCodeString);
                    tipo = "0";
                    //Send params to RequestManager
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING, getString(R.string.cargando),0);
                    RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_ZIPCODE_ADDRESSES, requestData,this);

                }
                break;

            case R.id.btn_buscar_zipcode:

                //Google Analytics
                MyApp.tracker().send(new HitBuilders.EventBuilder("CodigosPostales", "TapBoton")
                        .setLabel("Boton_Buscar")
                        .build());

                ciudadString    = ciudad.getText().toString();
                coloniaString   = colonia.getText().toString();
                if(spinner_state.getSelectedItemPosition() == 0){

                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_estado), 3000);
                    return;
                }else if(ciudadString== null || ciudadString.equals("")){
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR,getString(R.string.error_ciudad),3000);
                }
                else {
                    zipCode.setText("");
                    tipo = "1";
                    ciudadString    = ciudad.getText().toString();
                    coloniaString   = colonia.getText().toString();
                    estadoString    = spinner_state.getSelectedItem().toString();


                    if (estadoString.equalsIgnoreCase("Baja California Norte")) {
                        estadoString = "BAJA CALIFORNIA";
                    }
                    else if (estadoString.equalsIgnoreCase("Estado de México")) {
                        estadoString = "EDO. DE MEXICO";
                    }
                    else if (estadoString.equalsIgnoreCase("México, D.F.")) {
                        estadoString = "DISTRITO FEDERAL";
                    }

                    //Google Analytics
                    MyApp.tracker().send(new HitBuilders.EventBuilder("CodigosPostales", "SeleccionEstado")
                            .setLabel(estadoString)
                            .build());

                    Map<String, String> requestData = new HashMap<>();
                    requestData = new HashMap<>();
                    requestData.put("pais", "MEXICO");



                    requestData.put("estado", convertNonAscii(estadoString).toUpperCase());
                    Log.d("ESTADOREQUEST",requestData.get("estado")+".");
                    Log.d("Estado",estadoString);

                    if (Utilities.specialCharacteresInString(ciudadString)){
                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "No se permiten acentos ni caracteres especiales.", 3000);
                        return;
                    }
                    if (Utilities.specialCharacteresInString(coloniaString)){
                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "No se permiten acentos ni caracteres especiales.", 3000);
                        return;
                    }

                    requestData.put("ciudad", convertNonAscii(ciudadString).trim());
                    requestData.put("localidad", convertNonAscii(coloniaString).trim());

                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING, getString(R.string.cargando), 0);
                    RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_ZIPCODE, requestData,this);
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

    /**
     * This method hides the device keyboard when needed.
     * @param v
     * @param actionId
     * @param event
     * @return
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            Utilities.hideKeyboard(context, colonia);
            return true;
        }else if(actionId == EditorInfo.IME_ACTION_DONE) {
            Utilities.hideKeyboard(context, zipCode);
            return true;
        }
        return false;
    }
}
