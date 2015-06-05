package com.sellcom.apps.tracker_material.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAvisoPrivacidad extends TrackerFragment {
    String  TAG = "FRAG_AVISO_PRIVACIDAD";

    public FragmentAvisoPrivacidad() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aviso_privacidad, container, false);
    }


}
