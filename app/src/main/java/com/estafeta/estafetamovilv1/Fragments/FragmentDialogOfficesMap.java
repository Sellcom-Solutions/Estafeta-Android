package com.estafeta.estafetamovilv1.Fragments;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.LinearLayout;

import com.estafeta.estafetamovilv1.Augmented_Reality_Items.Sensores;
import com.estafeta.estafetamovilv1.Utils.DialogManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.estafeta.estafetamovilv1.R;

import java.util.Locale;
import java.util.Map;

import location.GPS_AR;

/**
 * This class shows in detail the office that is selected on the map offices.
 */
public class FragmentDialogOfficesMap extends DialogFragment implements View.OnClickListener{

    public static String TAG = "DIALOG_MAP_OFFICES";
    private Context context;
    SupportMapFragment supportMapFragment;
    FragmentManager fragmentManager;
    private static Map<String,String> list;
    private static LatLng latLng;
    private static String type;
    private String          sendText;
    private boolean flag = true;


    private TextView        txv_nombre_estafeta,
                            txv_direccion_estafeta,
                            txv_horario_estafeta,
                            txv_horario2_estafeta,
                            txv_correo_estafeta,
                            txv_ext_estafeta,
                            txv_telefono_estafeta;

    private Button          btn_ir_estafeta,
                            btn_compartir_estafeta,
                            btn_cerrar_estafeta;

    private LinearLayout    lin_ext;


    public FragmentDialogOfficesMap(){


    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            list.get("contenedor");

            if(flag) {
                try {

                    FragmentAR fragmentAR = (FragmentAR) getActivity().getSupportFragmentManager().findFragmentByTag(FragmentAR.TAG);
                    GPS_AR.getInstance(context).finalize();
                    Sensores.getInstance(context, null).setListener(null);
                    //Sensores.getInstance(context,this).finalize();
                    //super.finalize();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

                dismiss();
            }
        }catch (Exception e){

        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        fragmentManager = getChildFragmentManager();
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_MinWidth);
        setCancelable(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_map_offices, container, false);

        btn_ir_estafeta     = (Button)view.findViewById(R.id.btn_ir_estafeta);
        btn_ir_estafeta.setOnClickListener(this);

        btn_cerrar_estafeta = (Button)view.findViewById(R.id.btn_cerrar_estafeta);
        btn_cerrar_estafeta.setOnClickListener(this);

        btn_compartir_estafeta = (Button)view.findViewById(R.id.btn_compartir_estafeta);
        btn_compartir_estafeta.setOnClickListener(this);


        txv_nombre_estafeta     = (TextView) view.findViewById(R.id.txv_nombre_estafeta);
        txv_direccion_estafeta  = (TextView)view.findViewById(R.id.txv_direccion_estafeta);
        txv_horario_estafeta    = (TextView)view.findViewById(R.id.txv_horario_estafeta);
        txv_horario2_estafeta   = (TextView)view.findViewById(R.id.txv_horario2_estafeta);
        //txv_correo_estafeta     = (TextView)view.findViewById(R.id.txv_correo_estafeta);
        txv_ext_estafeta        = (TextView)view.findViewById(R.id.txv_ext_estafeta);
        txv_telefono_estafeta   = (TextView)view.findViewById(R.id.txv_telefono_estafeta);
        txv_telefono_estafeta.setOnClickListener(this);

        lin_ext                 = (LinearLayout)view.findViewById(R.id.lin_ext);

        txv_nombre_estafeta.setText(""+list.get("nombre"));

        if(list.get("calle1").equals("") || list.get("calle1").equals("null")){
            txv_direccion_estafeta.setText("");
        }else{
            txv_direccion_estafeta.setText(""+list.get("calle1"));
        }

        if(list.get("colonia_n").equals("") || list.get("colonia_n").equals("null")){
            //NOTHING
        }else{
            txv_direccion_estafeta.append("\n" + list.get("colonia_n"));
        }

        if(list.get("ciudad_n").equals("") || list.get("ciudad_n").equals("null")){
            //NOTHING
        }else{
            txv_direccion_estafeta.append("\n"+list.get("ciudad_n"));
        }

        if(list.get("codigo_postal").equals("") || list.get("codigo_postal").equals("null")){
            //NOTHING
        }else{
            txv_direccion_estafeta.append(" C.P. "+list.get("codigo_postal"));
        }

        txv_horario2_estafeta.setText(""+list.get("horario_atencion"));


        /*
        if(list.get("correo") == null){
            txv_correo_estafeta.setVisibility(View.GONE);
            txv_correo_estafeta.setText("");
        }else{
            txv_correo_estafeta.setVisibility(View.VISIBLE);
            txv_correo_estafeta.setText(""+list.get("correo"));
        }*/

        if(list.get("ext1").equals("0")){
            txv_ext_estafeta.setText("");
            lin_ext.setVisibility(View.GONE);
        }else{
            txv_ext_estafeta.setText(""+list.get("ext1"));
            lin_ext.setVisibility(View.VISIBLE);
        }

        if(list.get("telefono1") == null){
            txv_telefono_estafeta.setVisibility(View.GONE);
            //txv_correo_estafeta.setText("");
        }else{
            txv_telefono_estafeta.setVisibility(View.VISIBLE);
            txv_telefono_estafeta.setText(""+list.get("telefono1"));
        }

        sendText = txv_nombre_estafeta.getText().toString() + ".\n"+txv_direccion_estafeta.getText().toString() + ". \nHorario: " +
                txv_horario2_estafeta.getText().toString() + ".\nTeléfono: " + txv_telefono_estafeta.getText().toString()+".";

        supportMapFragment      = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.mapEstafeta);
        supportMapFragment      = SupportMapFragment.newInstance();
        fragmentManager.beginTransaction().add(R.id.mapEstafeta, supportMapFragment).commit();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (supportMapFragment != null) {

                    GoogleMap mapDialog = supportMapFragment.getMap();


                    if (mapDialog != null) {

                        mapDialog.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        mapDialog.setMyLocationEnabled(false);
                        mapDialog.getUiSettings().setMapToolbarEnabled(false);

                        if (type.equals("SU")) {
                            mapDialog.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_rojo)));
                        } else if (type.equals("CO")) {
                            mapDialog.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_azul)));
                        } else if (type.equals("CA")) {
                            mapDialog.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_gris)));
                        }

                    }


                }
            }
        }, 1000);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        DialogManager.sharedInstance().dismissDialog();
    }

    public static void setList(Map<String, String> list) {
        FragmentDialogOfficesMap.list = list;
    }

    public static void setLatLng(LatLng latLng) {
        FragmentDialogOfficesMap.latLng = latLng;
    }

    public static void setType(String type) {
        FragmentDialogOfficesMap.type = type;
    }


    /**
     * This method opens the office location with Google Maps application.
     * @param latitud
     * @param longitud
     */
    public void onClickMapa(float latitud, float longitud) {
        float latitude = latitud;
        float longitude = longitud;
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f,?z=%d&q=%f,%f (%s)", latitude, longitude, 16, latitude, longitude, list.get("nombre"));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        try {
            startActivity(intent);

            try {

                FragmentAR fragmentAR = (FragmentAR)getActivity().getSupportFragmentManager().findFragmentByTag(FragmentAR.TAG);
                fragmentAR.sensor.finalize();
                fragmentAR.gps.finalize();
                //Sensores.getInstance(context,this).finalize();
                //super.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        } catch(ActivityNotFoundException ex) {
            Toast.makeText(context, "Por favor instale Google Maps", Toast.LENGTH_LONG).show();
            /*
            String my_package_name = "com.google.android.apps.maps";
            String url = "market://details?id=" + my_package_name;
            final Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            try {
                startActivity(intent);
            }catch(ActivityNotFoundException excep){
                Toast.makeText(context, "Por favor instale Google Play", Toast.LENGTH_LONG).show();

            }
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

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_ir_estafeta:

                onClickMapa(Float.parseFloat(list.get("latitud")),Float.parseFloat(list.get("longitud")));

                break;

            case R.id.btn_compartir_estafeta:

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, sendText);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

                break;

            case R.id.btn_cerrar_estafeta:

                flag = false;
                dismiss();

                break;

            case R.id.txv_telefono_estafeta:

                String telefono = list.get("telefono1").replace("(","");
                telefono = telefono.replace(")","");
                onClickTelefono(telefono.trim());

                break;
        }

    }
}