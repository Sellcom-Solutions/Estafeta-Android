package com.sellcom.apps.tracker_material.Augmented_Reality_Items;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.graphics.Matrix;
import android.location.Location;
import android.util.TypedValue;
import android.view.View.OnClickListener;

import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.Utilities;

import database.model.Offices;


/**
 * Created by juan.guerra on 22/06/2015.
 */
public class ContenedorCajas {
    private RelativeLayout contenedor;
    private List<Marcador> lista;
    private int altoC;
    public static int tamLetra;
    private int color_fondo_AR;
    private int color_texto_AR;


    public ContenedorCajas(RelativeLayout contenedor) {
        this.contenedor=contenedor;
        lista=new LinkedList<Marcador>();
        float dimm=contenedor.getContext().getResources().getDimension(R.dimen.sizeAugmentedRealityText);
        tamLetra = (int)Pantalla.convertPixelsToSp(dimm,contenedor.getContext());
        Log.d("TAM TEX: ",String.valueOf(tamLetra));
        color_fondo_AR=Color.BLACK;
        color_texto_AR=Color.WHITE;
    }

    public void agregar(CajaDeTexto caja,
                        android.widget.RelativeLayout.LayoutParams parametros,Marcador marcador) {
        // TODO Auto-generated method stub
        contenedor.addView(caja, parametros);
        lista.add(marcador);
    }

    public void agregar(ImageView elemento,
                        android.widget.RelativeLayout.LayoutParams parametros,Marcador marcador) {
        // TODO Auto-generated method stub
        contenedor.addView(elemento, parametros);
        lista.add(marcador);
    }

    public void agregar(CajaDeTexto caja,
                        android.widget.RelativeLayout.LayoutParams parametros) {
        // TODO Auto-generated method stub
        contenedor.addView(caja, parametros);
    }

    public int numeroElementos(){
        return lista.size();
    }

    public void eliminar(CajaDeTexto caja){
        contenedor.removeView(caja);
        lista.remove(caja);
    }

    public void eliminar(){
        contenedor.removeAllViews();
        lista.clear();
    }

    public void agregarLugaraPantalla(OnClickListener listener,
                                      CajaDeTexto caja,int x,int y,Marcador marcador,Location ubicacion){
        /*Log.d("AGREGAR_RECUPERADO: ",String.valueOf(marcador.getId()));
        Log.d("AGREGAR_RECUPERADO: ",String.valueOf(marcador.getNombre()));
        Log.d("AGREGAR_RECUPERADO: ",String.valueOf(marcador.getTipo_elemento()));
        Log.d("AGREGAR_RECUPERADO: ",String.valueOf(marcador.getLocalizacionLugar().getLatitude()));
        Log.d("AGREGAR_RECUPERADO: ",String.valueOf(marcador.getLocalizacionLugar().getLongitude()));*/

        caja.setId(marcador.getId());
        caja.setTextSize(tamLetra);
        caja.setTextColor(color_texto_AR);
        float distance=ubicacion.distanceTo(marcador.getLocalizacionLugar());
        String units;
        if (distance>=1000){
            distance=distance/1000;
            units=" Km";
        }
        else{
            units=" m";
        }
        NumberFormat nf=NumberFormat.getInstance();

        nf.setMaximumFractionDigits(1);
        nf.setRoundingMode( RoundingMode.DOWN);
        units=nf.format(distance)+units;

        caja.setText(marcador.getNombre()+"\n"+units);
        caja.setX(x);
        caja.setY(y);
        //Fin codigo para posicionar elemento
        caja.setAlpha((float) 0.5);
        //Buscamos alto y ancho optimo para mostrar el nombre en pantalla
        altoC=caja.obtenerAlto()*(marcador.getNumLineas());
        marcador.setValoresX(x, (int) (CajaDeTexto.getAnchoGenerico(marcador.getTextoMasLargo())*Pantalla.DENSIDAD+5));
        marcador.setValoresY(y, altoC);
        RelativeLayout.LayoutParams parametros=new RelativeLayout.LayoutParams(
                (int) (CajaDeTexto.getAnchoGenerico(marcador.getTextoMasLargo())*Pantalla.DENSIDAD+5),
                altoC);
        caja.setBackgroundColor(color_fondo_AR);
        caja.setOnClickListener(listener);
        caja.setHeight(parametros.height);
        caja.setWidth(parametros.width);
        Marcador temp;
        Marcador temp2;
        agregar(caja, parametros, marcador);
        ImageView imagen=new ImageView(contenedor.getContext());
        imagen.setId(caja.getId());
        imagen.setOnClickListener(listener);
        Bitmap bMap=null;
        bMap=Utilities.getOfficesIcon(Utilities.convertOfficesIntToType(marcador.getTipo_elemento()), contenedor.getContext());
        bMap=bMap.createScaledBitmap(bMap, altoC, altoC, false);
        imagen.setImageBitmap(bMap);
        imagen.setBackgroundColor(color_fondo_AR);
        imagen.getBackground().setAlpha(127);
        Marcador ma=new Marcador(marcador.getId(), marcador.getTipo_elemento());
        imagen.setX(marcador.getXmin()-altoC);
        imagen.setY(marcador.getYmin());
        parametros=new RelativeLayout.LayoutParams(altoC
                ,altoC);
        ma.setValoresX(marcador.getXmin()-altoC, altoC);
        ma.setValoresY(marcador.getYmin(), altoC);
        agregar(imagen,parametros,ma);
        //Log.d("ContenedorCajas",marcador.getNombre());
        for (int location=0;location<lista.size()-2;location++){
            temp=lista.get(location);
            temp2=lista.get(location+1);
            if (temp.isTipo()){
                while(temp.verificarColisionMarcador(marcador)||marcador.verificarColisionMarcador(temp)
                        ||temp2.verificarColisionMarcador(marcador)||marcador.verificarColisionMarcador(temp2)
                        ||temp.verificarColisionMarcador(ma)||ma.verificarColisionMarcador(temp)){
                    //Log.d("Colision", "Colision");
                    temp=lista.get(location);
                    marcador=lista.get(lista.size()-2);
                    recorrerMarcadores(marcador,temp,ubicacion,lista.size()-2);
                }
            }
        }

        for (int location=0;location<lista.size()-2;location++){
            temp=lista.get(location);
            temp2=lista.get(location+1);
            if (temp.isTipo()){
                if(temp.verificarColisionMarcador(marcador)||marcador.verificarColisionMarcador(temp)
                        ||temp2.verificarColisionMarcador(marcador)||marcador.verificarColisionMarcador(temp2)
                        ||temp.verificarColisionMarcador(ma)||ma.verificarColisionMarcador(temp)){
                    //Regresamos a los valores originales
                    caja=(CajaDeTexto)this.contenedor.getChildAt(lista.size()-2);
                    caja.setX(x);
                    contenedor.removeViewAt(lista.size()-2);
                    parametros=new RelativeLayout.LayoutParams(
                            (int) (CajaDeTexto.getAnchoGenerico(marcador.getTextoMasLargo())*Pantalla.DENSIDAD+5),
                            caja.obtenerAlto()*(marcador.getNumLineas()));
                    contenedor.addView(caja, lista.size()-2,parametros);
                    marcador.setValoresX(x, (int) (CajaDeTexto.getAnchoGenerico(marcador.getTextoMasLargo())*Pantalla.DENSIDAD+5));
                    lista.set(lista.size()-2,marcador);

                    imagen=(ImageView)this.contenedor.getChildAt(lista.size()-1);
                    ma=new Marcador(marcador.getId(), marcador.getTipo_elemento());
                    imagen.setX(marcador.getXmin()-altoC);
                    imagen.setY(marcador.getYmin());
                    contenedor.removeViewAt(lista.size()-1);
                    parametros=new RelativeLayout.LayoutParams(altoC
                            ,altoC);
                    ma.setValoresX(marcador.getXmin()-altoC, altoC);
                    ma.setValoresY(marcador.getYmin(), altoC);
                    contenedor.addView(imagen, lista.size()-1,parametros);
                    lista.set(lista.size()-1,ma);
                    //recorrerMarcadores(marcador,temp,ubicacion,lista.size()-1);
                    while(temp.verificarColisionMarcador(marcador)||marcador.verificarColisionMarcador(temp)
                            ||temp2.verificarColisionMarcador(marcador)||marcador.verificarColisionMarcador(temp2)
                            ||temp.verificarColisionMarcador(ma)||ma.verificarColisionMarcador(temp)){
                        temp=lista.get(location);
                        marcador=lista.get(lista.size()-2);
                        //Log.d("Colision", "Colision"+String.valueOf(iii));
                        recorrerMarcadoresY(lista.size()-2,(int)((CajaDeTexto)this.contenedor.getChildAt(location)).getY());
                    }
                }
            }
        }
    }

    public void recorrerMarcadores(Marcador actual,Marcador anterior,Location ubicacion,int location){
        //Calculamos bearing de cada marcador
        double bearing_actual,bearing_anterior;
        bearing_actual=ubicacion.bearingTo(actual.getLocalizacionLugar());
        bearing_anterior=ubicacion.bearingTo(anterior.getLocalizacionLugar());
        if (bearing_actual<bearing_anterior||bearing_actual-bearing_anterior>Pantalla.Angulo_Tolerancia){
            //El marcador que se esta agregando esta a la izquierda
            recorrerIzquierda(location);
        }
        else{
            //El marcador que se esta agregando esta a la derecha
            recorrerDerecha(location);
        }
    }

    public void recorrerDerecha(int location){
        //Log.d("ContenedorCajas", String.valueOf(location));
        CajaDeTexto caja=(CajaDeTexto)this.contenedor.getChildAt(location);
        caja.setX(caja.getX()+30);

        RelativeLayout.LayoutParams parametros=new RelativeLayout.LayoutParams(
                (int) (CajaDeTexto.getAnchoGenerico(lista.get(location).getTextoMasLargo())*Pantalla.DENSIDAD+5),
                caja.obtenerAlto() * (lista.get(location).getNumLineas()));
        contenedor.removeViewAt(location);
        contenedor.addView(caja, location, parametros);

        Marcador m=lista.get(location);
        m.setValoresX((int) this.contenedor.getChildAt(location).getX(), (int)
                (CajaDeTexto.getAnchoGenerico(m.getTextoMasLargo())*Pantalla.DENSIDAD+5));
        //lista.add(1, m);
        lista.set(location,m);

        ImageView imagen=(ImageView)this.contenedor.getChildAt(location+1);
        imagen.setX(imagen.getX()+30);
        parametros=new RelativeLayout.LayoutParams(altoC
                , altoC);
        contenedor.removeViewAt(lista.size()-1);
        contenedor.addView(imagen, location+1,parametros);

        m=lista.get(location+1);
        m.setValoresX((int) this.contenedor.getChildAt(location+1).getX(), altoC);
        lista.set(location+1,m);
        // marcador.setValoresX(x, (int) (CajaDeTexto.getAnchoGenerico(marcador.getTextoMasLargo())*Pantalla.DENSIDAD+5));
        // marcador.setValoresY(y, caja.obtenerAlto()*(marcador.getNumLineas()));

    }

    public void recorrerIzquierda(int location){
        //Log.d("ContenedorCajas", String.valueOf(location));
        CajaDeTexto caja=(CajaDeTexto)this.contenedor.getChildAt(location);
        caja.setX(caja.getX()-30);

        RelativeLayout.LayoutParams parametros=new RelativeLayout.LayoutParams(
                (int) (CajaDeTexto.getAnchoGenerico(lista.get(location).getTextoMasLargo())*Pantalla.DENSIDAD+5),
                caja.obtenerAlto() * (lista.get(location).getNumLineas()));
        contenedor.removeViewAt(location);
        contenedor.addView(caja, location, parametros);

        Marcador m=lista.get(location);
        m.setValoresX((int) this.contenedor.getChildAt(location).getX(), (int)
                (CajaDeTexto.getAnchoGenerico(m.getTextoMasLargo())*Pantalla.DENSIDAD+5));
        //lista.add(1, m);
        lista.set(location,m);

        ImageView imagen = (ImageView)this.contenedor.getChildAt(location+1);
        imagen.setX(imagen.getX()-30);
        parametros=new RelativeLayout.LayoutParams(altoC
                , altoC);
        contenedor.removeViewAt(lista.size()-1);
        contenedor.addView(imagen, location+1,parametros);

        m=lista.get(location+1);
        m.setValoresX((int) this.contenedor.getChildAt(location+1).getX(), altoC);
        lista.set(location+1,m);
    }


    public void recorrerMarcadoresY(int location,int posY){
        //Calculamos bearing de cada marcador
        CajaDeTexto caja=(CajaDeTexto)this.contenedor.getChildAt(location);
        ImageView imagen=(ImageView)this.contenedor.getChildAt(location+1);
        //posY=(int)caja.getY();
        if (posY>=2*Pantalla.ALTO/3){//Movemos arriba
            caja.setY(caja.getY()-30);
            imagen.setY(imagen.getY()-30);
            //Log.d("ContenedorCajas", "Arriba");
        }
        else{ //Movemos abajo
            caja.setY(caja.getY()+30);
            imagen.setY(imagen.getY()+30);
            //Log.d("ContenedorCajas", "Abajo");
        }
        RelativeLayout.LayoutParams parametros=new RelativeLayout.LayoutParams(
                (int) (CajaDeTexto.getAnchoGenerico(lista.get(location).getTextoMasLargo())*Pantalla.DENSIDAD+5),
                caja.obtenerAlto()*(lista.get(location).getNumLineas()));
        contenedor.removeViewAt(location);
        contenedor.addView(caja, location,parametros);
        Marcador m=lista.get(location);
        m.setValoresY((int) this.contenedor.getChildAt(location).getY(), caja.obtenerAlto()*(m.getNumLineas()));
        //lista.add(1, m);
        lista.set(location,m);
        parametros=new RelativeLayout.LayoutParams(altoC
                ,altoC);
        contenedor.removeViewAt(lista.size()-1);
        contenedor.addView(imagen, location+1,parametros);
        m=lista.get(location+1);
        m.setValoresY((int) this.contenedor.getChildAt(location+1).getY(), altoC);
        lista.set(location+1,m);
    }




}
