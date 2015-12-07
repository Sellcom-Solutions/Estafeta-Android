package com.estafeta.estafetamovilv1.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import android.widget.TextView;

import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Utils.TrackerFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class only shows the Privacy Notice on a WebView.
 */
public class FragmentAvisoPrivacidad extends TrackerFragment {

    public static final String  TAG     = "FRAG_AVISO_PRIVACIDAD";

    private TextView            footer;

    public FragmentAvisoPrivacidad() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_aviso_privacidad, container, false);

        footer                          = (TextView)view.findViewById(R.id.footer);
        SimpleDateFormat formatter      = new SimpleDateFormat("yyyy");
        String currentYear              = formatter.format(new Date());
        footer.setText("©2012-"+currentYear+" "+getString(R.string.footer));

        WebView Wview                   = (WebView) view.findViewById(R.id.webview);
        Wview.setVerticalScrollBarEnabled(true);
        Wview.loadDataWithBaseURL(null,getString(R.string.aviso_privacidad), "text/html", "utf-8", null);

        return view;
    }


}
