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

    public static ArrayList<Map<String,String>> getAllInMaps(Context context){

        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME, null, null, null, null ,null, null);
        if (cursor != null && cursor.getCount() > 0) {
            ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                Map<String,String> map  = new HashMap<String, String>();

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
        return null;
    }

    public static void removeAll(Context context){
        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME, null, null, null, null ,null, ZPK);
        if (cursor != null && cursor.getCount() > 0) {

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                int id              = cursor.getInt(cursor.getColumnIndexOrThrow(ZPK));
                delete(context, id);
            }
        }
        cursor.close();
    }

    public static int delete(Context context, int id) {
        return DataBaseAdapter.getDB(context).delete(TABLE_NAME, ZPK + "=" + id, null);
    }


}
