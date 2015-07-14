package com.sellcom.apps.tracker_material.Fragments;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.DialogFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sellcom.apps.tracker_material.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anel 01/07/2015.
 */
public class FragmentDialogFavorite  extends DialogFragment implements View.OnClickListener {

    String TAG = "FRAG_DIALOG_FAVORITE";

    Context context;

    Button fav_call;
    Button fav_share;
    Button fav_closed;
    TextView fav_no_guia;
    TextView fav_codigo;
    TextView fav_reference;
    TextView fav_origen;
    TextView fav_destino;
    TextView fav_cp_destino;
    TextView fav_estatus;
    TextView fav_fecha;
    TextView fav_recibio;

    Map<String, String> data = new HashMap<>();
    Map<String, String> codes_info = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_MinWidth);
        //setCancelable(false);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_dialog_favorite, container, false);

        fav_reference = (TextView) view.findViewById(R.id.fav_reference);
        fav_no_guia = (TextView) view.findViewById(R.id.fav_no_guia);
        fav_codigo = (TextView) view.findViewById(R.id.fav_cod_rastreo);
        fav_origen = (TextView) view.findViewById(R.id.fav_origen);
        fav_destino = (TextView) view.findViewById(R.id.fav_destino);
        fav_cp_destino = (TextView) view.findViewById(R.id.fav_cp_destino);
        fav_estatus = (TextView) view.findViewById(R.id.fav_estatus);
        fav_fecha = (TextView) view.findViewById(R.id.fav_fecha);
        fav_recibio = (TextView) view.findViewById(R.id.fav_recibio);

        fav_call=(Button)view.findViewById(R.id.btn_fav_call);
        fav_share=(Button)view.findViewById(R.id.btn_fav_share);
        fav_closed=(Button)view.findViewById(R.id.btn_fav_closed);


        fav_call.setOnClickListener(this);
        fav_share.setOnClickListener(this);
        fav_closed.setOnClickListener(this);


        codes_info = (Map<String, String>) getArguments().getSerializable("code_array");
        Log.d(TAG, "size: " + codes_info.size());


        fav_reference.setText(codes_info.get("referencia"));
        fav_no_guia.setText(codes_info.get("no_guia"));
        fav_codigo.setText(codes_info.get("codigo_rastreo"));
        fav_origen.setText(codes_info.get("origen" ));
        fav_destino.setText(codes_info.get("destino"));
        fav_cp_destino.setText(codes_info.get("cp_destino"));
        fav_estatus.setText(codes_info.get("estatus"));
        fav_fecha.setText(codes_info.get("fechaHoraEntrega"));
        fav_recibio.setText(codes_info.get("recibio"));

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_fav_call:
                Log.d(TAG, "Action Call");
                String phoneNumber = "018003782338";
                onClickTelefono(phoneNumber);
                Log.d("Cancel", "" + getId());
                break;

            case R.id.btn_fav_share:
                Log.d("SHARE", "" + getId());

                Log.d(TAG, "Action Share");
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);

                String sendText = "Referencia: "+ fav_reference.getText()+". "
                        +"No.Guía: "+ fav_no_guia.getText()+". "
                        +"Código Rastreo: "+fav_codigo.getText()+". "
                        +"Estatus: "+fav_estatus.getText()+". "
                        +"Origen: "+fav_origen.getText()+". "
                        +"Destino: "+fav_destino.getText()+". "
                        +"CP: "+fav_cp_destino.getText()+". "
                        +"Fecha: "+fav_fecha.getText()+". "
                        +"Recibio: "+fav_recibio.getText()+". ";

          //      sendIntent.putExtra(Intent.EXTRA_SUBJECT, sendText);
                sendIntent.putExtra(Intent.EXTRA_TEXT, sendText);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;

            case R.id.btn_fav_closed:
                Log.d("Closed","" + getId());
                getDialog().dismiss();
                break;
        }

    }

    public void onClickTelefono(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}
