package com.estafeta.estafetamovilv1.Fragments;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;

import android.support.v7.widget.SwitchCompat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.TextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.google.android.gms.analytics.HitBuilders;
import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Utils.DialogManager;
import com.estafeta.estafetamovilv1.Utils.MyApp;

import java.util.HashMap;
import java.util.Map;

import database.model.Favorites;

/**
 * This class displays the dialog box to edit a reference to a favorite.
 */
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
            referencia,
            no_guia,
            codigo_rastreo;

    changeReference changeReference;

    Map<String, String> codes_info = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_MinWidth);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_edit_favorite, container, false);
        context = getActivity();

        btn_cancel = (Button) view.findViewById(R.id.btn_cancel_edit);
        btn_save = (Button) view.findViewById(R.id.btn_save);

        swch_confirmacion = (SwitchCompat) view.findViewById(R.id.swch_confirmar);


        fav_no_guia = (TextView) view.findViewById(R.id.fav_edit_no_guia);
        fav_no_guia.requestFocus();
        fav_codigo = (TextView) view.findViewById(R.id.fav_edit_cod_rastreo);


        fav_edit_referencia= (EditText)view.findViewById(R.id.fav_edit_referencia);


        btn_cancel.setOnClickListener(this);
        btn_save.setOnClickListener(this);
      //  swch_confirmacion.setChecked(false);

        no_guia = getArguments().getString("no_guia");
        codigo_rastreo = getArguments().getString("codigo_rastreo");



        codes_info = (Map<String, String>) getArguments().getSerializable("code_array");
        Log.d(TAG, "size: " + codes_info.size());


        fav_no_guia.setText(" " + no_guia);
        fav_codigo.setText(" " + codigo_rastreo);

        Log.d(TAG, "no_guia: " + no_guia);
        Log.d(TAG, "cod_rastreo: " + codigo_rastreo);
        Log.d(TAG, "no_guia: " + fav_no_guia.getText().toString());
        Log.d(TAG, "cod_rastreo: " + fav_codigo.getText().toString());


        String referencia = Favorites.getReferenceByNoGuia(context, "" + codes_info.get("no_guia"));

        if(referencia != null){
            fav_edit_referencia.setText(""+referencia);
        }
        //swch_confirmacion.setChecked(notifica);
        swch_confirmacion.setChecked(Boolean.parseBoolean(Favorites.getFavoriteByWayBill(getActivity(),codes_info.get("no_guia")).get("notifica")));

        //Para poner una linea roja debajo del editText
        fav_edit_referencia.getBackground().setColorFilter(getResources().getColor(R.color.estafeta_red), PorterDuff.Mode.SRC_ATOP);

        swch_confirmacion.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {

                    MyApp.tracker().send(new HitBuilders.EventBuilder("Configuracion", "TapBoton")
                            .setLabel("Boton_On")
                            .build());

                    notify=isChecked;
                    return;
                } else {

                    MyApp.tracker().send(new HitBuilders.EventBuilder("Configuracion", "TapBoton")
                            .setLabel("Boton_Off")
                            .build());

                    notify=isChecked;
                    return;
                }

            }
        });

        if (swch_confirmacion.isChecked()) {
           // Toast.makeText(context, "Switch is currently ON", Toast.LENGTH_SHORT).show();
            notify=true;
        } else {
           // Toast.makeText(context, "Switch is currently OFF", Toast.LENGTH_SHORT).show();
            notify=false;
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

                new HiloLoading().execute();
                context = getActivity();

                codes_info = (Map<String, String>) getArguments().getSerializable("code_array");
                Log.d(TAG, "Data" + codes_info.get("id_favoritos"));


                referencia  = fav_edit_referencia.getText().toString();

                //codigo_rastreo = getArguments().getString("codigo_rastreo");

                Favorites.updateReferenceAndNotifica(context, referencia, codes_info.get("id_favoritos"), notify);

                changeReference.changeDBReference();

                getDialog().dismiss();
                break;
            }

        }

    /**
     * This interface notifies the reference change.
     */
    public interface changeReference{
        public void changeDBReference();

    }

    public void setChangeReference(changeReference listener){
        changeReference = listener;
    }

    /**
     * This class simulate sending reference.
     */
    public class HiloLoading extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING,"Enviando información...",0);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            DialogManager.sharedInstance().dismissDialog();
        }
    }
}


