package com.estafeta.estafetamovilv1.Fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.estafeta.estafetamovilv1.Utils.Utilities;
import com.gc.materialdesign.widgets.Dialog;
import com.estafeta.estafetamovilv1.Augmented_Reality_Items.CajaDeTexto;
import com.estafeta.estafetamovilv1.Augmented_Reality_Items.Camara;
import com.estafeta.estafetamovilv1.Augmented_Reality_Items.ContenedorCajas;
import location.GPS_AR;
import com.estafeta.estafetamovilv1.Augmented_Reality_Items.ListaMarcadores;
import com.estafeta.estafetamovilv1.Augmented_Reality_Items.Marcador;
import com.estafeta.estafetamovilv1.Augmented_Reality_Items.Pantalla;
import com.estafeta.estafetamovilv1.Augmented_Reality_Items.Sensores;
import com.estafeta.estafetamovilv1.Augmented_Reality_Items.SensorsListener;
import com.estafeta.estafetamovilv1.R;
import com.estafeta.estafetamovilv1.Utils.DialogManager;
import com.estafeta.estafetamovilv1.Utils.TrackerFragment;
import com.google.android.gms.maps.model.LatLng;

import android.view.View.OnClickListener;
import android.widget.Toast;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by juan.guerra on 22/06/2015.
 * @deprecated
 */
public class FragmentAR extends TrackerFragment implements SensorsListener{


    public static final String TAG = "FRAG_AR";

    //9 a 6,
    //Para barrido vertical es +- 20 grados (Roll)
    private ContenedorCajas contenedor;
    public static Sensores sensor;
    public static List<Marcador> listaMarcadores;
    public static List<Marcador> listaPerfil;
    private OnClickListener listener;
    private boolean ocupado=false;
    public static boolean identificacion;
    private Location ubicacion;
    private Camara camara;
    private RelativeLayout RRA;
    private Context context;
    private View view;
    private RelativeLayout contenedorCajas;
    public static GPS_AR gps;
    private FragmentManager fragmentManager;


    private boolean flag = false;
///////////////////////////////////////////////////////////////////


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getActivity().getSupportFragmentManager();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_augmented_reality, container, false);
        context = getActivity();
        inicializar();


        return view;
    }

    public void inicializar(){
        ListaMarcadores.actualizarMarcadores(context);
        gps= GPS_AR.getInstance(context);
        //RRA.getId();
        contenedorCajas=new RelativeLayout(getActivity());
        RelativeLayout.LayoutParams parametros=new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
/*
        int marginPixel = 10;
        float density = context.getResources().getDisplayMetrics().density;
        int marginDp = (int)(marginPixel * density);
        parametros.setMargins(marginDp, marginDp, marginDp, marginDp);*/

        RRA=(RelativeLayout)view.findViewById(R.id.Principal_layout_RA);
        camara=new Camara(this.getActivity());
        RRA.addView(camara);
        RRA.addView(contenedorCajas, parametros);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ((ActionBarActivity)getActivity()).getSupportActionBar().hide();
        Pantalla.obtenerMedidas(getActivity());
        identificacion=true;
        contenedor=new ContenedorCajas(contenedorCajas,getActivity(),getActivity().getSupportFragmentManager());
        this.ubicacion=null;

        sensor=Sensores.getInstance(getActivity(), this);
        listener=new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                clicElemento(arg0.getId());
            }
        };
        ocupado=false;

        if (DialogManager.sharedInstance().isShowingDialog())
            DialogManager.sharedInstance().dismissDialog();
    }


    @Override
    public void identificar(){
        //Log.d("SENSOR","Valor Leido");
        if (identificacion)
        {
            if(!ocupado){
                ocupado=true;
                List<Marcador> identificados=new LinkedList<Marcador>();

                if(gps.getLoc()!=null)
                {
                    ubicacion=gps.getLoc();
                    identificados=ListaMarcadores.identificarMarcadores(ubicacion,sensor.getAzimuth());
                    //Log.d("Sensor1","Identificar");
                }
                else{
                    if(ubicacion!=null)
                        identificados=ListaMarcadores.identificarMarcadores(ubicacion,sensor.getAzimuth());
                    //else

                    //Log.d("GPS","GPS NULO");
                }
                contenedor.eliminar();
                agregarElementos(identificados);
            }
            ocupado=false;
        }
        else{
            contenedor.eliminar();
        }



    }
    String type = "";
    LatLng position;
    FragmentDialogOfficesMap dialogo;
    Map<String,String> oficina;

    public void agregarElementos(List<Marcador> identificados){

        Log.d("SENSOR","Agregar elementos en direccion: "+ String.valueOf(sensor.getAzimuth()));
        Marcador temporal=new Marcador();

        int posX,posY;

        for (int indice=0;indice<identificados.size();indice++){
            //Log.d("Main","Lugar Identificado");
            temporal=identificados.get(indice);
            posX= ListaMarcadores.verificarPosicionEjeX(ubicacion, sensor.getAzimuth(), temporal, getActivity());
            posY=ListaMarcadores.verificarPosicionEjeY(sensor.getRoll(), temporal, getActivity());

            if(posX == 10000 || posY == 10000){
                Log.v(TAG, "Finalizado forzoso");
                gps.finalize();
                sensor.finalize();
                return;
            }

            contenedor.agregarLugaraPantalla(listener, new CajaDeTexto(context), posX, posY, temporal, ubicacion);

            /*oficina = new HashMap<>();
            oficina.put("nombre",temporal.getNombre());
            oficina.put("calle1",temporal.getCalle1());
            oficina.put("ciudad_n",temporal.getCiudad_n());
            oficina.put("codigo_postal",temporal.getCodigo_postal());
            oficina.put("horario_atencion",temporal.getHorario_atencion());
            oficina.put("ext1",temporal.getExt1());
            oficina.put("telefono1", temporal.getTelefono1());
            oficina.put("colonia_n", temporal.getColonia_n());

            Location location = temporal.getLocalizacionLugar();

            position = new LatLng(location.getLatitude(), location.getLongitude());

            if (Utilities.convertOfficesIntToType(temporal.getTipo_elemento()).equals("SU")) {
                type = "SU";
            } else if (Utilities.convertOfficesIntToType(temporal.getTipo_elemento()).equals("CO")) {
                type = "CO";
            } else if (Utilities.convertOfficesIntToType(temporal.getTipo_elemento()).equals("CA")) {
                type = "CA";
            }

            contenedorCajas.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.LOADING, "Cargando oficina...", 0);

                    new AsyckTask().execute();


                }
            });*/
        }
        //Log.d("SENSOR", "Elementos agregados");

    }


    public void clicElemento(int ID){
    }

    @Override
    public void onResume() {
        super.onResume();
        if(flag){
            flag = false;
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        finalizar();
        flag = true;
    }


    public void finalizar(){
        try {
            GPS_AR.getInstance(context).finalize();
            Sensores.getInstance(context,null).setListener(null);
            //Sensores.getInstance(context,this).finalize();
            //super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public class AsyckTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            dialogo = new FragmentDialogOfficesMap();
            dialogo.setList(oficina);
            dialogo.setLatLng(position);
            dialogo.setType(type);
            dialogo.show(fragmentManager, FragmentDialogOfficesMap.TAG);

            return null;
        }


    }

}
