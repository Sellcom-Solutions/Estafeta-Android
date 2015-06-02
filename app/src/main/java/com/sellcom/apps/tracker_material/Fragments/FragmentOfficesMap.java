package com.sellcom.apps.tracker_material.Fragments;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sellcom.apps.tracker_material.Activities.MainActivity;
import com.sellcom.apps.tracker_material.R;
import com.google.android.gms.maps.model.LatLng;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;
import android.widget.Toast;

import location.GPSTracker;
import util.CustomMapFragment;

/**
 * Created by jonathan.vazquez on 22/05/15.
 */
public class FragmentOfficesMap extends TrackerFragment {

    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();

        LatLng position;
        Location myLocation = new GPSTracker(getActivity()).getCurrentLocation();
        if (myLocation != null)
            position = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        else {
            position = new LatLng(19.35, -99.35);
        }

        CustomMapFragment mapFragment = (CustomMapFragment) getChildFragmentManager().findFragmentByTag("map");

        if (mapFragment == null)
            mapFragment = CustomMapFragment.newInstance(position);



        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.dialog_from_bottom, R.anim.dialog_to_bottom);
        transaction.add(R.id.fragment_map_container, mapFragment).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offices_map, container, false);
    }

    public static int getDistance(int lat_a,int lng_a, int lat_b, int lon_b){
        int Radius = 6371000; //Radio de la tierra
        double lat1 = lat_a / 1E6;
        double lat2 = lat_b / 1E6;
        double lon1 = lng_a / 1E6;
        double lon2 = lon_b / 1E6;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon /2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return (int) (Radius * c);
    }


}
