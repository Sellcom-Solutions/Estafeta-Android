package com.sellcom.apps.tracker_material.Fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gc.materialdesign.widgets.Dialog;
import com.sellcom.apps.tracker_material.Augmented_Reality_Items.CajaDeTexto;
import com.sellcom.apps.tracker_material.Augmented_Reality_Items.Camara;
import com.sellcom.apps.tracker_material.Augmented_Reality_Items.ContenedorCajas;
import location.GPS_AR;
import com.sellcom.apps.tracker_material.Augmented_Reality_Items.ListaMarcadores;
import com.sellcom.apps.tracker_material.Augmented_Reality_Items.Marcador;
import com.sellcom.apps.tracker_material.Augmented_Reality_Items.Pantalla;
import com.sellcom.apps.tracker_material.Augmented_Reality_Items.Sensores;
import com.sellcom.apps.tracker_material.Augmented_Reality_Items.SensorsListener;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.DialogManager;
import com.sellcom.apps.tracker_material.Utils.TrackerFragment;
import android.view.View.OnClickListener;


import java.util.LinkedList;
import java.util.List;

/**
 * Created by juan.guerra on 22/06/2015.
 */
public class FragmentAR extends TrackerFragment implements SensorsListener{


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
    private GPS_AR gps;
///////////////////////////////////////////////////////////////////


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_augmented_reality, container, false);
        context = getActivity();
        if (verificarSensores())
            inicializar();
        else {
            if (DialogManager.sharedInstance().isShowingDialog())
                DialogManager.sharedInstance().dismissDialog();
        }

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

        RRA=(RelativeLayout)view.findViewById(R.id.Principal_layout_RA);
        camara=new Camara(this.getActivity());
        RRA.addView(camara);
        RRA.addView(contenedorCajas, parametros);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ((ActionBarActivity)getActivity()).getSupportActionBar().hide();
        Pantalla.obtenerMedidas(getActivity());
        identificacion=true;
        contenedor=new ContenedorCajas(contenedorCajas);
        this.ubicacion=null;

        sensor=Sensores.getInstance(getActivity(), this);
        listener=new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                clicElemento(arg0.getId());
            }
        };
        ocupado=false;
    }

    public boolean verificarSensores(){
        SensorManager sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null){
          //if (null == null){
                Dialog dialog = new Dialog(context,"Error",getResources().getString(R.string.no_sensor));
                dialog.show();
            getActivity().onBackPressed();
              return false;
        }
        else
              return true;
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


    public void agregarElementos(List<Marcador> identificados){
        Log.d("SENSOR","Agregar elementos en direccion: "+ String.valueOf(sensor.getAzimuth()));
        Marcador temporal=new Marcador();
        int posX,posY;
        for (int indice=0;indice<identificados.size();indice++){
            //Log.d("Main","Lugar Identificado");
            temporal=identificados.get(indice);
            posX= ListaMarcadores.verificarPosicionEjeX(ubicacion, sensor.getAzimuth(), temporal);
            posY=ListaMarcadores.verificarPosicionEjeY(sensor.getRoll(), temporal);
            contenedor.agregarLugaraPantalla(listener, new CajaDeTexto(context), posX, posY, temporal, ubicacion);
        }
        //Log.d("SENSOR", "Elementos agregados");

    }


    public void clicElemento(int ID){
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    public void finalizar(){
        try {
            GPS_AR.getInstance(context).finalize();

            Sensores.getInstance(context,this).setListener(null);
            //Sensores.getInstance(context,this).finalize();

            //super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

}
