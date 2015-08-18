package com.sellcom.apps.tracker_material.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.sellcom.apps.tracker_material.Async_Request.METHOD;
import com.sellcom.apps.tracker_material.Async_Request.RequestManager;
import com.sellcom.apps.tracker_material.Async_Request.UIResponseListenerInterface;
import com.sellcom.apps.tracker_material.R;
import com.sellcom.apps.tracker_material.Utils.DatesHelper;
import com.sellcom.apps.tracker_material.Utils.DialogManager;
import com.sellcom.apps.tracker_material.Utils.Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import database.DataBaseHelper;
import database.DataBaseManager;
import database.model.Codes;
import database.model.Offices;

import android.widget.TextView;

public class SplashScreenActivity extends ActionBarActivity implements UIResponseListenerInterface {

    String TAG= "SPLASH_SCREEN";
    Context context;
    String last_date,method="";
    TextView footer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        footer = (TextView)findViewById(R.id.footer);


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String currentYear = formatter.format(new Date());
        footer.setText("©2012-"+currentYear+" "+getString(R.string.footer));

        /*
        DataBaseHelper db=new DataBaseHelper(this);
        db.onUpgrade(db.getWritableDatabase(), 1, 2);
        */
            DialogManager.sharedInstance().setActivity(this);
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.SPLASH, "Configurando aplicación...", 0);
        if(Utilities.flag) {
            Utilities.flag = false;

            context = this;

            Log.d(TAG, "crea dbHelper");
            DataBaseManager.sharedInstance().insert(context);

            //Actualizar oficinas
            Map<String, String> requestData = new HashMap<>();
            //String fecha = Offices.getVersion(context);

            String fecha = "2012-09-16 15:01:22.000";

            SharedPreferences sharedPref        = getPreferences(Context.MODE_PRIVATE);
            last_date = sharedPref.getString("last_date", fecha);

            requestData.put("ultimaAct", last_date);

            SharedPreferences.Editor editor     = sharedPref.edit();
            editor.putString("last_date", DatesHelper.getStringDate(new Date()));
            editor.commit();

            Log.d(TAG, "Llama al servicio");
            method = "oficinas";

            DialogManager.sharedInstance().dismissDialog();
            DialogManager.sharedInstance().showDialog(DialogManager.TYPE_DIALOG.SPLASH, "Actualizando aplicación...", 0);
            RequestManager.sharedInstance().setListener(this);
            RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_OFFICES, requestData);
        }
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
                if(auxResponse == null){
                    Log.d(TAG,"nulo");
                }else{
                    Log.d(TAG,"auxResponse size "+auxResponse.size());
                }


                Map<String, String> item = new HashMap<>();
                item = auxResponse.get(0);
                if (item.get("method").equals(METHOD.REQUEST_OFFICES.toString())){
                    Log.d(TAG,"metodo oficinas");
                    final UpdateOffices updateOffices = new UpdateOffices(auxResponse);

                        updateOffices.execute();

                }else if(item.get("method").equals(METHOD.REQUEST_EXCEPTION_CODES.toString())){
                    Log.d(TAG,"decode response ExceptionCodes");

                    new UpdateExceptionCodes(auxResponse).execute();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            if(method.equals("oficinas")) {
                method = "codigos";
                Map<String, String> requestData = new HashMap<>();
                requestData.put("ultimaAct", last_date);
                RequestManager.sharedInstance().setListener(SplashScreenActivity.this);
                RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_EXCEPTION_CODES, requestData);
            }else if(method.equals("codigos")){
                Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

       /* Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(intent);*/
    }

    class UpdateOffices extends AsyncTask<String, Void, String>  {

        ArrayList<Map<String,String>> values;
        public UpdateOffices(ArrayList<Map<String, String>> values){
            this.values = values;
        }

        @Override
        protected String doInBackground(String... params) {
            if(values.size() != 0) {
                Offices.updateOffices(context, values);
            }
           // dbHelper.close();
            return null;
        }
        @Override
        protected void onPostExecute(String result) {/*
            Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
            startActivity(intent);
            finish();*/

            method = "codigos";
            Map<String, String> requestData = new HashMap<>();
            requestData.put("ultimaAct", last_date);
            RequestManager.sharedInstance().setListener(SplashScreenActivity.this);
            RequestManager.sharedInstance().makeRequest(METHOD.REQUEST_EXCEPTION_CODES, requestData);

        }


    }


    class UpdateExceptionCodes extends AsyncTask<String, Void, String>  {

        ArrayList<Map<String,String>> values;
        public UpdateExceptionCodes(ArrayList<Map<String, String>> values){
            this.values = values;
        }

        @Override
        protected String doInBackground(String... params) {
            if(values.size() != 0) {
                Codes.updateCodes(context, values);
            }
            // dbHelper.close();
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }


    }


}
