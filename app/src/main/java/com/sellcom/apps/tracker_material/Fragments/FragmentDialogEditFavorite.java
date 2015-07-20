package com.sellcom.apps.tracker_material.Fragments;
import android.content.ContentValues;
import android.content.Context;
import android.support.v4.app.DialogFragment;

import android.support.v7.widget.SwitchCompat;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.sellcom.apps.tracker_material.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import database.model.Favorites;


public class FragmentDialogEditFavorite extends DialogFragment implements View.OnClickListener {


    public static final String TAG = "FRAG_DIALOG_FAVORITE_EDIT";

    Context context;

    Button btn_cancel;
    Button btn_save;
    SwitchCompat swch_confirmacion;
    TextView fav_no_guia;
    TextView fav_codigo;
    EditText fav_edit_referencia;
    boolean notify;
    String
            alias,
            no_guia,
            codigo_rastreo;

    Map<String, String> data = new HashMap<>();
    Map<String, String> codes_info = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_MinWidth);
        //setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_edit_favorite, container, false);
        context = getActivity();

        btn_cancel = (Button) view.findViewById(R.id.btn_cancel_edit);
        btn_save = (Button) view.findViewById(R.id.btn_save);

        ContextThemeWrapper ctw = new ContextThemeWrapper(getActivity(), R.style.Color1SwitchStyle);

        swch_confirmacion = (SwitchCompat) view.findViewById(R.id.swch_confirmar);

        swch_confirmacion = new SwitchCompat(ctw);

        fav_no_guia = (TextView) view.findViewById(R.id.fav_edit_no_guia);
        fav_codigo = (TextView) view.findViewById(R.id.fav_edit_cod_rastreo);
        fav_edit_referencia= (EditText)view.findViewById(R.id.fav_edit_referencia);
        fav_edit_referencia.setFocusable(false);


        btn_cancel.setOnClickListener(this);
        btn_save.setOnClickListener(this);
      //  swch_confirmacion.setChecked(false);

        no_guia = getArguments().getString("no_guia");
        codigo_rastreo = getArguments().getString("codigo_rastreo");
        Log.d(TAG, "no_guia: " + no_guia);
        Log.d(TAG, "cod_rastreo: " + codigo_rastreo);
        codes_info = (Map<String, String>) getArguments().getSerializable("code_array");
        Log.d(TAG, "size: " + codes_info.size());


        fav_no_guia.setText(" " + codes_info.get("no_guia"));
        fav_codigo.setText(" "+codes_info.get("codigo_rastreo"));
        swch_confirmacion.setChecked(Boolean.parseBoolean(codes_info.get("notifica")));



        swch_confirmacion.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    Toast.makeText(context, "Confirmacion Activado" , Toast.LENGTH_SHORT).show();
                    notify=isChecked;
                    return;
                } else {
                    Toast.makeText(context, "Confirmacion Desactivado", Toast.LENGTH_SHORT).show();
                    notify=isChecked;
                    return;
                }

            }
        });

        if (swch_confirmacion.isChecked()) {
           // Toast.makeText(context, "Switch is currently ON", Toast.LENGTH_SHORT).show();
        } else {
           // Toast.makeText(context, "Switch is currently OFF", Toast.LENGTH_SHORT).show();
        }

       return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cancel_edit:
                Log.d("Cancel", "" + getId());
                getDialog().dismiss();
                Log.d(TAG, "Data" + codes_info);
                break;

            case R.id.btn_save:
                Log.d("SAVE", "" + getId());
                context = getActivity();

                codes_info = (Map<String, String>) getArguments().getSerializable("code_array");
                Log.d(TAG, "Data" + codes_info);


                alias  = fav_edit_referencia.getText().toString();

                codigo_rastreo = getArguments().getString("codigo_rastreo");

                codes_info.put("notifica", String.valueOf(notify));
                codes_info.put("alias", alias);

                Log.d(TAG, "alias: " + alias);
                Log.d(TAG, "notify: " + notify);


                Log.d(TAG, "Data" + codes_info);
                Favorites.update(context, codes_info);
                Log.d(TAG, "context" + context);
                Log.d(TAG, "Data" + codes_info);

                getDialog().dismiss();
                break;
            }

        }
    }


