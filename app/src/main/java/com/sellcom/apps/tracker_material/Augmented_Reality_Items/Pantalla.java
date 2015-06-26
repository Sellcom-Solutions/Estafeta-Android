package com.sellcom.apps.tracker_material.Augmented_Reality_Items;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.test.ActivityTestCase;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

/**
 * Created by juan.guerra on 22/06/2015.
 */
public class Pantalla {
    public static float ANCHO;
    public static float ALTO;
    public static float DENSIDAD;
    public static float ANCHO_PANTALLA;
    public static float ALTO_PANTALLA;
    public static float Paso_Pantalla_Eje_X;
    public static float Paso_Pantalla_Eje_Y;
    public static final float Angulo_Tolerancia=26;
    public static final float Angulo_ToleranciaY=15;
    public static final float ANCHO_MAXIMO=130;

    public static void obtenerMedidas(Activity c){
        Resources r = c.getResources();
        DENSIDAD = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                1, r.getDisplayMetrics());
        Log.d("PANTALLA", String.valueOf(DENSIDAD));
        // Tomamos la pantalla del dispositivo
        Display display = c.getWindowManager().getDefaultDisplay();

        Point size = new Point();
        if(Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.HONEYCOMB_MR2){
            display.getSize(size);
            ANCHO = size.x;
            ALTO = size.y;
        }
        else {
            // Esto es deprecated, pero es necesario para
            // versiones anteriores
            ANCHO = display.getWidth();
            ALTO = display.getHeight();
        }

        DisplayMetrics metrics = new DisplayMetrics();
        c.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        ANCHO_PANTALLA=(float) ((ANCHO/metrics.densityDpi)*2.54);
        ALTO_PANTALLA=(float) ((ALTO/metrics.densityDpi)*2.54);
        Paso_Pantalla_Eje_X=ANCHO/(2*Angulo_Tolerancia);
        //Para el eje Y:
        //Si esta en 90, debera posicionarse en ALTO/2
        //Si esta en 110, sebera posicionarse en ALTO
        Paso_Pantalla_Eje_Y=ALTO/(2*Angulo_ToleranciaY);
        Log.d("Medidas: ",String.valueOf(ANCHO_PANTALLA)+" "+String.valueOf(ALTO_PANTALLA));
        Log.d("Medidas: ",String.valueOf(ANCHO)+" "+String.valueOf(ALTO));
        Log.d("Densidad: ",String.valueOf(DENSIDAD));
        obtenerOrientacionDispositivo(c);


        Log.d("Cantidad de DP en X: ", String.valueOf(convertPixelsToDp(ANCHO,c)));
        Log.d("Cantidad de DP en Y: ",String.valueOf(convertPixelsToDp(ALTO,c)));


    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public static float convertPixelsToSp(float px,Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float sp = px / metrics.scaledDensity;
        return sp;
    }

    public static float convertSpsToPixels(float sp,Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = sp * metrics.scaledDensity;
        Log.d("Pantalla","Tamaño de 1 sp en telefono: "+
                String.valueOf( (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 1,
                        resources.getDisplayMetrics())));
        return px;
    }

    public static void obtenerOrientacionDispositivo(Context c){
        WindowManager windowMgr =(WindowManager)c.getSystemService(Context.WINDOW_SERVICE);
        int rotationIndex = windowMgr.getDefaultDisplay().getRotation();
        Log.d("ROTATION",String.valueOf(getDeviceDefaultOrientation(c)));
    }

    public static int getDeviceDefaultOrientation(Context c) {

        WindowManager windowManager =  (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        Configuration config = c.getResources().getConfiguration();

        int rotation = windowManager.getDefaultDisplay().getRotation();

        if ( ((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) &&
                config.orientation == Configuration.ORIENTATION_LANDSCAPE)
                || ((rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) &&
                config.orientation == Configuration.ORIENTATION_PORTRAIT)) {
            return Configuration.ORIENTATION_LANDSCAPE;
        } else {
            return Configuration.ORIENTATION_PORTRAIT;
        }
    }

}
