package util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sellcom.apps.tracker_material.Fragments.DialogMapOffices;
import com.sellcom.apps.tracker_material.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by juanc.jimenez on 16/05/14.
 */
public class CustomMapFragment extends SupportMapFragment implements GoogleMap.OnMarkerClickListener{

    private static Context context;
    private LatLng location;
    private static GoogleMap map;
    private static List<LatLng> list;
    private static List<String> listTypes;
    private static List<Marker> markerSU,markerCO,markerCA;
    private static List<Map<String,String>> listMap;



    private static int      cont;

    private static String   type;


    public static CustomMapFragment newInstance(Context context1, List list1, List listType, List<Map<String,String>> listMap1){
        CustomMapFragment fragment = new CustomMapFragment();

        context = context1;
        list = list1;
        listTypes = listType;
        listMap = listMap1;
        markerSU = new ArrayList<>();
        markerCO = new ArrayList<>();
        markerCA = new ArrayList<>();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
        View v = super.onCreateView(arg0, arg1, arg2);

        initMap();

        return v;
    }

    private void initMap(){
        map = getMap();
        if (map != null) {
            UiSettings settings = map.getUiSettings();
            settings.setAllGesturesEnabled(false);
            settings.setZoomGesturesEnabled(true);
            settings.setZoomControlsEnabled(false);
            settings.setScrollGesturesEnabled(true);
            settings.setMyLocationButtonEnabled(false);


            map.moveCamera(CameraUpdateFactory.newLatLngZoom(list.get(0), 13));
            map.setMyLocationEnabled(true);
            map.setTrafficEnabled(false);
            map.setOnMarkerClickListener(this);


            Log.d("CustomMapFragment", " list.size " + list.size());

            for(int i = 0; i<list.size(); i++){

                if(listTypes.get(i).equals("SU")) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(list.get(i));
                    markerOptions.title(listMap.get(i).get("no_oficina"));
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_rojo));
                    //map.addMarker(markerOptions);
                    markerSU.add(map.addMarker(markerOptions));


                }else if(listTypes.get(i).equals("CO")){
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(list.get(i));
                    markerOptions.title(listMap.get(i).get("no_oficina"));
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_azul));
                    //map.addMarker(markerOptions);
                    markerCO.add(map.addMarker(markerOptions));


                }else if(listTypes.get(i).equals("CA")){
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(list.get(i));
                    markerOptions.title(listMap.get(i).get("no_oficina"));
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_gris));
                    //map.addMarker(markerOptions);
                    markerCA.add(map.addMarker(markerOptions));

                }

            }



        }
    }

    public static void addSU(){

        for (int i = 0; i < markerSU.size(); i++){

            markerSU.get(i).setVisible(true);;

        }

    }

    public static void removeSU(){

        for (int i = 0; i < markerSU.size(); i++){

            markerSU.get(i).setVisible(false);;

        }

    }

    public static void addCO(){


        for (int i = 0; i < markerCO.size(); i++){

            markerCO.get(i).setVisible(true);;

        }

    }

    public static void removeCO(){

        for (int i = 0; i < markerCO.size(); i++){

            markerCO.get(i).setVisible(false);;

        }

    }

    public static void addCA(){

        for (int i = 0; i < markerCA.size(); i++){

            markerCA.get(i).setVisible(true);;

        }

    }

    public static void removeCA(){

        for (int i = 0; i < markerCA.size(); i++){

            markerCA.get(i).setVisible(false);;

        }

    }




    @Override
    public boolean onMarkerClick(Marker marker) {

        //Toast.makeText(context,"HOLA",Toast.LENGTH_SHORT).show();

        for(int i = 0; i < listMap.size(); i++){
            cont = i;

            if(marker.getTitle().equals(listMap.get(i).get("no_oficina"))){

                LatLng position = new LatLng(Double.parseDouble(listMap.get(i).get("latitud")), Double.parseDouble(listMap.get(i).get("longitud")));

                if (listTypes.get(cont).equals("SU")) {
                    type = "SU";
                } else if (listTypes.get(cont).equals("CO")) {
                    type = "CO";
                } else if (listTypes.get(cont).equals("CA")) {
                    type = "CA";
                }

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                DialogMapOffices dialogo = new DialogMapOffices();
                dialogo.setList(listMap.get(i));
                dialogo.setLatLng(position);
                dialogo.setType(type);
                dialogo.show(fragmentManager, DialogMapOffices.TAG);


            }


        }

                return true;
    }
}
