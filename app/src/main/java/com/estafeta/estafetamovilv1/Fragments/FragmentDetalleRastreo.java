package com.estafeta.estafetamovilv1.Fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.estafeta.estafetamovilv1.Async_Request.METHOD;
import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Utils.DialogManager;
import com.estafeta.estafetamovilv1.Utils.TrackerFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import database.model.Codes;
import database.model.Favorites;
import database.model.History;

/**
 * Created by rebecalopezmartinez.
 * This class shows the result of tracking done.
 */

public class FragmentDetalleRastreo extends TrackerFragment implements View.OnClickListener{

    public static final String      TAG = "FRAG_DETALLE_RASTREO";

    Context                         context;
    TrackerFragment                 fragment;
    FragmentManager                 fragmentManager;
    FragmentTransaction             fragmentTransaction;

    TextView                        no_guia,
                                    cod_rastreo,
                                    origen,
                                    fecha_recol,
                                    destino,
                                    cp_destino,
                                    estatus,
                                    fecha_hora_entrega,
                                    recibio,
                                    footer;

    ImageView                       img_estatus;

    CheckBox                        btn_favorito;

    Button                          btn_historia;

    Map<String, String>             data = new HashMap<>();
    ArrayList<Map<String, String>>  codes_info;
    LinearLayout                    lin_entrega,
                                    lin_recibio;

    String                          new_status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalle_rastreo, container, false);

        context                 = getActivity();

        no_guia                 = (TextView) view.findViewById(R.id.fd_no_guia);
        cod_rastreo             = (TextView) view.findViewById(R.id.fd_cod_rastreo);
        origen                  = (TextView) view.findViewById(R.id.fd_origen);
        fecha_recol             = (TextView) view.findViewById(R.id.fd_fecha_recol);
        destino                 = (TextView) view.findViewById(R.id.fd_destino);
        cp_destino              = (TextView) view.findViewById(R.id.fd_cp_destino);
        estatus                 = (TextView) view.findViewById(R.id.fd_estatus);
        fecha_hora_entrega      = (TextView) view.findViewById(R.id.fd_fecha_hora_entrega);
        recibio                 = (TextView) view.findViewById(R.id.fd_recibio);

        img_estatus             = (ImageView) view.findViewById(R.id.fd_img_status);

        btn_favorito            = (CheckBox) view.findViewById(R.id.fd_btn_favorito);

        btn_historia            = (Button) view.findViewById(R.id.btn_historia);

        final FloatingActionButton btn_call = (FloatingActionButton) view.findViewById(R.id.button_call);
        final FloatingActionButton btn_share = (FloatingActionButton) view.findViewById(R.id.button_share);
        //final FloatingActionButton btn_mail = (FloatingActionButton) view.findViewById(R.id.button_mail);

        lin_entrega             = (LinearLayout) view.findViewById(R.id.lin_entrega);
        lin_recibio             = (LinearLayout) view.findViewById(R.id.lin_recibio);

        codes_info = new ArrayList<>();

        footer                  = (TextView) view.findViewById(R.id.footer);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String currentYear = formatter.format(new Date());
        footer.setText("©2012-" + currentYear + " " + getString(R.string.footer));

        TrackerFragment.section_index = 9;

        btn_favorito.setOnClickListener(this);
        btn_historia.setOnClickListener(this);
        btn_call.setOnClickListener(this);
        btn_share.setOnClickListener(this);
        //btn_mail.setOnClickListener(this);

        String code = getArguments().getString("code");
        Log.d(TAG, "cod_rastreo: " + code);
        codes_info = (ArrayList<Map<String, String>>) getArguments().getSerializable("code_array");
        Log.d(TAG, "size: " + codes_info.size());

        data = Favorites.getFavoriteByWayBill(context, code);
        //Log.d(TAG,"data: "+data.size());

        if (data != null) {

            btn_favorito.setChecked(true);
            btn_favorito.setEnabled(false);

        }



        no_guia.setText(codes_info.get(0).get("wayBill"));
        cod_rastreo.setText(codes_info.get(0).get("shortWayBillId"));
        origen.setText(codes_info.get(0).get("PK_originName"));

        if(!(codes_info.get(0).get("PK_pickupDateTime") == null)){
            if (codes_info.get(0).get("PK_pickupDateTime").equals("")) {// NO TIENE INFORMACIÓN
                fecha_recol.setVisibility(View.GONE);
            } else {
                fecha_recol.setVisibility(View.VISIBLE);
                fecha_recol.setText(codes_info.get(0).get("PK_pickupDateTime"));
            }
        }else{
            fecha_recol.setVisibility(View.GONE);
        }

            destino.setText(codes_info.get(0).get("DD_destinationName"));
            cp_destino.setText(codes_info.get(0).get("DD_zipCode"));
            fecha_hora_entrega.setText(codes_info.get(0).get("DD_deliveryDateTime"));
            recibio.setText(codes_info.get(0).get("DD_receiverName"));

            String statusStr = codes_info.get(0).get("statusSPA");

            String codigoExcStr = codes_info.get(0).get("H_exceptionCode");

        Log.e(TAG,""+codes_info.get(1).get("H_eventDateTime"));

        String estatus_aux= selectImageOnStatus(statusStr, codigoExcStr);


        switch (estatus_aux) {
            case "celda_pr":
                img_estatus.setImageResource(R.drawable.estatus_transito);
                //Log.d("Codigo RE Adapter", "" + estatusStr);
                estatus.setText("Pendiente en tránsito");
                new_status="Pendiente en tránsito";
                codes_info.get(0).put("estatus1",new_status);
                lin_entrega.setVisibility(View.GONE);
                lin_recibio.setVisibility(View.GONE);
                break;

            case "celda_pe":
                img_estatus.setImageResource(R.drawable.estatus_transito);
                //Log.d("Codigo RE Adapter", "" + estatusStr);
                new_status="Pendiente";
                codes_info.get(0).put("estatus1", new_status);
                estatus.setText("Pendiente");
                lin_entrega.setVisibility(View.GONE);
                lin_recibio.setVisibility(View.GONE);
                break;

            case "celda_en":
                img_estatus.setImageResource(R.drawable.estatus_entregado);
                //Log.d("Codigo RE Adapter", "" + estatusStr);
                new_status="Entregado";
                codes_info.get(0).put("estatus1", new_status);
                estatus.setText("Entregado");
                lin_entrega.setVisibility(View.VISIBLE);
                lin_recibio.setVisibility(View.VISIBLE);
                break;

            default:
                img_estatus.setImageResource(R.drawable.estatus_sin);
                //Log.d("Codigo RE Adapter", "" + estatusStr);
                new_status="Sin información";
                codes_info.get(0).put("estatus1", new_status);
                estatus.setText("Sin información");
                lin_entrega.setVisibility(View.GONE);
                lin_recibio.setVisibility(View.GONE);
                break;
        }

        //Este código si sirve es para decodificar la firma, aun no se implementa pero se hara
        if(codes_info.get(0).get("signature") != null){

            try {
                ImageView img_signature = (ImageView)view.findViewById(R.id.img_signature);

                img_signature.setImageBitmap(decodeBase64(codes_info.get(0).get("signature")));
            }catch (Exception e){
                Toast.makeText(context,"no se pudo",Toast.LENGTH_SHORT).show();
            }


        }else{
            Toast.makeText(context,"firma null",Toast.LENGTH_SHORT).show();
        }

      //  }
        return view;
    }

    /**
     * @deprecated
     * @param input
     * @return
     */
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_historia:
                //Toast.makeText(context, "Módulo en Desarrollo", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putSerializable("codes_info", codes_info);
                bundle.putString("origin","detalle_rastreo");

                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragment = new FragmentHistory();
                fragment.addFragmentToStack(getActivity());
                fragment.setArguments(bundle);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, fragment, TAG);
                fragmentTransaction.commit();
                break;

            case R.id.fd_btn_favorito:
                Log.d(TAG, "btn favorito: ");
                if (btn_favorito.isChecked()) {
                    try {

                        long idFavorite = Favorites.insertMap(context, codes_info.get(0));

                        for(int i = 1; i<codes_info.size(); i++){

                            codes_info.get(i).put("favorite_id", String.valueOf(idFavorite));
                            History.insertMap(context, codes_info.get(i));

                        }

                        btn_favorito.setEnabled(false);
                        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.SUCCESS, context.getString(R.string.exito_agregar_fav),4050);


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

                String sendText ="";
                if(fecha_hora_entrega.getText().toString().equals("")){
                    sendText = "No.Guía: "          + no_guia.getText()+". "
                            +"Código rastreo: "     + cod_rastreo.getText()+". "
                            +"Origen: "             + origen.getText()+". "
                            +"Destino: "            + destino.getText()+". "
                            +"Estatus: "            + estatus.getText()+".";

                }else{
                    sendText = "No.Guía: "          + no_guia.getText()+". "
                            +"Código rastreo: "     + cod_rastreo.getText()+". "
                            +"Origen: "             + origen.getText()+". "
                            +"Destino: "            + destino.getText()+". "
                            +"Estatus: "            + estatus.getText()+". "
                            +"Fecha de entrega: "   + fecha_hora_entrega.getText()+". "
                            +"Recibió: "            + recibio.getText()+".";
                }


                sendIntent.putExtra(Intent.EXTRA_TEXT, sendText);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;

            /*
            case R.id.button_mail:

                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                String email = "";

                if(no_guia.getText().toString().equals("")){
                    if(cod_rastreo.getText().toString().equals("")){
                        //No se envia nada.
                    }else{
                        email = "Código rastreo: " + cod_rastreo.getText()+"\n"
                                +"Teléfono: "+"\n\n"
                                +"Selecciona la opción que necesitas: "+"\n\n"
                                +"( )Rastreo de envío"+"\n"
                                +"   ( )Estatus de envío"+"\n"
                                +"   ( )Fecha de entrega"+"\n"
                                +"( )Información general"+"\n\n"
                                +"--------------------------------------------------------"+"\n"
                                +"( )Comentarios o sugerencias"+"\n\n"
                                +"Estafeta Mexicana, S.A. de C.V.";
                    }

                }else{
                    if(cod_rastreo.getText().toString().equals("")){
                        //No se envia nada.
                        if(cod_rastreo.getText().toString().equals("")){
                            //No se envia nada.
                        }else{
                            email = "No. Guía: " + no_guia.getText()+"\n"
                                    +"Teléfono: "+"\n\n"
                                    +"Selecciona la opción que necesitas: "+"\n\n"
                                    +"( )Rastreo de envío"+"\n"
                                    +"   ( )Estatus de envío"+"\n"
                                    +"   ( )Fecha de entrega"+"\n"
                                    +"( )Información general"+"\n\n"
                                    +"--------------------------------------------------------"+"\n"
                                    +"( )Comentarios o sugerencias"+"\n\n"
                                    +"Estafeta Mexicana, S.A. de C.V.";
                        }
                    }else{
                        email = "No. Guía: " + no_guia.getText()+"\n"
                                +"Código rastreo: " + cod_rastreo.getText()+"\n"
                                +"Teléfono: "+"\n\n"
                                +"Selecciona la opción que necesitas: "+"\n\n"
                                +"( )Rastreo de envío"+"\n"
                                +"   ( )Estatus de envío"+"\n"
                                +"   ( )Fecha de entrega"+"\n"
                                +"( )Información general"+"\n\n"
                                +"--------------------------------------------------------"+"\n"
                                +"( )Comentarios o sugerencias"+"\n\n"
                                +"Estafeta Mexicana, S.A. de C.V.";
                    }
                }





                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, "emaildeestafeta@email.com");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Información de envíos Estafeta AM");
                emailIntent.putExtra(Intent.EXTRA_TEXT, email);

                try {
                    startActivity(Intent.createChooser(emailIntent, "Enviar correo..."));
                    //startActivity(emailIntent);
                }
                catch (android.content.ActivityNotFoundException ex) {
                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.ERROR, "No hay un cliente de email instalado",3000);
                }

                break;
            */

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

    /**
     * This method selects what kind of image corresponds to each scan.
     * @param status
     * @param code
     * @return
     */
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

    @Override
    public void prepareRequest(METHOD method, Map<String, String> params, boolean includeCredentials) {
        super.prepareRequest(method, params, includeCredentials);
    }

    @Override
    public void decodeResponse(String stringResponse) {
        super.decodeResponse(stringResponse);
    }
}
