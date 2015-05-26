package com.sellcom.apps.tracker_material.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.sellcom.apps.tracker_material.Adapters.SpinnerAdapter;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;
import com.sellcom.apps.tracker_material.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jose Luis 26/05/2015
 */
public class FragmentCodigoPostal extends TrackerFragment{

    private Spinner spinner_state;
    private String[] array_states;
    private SpinnerAdapter spinnerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_codigo_postal, container, false);

        if(view != null){
            spinner_state = (Spinner) view.findViewById(R.id.spinner_state);
            setSpinnerStates(spinner_state);
        }

        return view;
    }

    public void setSpinnerStates(Spinner spinner){
        List<Map<String, String>> listStates = new ArrayList<>();

        array_states = new String[10];
        array_states[0] = "Estado de Mexico";
        array_states[1] = "Distrito Federal";
        array_states[2] = "Jalisco";
        array_states[3] = "Hidalgo";
        array_states[4] = "Tamaulipas";
        array_states[5] = "Veracruz";
        array_states[6] = "Queretaro";
        array_states[7] = "San Luis Potosi";
        array_states[8] = "Quintana Roo";
        array_states[9] = "Estado*";

        for(int i = 0; i < 10; i++){
            Map<String, String> state = new HashMap<>();
            state.put("state", array_states[i]);
            listStates.add(state);
        }

        spinnerAdapter = new SpinnerAdapter(getActivity(), listStates, SpinnerAdapter.SPINNER_TYPE.STATES);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(listStates.size() - 1);

    }

}
