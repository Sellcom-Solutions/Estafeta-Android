package com.sellcom.apps.tracker_material.Fragments;
import android.content.Context;
import android.support.v4.app.DialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.util.Log;
import android.widget.TextView;

import com.sellcom.apps.tracker_material.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import database.model.Favorites;


public class FragmentDialogEditFavorite extends DialogFragment implements View.OnClickListener {

    String TAG = "FRAG_DIALOG_FAVORITE_EDIT";

    Context context;

    Button btn_cancel;
    Button btn_save;
    Switch swch_confirmacion;
    TextView no_guia;
    TextView    codigo;

    ArrayList codes_info = new ArrayList<>();
    Map<String, String> codigos_copy = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_MinWidth);
        //setCancelable(false);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_edit_favorite, container, false);

        btn_cancel          =(Button)view.findViewById(R.id.btn_cancel_edit);
        btn_save            =(Button)view.findViewById(R.id.btn_save);
        swch_confirmacion   =(Switch)view.findViewById(R.id.swch_confirmar);
        no_guia             =(TextView)view.findViewById(R.id.fav_edit_no_guia);
        codigo              =(TextView)view.findViewById(R.id.fav_edit_cod_rastreo);

        btn_cancel.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        swch_confirmacion.setOnClickListener(this);


        codes_info = Favorites.getAll(context);
        Log.e(TAG, "codes_info:  ---->  " + codes_info.size());


        String guia = codigos_copy.get("no_guia");
        Log.d("no_guia FE ", "" + no_guia);
        no_guia.setText("ng-"+guia);

       return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cancel_edit:
                Log.d("Cancel",""+getId());
                getDialog().dismiss();
                break;

            case R.id.btn_save:
                Log.d("SAVE","" + getId());
                getDialog().dismiss();
                break;
            }

        }
}
