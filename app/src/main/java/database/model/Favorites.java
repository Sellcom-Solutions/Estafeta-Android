package database.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import database.DataBaseAdapter;

/**
 * Created by rebecalopezmartinez on 28/05/15.
 */
public class Favorites {
    public static final String TABLE_NAME = "ctl_favoritos";

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



    public static long insertMap(Context context, Map<String, String> values){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String date = df.format(Calendar.getInstance().getTime());

        ContentValues cv = new ContentValues();
        cv.put(FECHA_REGISTRO,date);
        cv.put(ALIAS, " ");
        cv.put(RASTREO, values.get("shortWayBillId"));
        cv.put(CP_DESTINO, values.get("DD_zipCode"));
        cv.put(DESTINO, values.get("DD_destinationName"));
        cv.put(ESTATUS, values.get("statusSPA"));
        cv.put(FECHA_RECOLECCION, values.get("PK_pickupDateTime"));
        cv.put(FECHA_HORA_ENTREGA, values.get("DD_deliveryDateTime"));
        cv.put(HISTORIA, values.get("history_id"));
        cv.put(ORIGEN , values.get("PK_originName"));
        cv.put(GUIA, values.get("wayBill"));
        cv.put(RECIBIO, values.get("DD_receiverName"));
        cv.put(REFERENCIA, values.get("CI_reference"));
        cv.put(SIGNATURE, " ");
        cv.put(NOTIFICA,"false");

        return DataBaseAdapter.getDB(context).insert(TABLE_NAME,null,cv);
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

    public static String getIdByWayBill(Context context,String waybill){

        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME,
                new String[] {ID_CTL_FAVORITOS,GUIA},
                GUIA + "=?",
                new String[] {waybill}, null ,null, null);
        if(cursor != null){
            cursor.moveToFirst();
            String response= cursor.getString(cursor.getColumnIndexOrThrow(ID_CTL_FAVORITOS));
            return response;
        }

        else
            return null;
    }
}
