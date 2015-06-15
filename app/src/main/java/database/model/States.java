package database.model;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import database.DataBaseAdapter;

/**
 * Created by rebecalopezmartinez on 28/05/15.
 */
public class States {
    public static final String TABLE_NAME = "estados";

    public static final String Z_PK = "Z_PK";
    public static final String ZNUMEROESTADO = "ZNUMEROESTADO";
    public static final String ZLATITUD = "ZLATITUD";
    public static final String ZLONGITUD = "ZLONGITUD";
    public static final String ZNOMBRE = "ZNOMBRE";

    private int z_pk;
    private int znumero_estado;
    private String zlatitud;
    private String zlongitud;
    private String znombre;

    public States(int z_pk, int znumero_estado, String zlatitud, String zlongitud, String znombre) {
        this.z_pk = z_pk;
        this.znumero_estado = znumero_estado;
        this.zlatitud = zlatitud;
        this.zlongitud = zlongitud;
        this.znombre = znombre;
    }

    public static long insert(Context context, int znumero_estado, String zlatitud, String zlongitud,
                              String znombre){
        ContentValues cv = new ContentValues();
        cv.put(ZNUMEROESTADO, znumero_estado);
        cv.put(ZLATITUD,zlatitud);
        cv.put(ZLONGITUD, zlongitud);
        cv.put(ZNOMBRE, znombre);

        return DataBaseAdapter.getDB(context).insert(TABLE_NAME,null,cv);
    }

    public static ArrayList<Map<String,String>> getAllInMaps(Context context){

        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME, null, null, null, null ,null, null);
        if (cursor != null && cursor.getCount() > 0) {
            ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                Map<String,String> map  = new HashMap<String, String>();

                map.put(ZNUMEROESTADO,cursor.getString(cursor.getColumnIndexOrThrow(ZNUMEROESTADO)));
                map.put(ZLATITUD,cursor.getString(cursor.getColumnIndexOrThrow(ZLATITUD)));
                map.put(ZLONGITUD,cursor.getString(cursor.getColumnIndexOrThrow(ZLONGITUD)));
                map.put(ZNOMBRE,cursor.getString(cursor.getColumnIndexOrThrow(ZNOMBRE)));
                list.add(map);
            }
            Log.d("Campos recuperados: ", ""+list.size());
            cursor.close();
            return list;
        }
        return null;
    }

    public static ArrayList<Map<String,String>> getStatesNames(Context context){

        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME, null, null, null, null ,null, ZNUMEROESTADO);
        if (cursor != null && cursor.getCount() > 0) {
            ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                Map<String,String> map  = new HashMap<String, String>();

                map.put(ZNOMBRE,cursor.getString(cursor.getColumnIndexOrThrow(ZNOMBRE)));
                map.put(ZNUMEROESTADO,cursor.getString(cursor.getColumnIndexOrThrow(ZNUMEROESTADO)));
                map.put(ZLATITUD,cursor.getString(cursor.getColumnIndexOrThrow(ZLATITUD)));
                map.put(ZLONGITUD,cursor.getString(cursor.getColumnIndexOrThrow(ZLONGITUD)));
                list.add(map);
            }
            Log.d("Campos recuperados: ", ""+list.size());
            cursor.close();
            return list;
        }
        return null;
    }

    public static String getStateNameById(Context context,String Id){
        if(Id.equals("") ){
            return null;
        }
        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME,
                new String[] {ZNUMEROESTADO, ZNOMBRE},
                ZNUMEROESTADO +"= ?",
                new String[] {Id}, null ,null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
        }
        String estado = cursor.getString(cursor.getColumnIndexOrThrow(ZNOMBRE));
        cursor.close();

        return estado;
    }

    public static int getStateNumberByName(Context context,String name){
       // Log.d(TABLE_NAME,"estado: "+name);
        if(name.equals("") ){
            return 99;
        }
        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME,
                new String[] {ZNUMEROESTADO, ZNOMBRE},
                ZNOMBRE +"= ?",
                new String[] { name }, null ,null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(ZNUMEROESTADO));
            cursor.close();
            return id;
        }
        cursor.close();
       return 99;
    }

    public static void setStates(Context context, String file_name){

        try {
            AssetManager assetManager = context.getAssets();
            InputStream json_file = assetManager.open("dbtxt/"+file_name);
            byte[] formArray = new byte[json_file.available()];

            json_file.read(formArray);
            json_file.close();

            String json = new String(formArray, "UTF-8");

            JSONObject obj = null;
            try {
                obj = new JSONObject(json);
                JSONArray jarray = obj.getJSONArray("states");

                for(int i=0;i<jarray.length();i++){
                    JSONObject stateObject = jarray.getJSONObject(i);

                    int number = Integer.parseInt(stateObject.getString("number"));
                    String latitud = stateObject.getString("latitud");
                    String longitud = stateObject.getString("longitud");
                    String name = stateObject.getString("name");

                    insert(context,number,latitud,longitud,name);
                    Log.d("Insertar",name);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void removeAll(Context context){
        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME, null, null, null, null ,null, Z_PK);
        if (cursor != null && cursor.getCount() > 0) {

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                int id              = cursor.getInt(cursor.getColumnIndexOrThrow(Z_PK));
                delete(context, id);
            }
        }
        cursor.close();
    }

    public static int delete(Context context, int id) {
        return DataBaseAdapter.getDB(context).delete(TABLE_NAME, Z_PK + "=" + id, null);
    }
}
