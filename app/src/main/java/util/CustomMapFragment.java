package util;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.DialogManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juanc.jimenez on 16/05/14.
 */
public class CustomMapFragment extends SupportMapFragment implements GoogleMap.OnMarkerClickListener{

    private LatLng location;
    private static GoogleMap map;
    private static List<LatLng> list;
    private static List<String> listTypes;
    private static List<LatLng> listSU,listCO,listCA;
    private static List<Marker> markerSU,markerCO,markerCA;


    public static CustomMapFragment newInstance(List list1, List listType){
        CustomMapFragment fragment = new CustomMapFragment();
        list = list1;
        listTypes = listType;
        listSU = new ArrayList<>();
        listCO = new ArrayList<>();
        listCA = new ArrayList<>();
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

            Log.d("CustomMapFragment", " list.size " + list.size());

            for(int i = 0; i<list.size(); i++){

                if(listTypes.get(i).equals("SU")) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(list.get(i));
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_rojo));
                    //map.addMarker(markerOptions);
                    markerSU.add(map.addMarker(markerOptions));

                }else if(listTypes.get(i).equals("CO")){
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(list.get(i));
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_azul));
                    //map.addMarker(markerOptions);
                    markerCO.add(map.addMarker(markerOptions));

                }else if(listTypes.get(i).equals("CA")){
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(list.get(i));
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


        return false;
    }
}
