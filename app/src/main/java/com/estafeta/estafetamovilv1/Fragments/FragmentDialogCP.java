package com.estafeta.estafetamovilv1.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout;

import com.estafeta.estafetamovilv1.Adapters.CPAListAdapter;
import com.estafeta.estafetamovilv1.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class displays the dialog to display the addresses of zip codes.
 */
public class FragmentDialogCP extends DialogFragment implements View.OnClickListener {

    public static final String      TAG = "FRAG_DIALOG_CP";
    Context                         context;
    TextView                        txt_cp;
    TextView                        txt_estado;
    TextView                        txt_ciudad;
    LinearLayout                    colonia_title;
    ListView                        lst_colonias;
    Button                          cerrar;

    ArrayList<Map<String,String>>   colonias;
    String                          ciudad;
    String                          estado;
    String                          cp;
    String                          tipo;

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

        colonia_title   = (LinearLayout) view.findViewById(R.id.colonia_title);

        try {
           // colonias  = getArguments().getParcelableArrayList("col");
            colonias    = (ArrayList<Map<String,String>>) getArguments().getSerializable("col");
            Map<String,String> aux = new HashMap<>();

            Log.d("colonias","size:"+colonias.size());
            aux         = colonias.get(0);

            tipo        = getArguments().getString("tipo");
            if (tipo.equals("0")){
                cp      = aux.get("cp");
                colonia_title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                view.findViewById(R.id.tv_cp_header).setVisibility(view.GONE);
                txt_cp.setText(txt_cp.getText().toString()+" "+cp);
            }
            else {
                txt_cp.setText(txt_cp.getText().toString());
            }


            CPAListAdapter adapter = new CPAListAdapter(getActivity(),getActivity(),colonias,tipo);
            lst_colonias.setAdapter(adapter);

            ciudad      = aux.get("ciudad");
            txt_ciudad.setText(ciudad);

            estado      = aux.get("estado");
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