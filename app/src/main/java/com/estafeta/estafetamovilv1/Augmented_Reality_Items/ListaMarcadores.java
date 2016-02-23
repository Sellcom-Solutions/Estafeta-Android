package com.estafeta.estafetamovilv1.Augmented_Reality_Items;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.DisplayMetrics;
import android.util.Log;

import com.estafeta.estafetamovilv1.Fragments.FragmentOfficesMap;
import com.estafeta.estafetamovilv1.Utils.Utilities;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import database.model.Offices;
import location.GPS_AR;

/**
 * Created by juan.guerra on 23/06/2015.
 * @deprecated
 */
public class ListaMarcadores {

    public static List<Marcador> listaMarcadores;
    public static List<Marcador> newListaMarcadores;
    public static double tolerancia=Pantalla.Angulo_Tolerancia;
    public static double distancia;
    private static final float DISTANCIA= 5000;
    private static final int NUMERO_ELEMENTOS = 5;
    private static boolean flag = true;


    public static void actualizarMarcadores(Context context){
        listaMarcadores=new LinkedList<Marcador>();
        newListaMarcadores=new LinkedList<Marcador>();
        List<Map<String,String>> listOficinas;

        flag = true;
        listOficinas = Offices.getAllInMaps(context);
        Map<String,String> oficina;
        if (listOficinas == null)
            return;

        for (int index=0;index<listOficinas.size();index++){
            oficina=listOficinas.get(index);
            try{
                newListaMarcadores.add(new Marcador(
                        Integer.valueOf(oficina.get(Offices.ID_OFFICE)),
                        oficina.get(Offices.NOMBRE),
                        Double.valueOf(oficina.get(Offices.LATITUD)),
                        Double.valueOf(oficina.get(Offices.LONGITUD)),
                        Utilities.convertOfficesTypeToInt(oficina.get(Offices.TIPO_OFICINA)),
                        oficina.get(Offices.CALLE1),
                        oficina.get(Offices.CIUDAD_N),
                        oficina.get(Offices.CODIGO_POSTAL),
                        oficina.get(Offices.HORARIO_ATENCION),
                        oficina.get(Offices.EXT1),
                        oficina.get(Offices.TELEFONO1),
                        oficina.get(Offices.COLONIA_N),
                        oficina.get(Offices.LATITUD),
                        oficina.get(Offices.LONGITUD)));



            }
            catch(java.lang.NumberFormatException e){
                //e.printStackTrace();
            }
        }

    }

    public static List<Marcador> verificarMarcadores(Location loc,double direccion){
        //Log.d("Direccion", "B: "+String.valueOf(direccion));
        List<Marcador> identificados=new LinkedList<Marcador>();
        Marcador aux;
        //Verificar Bearing
        //Log.d("Marcadores Size",String.valueOf(listaMarcadores.size()));

        if(flag) {
            flag = false;
            for (int i = 0; i < newListaMarcadores.size(); i++) {
                if (loc.distanceTo(newListaMarcadores.get(i).getLocalizacionLugar()) <= DISTANCIA) {
                    listaMarcadores.add(newListaMarcadores.get(i));
                }
            }

            listaMarcadores = ordenarMarcadores(listaMarcadores, loc);
            Log.d("lista","sizeList: "+ listaMarcadores.size());
        }
        //*****************************************************************************************************************************
        for(int i=0;i<listaMarcadores.size();i++){
            aux=listaMarcadores.get(i);
            aux.setEnVista(false);
            double bearing=loc.bearingTo(aux.getLocalizacionLugar());
            if(bearing<0){
                bearing=360+bearing;
            }
            //Log.d("Bearing", "Bearing: "+String.valueOf(i)+": "+String.valueOf(     bearing     ));
            //Log.d("Direccion", "Direccion: "+String.valueOf(direccion));
            double Dmin,Dmax;
            Dmax=direccion+tolerancia;
            if (Dmax>360)
                Dmax=Dmax-360;

            Dmin=direccion-tolerancia;
            if (Dmin<0)
                Dmin=360+Dmin;

            if ( (Dmax>=0&&Dmax<=2*tolerancia)&& (Dmin<=360&&Dmin>=360-2*tolerancia) ){
                //Casos particulares
                if ( bearing<2*tolerancia){
                    //Caso bearing a la derecha
                    if(bearing<Dmax){
                        //Identificado
                        //Log.d("Barra",String.valueOf(Barra.DISTANCIA));
                        //Log.d("Distancia",String.valueOf(loc.distanceTo(aux.getLocalizacionLugar())));
                        if(loc.distanceTo(aux.getLocalizacionLugar()) <= DISTANCIA){
                            identificados.add(aux);
                            //Log.d("Identificados", "Identificado: "+ String.valueOf(loc.bearingTo(aux.getLocalizacionLugar())));
                            //aux.setEnVista(true);
                        }
                    }
                }
                else if( bearing>360-2*tolerancia ){
                    //Caso bearing a la izquierda
                    if (bearing>Dmin){
                        //Identificado
                        //Log.d("Barra",String.valueOf(Barra.DISTANCIA));
                        //Log.d("Distancia",String.valueOf(loc.distanceTo(aux.getLocalizacionLugar())));
                        if(loc.distanceTo(aux.getLocalizacionLugar()) <= DISTANCIA){
                            identificados.add(aux);
                            //Log.d("Identificados", "Identificado: "+ String.valueOf(loc.bearingTo(aux.getLocalizacionLugar())));
                            //aux.setEnVista(true);
                        }
                    }
                }
            }
            else{
                //Casos generales
                if( (bearing>=direccion-tolerancia) && (bearing<=direccion+tolerancia) ){
                    //Log.d("Barra",String.valueOf(Barra.DISTANCIA));
                    //Log.d("Distancia",String.valueOf(loc.distanceTo(aux.getLocalizacionLugar())));
                    if(loc.distanceTo(aux.getLocalizacionLugar()) <= DISTANCIA){
                        identificados.add(aux);
                        //Log.d("Identificados", "Identificado: "+ String.valueOf(loc.bearingTo(aux.getLocalizacionLugar())));
                        //aux.setEnVista(true);
                    }
                    //Log.d("Bearing 2", "Identificados: "+String.valueOf(identificados.size()));
                    //Log.d("Bearing 3", "Identificados: "+String.valueOf(identificados.size()));
                }
            }

        }
        //*****************************************************************************************************************************


        //Log.d("Identificados", "Identificados: "+String.valueOf(identificados.size()));
        return identificados;
        //Toast t=Toast.makeText(c,"Identificados: "+identificados.size(),Toast.LENGTH_LONG);
        //t.show();
    }


    public static List<Marcador> identificarMarcadores(Location loc,double direccion){

        List<Marcador> identificados;
        identificados=verificarMarcadores(loc, direccion);
        if (identificados.size()>NUMERO_ELEMENTOS)
            identificados=ordenarMarcadores(identificados, loc);
        Marcador temp;
        for (int location=0;location<identificados.size();location++){
            temp=identificados.get(location);
            temp.setEnVista(true);
            identificados.set(location, temp);
        }


        //Ordenamos para obtener los 3 lugares mas cercanos
        return identificados;
    }

    public static List<Marcador> ordenarMarcadores(List<Marcador> marcadores,Location loc){
        int valores[]=new int[NUMERO_ELEMENTOS];
        double minimo;
        double distancia;
        List<Marcador> marc=new LinkedList<Marcador>();
        for (int ciclos=0;ciclos<NUMERO_ELEMENTOS;ciclos++){
            minimo=Double.MAX_VALUE;
            for (int location=0;location<marcadores.size();location++){
                distancia=loc.distanceTo(marcadores.get(location).getLocalizacionLugar());
                if (distancia<minimo){
                    minimo=distancia;
                    valores[ciclos]=location;
                }
            }
            //Encotre el maximo:
            if (ciclos>=marcadores.size()){
                return marc;
            }
            marc.add(marcadores.get(valores[ciclos]));
            marcadores.remove(valores[ciclos]);
        }
        return marc;
    }


    public static int verificarPosicionEjeX(Location loc,double direccion,Marcador marcador, Activity activity){
        int posicionX;
		/*if(direccion<0)
			direccion=360+direccion;

		direccion=direccion+90;

		if(direccion>360)
			direccion=direccion-360;*/
        //Verificar Bearing

        int ancho = 0, alto = 0;

        DisplayMetrics metrics = new DisplayMetrics();
        try {
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        }catch (Exception e){
            return 10000;
        }
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        float widthDpi = metrics.xdpi;
        float heightDpi = metrics.ydpi;

        float widthInches = widthPixels / widthDpi;
        float heightInches = heightPixels / heightDpi;

        double diagonalInches = Math.sqrt(
                (widthInches * widthInches)
                        + (heightInches * heightInches));

        if (diagonalInches >= 10) {
            //Device is a 10" tablet

            ancho = (int) (200 * Pantalla.DENSIDAD + 5);
            alto = (int) (130 * Pantalla.DENSIDAD + 5);

        }
        else if (diagonalInches >= 7) {
            //Device is a 7" tablet

            ancho = (int) (160 * Pantalla.DENSIDAD + 5);
            alto = (int) (100 * Pantalla.DENSIDAD + 5);

        }else{
            ancho = (int) (120 * Pantalla.DENSIDAD + 5);
            alto = (int) (70 * Pantalla.DENSIDAD + 5);
        }


        double bearing=loc.bearingTo(marcador.getLocalizacionLugar());
        double diferencia=0;
        if(bearing<0){
            bearing=360+bearing;
        }
        //Log.d("D", "Bearing: "+String.valueOf(bearing));
        //Log.d("D", "Direccion: "+String.valueOf(direccion));
        double Dmin,Dmax;

        Dmax=direccion+tolerancia;
        if (Dmax>360)
            Dmax=Dmax-360;

        Dmin=direccion-tolerancia;
        if (Dmin<0)
            Dmin=360+Dmin;

        if ( (Dmax>=0&&Dmax<=2*tolerancia)&& (Dmin<=360&&Dmin>=360-2*tolerancia) ){
            //Casos particulares

				/*
				 * Bearing=10;
				 * Direccion=15 ->-5
				 * Direccion=5; ->5
				 *
				 * Direccion=350; ->20
				 * */
            if ( bearing<2*tolerancia){
                //Caso bearing a la derecha
                if (direccion<2*tolerancia){
                    //Log.d("D", "Derecha 1");
                    diferencia=bearing-direccion;
                }
                else {
                    diferencia=360-direccion+bearing;
                    //Log.d("D", "Derecha 2");
                }
            }
            else if( bearing>360-2*tolerancia ){
                //Caso bearing a la izquierda
                if (direccion<2*tolerancia){
                    diferencia=-(360+direccion)+bearing;
                    //Log.d("I", "Izquierda 1");
                }
                else{
                    diferencia=bearing-direccion;
                    //Log.d("I", "Izquierda 2");
                }
            }
        }
        else{
            //Casos generales
            diferencia=bearing-direccion;
            //Log.d("G", "General");
        }

        //diferencia=bearing-direccion;

        //diferencia=Math.abs(diferencia);
        posicionX=(int)  (Pantalla.ANCHO/2+ (Pantalla.Paso_Pantalla_Eje_X*diferencia)) ;

        //posicionX=posicionX-parametros.width;

        posicionX= posicionX-   ancho/2 ;


        //Log.d("ListaMarcadores","PosX: "+String.valueOf(posicionX));
        return posicionX;
        //Toast t=Toast.makeText(c,"Identificados: "+identificados.size(),Toast.LENGTH_LONG);
        //t.show();
    }

    public static int verificarPosicionEjeY(double direccion,Marcador marcador, Activity activity){
        int posicionY;

        int ancho = 0, alto = 0;

        DisplayMetrics metrics = new DisplayMetrics();
        try {
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        }catch (Exception e){
            return 10000;
        }
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        float widthDpi = metrics.xdpi;
        float heightDpi = metrics.ydpi;

        float widthInches = widthPixels / widthDpi;
        float heightInches = heightPixels / heightDpi;

        double diagonalInches = Math.sqrt(
                (widthInches * widthInches)
                        + (heightInches * heightInches));

        if (diagonalInches >= 10) {
            //Device is a 10" tablet

            ancho = (int) (200 * Pantalla.DENSIDAD + 5);
            alto = (int) (130 * Pantalla.DENSIDAD + 5);

        }
        else if (diagonalInches >= 7) {
            //Device is a 7" tablet

            ancho = (int) (160 * Pantalla.DENSIDAD + 5);
            alto = (int) (100 * Pantalla.DENSIDAD + 5);

        }else{
            ancho = (int) (130 * Pantalla.DENSIDAD + 5);
            alto = (int) (70 * Pantalla.DENSIDAD + 5);
        }

        //direccion=-direccion;
        double diferencia;
        if(direccion<0)
            return (int) (Pantalla.ALTO+50);
        diferencia=90-direccion;

        posicionY=     (int) (Pantalla.ALTO/2- (Pantalla.Paso_Pantalla_Eje_Y*diferencia));
        //posicionX=posicionX-parametros.width;

        posicionY= posicionY-   alto ;

        posicionY = (int) (Pantalla.ALTO/2) - alto;
        //posicionY=
        return posicionY;
        //Toast t=Toast.makeText(c,"Identificados: "+identificados.size(),Toast.LENGTH_LONG);
        //t.show();
    }



    public static double calcularDiferencia(double direccion,double bearing){
        double diferencia;
        if(bearing<0){
            bearing=360+bearing;
        }
        //Log.d("D", "Bearing: "+String.valueOf(bearing));
        //Log.d("D", "Direccion: "+String.valueOf(direccion));
        diferencia=bearing-direccion;

        if (diferencia < 0){
            if (diferencia < -90 && diferencia > -180)
                diferencia=-90;
            else if (diferencia < -180 && diferencia > -270)
                diferencia=90;
        }
        else {
            if (diferencia > 90 && diferencia < 180)
                diferencia = 90;
            else if (diferencia >180 && diferencia < 270)
                diferencia=-90;
        }


        return diferencia;
    }


}
