package com.estafeta.estafetamovilv1.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.widgets.Dialog;
import com.google.android.gms.analytics.HitBuilders;
import com.estafeta.estafetamovilv1.Adapters.SpinnerAdapter;
import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Utils.DialogManager;
import com.estafeta.estafetamovilv1.Utils.MyApp;
import com.estafeta.estafetamovilv1.Utils.TrackerFragment;
import com.estafeta.estafetamovilv1.Utils.Utilities;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import database.model.Offices;
import database.model.States;
import location.GPSTracker;

/**
 * Created by jonathan.vazquez on 21/05/15.
 * In this class, the necessary data is entered to find an office.
 */
public class FragmentOffices extends TrackerFragment implements View.OnClickListener,EditText.OnEditorActionListener{

    public static final String TAG = "FRAG_OFFICES";

    Context context;
    private Button      btn_near,
                        btn_search,
                        btn_ar;


    Bundle bundle = new Bundle();
    private Spinner     spn_state;
    private String[]    arraySpinner;
    private ArrayAdapter<String>    arrayAdapter;
    private SpinnerAdapter  spinnerAdap;

    private TrackerFragment fragment;

    private String[] selectionArgs = {};

    private List<Map<String,String>> mapList;

    private EditText    edt_city,
                        edt_colony,
                        edt_zip_code;

    private TextView footer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offices,container,false);

        if(view != null){

            mapList = new ArrayList<Map<String,String>>();



            btn_near = (Button) view.findViewById(R.id.btn_near);
            btn_search = (Button) view.findViewById(R.id.btn_search);
            btn_ar = (Button) view.findViewById(R.id.btn_ar);

            spn_state = (Spinner) view.findViewById(R.id.spn_state);
            spn_state.requestFocus();

            edt_city = (EditText) view.findViewById(R.id.edt_city);
            edt_colony = (EditText) view.findViewById(R.id.edt_colony);
            edt_zip_code = (EditText) view.findViewById(R.id.edt_zip_code);
            edt_zip_code.setOnEditorActionListener(this);

            btn_near.setOnClickListener(this);
            btn_search.setOnClickListener(this);
            btn_ar.setOnClickListener(this);

            footer      = (TextView)view.findViewById(R.id.footer);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
            String currentYear = formatter.format(new Date());
            footer.setText("©2012-" + currentYear + " " + getString(R.string.footer));

            setStatesToSpinner(spn_state, context);

        }

        return view;
    }

    @Override
    public void onClick(View v) {

        /*if(!isNetworkAvailable()){
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "Necesita tener acceso a Internet", 3000);
        }else {*/

            switch (v.getId()) {

                case R.id.btn_near:

                    //Google Analytics
                    MyApp.tracker().send(new HitBuilders.EventBuilder("Oficinas", "TapBoton")
                            .setLabel("Boton_Ubicacion")
                            .build());

                    Location myLocation = new GPSTracker(getActivity()).getCurrentLocation();
                    if (myLocation != null) {
                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING, "Buscando oficinas cercanas...", 0);
                        nearOffice();
                    } else {
                        Toast.makeText(context, getString(R.string.encender_gps), Toast.LENGTH_SHORT).show();
                    }

                    break;

                case R.id.btn_search:

                    //Google Analytics
                    MyApp.tracker().send(new HitBuilders.EventBuilder("Oficinas", "TapBoton")
                            .setLabel("Boton_Direccion")
                            .build());

                    if (spn_state.getSelectedItemPosition() == 0) {

                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_estado), 3000);
                        return;
                    }/*else if(edt_city.getText().toString().equals("")) {

                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, getString(R.string.error_ciudad), 3000);
                    return;



                }*/
                    if(edt_zip_code.getText().toString().length() > 0 && edt_zip_code.getText().toString().length() != 5) {

                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "Código postal no válido.", 3000);
                        return;

                    }else {
                        Location myLocationAdvance = new GPSTracker(getActivity()).getCurrentLocation();
                        if (myLocationAdvance != null) {
                            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING, "Buscando oficinas...", 0);
                            searchOffice();
                        } else {
                            Toast.makeText(context, getString(R.string.encender_gps), Toast.LENGTH_SHORT).show();
                        }
                    }


                    break;

                case R.id.btn_ar:

                    if (!(((LocationManager) context.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER))) {
                        showSettingsAlert();
                    } else {
                        openFragmentAR();
                    }


                    break;
            }
        //}
    }

    /**
     * @deprecated
     */
    public void openFragmentAR(){
        //System.gc();

        if(verificarSensores()) {
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING, getString(R.string.loading_AR_elements), 0);


            Handler handler = new Handler ();//Para dar un tiempo al dialog

            handler.postDelayed(new Runnable() {
                public void run() {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragment = new FragmentAR();
                    fragment.addFragmentToStack(getActivity());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.container, fragment, FragmentAR.TAG);
                    fragmentTransaction.commit();
                }
            }, 2000);

        }
    }

    /**
     * @deprecated
     */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(context.getResources().getString(R.string.gps_alert_title));

        // Setting Dialog Message
        alertDialog.setMessage(context.getResources().getString(R.string.gps_alert_message));

        // On pressing Settings button
        alertDialog.setPositiveButton(context.getResources().getString(R.string.gps_alert_go), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton(context.getResources().getString(R.string.gps_alert_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }

    /**
     * This method indicates to find a nearby office.
     */
    private void nearOffice(){

        bundle.putString("typeSearch", "cerca_de_mi");
        FragmentManager fragmentManager         = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragment   = new FragmentOfficesMap();
        fragment.addFragmentToStack(getActivity());
        fragment.setArguments(bundle);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.container, fragment, FragmentOfficesMap.TAG);
        fragmentTransaction.commit();


    }

    /**
     * This method sends the data needed to find an office.
     */
    private void searchOffice(){
        String state, city="", colony, zipCode;
        selectionArgs = null;
        ArrayList<String> args = new ArrayList<String>();
        String sql = "select * from offices where estado=? ";

        state   = "" + (spn_state.getSelectedItemPosition());
        String auxState = state;

        switch (state){
            case "15":
                state = "17";
                break;
            case "16":
                state = "15";
                break;
            case "17":
                state = "16";
                break;
        }

        //Google Analytics
        String locationName = States.getStateNameById(getActivity(),state);
        MyApp.tracker().send(new HitBuilders.EventBuilder("Oficinas", "SeleccionEstado")
                .setLabel(locationName)
                .build());


        Log.e("Prompt"," ------ "+(spn_state.getSelectedItemPosition() + 1));
        args.add("" + state);

        city  = "" + edt_city.getText().toString();


        city    = Normalizer.normalize(city, Normalizer.Form.NFD);
        city    = city.replaceAll("[^\\p{ASCII}]", "");


        if(!city.equals("")){
            args.add("%" + city + "%");
            args.add("%" + city + "%");
            sql += " and (ciudad_n like lower(?) or ciudad_n_decode like lower(?))";
/*
            args.add("%" + city + "%");
            sql += " or ciudad_n_decode like lower(?)";
*/
        }
        colony  = "" + edt_colony.getText().toString();

        colony    = Normalizer.normalize(colony, Normalizer.Form.NFD);
        colony    = colony.replaceAll("[^\\p{ASCII}]", "");

        if(!colony.equals("")){
            args.add("%" + colony + "%");
            args.add("%" + colony + "%");
            sql += " and (colonia_n like lower(?) or colonia_n_decode like lower(?))";

/*
            args.add("%" + colony + "%");
            sql += " or colonia_n_decode like lower(?)";
*/
        }
        zipCode = "" + edt_zip_code.getText().toString();
        if(!zipCode.equals("")){
            args.add(zipCode);
            sql += " and codigo_postal like ? ";
        }

        selectionArgs = args.toArray(new String[args.size()]);


        mapList = Offices.getOfficesByCity(context, sql, selectionArgs);

        if(mapList != null) {
            Log.e("Oficinas:",""+sql+args.get(0));
            bundle.putString("typeSearch","busqueda_avanzada");
            bundle.putString("sql",sql);
            bundle.putStringArray("selectionArgs",selectionArgs);
            bundle.putString("state",auxState);

            FragmentManager fragmentManager         = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragment   = new FragmentOfficesMap();
            fragment.addFragmentToStack(getActivity());
            fragment.setArguments(bundle);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.container, fragment, FragmentOfficesMap.TAG);
            fragmentTransaction.commit();
            //Toast.makeText(context, "" + mapList.size(), Toast.LENGTH_SHORT).show();
        }else {

            bundle.putString("typeSearch", "nada");
            bundle.putString("state",auxState);
            FragmentManager fragmentManager         = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragment   = new FragmentOfficesMap();
            fragment.addFragmentToStack(getActivity());
            fragment.setArguments(bundle);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.container, fragment, FragmentOfficesMap.TAG);
            fragmentTransaction.commit();
        }
    }


    /**
     * This method checks for Internet available.
     * @return
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
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
            Utilities.hideKeyboard(context, edt_zip_code);
            return true;
        }
        return false;
    }

    public boolean verificarSensores(){
        try {
            SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
            if (sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null) {
                //if (null == null){
                Dialog dialog = new Dialog(context, "Error", getResources().getString(R.string.no_sensor));
                dialog.show();
                return false;
            } else
                return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
