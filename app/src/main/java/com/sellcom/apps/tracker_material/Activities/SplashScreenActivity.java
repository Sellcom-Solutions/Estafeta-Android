package com.sellcom.apps.tracker_material.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import database.DataBaseManager;
import database.model.Offices;
import database.model.States;

import com.sellcom.apps.tracker_material.Async_Request.METHOD;
import com.sellcom.apps.tracker_material.Async_Request.RequestManager;
import com.sellcom.apps.tracker_material.Async_Request.UIResponseListenerInterface;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.DialogManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SplashScreenActivity extends ActionBarActivity implements UIResponseListenerInterface {

    String TAG= "SPLASH_SCREEN";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context = this;

        DialogManager.sharedInstance().setActivity(this);
        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.SPLASH, getString(R.string.cargando),0);

        Log.d(TAG,"crea dbHelper");
        DataBaseManager.sharedInstance().insert(context);

        //Actualizar oficinas
        Map<String, String> requestData =  new HashMap<>();
        String fecha = Offices.getVersion(context);
        requestData.put("ultimaAct",fecha);

        Log.d(TAG, "Llama al servicio");
        RequestManager.sharedInstance().setListener(this);
        RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_OFFICES, requestData);

        //new CreateDB().execute();
    }

    protected void onResume(){
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void prepareRequest(METHOD method, Map<String, String> params, boolean includeCredentials) {

    }

    @Override
    public void decodeResponse(String stringResponse) {


        if(stringResponse != null && !stringResponse.equals("0")){
            //Log.d(TAG,"response: "+stringResponse);
            ArrayList<Map<String,String>> auxResponse = new ArrayList<>();
            try {
                auxResponse = RequestManager.sharedInstance().getResponseArray();
                Log.d(TAG,"auxResponse size"+auxResponse.size());
                Map<String, String> item = new HashMap<>();
                item = auxResponse.get(0);
                if (item.get("method").equals(METHOD.REQUEST_OFFICES.toString())){
                    Log.d(TAG,"metodo oficinas");
                    Offices.updateOffices(context,auxResponse);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Log.d(TAG,"response null");
        }

        Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
        startActivity(intent);

       // DialogManager.sharedInstance().dismissDialog();
    }

    /*class CreateDB extends AsyncTask<String, Void, String> implements UIResponseListenerInterface {

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected String doInBackground(String... params) {
            //Crear DB
           // DataBaseHelper dbHelper = new DataBaseHelper(context);
            Log.d("SplashScreen","crea dbHelper");
            DataBaseManager.sharedInstance().insert(context);

            //Actualizar oficinas
            Map<String, String> requestData =  new HashMap<>();
            String fecha = Offices.getVersion(context);
            requestData.put("ultimaAct",fecha);

            Log.d("SplashScreen","Antes de Request");
            RequestManager.sharedInstance().setListener(this);
            RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_OFFICES, requestData);


           // dbHelper.close();
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
           *//* Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
            startActivity(intent);
            DialogManager.sharedInstance().dismissDialog();*//*

        }

        @Override
        public void prepareRequest(METHOD method, Map<String, String> params, boolean includeCredentials) {

        }

        @Override
        public void decodeResponse(String stringResponse) {
            if(stringResponse != null){
                Log.v("SplashScreen resp", stringResponse);

            }else{
                Log.v("SplashScreenActivity", "El servidor devolvio null");
            }
        }
    }*/

}
