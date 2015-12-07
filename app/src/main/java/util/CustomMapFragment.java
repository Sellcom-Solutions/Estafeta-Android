package util;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.estafeta.estafetamovilv1.Fragments.FragmentDialogOfficesMap;
import com.estafeta.estafetamovilv1.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by juanc.jimenez on 16/05/14.
 * This class helps to control the operation of the markers on the map offices.
 */
public class CustomMapFragment extends SupportMapFragment implements GoogleMap.OnMarkerClickListener{

    private static Context context;
    private LatLng location;
    private static GoogleMap map;
    private static List<LatLng> list;
    private static List<String> listTypes;
    private static List<Marker> markerSU,markerCO,markerCA;
    private static List<Map<String,String>> listMap;
    private static String typeSearch;
    private MarkerOptions markerOptions;



    private static int      cont;

    private static String   type;


    public static CustomMapFragment newInstance(Context context1, List list1, List listType, List<Map<String,String>> listMap1, String type){
        CustomMapFragment fragment = new CustomMapFragment();

        context = context1;
        list = list1;
        listTypes = listType;
        listMap = listMap1;
        markerSU = new ArrayList<>();
        markerCO = new ArrayList<>();
        markerCA = new ArrayList<>();
        typeSearch = type;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
        View v = super.onCreateView(arg0, arg1, arg2);

        initMap();

        return v;
    }

    /**
     * Insert the appropriate markers for each type of office.
     */
    private void initMap(){
        map = getMap();
        if (map != null) {
            UiSettings settings = map.getUiSettings();
            settings.setAllGesturesEnabled(true);
            settings.setZoomGesturesEnabled(true);
            settings.setZoomControlsEnabled(false);
            settings.setCompassEnabled(true);
            settings.setScrollGesturesEnabled(true);
            settings.setMyLocationButtonEnabled(false);
            Log.d("CustomMapFragment", " list.size " + list.size());
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            if(!typeSearch.equals("busqueda_avanzada"))
                builder.include(list.get(0));

            for(int i = 0; i<list.size(); i++){

                markerOptions = new MarkerOptions();

                if(listTypes.get(i).equals("SU")) {
                    markerOptions.position(list.get(i));
                    markerOptions.title(listMap.get(i).get("no_oficina"));
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_rojo));
                    //map.addMarker(markerOptions);
                    markerSU.add(map.addMarker(markerOptions));
                    builder.include(markerOptions.getPosition());

                }else if(listTypes.get(i).equals("CO")){
                    markerOptions.position(list.get(i));
                    markerOptions.title(listMap.get(i).get("no_oficina"));
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_azul));
                    //map.addMarker(markerOptions);
                    markerCO.add(map.addMarker(markerOptions));

                    builder.include(markerOptions.getPosition());
                }else if(listTypes.get(i).equals("CA")){
                    markerOptions.position(list.get(i));
                    markerOptions.title(listMap.get(i).get("no_oficina"));
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_gris));
                    //map.addMarker(markerOptions);
                    markerCA.add(map.addMarker(markerOptions));
                    builder.include(markerOptions.getPosition());
                }



            }

            if(typeSearch.equals("nada")){
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(list.get(0), 4));
            }else {
                if (list.size() > 2) {

                    final LatLngBounds bounds = builder.build();

                    final int padding = 100; // offset from edges of the map in pixels
                    //CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                    map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

                        @Override
                        public void onCameraChange(CameraPosition arg0) {
                            // Move camera.
                            map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
                            // Remove listener to prevent position reset on camera move.
                            map.setOnCameraChangeListener(null);
                        }
                    });
                } else if (list.size() == 2) {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(list.get(1), 17));
                } else if (list.size() == 1) {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(list.get(0), 10));
                }

            }

            map.setMyLocationEnabled(true);
            map.setTrafficEnabled(false);
            map.setOnMarkerClickListener(this);


        }
    }

    /**
     * Putting visible markers SU offices.
     */
    public static void addSU(){

        for (int i = 0; i < markerSU.size(); i++){

            markerSU.get(i).setVisible(true);;

        }

    }

    /**
     * Putting invisible markers SU offices.
     */
    public static void removeSU(){

        for (int i = 0; i < markerSU.size(); i++){

            markerSU.get(i).setVisible(false);;

        }

    }

    /**
     * Putting visible markers CO offices.
     */
    public static void addCO(){


        for (int i = 0; i < markerCO.size(); i++){

            markerCO.get(i).setVisible(true);;

        }

    }

    /**
     * Putting invisible markers CO offices.
     */
    public static void removeCO(){

        for (int i = 0; i < markerCO.size(); i++){

            markerCO.get(i).setVisible(false);;

        }

    }

    /**
     * Putting visible markers CA offices.
     */
    public static void addCA(){

        for (int i = 0; i < markerCA.size(); i++){

            markerCA.get(i).setVisible(true);;

        }

    }

    /**
     * Putting invisible markers CA offices.
     */
    public static void removeCA(){

        for (int i = 0; i < markerCA.size(); i++){

            markerCA.get(i).setVisible(false);;

        }

    }


    /**
     * Opens a dialog with the information office of the marker that was pressed.
     * @param marker
     * @return
     */
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
                FragmentDialogOfficesMap dialogo = new FragmentDialogOfficesMap();
                dialogo.setList(listMap.get(i));
                dialogo.setLatLng(position);
                dialogo.setType(type);
                dialogo.show(fragmentManager, FragmentDialogOfficesMap.TAG);


            }


        }

                return true;
    }
}
