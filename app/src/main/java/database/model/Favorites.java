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
public class Favorites {
    public static final String TABLE_NAME = "ctl_favoritos";

    public static final String ID_CTL_FAVORITOS = "id_ctl_favoritos";
    public static final String GUIA = "guia";
    public static final String RASTREO = "rastreo";
    public static final String ALIAS = "alias";
    public static final String FECHA_REGISTRO = "fecha_registro";
    public static final String NOTIFICA = "notifica";

    private int id_favoritos;
    private String guia;
    private String rastreo;
    private  String alias;
    private String fecha_registro;
    private int notifica;

    public Favorites(int id_favoritos,String guia,String rastreo,String alias,String fecha_registro,
                     int notifica) {
        this.id_favoritos = id_favoritos;
        this.guia = guia;
        this.rastreo = rastreo;
        this.alias = alias;
        this.fecha_registro = fecha_registro;
        this.notifica = notifica;
    }

    public static long insert(Context context,String guia,String rastreo,String alias,
                              String fecha_registro, int notifica){
        ContentValues cv = new ContentValues();
        cv.put(GUIA, guia);
        cv.put(RASTREO, rastreo);
        cv.put(ALIAS, alias);
        cv.put(FECHA_REGISTRO, fecha_registro);
        cv.put(NOTIFICA,notifica);

        return DataBaseAdapter.getDB(context).insert(TABLE_NAME,null,cv);

    }

    public static long insertMap(ArrayList<Map<String, String>> values){
        for(int i=0;i<values.size();i++){

        }
        return 0;
    }

    public static ArrayList<Map<String,String>> getAll(Context context){

        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME, null, null, null, null ,null, null);
        if (cursor != null && cursor.getCount() > 0) {
            ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                Map<String,String> map  = new HashMap<String, String>();

                map.put(ID_CTL_FAVORITOS,cursor.getString(cursor.getColumnIndexOrThrow(ID_CTL_FAVORITOS)));
                map.put(GUIA,cursor.getString(cursor.getColumnIndexOrThrow(GUIA)));
                map.put(RASTREO,cursor.getString(cursor.getColumnIndexOrThrow(RASTREO)));
                map.put(ALIAS,cursor.getString(cursor.getColumnIndexOrThrow(ALIAS)));
                map.put(FECHA_REGISTRO,cursor.getString(cursor.getColumnIndexOrThrow(FECHA_REGISTRO)));
                map.put(NOTIFICA,cursor.getString(cursor.getColumnIndexOrThrow(NOTIFICA)));

                list.add(map);
            }
            Log.d("Campos recuperados: ", "" + list.size());
            cursor.close();
            return list;
        }
        return null;
    }
}
