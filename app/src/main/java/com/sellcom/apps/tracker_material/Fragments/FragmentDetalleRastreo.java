package com.sellcom.apps.tracker_material.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.DialogManager;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;

import java.util.HashMap;
import java.util.Map;

import database.model.Codes;
import database.model.Favorites;
import database.model.History;

/**
 * Created by rebecalopezmartinez .
 */

public class FragmentDetalleRastreo extends TrackerFragment implements View.OnClickListener{

    String TAG= "FRAG_DETALLE_RASTREO";

    Context context;
    TrackerFragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    TextView no_guia,
             cod_rastreo,
             origen,
             fecha_recol,
             destino,
             cp_destino,
             estatus,
             fecha_hora_entrega,
             recibio;
    ImageView img_estatus;
    CheckBox btn_favorito;
    Button btn_historia;

    Map<String, String> data = new HashMap<>();
    Map<String, String> codes_info = new HashMap<>();

    public FragmentDetalleRastreo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_detalle_rastreo, container, false);
        context = getActivity();

        no_guia             = (TextView) view.findViewById(R.id.fd_no_guia);
        cod_rastreo         = (TextView) view.findViewById(R.id.fd_cod_rastreo);
        origen              = (TextView) view.findViewById(R.id.fd_origen);
        fecha_recol         = (TextView) view.findViewById(R.id.fd_fecha_recol);
        destino             = (TextView) view.findViewById(R.id.fd_destino);
        cp_destino          = (TextView) view.findViewById(R.id.fd_cp_destino);
        estatus             = (TextView) view.findViewById(R.id.fd_estatus);
        fecha_hora_entrega  = (TextView) view.findViewById(R.id.fd_fecha_hora_entrega);
        recibio             = (TextView) view.findViewById(R.id.fd_recibio);
        img_estatus         = (ImageView) view.findViewById(R.id.fd_img_status);
        btn_favorito        = (CheckBox) view.findViewById(R.id.fd_btn_favorito);
        btn_historia        = (Button)view.findViewById(R.id.btn_historia);
        final FloatingActionButton btn_call = (FloatingActionButton) view.findViewById(R.id.button_call);
        final FloatingActionButton btn_share = (FloatingActionButton) view.findViewById(R.id.button_share);

        btn_favorito.setOnClickListener(this);
        btn_historia.setOnClickListener(this);
        btn_call.setOnClickListener(this);
        btn_share.setOnClickListener(this);

        String code = getArguments().getString("code");
        Log.d(TAG,"cod_rastreo: "+code);
        codes_info = (Map<String, String>) getArguments().getSerializable("code_array");
        Log.d(TAG, "size: "+codes_info.size());

        data = Favorites.getFavoriteByWayBill(context,code);
            //Log.d(TAG,"data: "+data.size());

        if(data != null) {
/*            long idHistory = History.insertMap(context, codes_info);
            Log.d(TAG,"Despues de insertar en History");
            codes_info.put("history_id", String.valueOf(idHistory));

            Favorites.update(context,codes_info);

            no_guia.setText(data.get("no_guia"));
            cod_rastreo.setText(data.get("codigo_rastreo"));
            origen.setText(data.get("origen"));
            fecha_recol.setText(data.get("fecha_recoleccion"));
            destino.setText(data.get("destino"));
            cp_destino.setText(data.get("cp_destino"));
            fecha_hora_entrega.setText(data.get("fechaHoraEntrega"));
            recibio.setText(data.get("recibio"));*/
            btn_favorito.setChecked(true);
            btn_favorito.setEnabled(false);

            /*String statusStr = data.get("estatus");
            switch (statusStr) {
                case "EN_TRANSITO":
                    img_estatus.setImageResource(R.drawable.estatus_transito);
                    //Log.d("Codigo RE Adapter", "" + estatusStr);
                    estatus.setText("Pendiente de entrega");
                    break;

                case "ENTREGADO":
                    img_estatus.setImageResource(R.drawable.estatus_entregado);
                    //Log.d("Codigo RE Adapter", "" + estatusStr);
                    estatus.setText("Entregado");
                    break;

                default:
                    img_estatus.setImageResource(R.drawable.estatus_sin);
                    //Log.d("Codigo RE Adapter", "" + estatusStr);
                    estatus.setText("Sin información");
                    break;
            }*/

        }
       // else {
            no_guia.setText(codes_info.get("wayBill"));
            cod_rastreo.setText(codes_info.get("shortWayBillId"));
            origen.setText(codes_info.get("PK_originName"));
            fecha_recol.setText(codes_info.get("PK_pickupDateTime"));
            destino.setText(codes_info.get("DD_destinationName"));
            cp_destino.setText(codes_info.get("DD_zipCode"));
            fecha_hora_entrega.setText(codes_info.get("DD_deliveryDateTime"));
            recibio.setText(codes_info.get("DD_receiverName"));

            String statusStr = codes_info.get("statusSPA");
            String codigoExcStr = codes_info.get("H_exceptionCode");

        String estatus_aux= selectImageOnStatus(statusStr, codigoExcStr);

        switch (estatus_aux) {
            case "celda_pr":
                img_estatus.setImageResource(R.drawable.estatus_transito);
                //Log.d("Codigo RE Adapter", "" + estatusStr);
                estatus.setText("Proceso de entrega");
                break;

            case "celda_pe":
                img_estatus.setImageResource(R.drawable.estatus_sin);
                //Log.d("Codigo RE Adapter", "" + estatusStr);
                estatus.setText("Pendiente");
                break;

            case "celda_en":
                img_estatus.setImageResource(R.drawable.estatus_entregado);
                //Log.d("Codigo RE Adapter", "" + estatusStr);
                estatus.setText("Entregado");
                break;

            default:
                img_estatus.setImageResource(R.drawable.estatus_sin);
                //Log.d("Codigo RE Adapter", "" + estatusStr);
                estatus.setText("Sin información");
                break;
        }

      //  }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_historia:
                Toast.makeText(context, "Módulo en Desarrollo", Toast.LENGTH_SHORT).show();
/*
                Bundle bundle = new Bundle();
                bundle.putSerializable("codes_info",(java.io.Serializable) codes_info);

                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragment = new FragmentHistory();
                fragment.addFragmentToStack(getActivity());
                fragment.setArguments(bundle);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, fragment, TAG);
                fragmentTransaction.commit();*/
                break;

            case R.id.fd_btn_favorito:
                Log.d(TAG, "btn favorito: ");
                if (btn_favorito.isChecked()) {
                    try {
                        long idHistory = History.insertMap(context, codes_info);
                        Log.d(TAG, "Despues de insertar en History");
                        codes_info.put("history_id", String.valueOf(idHistory));
                        Favorites.insert(context, codes_info);
                        Log.d(TAG, "Despues de insertar en Favorites");
                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.SUCCESS, context.getString(R.string.exito_agregar_fav),3000);
                        btn_favorito.setEnabled(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.button_call:
                Log.d(TAG, "Action Call");
                String phoneNumber = "018003782338";
                onClickTelefono(phoneNumber);
                break;

            case R.id.button_share:
                Log.d(TAG, "Action Share");
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);

                String sendText = "No.Guía: "+ no_guia.getText()+". "
                                  +"Código Rastreo: "+cod_rastreo.getText()+". "
                                  +"Origen: "+origen.getText()+". "
                                  +"Destino: "+destino.getText()+". ";

                sendIntent.putExtra(Intent.EXTRA_SUBJECT, sendText);
                sendIntent.putExtra(Intent.EXTRA_TEXT, sendText);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
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

    public String selectImageOnStatus(String status, String code){

        boolean conCodigo = false;
        if (code != null)
            conCodigo = Codes.existsZclave(code, context);

        String imagen = "celda_";
        if (status == null) {
            imagen = imagen + "no";
        } else {
            if (status.equals("CONFIRMADO")) {
                imagen = imagen + "en";
            } else if (status.equals("DEVUELTO")) {
                imagen = imagen + "pe";
            } else if (status.equals("EN_TRANSITO")) {
                if (conCodigo) {
                    imagen = imagen + "pe";
                } else {
                    imagen = imagen + "pr";
                }
            }
        }
        return imagen;
    }
}
