package com.estafeta.estafetamovilv1.Augmented_Reality_Items;


import android.hardware.Camera;
import android.view.SurfaceHolder;

/**
 * Created by juan.guerra on 22/06/2015.
 * @deprecated
 */
public class CamaraCallback implements SurfaceHolder.Callback{


    private static Camera camera = null;
    public CamaraCallback() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        if (camera != null) {
            try {
                camera.stopPreview();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                camera.release();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            camera = null;
        }
        if (camera == null) {
            camera = Camera.open();
        }
        try {
            camera.setPreviewDisplay(holder);
        }
        catch (Throwable t) {

        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
        Camera.Parameters parameters=camera.getParameters();
        Camera.Size size=getBestPreviewSize(width, height, parameters);
        if (size!=null) {
            parameters.setPreviewSize(size.width, size.height);
            camera.setParameters(parameters);
            camera.startPreview();
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }

    }



    private Camera.Size getBestPreviewSize(int width, int height,Camera.Parameters parameters) {
        Camera.Size result=null;
        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width<=width && size.height<=height) {
                if (result==null) {
                    result=size;
                }
                else {
                    int resultArea=result.width*result.height;
                    int newArea=size.width*size.height;
                    if (newArea>resultArea) {
                        result=size;
                    }
                }
            }
        }

        return(result);
    }

}
