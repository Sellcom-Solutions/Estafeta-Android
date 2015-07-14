package com.sellcom.apps.tracker_material.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sellcom.apps.tracker_material.Activities.MainActivity;
import com.sellcom.apps.tracker_material.Adapters.RastreoEfectuadoAdapter;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.DialogManager;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import database.model.Favorites;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRastreoEfectuado extends TrackerFragment implements AdapterView.OnItemClickListener {


    TrackerFragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    String TAG = "FRAG_RASTREO_EFECTUADO";
    Context context;
    ListView lst_rastreo_efectuado;

    RastreoEfectuadoAdapter efectuadoAdapter;
    ArrayList<Map<String,String>> codes= new ArrayList<>();
    ArrayList<ArrayList<Map<String, String>>> codes_info = new ArrayList<>();

    public FragmentRastreoEfectuado() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_rastreo_efectuado, container, false);
        context = getActivity();
        lst_rastreo_efectuado = (ListView)view.findViewById(R.id.lst_rastreo_efectuado);

        codes_info = (ArrayList<ArrayList<Map<String,String>>>) getArguments().getSerializable("codes_info");

        efectuadoAdapter = new RastreoEfectuadoAdapter(getActivity(),context ,codes_info,getActivity().getSupportFragmentManager());
        lst_rastreo_efectuado.setAdapter(efectuadoAdapter);

        try {
            DialogManager.sharedInstance().dismissDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                //Log.d(TAG,"item selected");
                Toast.makeText(context,"Módulo en Desarrollo",Toast.LENGTH_SHORT).show();
                Bundle bundle= new Bundle();
                bundle.putSerializable("codes_info", codes_info);
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragment = new FragmentFavorites();
                fragment.addFragmentToStack(getActivity());
                fragment.setArguments(bundle);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, fragment, TAG);
                fragmentTransaction.commit();
                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.v(TAG,"item id"+id);
        Toast.makeText(context,
                "Click ListItem Number " + position, Toast.LENGTH_LONG)
                .show();

    }
}
