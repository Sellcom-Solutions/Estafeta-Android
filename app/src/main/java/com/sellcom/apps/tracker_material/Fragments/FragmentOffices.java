package com.sellcom.apps.tracker_material.Fragments;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.sellcom.apps.tracker_material.Activities.MainActivity;
import com.sellcom.apps.tracker_material.Adapters.SpinnerAdapter;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.DialogManager;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.model.Offices;
import location.GPSTracker;

/**
 * Created by jonathan.vazquez on 21/05/15.
 */
public class FragmentOffices extends TrackerFragment implements View.OnClickListener{

    public static final String TAG = "FRAG_OFFICES";

    Context context;
    private Button      btn_near,
                        btn_search;

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

            spn_state = (Spinner) view.findViewById(R.id.spn_state);

            edt_city = (EditText) view.findViewById(R.id.edt_city);
            edt_colony = (EditText) view.findViewById(R.id.edt_colony);
            edt_zip_code = (EditText) view.findViewById(R.id.edt_zip_code);

            btn_near.setOnClickListener(this);
            btn_search.setOnClickListener(this);

            setStatesToSpinner(spn_state, context);
        }

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_near:
                Location myLocation = new GPSTracker(getActivity()).getCurrentLocation();
                if(myLocation != null){
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING,"Cargando Oficinas...");
                    nearOffice();
                }else{
                    Toast.makeText(context, "Favor de encender su GPS", Toast.LENGTH_SHORT).show();
                }


                //DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING, "Cargando Oficinas...");

                break;

            case  R.id.btn_search:
                Location myLocationAdvance = new GPSTracker(getActivity()).getCurrentLocation();
                if(myLocationAdvance != null){
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING,"Cargando Oficinas...");
                    searchOffice();
                }else{
                    Toast.makeText(context, "Favor de encender su GPS", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

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

    private void searchOffice(){
        String state, city, colony, zipCode;
        selectionArgs = null;
        ArrayList<String> args = new ArrayList<String>();
        String sql = "select * from offices where estado=? ";

        state   = "" + (spn_state.getSelectedItemPosition() + 1);
        args.add("" + state);


        city    = "" + edt_city.getText().toString();
        if(!city.equals("")){
            args.add("%" + city + "%");
            sql += " and ciudad_n like ? ";
        }
        colony  = "" + edt_colony.getText().toString();
        if(!colony.equals("")){
            args.add("%" + colony + "%");
            sql += " and colonia_n like ? ";
        }
        zipCode = "" + edt_zip_code.getText().toString();
        if(!zipCode.equals("")){
            args.add(zipCode);
            sql += " and codigo_postal like ? ";
        }

        selectionArgs = args.toArray(new String[args.size()]);

        mapList = Offices.getOfficesByCity(context, sql, selectionArgs);
        if(mapList != null) {

            bundle.putString("typeSearch","busqueda_avanzada");
            bundle.putString("sql",sql);
            bundle.putStringArray("selectionArgs",selectionArgs);
            bundle.putString("state",state);

            FragmentManager fragmentManager         = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragment   = new FragmentOfficesMap();
            fragment.addFragmentToStack(getActivity());
            fragment.setArguments(bundle);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.container, fragment, FragmentOfficesMap.TAG);
            fragmentTransaction.commit();
            //Toast.makeText(context, "" + mapList.size(), Toast.LENGTH_SHORT).show();
        }else{

            bundle.putString("typeSearch","nada");
            bundle.putString("state",state);
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

    public void setSpinnerStates(Spinner spinner){

        List<Map<String, String>> listStates = new ArrayList<>();

        arraySpinner = new String[10];

        arraySpinner[0] = "Distrito Federal";
        arraySpinner[1] = "Jalisco";
        arraySpinner[2] = "Nuevo Leon";
        arraySpinner[3] = "Estado de Mexico";
        arraySpinner[4] = "Colina";
        arraySpinner[5] = "Guerrero";
        arraySpinner[6] = "Guanjuato";
        arraySpinner[7] = "Oaxaca";
        arraySpinner[8] = "Baja California Sur";
        arraySpinner[9] = "Estado*";


        for (int i = 0; i < 10 ; i++){
            Log.d(TAG,arraySpinner[i]);
            Map<String,String> map = new HashMap<>();
            map.put("ZNOMBRE",arraySpinner[i]);
            listStates.add(map);
        }

        //listStates = States.getAllInMaps(getActivity());
        //arraySpinner = new String[listStates.size()];
        /*for (int i = 0; i < 5; i++) {
            //arraySpinner[i] = listStates.get(i).get("state");
        }
        */
        //arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, arraySpinner);
        //arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);

        spinnerAdap = new SpinnerAdapter(getActivity(),listStates, SpinnerAdapter.SPINNER_TYPE.STATES);
        spinner.setAdapter(spinnerAdap);
        //spinner.setSelection(listStates.size() - 1);

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

}
