package com.estafeta.estafetamovilv1.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.estafeta.estafetamovilv1.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class contains general methods that deal in various class.
 */
public class Utilities {

    public static boolean flag = true;
    public static int position = 0;

    /**
     * @deprecated
     * @param context
     * @return
     */
    public static boolean isHandset(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                < Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * Hide Keyboard.
     * @param context
     * @param editField
     */
    public static void hideKeyboard(Context context, View editField) {

        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editField.getWindowToken(), 0);
    }


    /**
     * @deprecated
     * @param obj
     * @return
     * @throws JSONException
     */
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

    /**
     * @deprecated
     * @param key
     * @param value
     * @param activity
     */
    public static void saveInPreferencesKeyAndValue(String key, String value, Activity activity){
        SharedPreferences sharedPref        = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor     = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * @deprecated
     * @param key
     * @param activity
     * @return
     */
    public static String getPreferencesValueForKey(String key, Activity activity){
        SharedPreferences sharedPref        = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(key,"CLEAR");
    }

    /**
     * Office converts one type to an integer.
     * @param type
     * @return
     */
    public static int convertOfficesTypeToInt(String type){
        if(type.equals("SU"))
            return 0;
        else if(type.equals("CO"))
            return 1;
        else if (type.equals("CA"))
            return 2;
        return -1;
    }

    /**
     * Converts an integer to an 'office'.
     * @param type
     * @return
     */
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

    /**
     * You get the icon of an office.
     * @param type
     * @param context
     * @return
     */
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


    /**
     * Validates that a text is to type e-mail.
     * @param context
     * @param email
     * @param view
     * @return
     */
    public static boolean validateEmail(Context context, String email, EditText view){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9_-]+\\.+[a-zA-Z.]+";
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

    /**
     * Validates that a text is to type e-mail.
     * @param context
     * @param email
     * @return
     */
    public static boolean validateEmail(Context context, String email){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9_-]+\\.+[a-zA-Z.]+";
        email=email.trim();
        if (email.isEmpty()){
            return false;
        }

        if (email.matches(emailPattern))
        {
            return true;
        }
        else{
            return false;
        }

    }

    /**
     * Validates that a text is to type number.
     * @param context
     * @param email
     * @param view
     * @return
     */
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


    /**
     * Validates whether a text field is empty.
     * @param context
     * @param field
     * @param view
     * @return
     */
    public static boolean validateCommonField(Context context,String field,EditText view){
        if(field.isEmpty()){
            view.setError(context.getResources().getText(R.string.error_empty_field).toString());
            view.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Validate that a number of guide or tracking code is valid according to the business rules of Estafeta.
     * @param preCode
     * @return
     */
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

    /**
     * Validates that a text has just integers.
     * @param code
     * @return
     */
    public static boolean validatedigits(String code){
        String pattern = "[0-9]+";
        if (code.matches(pattern))
            return true;
        return false;
    }

    /**
     * Validates that a text is alphanumeric.
     * @param code
     * @return
     */
    public static boolean validatetext(String code){
        String pattern = "[a-zA-Z0-9]+";
        if (code.matches(pattern))
            return true;
        return false;
    }

    /**
     * Check that a text is safe.
     * @param cadena
     * @return
     */
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

    /**
     * Assign a format to an amount of money.
     * @param amount
     * @param strLength
     * @return
     */
    public static String setReceiptMoneyNumberFormat (double amount, int strLength){

        DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
        simbolo.setDecimalSeparator('.');
        simbolo.setGroupingSeparator(',');

        DecimalFormat form  = new DecimalFormat("###,###.##",simbolo);
        String strAmount    = form.format(amount);
        while (strAmount.length()<strLength){
            strAmount = " "+strAmount;
        }

        if(strAmount.equals(" 0")){
            strAmount = "0.00";
            strAmount = "$"+strAmount;

            return strAmount;
        }

        for(int i =0; i<strAmount.length(); i++){
            if((""+strAmount.charAt(i)).equals(".")){
                if((strAmount.length()-1)-i == 1){
                    strAmount = strAmount + "0";
                }
            }

        }

        strAmount = "$"+strAmount;

        return strAmount;
    }



    public static boolean specialCharacteresInString(String str){

        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        return m.find();

    }

    public static void setCustomHint(Context context, String text, TextView textView){

        String colored = "*";
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(text);
        int start = builder.length();
        builder.append(colored);
        int end = builder.length();

        builder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.estafeta_red)), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setHint(builder);

    }

    /**
     * Validates that a text is to type e-mail.
     * @param email
     * @return
     */
    public static boolean validateEmail(String email){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9_-]+\\.+[a-zA-Z.]+";
        email=email.trim();
        if (email.isEmpty()){
            return false;
        }

        if (email.matches(emailPattern))
        {
            return true;
        }
        else{
            return false;
        }

    }

    public static String bitmapToString(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    public static Bitmap stringToBitmap(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

}