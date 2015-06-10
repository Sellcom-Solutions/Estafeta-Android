package com.sellcom.apps.tracker_material.Fragments;

import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.sellcom.apps.tracker_material.Adapters.SpinnerAdapter;
import com.sellcom.apps.tracker_material.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jonathan.vazquez on 28/05/15.
 */
public class FragmentQuotationSearchZC extends DialogFragment implements View.OnClickListener{

    public static final String TAG = "FRAG_QUOTATION_ZC";


    private EditText    edt_city,
                        edt_colony,
                        edt_dummy;

    private Button      btn_cancel,
                        btn_accept;

    private Spinner     spn_state;
    private String[]    arraySpinner;
    private ArrayAdapter<String> arrayAdapter;
    private SpinnerAdapter  spinnerAdap;



    public FragmentQuotationSearchZC() {
    }

    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null) {
            return;
        }
        setCancelable(true);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_quotation_search_zc, container, false);

        if (getDialog() != null) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            getDialog().setCanceledOnTouchOutside(false);
        }


        if(view != null){

            spn_state   = (Spinner) view.findViewById(R.id.spn_state);

            edt_city    = (EditText) view.findViewById(R.id.edt_city);
            edt_colony  = (EditText) view.findViewById(R.id.edt_colony);
            edt_dummy  = (EditText) view.findViewById(R.id.edt_dummy);

            btn_cancel  = (Button) view.findViewById(R.id.btn_cancel);
            btn_accept  = (Button) view.findViewById(R.id.btn_accept);


            btn_cancel.setOnClickListener(this);
            btn_accept.setOnClickListener(this);

            edt_dummy.requestFocus();

            setSpinnerStates(spn_state);

        }

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_cancel:
                Log.e(TAG,"CANCEL");

                dismiss();
                break;

            case  R.id.btn_accept:
                Log.e(TAG,"ACCEPT");
                searchZC();
                break;
        }
    }


    public void setSpinnerStates(Spinner spinner){

        List<Map<String, String>> listStates = new ArrayList<>();

        arraySpinner = new String[10];

        arraySpinner[0] = "Distrito Federal";
        arraySpinner[1] = "Jalisco";
        arraySpinner[2] = "Nuevo Leon";
        arraySpinner[3] = "Estado de Mexico";
        arraySpinner[4] = "Colina";
        arraySpinner[5] = "Guerrero";
        arraySpinner[6] = "Guanjuato";
        arraySpinner[7] = "Oaxaca";
        arraySpinner[8] = "Baja California Sur";
        arraySpinner[9] = "Estado*";


        for (int i = 0; i < 10 ; i++){
            Log.d(TAG, arraySpinner[i]);
            Map<String,String> map = new HashMap<>();
            map.put("state",arraySpinner[i]);
            listStates.add(map);
        }

        //listStates = States.getAllInMaps(getActivity());
        //arraySpinner = new String[listStates.size()];
        /*for (int i = 0; i < 5; i++) {
            //arraySpinner[i] = listStates.get(i).get("state");
        }
        */
        //arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, arraySpinner);
        //arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);

        spinnerAdap = new SpinnerAdapter(getActivity(),listStates, SpinnerAdapter.SPINNER_TYPE.STATES);
        spinner.setAdapter(spinnerAdap);
        spinner.setSelection(listStates.size() - 1);

    }

    private void searchZC(){

    }
}
