package com.sellcom.apps.tracker_material.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.sellcom.apps.tracker_material.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import database.model.Offices;

public class Utilities {

    public static boolean isHandset(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                < Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static void hideKeyboard(Context context, View editField) {

        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editField.getWindowToken(), 0);
    }

    public static Bitmap stringToBitmap(String encodedString) {

        try{
            byte [] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
    public static Map<String,String> jsonToMap(JSONObject obj) throws JSONException {
        Map<String, String> map = new HashMap<String, String>();
        Iterator<?> keys        = obj.keys();

        while( keys.hasNext() ){
            String key = (String)keys.next();
            String value = obj.getString(key);
            map.put(key, value);
        }
        return  map;
    }
    public static void saveInPreferencesKeyAndValue(String key, String value, Activity activity){
        SharedPreferences sharedPref        = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor     = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public static String getPreferencesValueForKey(String key, Activity activity){
        SharedPreferences sharedPref        = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(key,"CLEAR");
    }

    public static int convertOfficesTypeToInt(String type){
        if(type.equals("SU"))
            return 0;
        else if(type.equals("CO"))
            return 1;
        else if (type.equals("CA"))
            return 2;
        return -1;
    }

    public static String convertOfficesIntToType(int type){
        switch (type){
            case 0:
                return "SU";
            case 1:
                return "CO";
            case 2:
                return "CA";
        }
        return "-1";
    }

    public static Bitmap getOfficesIcon(String type,Context context){
        Bitmap bMap=null;
        if (type.equals("SU"))
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.pin_rojo);
        else if (type.equals("CO"))
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.pin_azul);
        else if(type.equals("CA"))
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.pin_gris);
        return bMap;
    }

}