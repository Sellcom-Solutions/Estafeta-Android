package com.sellcom.apps.tracker_material.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sellcom.apps.tracker_material.Activities.MainActivity;
import com.sellcom.apps.tracker_material.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFavorites extends Fragment {
    String TAG = "FRAG_FAVORITES";

    public FragmentFavorites() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_favorites, container, false);

        return view;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (!((MainActivity) getActivity()).isDrawerOpen) {
            menu.clear();
            inflater.inflate(R.menu.menu_favorites, menu);
        }
    }

}
