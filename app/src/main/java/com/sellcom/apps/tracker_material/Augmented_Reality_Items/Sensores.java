package com.sellcom.apps.tracker_material.Augmented_Reality_Items;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.Log;

/**
 * Created by juan.guerra on 22/06/2015.
 */
public class Sensores implements SensorEventListener{

    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private Sensor sensorMagneticField;
    private float[] valuesAccelerometer;
    private float[] valuesMagneticField;
    private int contar;
    private float[] matrixR;
    private float[] matrixI;
    private float[] matrixValues;
    private double azimuth;
    private double azimuth_Anterior;
    public static int AuxiliarActualizacion=1;
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

    private final int tolerancia_Azimuth=4;
    private double roll_anterior;
    private final int tolerancia_roll=4;
    private static Sensores sensor;

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
        valuesAccelerometer = new float[3];
        valuesMagneticField = new float[3];
        matrixR = new float[9];
        matrixI = new float[9];
        matrixValues = new float[3];
        azimuth_Anterior=-1;
        roll_anterior=1;

        sensorManager.registerListener(this,
                sensorAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);

        sensorManager.registerListener(this,
                sensorMagneticField,
                SensorManager.SENSOR_DELAY_NORMAL);
        this.setListener(listener);
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
                    valuesAccelerometer[i] = event.values[i];
                }
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                for(int i =0; i < 3; i++){
                    valuesMagneticField[i] = event.values[i];
                }

                boolean success = SensorManager.getRotationMatrix(
                        matrixR,
                        matrixI,
                        valuesAccelerometer,
                        valuesMagneticField);
                if(success){
                    SensorManager.getOrientation(matrixR, matrixValues);
                    azimuth =(int) Math.toDegrees(matrixValues[0]);
                    if(azimuth<0)
                        azimuth=360+azimuth;
                    azimuth=azimuth+90;
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
                    pitch = (int)Math.toDegrees(matrixValues[1]);
                    roll = (int)Math.toDegrees(matrixValues[2]);
                    roll=-roll;
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
                    //if (contar==1)
                        listener.identificar();
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
            sensorManager.unregisterListener(this,sensorMagneticField);
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

}
