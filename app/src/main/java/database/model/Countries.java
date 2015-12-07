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
 * This class contains methods that let you edit the table 'Countries' Database.
 */
public class Countries {
    public static final String TABLE_NAME = "paises";

    public static final String ID = "id";
    public static final String IDPAIS = "idpais";
    public static final String NOMBREPAIS_ESP = "nombrepais_esp";
    public static final String NOMBREPAIS_ING = "nombrepais_ing";

    private int id;
    private String idpais;
    private String nombrepais_esp;
    private String nombrepais_ing;

    public Countries(int id, String idpais, String nombrepais_esp, String nombrepais_ing) {
        this.id = id;
        this.idpais = idpais;
        this.nombrepais_esp = nombrepais_esp;
        this.nombrepais_ing = nombrepais_ing;
    }

    /**
     * It allows you to insert data into the database.
     * @param context
     * @param idpais
     * @param nombrepais_esp
     * @param nombrepais_ing
     * @return
     */
    public static long insert(Context context, String idpais, String nombrepais_esp,
                              String nombrepais_ing){
        ContentValues cv = new ContentValues();
        cv.put(IDPAIS, idpais);
        cv.put(NOMBREPAIS_ESP, nombrepais_esp);
        cv.put(NOMBREPAIS_ING, nombrepais_ing);

        return DataBaseAdapter.getDB(context).insert(TABLE_NAME,null,cv);

    }

    /**
     * It lets get all the information in this table within the database.
     * @param context
     * @return
     */
    public static ArrayList<Map<String,String>> getCountriesNames(Context context){

        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME, null, null, null, null ,null, ID);
        if (cursor != null && cursor.getCount() > 0) {
            ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                Map<String,String> map  = new HashMap<String, String>();

                map.put(IDPAIS,cursor.getString(cursor.getColumnIndexOrThrow(IDPAIS)));
                map.put(NOMBREPAIS_ESP,cursor.getString(cursor.getColumnIndexOrThrow(NOMBREPAIS_ESP)));
                map.put(NOMBREPAIS_ING,cursor.getString(cursor.getColumnIndexOrThrow(NOMBREPAIS_ING)));

                list.add(map);
            }
            Log.d("Campos recuperados: ", "" + list.size());
            cursor.close();
            return list;
        }
        return null;
    }

    /**
     * Gets the ID of the country by an id.
     * @param context
     * @param id
     * @return
     */
    public static Map<String,String> getIdPaisById(Context context, String id) {

        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME, null, "id = ?",
                new String[] { id},
                null,
                null,
                ID);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            Map<String, String> map = new HashMap<String, String>();

            map.put(ID, cursor.getString(cursor.getColumnIndexOrThrow(ID)));
            map.put(IDPAIS, cursor.getString(cursor.getColumnIndexOrThrow(IDPAIS)));
            map.put(NOMBREPAIS_ESP,cursor.getString(cursor.getColumnIndexOrThrow(NOMBREPAIS_ESP)));
            map.put(NOMBREPAIS_ING, cursor.getString(cursor.getColumnIndexOrThrow(NOMBREPAIS_ING)));
            cursor.close();
            return map;
        }
        return null;
    }

    /**
     * @deprecated
     * @param context
     */
    public static void removeAll(Context context){
        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME, null, null, null, null ,null, ID);
        if (cursor != null && cursor.getCount() > 0) {

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                int id              = cursor.getInt(cursor.getColumnIndexOrThrow(ID));
                delete(context, id);
            }
        }
        cursor.close();
    }

    /**
     * Delete items from the database.
     * @param context
     * @param id
     * @return
     */
    public static int delete(Context context, int id) {
        return DataBaseAdapter.getDB(context).delete(TABLE_NAME, ID + "=" + id, null);
    }
}
