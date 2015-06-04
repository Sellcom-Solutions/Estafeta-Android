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
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.sellcom.apps.tracker_material.R;
import com.google.android.gms.maps.model.LatLng;
import com.sellcom.apps.tracker_material.Utils.DialogManager;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;

import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import database.model.Offices;
import location.GPSTracker;
import util.CustomMapFragment;

/**
 * Created by jonathan.vazquez on 22/05/15.
 */
public class FragmentOfficesMap extends TrackerFragment implements View.OnClickListener{

    private Context                         context;

    List <LatLng>                           listPositions;
    List <String>                           listType;
    List<Map<String,String>>                listOficinas;
    private double                          latitud,
                                            longitud;
    private LatLng                          currentPosition;
    private int                             distanceM = 0;
    private CustomMapFragment               mapFragment;

    private CheckBox                     rg1,rg2,rg3;
    private LinearLayout                    lin_container;
    private FloatingActionButton            click,fab_gravity_center;
    Animation                               filterDown,filterUp,animacionDown,animacionUp;
    float                                   height,currentHeight;
    private boolean                         flagFAB = true;

    private TextView                        txv_footer;

    private String Type;

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


        txv_footer      = (TextView)view.findViewById(R.id.txv_footer);

        rg1             = (CheckBox)view.findViewById(R.id.rg1);
        rg1.setOnClickListener(this);
        rg2             = (CheckBox)view.findViewById(R.id.rg2);
        rg2.setOnClickListener(this);
        rg3             = (CheckBox)view.findViewById(R.id.rg3);
        rg3.setOnClickListener(this);

        listPositions   = new ArrayList();
        listType        = new ArrayList();
        listOficinas    = new ArrayList<Map<String, String>>();
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
            txv_footer.setVisibility(View.GONE);
            lin_container.startAnimation(filterDown);
            lin_container.setVisibility(View.GONE);


        }else{
            lin_container.startAnimation(filterUp);
            lin_container.setVisibility(View.VISIBLE);
            txv_footer.startAnimation(filterUp);
            txv_footer.setVisibility(View.VISIBLE);

        }


    }

    public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
        double earthRadius = 6371;
        //kilometers
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);
        return dist;
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

            listOficinas = Offices.getAllInMaps(context);

            Location myLocation = new GPSTracker(getActivity()).getCurrentLocation();
            if (myLocation != null) {
                currentPosition = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                listPositions.add(currentPosition);
                listType.add("0");
            }

            mapFragment = (CustomMapFragment) getChildFragmentManager().findFragmentByTag("map");

            LatLng position;
            for(int i = 0; i<listOficinas.size(); i++){
                latitud     = Double.parseDouble(listOficinas.get(i).get("latitud"));
                longitud    = Double.parseDouble(listOficinas.get(i).get("longitud"));


                distanceM = (int) distFrom((float) myLocation.getLatitude(), (float) myLocation.getLongitude(), (float)latitud, (float)longitud);
                Log.d("Distancia", distanceM +"");

                if (distanceM <= 7.5){
                    position = new LatLng(latitud, longitud);
                    listPositions.add(position);
                    listType.add(listOficinas.get(i).get("tipo_oficina"));

                }


            }
        }

        @Override
        protected String doInBackground(String... params) {

            mapFragment = CustomMapFragment.newInstance(listPositions, listType);

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
