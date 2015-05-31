package com.sellcom.apps.tracker_material.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import database.model.States;

import com.sellcom.apps.tracker_material.R;

import database.DataBaseAdapter;
import database.DataBaseHelper;

public class SplashScreenActivity extends ActionBarActivity {

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context = this;
         Log.d("SplashScreen","Llama al hilo");
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

    class CreateDB extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected String doInBackground(String... params) {
            //Crear DB
            DataBaseHelper dbHelper = new DataBaseHelper(context);
            Log.d("SplashScreen","crea dbHelper");

            DataBaseAdapter.openDB(context);
            //dbHelper.close();
            States.setStates(context,"states.json");

            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
            startActivity(intent);

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
        }

    }
}
