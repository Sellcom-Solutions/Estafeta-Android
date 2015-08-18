package com.sellcom.apps.tracker_material.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.sellcom.apps.tracker_material.Adapters.HistoryAdapter;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import database.model.History;

/**
 * Created by rebecalopezmartinez on 19/06/15.
 */
public class FragmentHistory extends TrackerFragment {

    String TAG = "FRAG_HISTORY";

    ListView history_lst;
    //ArrayList<Map<String, String>> history_data = new ArrayList<>();
    ArrayList<Map<String, String>> codes = new ArrayList<Map<String, String>>();
    Map<String,String> map = new HashMap<String,String>();
    String origin = "";
    private TextView footer;

    Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        history_lst = (ListView) view.findViewById(R.id.history_lst);

        origin = getArguments().getString("origin");

        if(origin.equalsIgnoreCase("detalle_rastreo")){
            codes = (ArrayList<Map<String, String>>) getArguments().getSerializable("codes_info");
        }else if(origin.equalsIgnoreCase("detalle_favorito")){
            map = (Map<String, String>) getArguments().getSerializable("codes_info");

            codes = History.getHistotyByFavoriteId(context,map.get("id_favoritos"));
            Log.d(TAG,"size code_info: "+codes.size());
            codes.add(0, new HashMap<String, String>());

        }
        TrackerFragment.section_index = 6;

        footer      = (TextView)view.findViewById(R.id.footer);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String currentYear = formatter.format(new Date());
        footer.setText("©2012-"+currentYear+" "+getString(R.string.footer));




        try {
            Log.d(TAG, "size" + codes.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //history_data.add(codes_info);


        //HistoryAdapter adapter = new HistoryAdapter(getActivity(),history_data);
        HistoryAdapter adapter = new HistoryAdapter(getActivity(),codes);

        history_lst.setAdapter(adapter);

        return view;
    }


}
