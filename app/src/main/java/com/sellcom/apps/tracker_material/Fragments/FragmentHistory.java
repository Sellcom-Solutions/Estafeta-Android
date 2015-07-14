package com.sellcom.apps.tracker_material.Fragments;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sellcom.apps.tracker_material.Adapters.HistoryAdapter;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rebecalopezmartinez on 19/06/15.
 */
public class FragmentHistory extends TrackerFragment {

    String TAG = "FRAG_HISTORY";

    ListView history_lst;
    //ArrayList<Map<String, String>> history_data = new ArrayList<>();
    ArrayList<Map<String, String>> codes_info;

    public FragmentHistory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        history_lst = (ListView) view.findViewById(R.id.history_lst);

        codes_info = (ArrayList<Map<String, String>>) getArguments().getSerializable("codes_info");



        try {
            Log.d(TAG, "size" + codes_info.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //history_data.add(codes_info);


        //HistoryAdapter adapter = new HistoryAdapter(getActivity(),history_data);
        HistoryAdapter adapter = new HistoryAdapter(getActivity(),codes_info);

        history_lst.setAdapter(adapter);

        return view;
    }


}
