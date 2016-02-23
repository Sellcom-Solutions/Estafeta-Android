package com.estafeta.estafetamovilv1.Fragments;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Utils.TrackerFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anel 01/07/2015.
 * This class shows the detail of a saved favorites scan.
 */
public class FragmentDetailFavorite extends TrackerFragment implements View.OnClickListener {

    String TAG = "FRAG_DIALOG_FAVORITE";

    Context context;

    TextView fav_no_guia,
            fav_codigo,
            fav_origen,
            fd_fecha_recol,
            fav_destino,
            fav_cp_destino,
             fav_estatus,
             fav_fecha,
             fav_recibio,
            footer;

    Button btn_historia;

    ImageView   img_estatus;

    Map<String, String> data = new HashMap<>();
    Map<String, String> codes_info = new HashMap<>();

    LinearLayout lin_entrega,
            lin_recibio;

    TrackerFragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        //setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_MinWidth);
        //setCancelable(false);
        fragmentManager = getActivity().getSupportFragmentManager();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_detail_favorite, container, false);

        fav_no_guia = (TextView) view.findViewById(R.id.fav_no_guia);
        fav_codigo = (TextView) view.findViewById(R.id.fav_cod_rastreo);
        fav_origen = (TextView) view.findViewById(R.id.fav_origen);
        fd_fecha_recol = (TextView)view.findViewById(R.id.fd_fecha_recol);
        fav_destino = (TextView) view.findViewById(R.id.fav_destino);
        fav_cp_destino = (TextView) view.findViewById(R.id.fav_cp_destino);
        fav_estatus = (TextView) view.findViewById(R.id.fav_estatus);
        fav_fecha = (TextView) view.findViewById(R.id.fav_fecha);
        fav_recibio = (TextView) view.findViewById(R.id.fav_recibio);

        lin_entrega         = (LinearLayout)view.findViewById(R.id.lin_entrega);
        lin_recibio         = (LinearLayout)view.findViewById(R.id.lin_recibio);

        img_estatus         = (ImageView) view.findViewById(R.id.fd_img_status);


        btn_historia = (Button)view.findViewById(R.id.btn_historia);
        btn_historia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("codes_info", (java.io.Serializable) codes_info);
                bundle.putString("origin", "detalle_favorito");

                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragment = new FragmentHistory();
                fragment.addFragmentToStack(getActivity());
                fragment.setArguments(bundle);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, fragment, TAG);
                fragmentTransaction.commit();
            }
        });

        footer      = (TextView)view.findViewById(R.id.footer);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String currentYear = formatter.format(new Date());
        footer.setText("©2012-"+currentYear+" "+getString(R.string.footer));

        TrackerFragment.section_index = 8;

        final FloatingActionButton btn_call = (FloatingActionButton) view.findViewById(R.id.btn_fav_call);
        final FloatingActionButton btn_share = (FloatingActionButton) view.findViewById(R.id.btn_fav_share);

        btn_call.setOnClickListener(this);
        btn_share.setOnClickListener(this);


        codes_info = (Map<String, String>) getArguments().getSerializable("code_array");
        Log.d(TAG, "size: " + codes_info.size());


        if(!(codes_info.get("no_guia") == null)) {
            fav_no_guia.setText(" " + codes_info.get("no_guia"));
        }
        if(!(codes_info.get("codigo_rastreo") == null)) {
            fav_codigo.setText(" " + codes_info.get("codigo_rastreo"));
        }
        if(!(codes_info.get("origen") == null)) {
            fav_origen.setText(" " + codes_info.get("origen"));
        }
        if(!(codes_info.get("fecha_recoleccion") == null)) {

            if(codes_info.get("fecha_recoleccion").equals("")){// NO TIENE INFORMACIÓN
                fd_fecha_recol.setVisibility(View.GONE);
            }else{
                fd_fecha_recol.setVisibility(View.VISIBLE);
                fd_fecha_recol.setText(" " + codes_info.get("fecha_recoleccion"));
            }

        }else{
            fd_fecha_recol.setVisibility(View.GONE);
        }
        if(!(codes_info.get("destino") == null)) {
            fav_destino.setText(" " + codes_info.get("destino"));
        }
        if(!(codes_info.get("cp_destino") == null)) {
            fav_cp_destino.setText(" " + codes_info.get("cp_destino"));
        }
        if(!(codes_info.get("estatus") == null)) {
            Log.d(TAG,"estatus: "+codes_info.get("estatus"));

          /*  if(codes_info.get("estatus").equals("CONFIRMADO")){
                fav_estatus.setText(" Entregado");
            }else if(codes_info.get("estatus").equals("DEVUELTO")){
                fav_estatus.setText(" Pendiente");
            }else if(codes_info.get("estatus").equals("EN_TRANSITO")){
                fav_estatus.setText(" Pendiente en tránsito");
            }*/

            setStatus(codes_info.get("estatus"));
            fav_estatus.setText(codes_info.get("estatus"));

        }
        if(!(codes_info.get("fechaHoraEntrega") == null)) {
            fav_fecha.setText(codes_info.get("fechaHoraEntrega"));
        }
        if(!(codes_info.get("recibio") == null)) {
            fav_recibio.setText(" " + codes_info.get("recibio"));
        }



        return view;
    }

    public void setStatus(String status){

        switch (status){
            case "Entregado":
                img_estatus.setImageResource(R.drawable.estatus_entregado);
                lin_entrega.setVisibility(View.VISIBLE);
                lin_recibio.setVisibility(View.VISIBLE);
                break;

            case "Pendiente":
                img_estatus.setImageResource(R.drawable.estatus_transito);
                lin_entrega.setVisibility(View.GONE);
                lin_recibio.setVisibility(View.GONE);
                break;

            case "Sin información":
                img_estatus.setImageResource(R.drawable.estatus_sin);
                lin_entrega.setVisibility(View.GONE);
                lin_recibio.setVisibility(View.GONE);
                break;

            case "Pendiente en tránsito":
                img_estatus.setImageResource(R.drawable.estatus_transito);
                lin_entrega.setVisibility(View.GONE);
                lin_recibio.setVisibility(View.GONE);
                break;
        }

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

                String sendText ="";
                if(fav_fecha.getText().toString().equals("")){
                    sendText = "No. Guía: "+ fav_no_guia.getText()+". "
                            +"Código rastreo: "+fav_codigo.getText()+". "
                            +"Origen: "+fav_origen.getText()+". "
                            +"Destino: "+fav_destino.getText()+". "
                            +"Estatus: "+fav_estatus.getText()+".";
                }else{
                    sendText = "No. Guía: "+ fav_no_guia.getText()+". "
                            +"Código rastreo: "+fav_codigo.getText()+". "
                            +"Origen: "+fav_origen.getText()+". "
                            +"Destino: "+fav_destino.getText()+". "
                            +"Estatus: "+fav_estatus.getText()+". "
                            +"Fecha de entrega: "+fav_fecha.getText()+". "
                            +"Recibió: "+fav_recibio.getText()+".";
                }

                sendIntent.putExtra(Intent.EXTRA_TEXT, sendText);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
        }

    }

    /**
     * This method calls to a phone number.
     * @param phoneNumber Phone Number.
     */
    public void onClickTelefono(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}
