package com.sellcom.apps.tracker_material.Fragments;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.sellcom.apps.tracker_material.R;
import com.google.android.gms.maps.model.LatLng;
import com.sellcom.apps.tracker_material.Utils.DialogManager;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;
import com.sellcom.apps.tracker_material.Utils.Utilities;

import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.model.Offices;
import database.model.States;
import location.GPSTracker;
import util.CustomMapFragment;

/**
 * Created by jonathan.vazquez on 22/05/15.
 */
public class FragmentOfficesMap extends TrackerFragment implements View.OnClickListener{

    public static final String              TAG = "FragmentOfficesMap";
    private Context                         context;

    List <LatLng>                           listPositions;
    List <String>                           listType;
    List<Map<String,String>>                listOficinas,
                                            listOficinasFiltradas;
    private double                          latitud,
                                            longitud;
    private LatLng                          currentPosition;
    private int                             distanceM = 0;
    public static final float distancia_max_deteccion = 7500; //7.5 km
    private CustomMapFragment               mapFragment;

    private CheckBox                        rg1,rg2,rg3;
    private LinearLayout                    lin_container;
    private FloatingActionButton            click,fab_gravity_center;
    Animation                               filterDown,filterUp,animacionDown,animacionUp;
    float                                   height,currentHeight;
    private boolean                         flagFAB = true;

    private TextView                        footer;

    private String Type,typeSearch,sql,state;
    private String[] selectionArgs = {};

    private List<Map<String,String>>    listStates;

    private Handler puente = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if(msg.obj.equals("fab_gravity_center")){
                click.setClickable(true);
            }else if(msg.obj.equals("click")){
                fab_gravity_center.setClickable(true);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
        TrackerFragment.section_index = 1;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offices_map, container, false);

        lin_container   = (LinearLayout)view.findViewById(R.id.lin_container);
        lin_container.setVisibility(View.GONE);


        footer      = (TextView)view.findViewById(R.id.txv_footer);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String currentYear = formatter.format(new Date());
        footer.setText("©2012-"+currentYear+" "+getString(R.string.footer));

        rg1             = (CheckBox)view.findViewById(R.id.rg1);
        rg1.setOnClickListener(this);
        rg2             = (CheckBox)view.findViewById(R.id.rg2);
        rg2.setOnClickListener(this);
        rg3             = (CheckBox)view.findViewById(R.id.rg3);
        rg3.setOnClickListener(this);

        listPositions   = new ArrayList();
        listType        = new ArrayList();
        listOficinas    = new ArrayList<Map<String, String>>();
        listOficinasFiltradas = new ArrayList<Map<String, String>>();
        listStates      = new ArrayList<Map<String, String>>();
        typeSearch = getArguments().getString("typeSearch");

        new CargarSucursales().execute();


        filterDown    = new AnimationUtils().loadAnimation(context,R.anim.anim_down_filters_map);
        filterUp      = new AnimationUtils().loadAnimation(context, R.anim.anim_up_filters_map);

        animacionDown = new AnimationUtils().loadAnimation(context,R.anim.fab_button_down);
        animacionUp   = new AnimationUtils().loadAnimation(context,R.anim.fab_button_up);

        fab_gravity_center  = (FloatingActionButton)view.findViewById(R.id.fab_gravity_center);
        fab_gravity_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLinContainer();
            }
        });

        click = (FloatingActionButton)view.findViewById(R.id.multiple_actions_down);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLinContainer();
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
    }




    public void openLinContainer(){



        if(click.getVisibility() == View.VISIBLE) {
            click.setVisibility(View.GONE);

            fab_gravity_center.startAnimation(filterUp);
            fab_gravity_center.setVisibility(View.VISIBLE);
            click.setClickable(false);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = new Message();
                    msg.obj = "click";
                    puente.sendMessage(msg);


                }
            }).start();

        }else{

            fab_gravity_center.setVisibility(View.GONE);

            click.startAnimation(animacionUp);
            click.setVisibility(View.VISIBLE);
            fab_gravity_center.setClickable(false);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = new Message();
                    msg.obj = "fab_gravity_center";
                    puente.sendMessage(msg);


                }
            }).start();

        }


        if(lin_container.getVisibility() == View.VISIBLE) {
            footer.setVisibility(View.GONE);
            lin_container.startAnimation(filterDown);
            lin_container.setVisibility(View.GONE);


        }else{
            lin_container.startAnimation(filterUp);
            lin_container.setVisibility(View.VISIBLE);
            footer.startAnimation(filterUp);
            footer.setVisibility(View.VISIBLE);
            lin_container.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //mapFragment.getMap().getUiSettings().setAllGesturesEnabled(false);
                    Log.d("fdsa", "rfeds");
                    return false;
                }
            });
            lin_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d("fdsa", "rfeds");
                }
            });

        }


    }

    public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
        Location loc,loc2;
        loc=new Location("");
        loc2=new Location("");
        loc.setLatitude(lat1);
        loc.setLongitude(lng1);
        loc.setAltitude(0);
        loc2.setLatitude(lat2);
        loc2.setLongitude(lng2);
        loc2.setAltitude(0);
        return loc.distanceTo(loc2);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.rg1:

                if(rg1.isChecked()){
                    rg1.setChecked(true);
                    CustomMapFragment.addSU();
                }else{
                    rg1.setChecked(false);
                    CustomMapFragment.removeSU();
                }
                break;

            case R.id.rg2:

                if(rg2.isChecked()){
                    rg2.setChecked(true);
                    CustomMapFragment.addCA();
                }else{
                    rg2.setChecked(false);
                    CustomMapFragment.removeCA();
                }
                break;

            case R.id.rg3:

                if(rg3.isChecked()){
                    rg3.setChecked(true);
                    CustomMapFragment.addCO();
                }else{
                    rg3.setChecked(false);
                    CustomMapFragment.removeCO();
                }
                break;

        }

    }


    class CargarSucursales extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if(typeSearch.equals("cerca_de_mi")) {
                listOficinas = Offices.getAllInMaps(context);
            }else if(typeSearch.equals("busqueda_avanzada")){
                sql = getArguments().getString("sql");
                selectionArgs = getArguments().getStringArray("selectionArgs");
                state = getArguments().getString("state");
                listOficinas = Offices.getOfficesByCity(context, sql, selectionArgs);
                Log.e("oficinas: ",""+ listOficinas.size());
                listStates = States.getStatesNames(context);
                //Toast.makeText(context, "" + listOficinas.size(), Toast.LENGTH_SHORT).show();
            }else if(typeSearch.equals("nada")){
                listOficinas = new ArrayList<Map<String, String>>();
                state = getArguments().getString("state");
                listStates = States.getStatesNames(context);
            }


            if(typeSearch.equals("cerca_de_mi")) {


                Location myLocation = new GPSTracker(getActivity()).getCurrentLocation();
                if (myLocation != null) {
                    currentPosition = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    listPositions.add(currentPosition);
                    listOficinasFiltradas.add(new HashMap<String, String>());
                    listType.add("0");
                }


                mapFragment = (CustomMapFragment) getChildFragmentManager().findFragmentByTag("map");

                LatLng position;

                for (int i = 0; i < listOficinas.size(); i++) {
                    try {
                        latitud = Utilities.getSaveString(listOficinas.get(i).get("latitud"));
                        longitud = Utilities.getSaveString(listOficinas.get(i).get("longitud"));


                        distanceM = (int) distFrom((float) myLocation.getLatitude(), (float) myLocation.getLongitude(), (float) latitud, (float) longitud);
                        Log.d("Distancia", distanceM + "");

                        if (distanceM <= distancia_max_deteccion) {
                            listOficinasFiltradas.add(listOficinas.get(i));
                            position = new LatLng(latitud, longitud);
                            listPositions.add(position);
                            listType.add(listOficinas.get(i).get("tipo_oficina"));

                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }


                }
            }else if(typeSearch.equals("busqueda_avanzada")){
                currentPosition = new LatLng((Double.parseDouble(listStates.get((Integer.parseInt(state)-1)).get("ZLATITUD"))), Double.parseDouble(listStates.get((Integer.parseInt(state)-1)).get("ZLONGITUD")));
                listPositions.add(currentPosition);
                listOficinasFiltradas.add(new HashMap<String, String>());
                listType.add("0");

                mapFragment = (CustomMapFragment) getChildFragmentManager().findFragmentByTag("map");

                LatLng position;
                for (int i = 0; i < listOficinas.size(); i++) {

                    latitud = Utilities.getSaveString(listOficinas.get(i).get("latitud"));
                    longitud = Utilities.getSaveString(listOficinas.get(i).get("longitud"));

                    Log.d("offices map: ","Estado "+ listOficinas.get(i).get("estado") +" Colonia "+ listOficinas.get(i).get("colonia")+" Latitud "+latitud+ " Longitud "+longitud);
                    if(latitud == 0 && longitud == 0){
                        //NOTHING
                    }else{
                        listOficinasFiltradas.add(listOficinas.get(i));
                        position = new LatLng(latitud, longitud);
                        listPositions.add(position);
                        listType.add(listOficinas.get(i).get("tipo_oficina"));
                    }
                    //distanceM = (int) distFrom((float) myLocation.getLatitude(), (float) myLocation.getLongitude(), (float) latitud, (float) longitud);
                    //Log.d("Distancia", distanceM + "");

                   // if (distanceM <= distancia_max_deteccion) {

                    //}

                }

            }else if(typeSearch.equals("nada")){
                currentPosition = new LatLng((Utilities.getSaveString(listStates.get((Integer.parseInt(state) - 1)).get("ZLATITUD"))), Utilities.getSaveString(listStates.get((Integer.parseInt(state) - 1)).get("ZLONGITUD")));
                listPositions.add(currentPosition);
                listOficinasFiltradas.add(new HashMap<String, String>());
                listType.add("0");

                mapFragment = (CustomMapFragment) getChildFragmentManager().findFragmentByTag("map");

            }

            //Toast.makeText(getActivity(), ""+(listOficinasFiltradas.size()-1), Toast.LENGTH_SHORT).show();

        }

        @Override
        protected String doInBackground(String... params) {



            mapFragment = CustomMapFragment.newInstance(getActivity(), listPositions, listType, listOficinasFiltradas, typeSearch);

            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            //transaction.setCustomAnimations(R.anim.dialog_from_bottom, R.anim.dialog_to_bottom);
            transaction.add(R.id.fragment_map_container, mapFragment).commit();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }




            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            DialogManager.sharedInstance().dismissDialog();
        }
    }
}
