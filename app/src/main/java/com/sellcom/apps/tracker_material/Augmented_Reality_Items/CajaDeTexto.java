package com.sellcom.apps.tracker_material.Augmented_Reality_Items;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
/**
 * Created by juan.guerra on 22/06/2015.
 */
public class CajaDeTexto extends TextView{
    private int ancho;
    private int alto;

    public CajaDeTexto(Context context) {
        super(context);
        this.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
    }

    public int obtenerAncho(){
        Paint paint = new Paint();
        paint.setTextSize(getTextSize());
        setAncho((int) (paint.measureText(getTexto()) + 4 * 2));
        return getAncho();
    }

    public static int getAnchoGenerico(String texto,float tamLetra,int o){
        Paint paint = new Paint();
        paint.setTextSize(tamLetra);
        return (int) (paint.measureText(texto+"\n") + 4 * 2);

    }

    public static int getAnchoGenerico(String texto){
        Paint paint = new Paint();
        paint.setTextSize(ContenedorCajas.tamLetra);
        return (int) (paint.measureText(texto+"\n") + 4 * 2);

    }

    public static int getAnchoGenerico(String texto,float escala){
        Paint paint = new Paint();
        paint.setTextSize(ContenedorCajas.tamLetra);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.ITALIC));
        return  (int) (paint.measureText(texto, 0, texto.length())*escala);
    }

    public int obtenerAlto(){
        Paint paint = new Paint();
        paint.setTextSize(getTextSize());
        setAlto((int) (-paint.ascent() + paint.descent() + 2 * 2));
        return getAlto();
    }

    public float obtenerAltoParaTexto(int referencia){
        Paint paint = new Paint();
        for (int tam=ContenedorCajas.tamLetra;tam<=60;tam++){
            paint.setTextSize(tam);
            setAlto((int) (-paint.ascent() + paint.descent() + 2 * 2));
            setAlto(getAlto()*2);
            if (Math.abs(referencia-getAlto())<=2){
                setAlto(getAlto()/2);
                return getTextSize();
            }
        }
        setAlto(getAlto()/2);
        return getTextSize();
    }


    public static int getAltoGenerico(){
        Paint paint = new Paint();
        paint.setTextSize(ContenedorCajas.tamLetra);
        return ((int) (-paint.ascent() + paint.descent() + 2 * 2));
    }


    public String getTexto() {
        return super.getText().toString();
    }
    public void setTexto(String texto) {
        super.setText(texto);
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }


}
