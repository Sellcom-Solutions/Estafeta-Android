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

import java.util.HashMap;
import java.util.Map;

import database.DataBaseAdapter;
import database.DataBaseHelper;

public class SplashScreenActivity extends ActionBarActivity {

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context = this;
         Log.d("SplashScreen", "Llama al hilo");
        DialogManager.sharedInstance().setActivity(this);
        DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.SPLASH, getString(R.string.cargando));

        new CreateDB().execute();
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

    class CreateDB extends AsyncTask<String, Void, String> implements UIResponseListenerInterface {

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

            RequestManager.sharedInstance().setListener(this);
            RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_OFFICES, requestData);


           // dbHelper.close();
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
            startActivity(intent);
            DialogManager.sharedInstance().dismissDialog();

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
    }

    class RefreshDB extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected String doInBackground(String... params) {
            //Crear DB
            DataBaseHelper dbHelper = new DataBaseHelper(context);

            dbHelper.close();

            return null;
        }
        @Override
        protected void onPostExecute(String result) {

            Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
            startActivity(intent);
            DialogManager.sharedInstance().dismissDialog();
        }

    }
}
