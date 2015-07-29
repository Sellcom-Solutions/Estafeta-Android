package com.sellcom.apps.tracker_material.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import android.widget.TextView;

import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAvisoPrivacidad extends TrackerFragment {
    String  TAG = "FRAG_AVISO_PRIVACIDAD";

    private TextView footer;

    public FragmentAvisoPrivacidad() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_aviso_privacidad, container, false);

        footer      = (TextView)view.findViewById(R.id.footer);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String currentYear = formatter.format(new Date());
        footer.setText("©2012-"+currentYear+" "+getString(R.string.footer));

        WebView Wview = (WebView) view.findViewById(R.id.webview);
        Wview.setVerticalScrollBarEnabled(true);


        Wview.loadDataWithBaseURL(null,getString(R.string.aviso_privacidad), "text/html", "utf-8", null);

        return view;
    }


}
