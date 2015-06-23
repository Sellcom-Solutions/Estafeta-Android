package com.sellcom.apps.tracker_material.Augmented_Reality_Items;

import android.location.Location;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by juan.guerra on 22/06/2015.
 */
public class Marcador extends ObjetoPintable{

    private int id;
    private String nombre;
    private String textoOriginal;
    private String textoMasLargo;
    private Location localizacionLugar;
    private int tipo_elemento;
    private int numLineas;
    private boolean enVista;
    public static int tamMax=20;
    private boolean tipo;




    public Marcador(int id,String nombre,double Latitud,double Longitud,int tipo_elemento) {
        super();
        this.localizacionLugar=new Location("");
        this.localizacionLugar.setLatitude(Latitud);
        this.localizacionLugar.setLongitude(Longitud);
        this.localizacionLugar.setAltitude(0);
        this.id=id;
        this.nombre=nombre;
        this.textoOriginal=nombre;
        this.tipo_elemento=tipo_elemento;
        this.enVista=false;
        this.tipo=true;//CajaDetexto
        separarTexto();

        // TODO Auto-generated constructor stub
    }
    public Marcador(int id,int tipo_elemento) {
        super();
        this.localizacionLugar=new Location("");
        this.tipo=false;//ImageView
        this.tipo_elemento=tipo_elemento;
    }

    public Marcador() {
        super();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String gettextoOriginal() {
        return textoOriginal;
    }

    public void settextoOriginal(String textoOriginal) {
        this.textoOriginal = textoOriginal;
    }

    public Location getLocalizacionLugar() {
        return localizacionLugar;
    }

    public int getTipo_elemento(){
        return this.tipo_elemento;
    }

    public void setLocalizacionLugar(Location localizacionLugar) {
        this.localizacionLugar = localizacionLugar;
    }

    public boolean isEnVista() {
        return enVista;
    }

    public void setEnVista(boolean enVista) {
        this.enVista = enVista;
    }

    private void separarTexto(){

        String texto=nombre;

        List<String> ll=new LinkedList<String>();
        int fin=texto.indexOf(" ");
        while(fin!=-1){
            ll.add(texto.substring(0, fin));
            texto=texto.substring(fin+1,texto.length());
            fin=texto.indexOf(" ");
        }
        ll.add(texto);
        List<String> ll2=new LinkedList<String>();
        ll2.add("");
        String aux;


        int tamm;
        for (int i=0;i<ll.size();i++){
            aux=ll.get(i);
            tamm=ll2.get(ll2.size()-1).concat(aux).length();
            //Log.d("THIS",ll2.get(ll2.size()-1).concat(aux)+": "+ String.valueOf(tamm));

            if (ll2.get(ll2.size()-1).concat(aux).length() <=tamMax){
                ll2.set(ll2.size()-1,ll2.get(ll2.size()-1).concat(aux)+" ");
            }
            else
            {
                ll2.add(aux+" ");
                //Log.d("THIS2","Entro al caso contrario");

            }
        }

        int tamMax=Integer.MIN_VALUE;
        for (int i=0;i<ll2.size();i++){

            ll2.set(i,ll2.get(i).substring(0,ll2.get(i).length()-1));
        }


        for (int i=0;i<ll2.size();i++){
            if (ll2.get(i).length()>tamMax){
                tamMax=ll2.get(i).length();
                textoMasLargo=ll2.get(i);
            }
        }

        //Log.d("THIS",textoMasLargo);
        String separado="";
        numLineas=ll2.size()+1;

        if (ll2.size()>1)
        {
            for(int eee=0;eee<ll2.size()-1;eee++){
                aux=ll2.get(eee).concat("\n");
                separado=separado+aux;
            }
            separado=separado+ll2.get(ll2.size()-1);
        }
        else
            separado=ll2.get(0);
        this.nombre=separado;
    }

    public String getTextoOriginal() {
        return textoOriginal;
    }

    public void setTextoOriginal(String textoOriginal) {
        this.textoOriginal = textoOriginal;
    }

    public String getTextoMasLargo() {
        return textoMasLargo;
    }

    public void setTextoMasLargo(String textoMasLargo) {
        this.textoMasLargo = textoMasLargo;
    }

    public int getNumLineas() {
        return numLineas;
    }

    public void setNumLineas(int numLineas) {
        this.numLineas = numLineas;
    }


    public boolean isTipo(){
        return this.tipo;
    }




}
