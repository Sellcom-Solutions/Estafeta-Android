package com.sellcom.apps.tracker_material.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.sellcom.apps.tracker_material.Adapters.CPAListdapter;
import com.sellcom.apps.tracker_material.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDialogCP extends DialogFragment implements View.OnClickListener {
    String  TAG = "FRAG_DIALOG_CP";
    Context context;
    TextView txt_cp;
    TextView txt_estado;
    TextView txt_ciudad;
    ListView lst_colonias;
    Button   cerrar;

    List colonias;
    String ciudad;
    String estado;
    String cp;

    public FragmentDialogCP() {
        // Required empty public constructor
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_MinWidth);
        setCancelable(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dialog_cp, container, false);

        txt_cp          = (TextView) view.findViewById(R.id.tv_dialog_cp);
        txt_estado      = (TextView) view.findViewById(R.id.tv_dialog_estado);
        txt_ciudad      = (TextView) view.findViewById(R.id.tv_dialog_ciudad);
        lst_colonias    = (ListView) view.findViewById(R.id.lv_dialog_colonia);
        cerrar          = (Button)   view.findViewById(R.id.btn_dialog_cerrar);

        try {
            colonias = getArguments().getParcelableArrayList("col");
            Log.d("colonias","size:"+colonias.size());
            CPAListdapter adapter = new CPAListdapter(getActivity(),colonias);
            lst_colonias.setAdapter(adapter);

            ciudad = getArguments().getString("ciudad");
            txt_ciudad.setText(ciudad);
            cp = getArguments().getString("cp");
            txt_cp.setText(txt_cp.getText().toString()+" "+cp);
            estado = getArguments().getString("estado");
            txt_estado.setText(estado);

        } catch (Exception e) {
            e.printStackTrace();
        }

        cerrar.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        getDialog().dismiss();
    }
}
