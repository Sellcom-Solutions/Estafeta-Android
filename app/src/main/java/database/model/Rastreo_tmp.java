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
 * Created by rebecalopezmartinez on 08/06/15.
 */
public class Rastreo_tmp {
    public static final String TABLE_NAME = "rastreo_tmp";

    public static final String R_ID = "id";
    public static final String RCODIGO = "codigo";
    public static final String RFAVORITO = "favorito";

    public static long insert(Context context, Map<String, String> item){
        long result = 0;

            ContentValues cv = new ContentValues();

            cv.put(RCODIGO,item.get("codigo") );
            cv.put(RFAVORITO,item.get("favorito"));
            result = DataBaseAdapter.getDB(context).insert(TABLE_NAME,null,cv);

        return result;
    }

    public static ArrayList<Map<String,String>> getAllInMaps(Context context){
        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME, null, null, null, null ,null, null);
        if (cursor != null && cursor.getCount() > 0) {
            ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                Map<String,String> map  = new HashMap<String, String>();

                map.put(R_ID,cursor.getString(cursor.getColumnIndexOrThrow(R_ID)));
                map.put(RCODIGO,cursor.getString(cursor.getColumnIndexOrThrow(RCODIGO)));
                map.put(RFAVORITO,cursor.getString(cursor.getColumnIndexOrThrow(RFAVORITO)));
                list.add(map);
            }
            Log.d("Campos recuperados: ", "" + list.size());
            cursor.close();
            return list;
        }
        return null;
    }

    public static void update(Context context, Map<String,String> data){
        ContentValues cv = new ContentValues();
        cv.put(RCODIGO,data.get("codigo") );
        cv.put(RFAVORITO,data.get("favorito"));

        DataBaseAdapter.getDB(context).update(TABLE_NAME,cv,R_ID+"=?",new String[] {data.get("id")});
    }

    public static void removeAll(Context context){
        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME, null, null, null, null ,null, R_ID);
        if (cursor != null && cursor.getCount() > 0) {

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                int id              = cursor.getInt(cursor.getColumnIndexOrThrow(R_ID));
                delete(context, id);
            }
        }
        cursor.close();
    }

    public static int delete(Context context, int id) {
        return DataBaseAdapter.getDB(context).delete(TABLE_NAME, R_ID + "=" + id, null);
    }

}
