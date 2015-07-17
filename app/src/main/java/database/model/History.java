package database.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.sellcom.apps.tracker_material.Utils.DatesHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import database.DataBaseAdapter;

/**
 * Created by rebecalopezmartinez on 28/05/15.
 */
public class History {
    public static final String TABLE_NAME = "history";

    public static final String ID_HISTORY   = "id_history";
    public static final String ADD_DATE     = "add_date";
    public static final String CODE_NUMBER  = "code_number";
    public static final String FAVORITE     = "favorite";
    public static final String MOV_COMS     = "H_eventDescriptionSPA";
    public static final String MOV_DATE     = "H_eventDateTime";
    public static final String MOV_PLACE    = "H_eventPlaceName";


    public static long insertMap(Context context, Map<String, String> values){

        String date = DatesHelper.getStringDate(new Date());

        ContentValues cv = new ContentValues();
        cv.put(ADD_DATE,date);
        cv.put(CODE_NUMBER, values.get("shortWayBillId"));
        cv.put(FAVORITE, values.get("favorite_id"));
        cv.put(MOV_COMS, values.get("H_eventDescriptionSPA"));
        cv.put(MOV_DATE, values.get("H_eventDateTime"));
        cv.put(MOV_PLACE, values.get("H_eventPlaceName"));

        return DataBaseAdapter.getDB(context).insert(TABLE_NAME,null,cv);
    }


    public static void insertMapByFavorite(Context context, Map<String, String> values){

        String aux= getIdById_history(context, values.get("H_eventDateTime"));
        Log.d(TABLE_NAME, "id: " + aux);
        if(aux != null){
            Log.d(TABLE_NAME,"row exist!!");
        }else{
            insertMap(context,values);
        }

    }

    public static String getIdById_history(Context context,String H_eventDateTime){

        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME,
                null,
                MOV_DATE + "=?",
                new String[]{H_eventDateTime}, null, null, null);

        if(cursor != null & cursor.getCount() > 0){
            cursor.moveToFirst();
            String response = cursor.getString(cursor.getColumnIndexOrThrow(ID_HISTORY));
            cursor.close();
            return response;
        }else
            return null;
    }

    public static long update(Context context, Map<String, String> values){

        ContentValues cv = new ContentValues();
        cv.put(CODE_NUMBER, values.get("shortWayBillId"));
        cv.put(MOV_COMS, values.get("H_eventDescriptionSPA"));
        cv.put(MOV_DATE, values.get("H_eventDateTime"));
        cv.put(MOV_PLACE, values.get("H_eventPlaceName"));

        long response = DataBaseAdapter.getDB(context).update(TABLE_NAME, cv, FAVORITE + "=?", new String[]{values.get("favorite_id")});
        Log.d(TABLE_NAME,"update: "+response);
        return response;
    }

    public static ArrayList<Map<String,String>> getHistotyByFavoriteId(Context context, String favorite){

        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME, null, FAVORITE + "=" + favorite, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                Map<String,String> map  = new HashMap<String, String>();

            map.put(ADD_DATE,cursor.getString(cursor.getColumnIndexOrThrow(ADD_DATE)));
            map.put(ID_HISTORY,cursor.getString(cursor.getColumnIndexOrThrow(ID_HISTORY)));
            map.put(CODE_NUMBER,cursor.getString(cursor.getColumnIndexOrThrow(CODE_NUMBER)));
            map.put(FAVORITE,cursor.getString(cursor.getColumnIndexOrThrow(FAVORITE)));
            map.put(MOV_COMS,cursor.getString(cursor.getColumnIndexOrThrow(MOV_COMS)));
            map.put(MOV_DATE,cursor.getString(cursor.getColumnIndexOrThrow(MOV_DATE)));
            map.put(MOV_PLACE,cursor.getString(cursor.getColumnIndexOrThrow(MOV_PLACE)));

                list.add(map);
            }
            Log.d("Campos recuperados: ", "" + list.size());
            cursor.close();
            return list;
        }
        return null;
    }

    public static int delete(Context context, String favorite) {
        int resp = DataBaseAdapter.getDB(context).delete(TABLE_NAME, FAVORITE + "=?", new String[]{favorite});
        //Log.d(TABLE_NAME,"delete resp:"+resp);
        return resp;
    }


}
