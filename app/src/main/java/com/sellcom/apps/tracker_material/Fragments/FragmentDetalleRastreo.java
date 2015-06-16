package com.sellcom.apps.tracker_material.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import database.model.Favorites;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDetalleRastreo extends TrackerFragment {

    String TAG= "FRAG_DETALLE_RASTREO";

    Context context;
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

    Map<String, String> data = new HashMap<>();

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

        //String code = (String) getArguments().getSerializable("codes");
        String code = getArguments().getString("code");
        Log.d(TAG,"cod_rastreo: "+code);

        try {
            data = Favorites.getFavoriteByWayBill(context,code);
            Log.d(TAG,"data: "+data.size());

            no_guia.setText(data.get("no_guia"));
            cod_rastreo.setText(data.get("codigo_rastreo"));
            origen.setText(data.get("origen"));
            fecha_recol.setText(data.get("fecha_recoleccion"));
            destino.setText(data.get("destino"));
            cp_destino.setText(data.get("cp_destino"));
            fecha_hora_entrega.setText(data.get("echaHoraEntrega"));
            recibio.setText(data.get("recibio"));

            String statusStr = data.get("estatus");
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }


}
