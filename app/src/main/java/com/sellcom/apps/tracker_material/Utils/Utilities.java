package com.sellcom.apps.tracker_material.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.sellcom.apps.tracker_material.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import database.model.Offices;

public class Utilities {

    public static boolean flag = true;

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
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_filtro_rojo_reality);
        else if (type.equals("CO"))
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_filtro_azul_reality);
        else if(type.equals("CA"))
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_filtro_gris_reality);
        return bMap;
    }


    public static boolean validateEmail(Context context, String email, EditText view){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-z]+";
        email=email.trim();
        if (email.isEmpty()){
            view.setError(context.getResources().getText(R.string.error_empty_field).toString());
            view.requestFocus();
            return false;
        }

        if (email.matches(emailPattern))
        {
            return true;
        }
        else{
            view.setError(context.getResources().getText(R.string.error_invalid_email_address).toString());
            view.requestFocus();
            return false;
        }

    }

    public static boolean validateNumber(Context context, String email, EditText view){
        if (email.isEmpty()){
            view.setError(context.getResources().getText(R.string.error_empty_field).toString());
            view.requestFocus();
            return false;
        }
        if (email.length()!=10){
            view.setError(context.getResources().getText(R.string.error_phone_field).toString());
            view.requestFocus();
            return false;
        }
            return true;
    }


    public static boolean validateCommonField(Context context,String field,EditText view){
        if(field.isEmpty()){
            view.setError(context.getResources().getText(R.string.error_empty_field).toString());
            view.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean validateCode(String preCode){

        Log.d("Longitud: ",""+preCode.length());

        if (preCode.length() == 10) {
            return validatedigits(preCode);

        }else if (preCode.length() == 22){
            boolean correctCode = false;
            for(int i = 0; i<preCode.length(); i++) {
                if (i == 0) {
                    correctCode = Utilities.validatetext("" + preCode.charAt(i));
                } else if (i > 0 && i < 3) {
                    correctCode = Utilities.validatetext("" + preCode.charAt(i));
                } else if (i > 2 && i < 10) {
                    correctCode = Utilities.validatedigits("" + preCode.charAt(i));
                } else if (i > 9 && i < 13) {
                    correctCode = Utilities.validatetext("" + preCode.charAt(i));
                } else if (i > 12 && i < 15) {
                    correctCode = Utilities.validatetext("" + preCode.charAt(i));
                } else if (i > 14 && i < 22) {
                    correctCode = Utilities.validatedigits("" + preCode.charAt(i));
                }

                if(!correctCode){
                    return false;
                }
            }
            return correctCode;

        }else{
            Log.d("Longitud: ",""+String.valueOf(preCode));
            return false;
        }


    }

    public static boolean validatedigits(String code){
        String pattern = "[0-9]+";
        if (code.matches(pattern))
            return true;
        return false;
    }

    public static boolean validatetext(String code){
        String pattern = "[a-zA-Z0-9]+";
        if (code.matches(pattern))
            return true;
        return false;
    }

    public static double getSaveString(String cadena){
        double saveString = 0;

        try{

            Double d = Double.parseDouble(""+cadena);
            saveString = d;

        }catch (Exception e){
            return saveString;
        }

        return saveString;
    }

}