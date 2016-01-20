package com.estafeta.estafetamovilv1.Augmented_Reality_Items;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by juan.guerra on 22/06/2015.
 * @deprecated
 */
public class Sensores implements SensorEventListener{

    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private Sensor sensorMagneticField;
    private Sensor sensorOrientation;
    private double azimuthO;
    private float[] valuesAccelerometer;
    private float[] valuesMagneticField;
    private int contar;
    private float[] matrixR;
    private float[] matrixI;
    private float[] matrixValues;
    private double azimuth;
    private double azimuth_Anterior;
    private double azimuth_AnteriorO;

    private  float []newValues;
    private float []RMXB;

    public static int AuxiliarActualizacion=2;
    public static long TiempoAuxiliarActualizacion = 1000;
    private SensorsListener listener;

    public double getAzimuth() {
        return azimuth;
    }
    public double getRoll() {
        return roll;
    }

    private double pitch;
    private double roll;
    private Context a;

    private final int tolerancia_Azimuth=3;
    private double roll_anterior;
    private final int tolerancia_roll=3;
    private static Sensores sensor;

    private long lastTime;

    private float ALPHA = 0.1f;

    public synchronized static Sensores getInstance(Context a,SensorsListener listener){
        if (sensor==null){
            sensor=new Sensores();
            sensor.initSensor(a,listener);
        }
        else
            sensor.setListener(listener);
        return sensor;
    }


    public void initSensor(Context a,SensorsListener listener){
        contar=0;
        this.a=a;
        sensorManager = (SensorManager)a.getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorOrientation = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        RMXB = new float[9];
        newValues = new float[3];
        valuesAccelerometer = new float[3];
        valuesMagneticField = new float[3];
        matrixR = new float[9];
        matrixI = new float[9];
        matrixValues = new float[3];
        azimuth_Anterior=-1;
        azimuth_AnteriorO=-1;
        roll_anterior=1;

        sensorManager.registerListener(this,
                sensorAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);

        sensorManager.registerListener(this,
                sensorMagneticField,
                SensorManager.SENSOR_DELAY_NORMAL);


        /*sensorManager.registerListener(this,
                sensorOrientation,
                SensorManager.SENSOR_DELAY_NORMAL);*/


        this.setListener(listener);
        lastTime = System.nanoTime();

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (listener== null){
            this.finalize();
            return;
        }


        // TODO Auto-generated method stub
        switch(event.sensor.getType()){

            case Sensor.TYPE_ACCELEROMETER:
                for(int i =0; i < 3; i++){
                    valuesAccelerometer = lowPass(event.values.clone(), valuesAccelerometer);

                    //valuesAccelerometer[i] = event.values[i];
                }
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                for(int i =0; i < 3; i++){
                    valuesMagneticField = lowPass(event.values.clone(), valuesMagneticField);
                    //valuesMagneticField[i] = event.values[i];

                }

                boolean success = SensorManager.getRotationMatrix(
                        matrixR,
                        matrixI,
                        valuesAccelerometer,
                        valuesMagneticField);
                if(success){
                    SensorManager.getOrientation(matrixR, matrixValues);

                    SensorManager.remapCoordinateSystem(matrixR,SensorManager.AXIS_X,SensorManager.AXIS_Z,RMXB);

                    SensorManager.getOrientation(RMXB , newValues);


                    azimuth =(int) Math.toDegrees(newValues[0]);
                    if(azimuth<0)
                        azimuth=360+azimuth;
                    //azimuth=azimuth+90;
                    if(azimuth>360)
                        azimuth=azimuth-360;
                    if(azimuth_Anterior!=-1){
                        if (azimuth>azimuth_Anterior){
                            if(azimuth%tolerancia_Azimuth!=0){
                                if(azimuth-azimuth_Anterior<tolerancia_Azimuth)
                                    azimuth=azimuth_Anterior;
                                else
                                    azimuth=azimuth-azimuth%tolerancia_Azimuth;
                            }
                        }
                        else{
                            if(azimuth%tolerancia_Azimuth!=0){
                                if (azimuth_Anterior-azimuth<tolerancia_Azimuth)
                                    azimuth=azimuth_Anterior;
                                else
                                    azimuth=azimuth-azimuth%tolerancia_Azimuth;
                            }
                        }
                    }
                    else
                        azimuth=azimuth-azimuth%tolerancia_Azimuth;

                    pitch = (int)Math.toDegrees(newValues[2]);
                    roll = (int)Math.toDegrees(newValues[1]);
                    roll=-roll+85;
                    if(roll_anterior!=-1){
                        if (roll>roll_anterior){
                            if(roll%tolerancia_roll!=0){
                                if(roll-roll_anterior<tolerancia_roll)
                                    roll=roll_anterior;
                                else
                                    roll=roll-roll%tolerancia_roll;
                            }
                        }
                        else{
                            if(roll%tolerancia_roll!=0){
                                if(roll_anterior-roll<tolerancia_roll)
                                    roll=roll_anterior;
                                else
                                    roll=roll-roll%tolerancia_roll;
                            }
                        }
                    }
                    else
                        roll=roll-roll%tolerancia_roll;
                    roll_anterior=roll;

                    azimuth_Anterior=azimuth;
                    //Log.d("Main",String.valueOf(azimuth));
                    contar++;
                    contar=contar%AuxiliarActualizacion;
                    long newTime;
                    newTime = System.nanoTime();
                    //Log.d("Time: ",String.valueOf((newTime - lastTime)/1000000));


                    //if (contar==0)
                    if ( ((newTime - this.lastTime)/1000000) >= TiempoAuxiliarActualizacion ){
                        //Log.d("Final Time: ", String.valueOf((newTime - this.lastTime) / 1000000));
                        this.lastTime = newTime;
                        listener.identificar();
                    }

                }
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }


    @Override
    public void finalize(){
        if (sensorManager!=null){
            sensorManager.unregisterListener(this, sensorAccelerometer);
            sensorManager.unregisterListener(this, sensorMagneticField);
            //sensorManager.unregisterListener(this,sensorOrientation);
        }
        sensorManager=null;
        Log.d("Sensores", "Finalizado");
        try {
            sensor=null;
            super.finalize();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setListener(SensorsListener listener){
        this.listener=listener;
    }

    protected float[] lowPass( float[] input, float[] output ) {
        if ( output == null ) return input;
        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

}
