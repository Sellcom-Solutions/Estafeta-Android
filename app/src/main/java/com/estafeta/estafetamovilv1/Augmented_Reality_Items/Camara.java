package com.estafeta.estafetamovilv1.Augmented_Reality_Items;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by juan.guerra on 22/06/2015.
 * @deprecated
 */
public class Camara extends SurfaceView {

    private static SurfaceHolder holder = null;
    private static Camera camera = null;
    private CamaraCallback surfaceCallback;

    public Camara(Context context) {
        super(context);
        try {
            holder = getHolder();
            surfaceCallback=new CamaraCallback();
            holder.addCallback(surfaceCallback);
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }




        // TODO Auto-generated constructor stub
    }

    public void destruir(){
        surfaceCallback.surfaceDestroyed(null);
    }



}
