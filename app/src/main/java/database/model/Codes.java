package database.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import database.DataBaseAdapter;

/**
 * Created by rebecalopezmartinez on 28/05/15.
 */
public class Codes {
    public static final String TABLE_NAME = "codigos";

    public static final String ZPK = "Z_PK";
    public static final String ZCLAVE = "ZCLAVE";
    public static final String ZDESCRIPCIONCLAVE = "ZDESCRIPCIONCLAVE";
    public static final String ZIDCATALOGO = "ZIDCATALOGO";
    public static final String ZVERSION = "ZVERSION";

    private String zpk;
    private String zclave;
    private String zdescripcionclave;
    private String zidcatalogo;
    private String zversion;

    public Codes(String zpk,String zclave,String zdescripcionclave,String zidcatalogo,String zversion) {
        this.zpk = zpk;
        this.zclave = zclave;
        this.zdescripcionclave = zdescripcionclave;
        this.zidcatalogo = zidcatalogo;
        this.zversion = zversion;
    }

    public static long insert(Context context,String zpk,String zclave,String zdescripcionclave,String zidcatalogo,
                              String zversion){
        ContentValues cv = new ContentValues();

        cv.put(ZCLAVE,zclave);
        cv.put(ZDESCRIPCIONCLAVE,zdescripcionclave);
        cv.put(ZIDCATALOGO,zidcatalogo);
        cv.put(ZVERSION,zversion);

        return DataBaseAdapter.getDB(context).insert(TABLE_NAME,null,cv);

    }
    public static long updateCodes(Context context, ArrayList<Map<String, String>> values){
        ContentValues cv = new ContentValues();
        long aux=0;

        for(int i = 0; i <values.size(); i++) {
            Map<String, String> item = new HashMap<>();
            item = values.get(i);

            //Log.d(TABLE_NAME,"status"+item.get("status"));
            if (item.get("status").equals("true")) {

                cv.put(ZCLAVE, item.get("clave"));
                cv.put(ZDESCRIPCIONCLAVE, item.get("descripcion"));
                cv.put(ZIDCATALOGO, "NULL");
                cv.put(ZVERSION, item.get("ultimaAct"));

                if(item.get("encontrado").equals("true")){

                    if (item.get("modificado").equals("true")) {
                        try {
                            aux = DataBaseAdapter.getDB(context).update(TABLE_NAME, cv, ZCLAVE + "=?", new String[]{item.get("clave")});
                            Log.d(TABLE_NAME, "actualizar codes aux: " + aux);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d(TABLE_NAME, "No actualizado");
                        }

                    }

                }else if(item.get("encontrado").equals("false")){
                    try {
                        aux = DataBaseAdapter.getDB(context).insert(TABLE_NAME, null, cv);
                        Log.d(TABLE_NAME, "insertar codes aux: " + aux);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TABLE_NAME, "No insertado");
                    }
                }

            }else{
                String clave = item.get("clave");
                //Log.d(TABLE_NAME,"idOficina: "+auxId);
                delete(context,clave);
            }
        }

        return aux;
    }


    public static ArrayList<Map<String,String>> getAllInMaps(Context context){

        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME, null, null, null, null ,null, null);
        if (cursor != null && cursor.getCount() > 0) {
            ArrayList<Map<String,String>> list = new ArrayList<>();

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                Map<String,String> map  = new HashMap<>();

                map.put(ZCLAVE,cursor.getString(cursor.getColumnIndexOrThrow(ZCLAVE)));
                map.put(ZDESCRIPCIONCLAVE,cursor.getString(cursor.getColumnIndexOrThrow(ZDESCRIPCIONCLAVE)));
                map.put(ZIDCATALOGO,cursor.getString(cursor.getColumnIndexOrThrow(ZIDCATALOGO)));
                map.put(ZVERSION,cursor.getString(cursor.getColumnIndexOrThrow(ZVERSION)));

                list.add(map);
            }
            Log.d("Campos recuperados: ", "" + list.size());
            cursor.close();
            return list;
        }
        try {
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getCodeByClave(Context context,String clave){

        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME,
                null,
                ZCLAVE + "=?",
                new String[] {clave}, null ,null, null);

        if(cursor != null & cursor.getCount() > 0){
            cursor.moveToFirst();
            String response= cursor.getString(cursor.getColumnIndexOrThrow(ZVERSION));
            cursor.close();
            return response;
        }
        else {
            cursor.close();
            return null;
        }
    }

    public static boolean existsZclave(String zclave, Context context){
        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME,
                null,
                ZCLAVE + "=?",
                new String[]{zclave}, null ,null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            if(cursor.getString(cursor.getColumnIndexOrThrow(ZCLAVE)).equals(zclave)){
                cursor.close();
                return true;
            }
            cursor.close();
            return false;
        }
        try {
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void removeAll(Context context){
        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME, null, null, null, null ,null, ZPK);
        if (cursor != null && cursor.getCount() > 0) {

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String id              = cursor.getString(cursor.getColumnIndexOrThrow(ZPK));
                delete(context, id);
            }
        }
        try {
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int delete(Context context, String clave) {
        return DataBaseAdapter.getDB(context).delete(TABLE_NAME, ZCLAVE + "=" + clave, null);
    }


}
