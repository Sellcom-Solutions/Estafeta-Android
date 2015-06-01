package com.sellcom.apps.tracker_material.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sellcom.apps.tracker_material.Activities.MainActivity;
import com.sellcom.apps.tracker_material.Adapters.RastreoEfectuadoAdapter;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRastreoEfectuado extends TrackerFragment {

    String TAG = "FRAG_RASTREO_EFECTUADO";
    ListView lst_rastreo_efectuado;
    RastreoEfectuadoAdapter efectuadoAdapter;

    public FragmentRastreoEfectuado() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_rastreo_efectuado, container, false);
        lst_rastreo_efectuado = (ListView)view.findViewById(R.id.lst_rastreo_efectuado);

        String [] values = {"prueba","prueba2","prueba3"};
        efectuadoAdapter = new RastreoEfectuadoAdapter(getActivity(),values);
        lst_rastreo_efectuado.setAdapter(efectuadoAdapter);
        
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (!((MainActivity) getActivity()).isDrawerOpen) {
            menu.clear();
            inflater.inflate(R.menu.rastreo, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d(TAG, "Selected:  " + item.getItemId());

        switch (item.getItemId()) {
            case R.id.add_favorite:
                Log.d(TAG,"Agregar a favoritos");
                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }

}
