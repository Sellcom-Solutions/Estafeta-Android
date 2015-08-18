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
public class Favorites {
    public static final String TABLE_NAME = "favorites";

    public static final String ID_CTL_FAVORITOS = "id_favoritos";
    public static final String FECHA_REGISTRO = "add_date";
    public static final String ALIAS = "alias";
    public static final String RASTREO = "codigo_rastreo";
    public static final String CP_DESTINO = "cp_destino";
    public static final String DESTINO = "destino";
    public static final String ESTATUS = "estatus";
    public static final String FECHA_RECOLECCION = "fecha_recoleccion";
    public static final String FECHA_HORA_ENTREGA = "fechaHoraEntrega";
    public static final String HISTORIA = "historia";
    public static final String GUIA = "no_guia";
    public static final String ORIGEN = "origen";
    public static final String RECIBIO = "recibio";
    public static final String REFERENCIA = "referencia";
    public static final String SIGNATURE = "signature";
    public static final String NOTIFICA = "notifica";

    public static long insert(Context context, Map<String, String> values){

        String date = DatesHelper.getStringDate(new Date());

        Log.d(TABLE_NAME, "fecha insertada en favorito: " + date);

        ContentValues cv = new ContentValues();
        cv.put(FECHA_REGISTRO,""+date);
        cv.put(ALIAS, " ");
        cv.put(RASTREO, values.get("shortWayBillId"));
        cv.put(CP_DESTINO, values.get("DD_zipCode"));
        cv.put(DESTINO, values.get("DD_destinationName"));
        cv.put(ESTATUS, values.get("estatus1"));
        cv.put(FECHA_RECOLECCION, values.get("PK_pickupDateTime"));
        cv.put(FECHA_HORA_ENTREGA, values.get("DD_deliveryDateTime"));
        cv.put(HISTORIA, " ");
        cv.put(ORIGEN , values.get("PK_originName"));
        cv.put(GUIA, values.get("wayBill"));
        cv.put(RECIBIO, values.get("DD_receiverName"));
        cv.put(REFERENCIA, values.get("CI_reference"));
        cv.put(SIGNATURE, values.get("signature"));
        cv.put(NOTIFICA,"false");

        long response = DataBaseAdapter.getDB(context).insert(TABLE_NAME,null,cv);
        Log.d(TABLE_NAME,"insert: "+response);
        return response;
    }

    public static long updateReference(Context context, String referencia, String id_favoritos){

        ContentValues cv = new ContentValues();
            cv.put(REFERENCIA, referencia);

        long response = DataBaseAdapter.getDB(context).update(TABLE_NAME, cv, ID_CTL_FAVORITOS + "=?", new String[]{id_favoritos});

        Log.d("EditDialog",""+response);

        return response;
    }

    public static long update(Context context, Map<String, String> values){
        ContentValues cv = new ContentValues();
        cv.put(ALIAS, " ");
        cv.put(RASTREO, values.get("shortWayBillId"));
        cv.put(CP_DESTINO, values.get("DD_zipCode"));
        cv.put(DESTINO, values.get("DD_destinationName"));
        cv.put(ESTATUS, values.get("estatus1"));
        cv.put(FECHA_RECOLECCION, values.get("PK_pickupDateTime"));
        cv.put(FECHA_HORA_ENTREGA, values.get("DD_deliveryDateTime"));
        cv.put(HISTORIA, values.get("history_id"));
        cv.put(ORIGEN , values.get("PK_originName"));
        cv.put(GUIA, values.get("wayBill"));
        cv.put(RECIBIO, values.get("DD_receiverName"));
        cv.put(REFERENCIA, values.get("CI_reference"));
        cv.put(SIGNATURE, values.get("signature"));
        cv.put(NOTIFICA,"false");

        long response = DataBaseAdapter.getDB(context).update(TABLE_NAME, cv, GUIA + "=?", new String[]{values.get("wayBill")});
        Log.d(TABLE_NAME,"update: "+response);
        return response;
    }

    public static long insertMap(Context context, Map<String, String> values){

        String aux= getIdByWayBill(context,values.get("wayBill"));
        Log.d(TABLE_NAME,"id: "+aux);
        if(aux != null){
            Log.d(TABLE_NAME,"row updated");
            return update(context,values);
        }
        else {
            Log.d(TABLE_NAME,"row inserted");
            return insert(context,values);
        }

    }

    public static ArrayList<Map<String,String>> getAll(Context context){

        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                Map<String,String> map  = new HashMap<String, String>();

                map.put(ALIAS,cursor.getString(cursor.getColumnIndexOrThrow(ALIAS)));
                map.put(ID_CTL_FAVORITOS,cursor.getString(cursor.getColumnIndexOrThrow(ID_CTL_FAVORITOS)));
                map.put(RASTREO,cursor.getString(cursor.getColumnIndexOrThrow(RASTREO)));
                map.put(FECHA_REGISTRO,cursor.getString(cursor.getColumnIndexOrThrow(FECHA_REGISTRO)));
                map.put(CP_DESTINO,cursor.getString(cursor.getColumnIndexOrThrow(CP_DESTINO)));
                map.put(DESTINO,cursor.getString(cursor.getColumnIndexOrThrow(DESTINO)));
                map.put(ESTATUS,cursor.getString(cursor.getColumnIndexOrThrow(ESTATUS)));
                map.put(FECHA_RECOLECCION,cursor.getString(cursor.getColumnIndexOrThrow(FECHA_RECOLECCION)));
                map.put(FECHA_HORA_ENTREGA,cursor.getString(cursor.getColumnIndexOrThrow(FECHA_HORA_ENTREGA)));
                map.put(HISTORIA,cursor.getString(cursor.getColumnIndexOrThrow(HISTORIA)));
                map.put(ORIGEN,cursor.getString(cursor.getColumnIndexOrThrow(ORIGEN)));
                map.put(GUIA,cursor.getString(cursor.getColumnIndexOrThrow(GUIA)));
                map.put(RECIBIO,cursor.getString(cursor.getColumnIndexOrThrow(RECIBIO)));
                map.put(REFERENCIA,cursor.getString(cursor.getColumnIndexOrThrow(REFERENCIA)));
                map.put(SIGNATURE,cursor.getString(cursor.getColumnIndexOrThrow(SIGNATURE)));
                map.put(NOTIFICA,cursor.getString(cursor.getColumnIndexOrThrow(NOTIFICA)));

                list.add(map);
            }
            Log.d("Campos recuperados: ", "" + list.size());
            cursor.close();
            return list;
        }
        return null;
    }

    public static String getIdByWayBill(Context context,String waybill){

        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME,
                null,
                GUIA + "=?",
                new String[] {waybill}, null ,null, null);

        if(cursor != null & cursor.getCount() > 0){
            cursor.moveToFirst();
            String response= cursor.getString(cursor.getColumnIndexOrThrow(ID_CTL_FAVORITOS));
            cursor.close();
            return response;
        }

        else
            return null;
    }

    public static String getReferenceByNoGuia(Context context,String no_guia){

        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME,
                null,
                GUIA + "=?",
                new String[] {no_guia}, null ,null, null);

        if(cursor != null & cursor.getCount() > 0){
            cursor.moveToFirst();
            String response= cursor.getString(cursor.getColumnIndexOrThrow(REFERENCIA));
            cursor.close();
            return response;
        }

        else
            return null;
    }

    public static Map<String, String> getFavoriteByWayBill(Context context, String waybill){
        Map<String, String> map = new HashMap<>();
        Cursor cursor = DataBaseAdapter.getDB(context).query(
                TABLE_NAME,
                null, GUIA + "=?",
                new String[]{waybill}, null ,null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            map.put(ALIAS,cursor.getString(cursor.getColumnIndexOrThrow(ALIAS)));
            map.put(ID_CTL_FAVORITOS,cursor.getString(cursor.getColumnIndexOrThrow(ID_CTL_FAVORITOS)));
            map.put(RASTREO,cursor.getString(cursor.getColumnIndexOrThrow(RASTREO)));
            map.put(CP_DESTINO,cursor.getString(cursor.getColumnIndexOrThrow(CP_DESTINO)));
            map.put(DESTINO,cursor.getString(cursor.getColumnIndexOrThrow(DESTINO)));
            map.put(ESTATUS,cursor.getString(cursor.getColumnIndexOrThrow(ESTATUS)));
            map.put(FECHA_RECOLECCION,cursor.getString(cursor.getColumnIndexOrThrow(FECHA_RECOLECCION)));
            map.put(FECHA_HORA_ENTREGA,cursor.getString(cursor.getColumnIndexOrThrow(FECHA_HORA_ENTREGA)));
            map.put(HISTORIA,cursor.getString(cursor.getColumnIndexOrThrow(HISTORIA)));
            map.put(ORIGEN,cursor.getString(cursor.getColumnIndexOrThrow(ORIGEN)));
            map.put(GUIA,cursor.getString(cursor.getColumnIndexOrThrow(GUIA)));
            map.put(RECIBIO,cursor.getString(cursor.getColumnIndexOrThrow(RECIBIO)));
            map.put(REFERENCIA,cursor.getString(cursor.getColumnIndexOrThrow(REFERENCIA)));
            map.put(SIGNATURE,cursor.getString(cursor.getColumnIndexOrThrow(SIGNATURE)));
            map.put(NOTIFICA,cursor.getString(cursor.getColumnIndexOrThrow(NOTIFICA)));
            cursor.close();
            return map;
        }
        else{
            cursor.close();
            return null;
        }
    }

    public static int delete(Context context, String id) {
        int resp = DataBaseAdapter.getDB(context).delete(TABLE_NAME, ID_CTL_FAVORITOS + "=?" , new String[]{ id });
        //Log.d(TABLE_NAME,"delete resp:"+resp);
        return resp;
    }

}
