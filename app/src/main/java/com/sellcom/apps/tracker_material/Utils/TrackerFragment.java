package com.sellcom.apps.tracker_material.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Spinner;

import com.sellcom.apps.tracker_material.Activities.MainActivity;
import com.sellcom.apps.tracker_material.Adapters.SpinnerAdapter;
import com.sellcom.apps.tracker_material.Async_Request.METHOD;
import com.sellcom.apps.tracker_material.Async_Request.RequestManager;
import com.sellcom.apps.tracker_material.Async_Request.UIResponseListenerInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackerFragment extends Fragment implements UIResponseListenerInterface {
    public boolean  isFromDrawer    = true;
    public int      section_index   = 0;
    public String   tag             = "";

    public SpinnerAdapter spinnerAdapter;

    public enum FRAGMENT_TAG {
        FRAG_RASTREO("rastreo"),
        FRAG_RASTREO_EFECTUADO("rastreo_efectuado"),
        FRAG_RASTREO_DIALOGO("rastreo_dialogo"),
        FRAG_OFFICES ("officinas"),
        FRAG_CODIGO_POSTAL("codigo_postal")
        ;

        private final String name;

        private FRAGMENT_TAG(String s) {
            name = s;
        }

        public boolean equalsName(String otherName){
            return (otherName == null)? false:name.equals(otherName);
        }
        public String toString(){
            return name;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(section_index);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).onSectionAttached(section_index);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void prepareRequest(METHOD method, Map<String, String> params, boolean includeCredentials) {
        RequestManager.sharedInstance().setListener(this);
        //RequestManager.sharedInstance().makeRequestWithDataAndMethodIncludeCredentials(params, method, includeCredentials);
    }

    @Override
    public void decodeResponse(String stringResponse) {

    }


    public void addFragmentToStack(Activity activity){

        ((MainActivity) activity).incrementDepthCounter();

    }


    //created by jose luis at 27/05/2015
    public void setStatesToSpinner(Spinner spinner) {
        //get the states from database
        String[] states = getStates();

        //Initialize the ListMap<>
        List<Map<String, String>> listStates = new ArrayList<>();

        //Add all states to ListMap
        for (int i = 0; i < states.length; i++){
            Map<String, String> state = new HashMap<>();
            state.put("state", states[i]);
            listStates.add(state);
        }

        //set the spinnerAdapter with the values states
        spinnerAdapter = new SpinnerAdapter(getActivity(), listStates, SpinnerAdapter.SPINNER_TYPE.STATES);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(listStates.size() - 1);

    }

    //Simulate call to database, return array or ArrayList<?>
    public String[] getStates(){

        //initialize size of array
        String[] array_states = new String[10];

        //Set values in X position
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

        return array_states;
    }
}
