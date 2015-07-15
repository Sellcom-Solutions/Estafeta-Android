package database.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import database.DataBaseAdapter;

/**
 * Created by juan.guerra on 13/07/2015.
 */
public class Buys {

    public static final String TABLE_NAME = "compra";

    public static final String ID = "id";
    public static final String NOMBRE_REMITENTE = "Nombre_remitente";
    public static final String ORIGEN = "Origen";
    public static final String CPO = "CPO";
    public static final String NOMBRE_DESTINATARIO = "Nombre_destinatario";
    public static final String DESTINO = "Destino";
    public static final String CPD = "CPD";
    public static final String TIPO_SERVICIO = "Tipo_servicio";
    public static final String GARANTIA = "Garantia";
    public static final String COSTO = "Costo";
    public static final String REFERENCIA = "Referencia";
    public static final String DATE = "Fecha";



    public static List<Map<String,String>> getAll(Context context)
    {
        List <Map<String,String>> returned=new ArrayList<>();
        Map<String,String> object;
        Cursor cursor= DataBaseAdapter.getDB(context).query(TABLE_NAME, null, null, null, null, null, null);

        if(cursor.moveToFirst()){
            int index;

            do{
                object=new HashMap<String,String>();
                if (cursor.getColumnIndexOrThrow(ID) != -1)
                    object.put(ID,cursor.getString(cursor.getColumnIndexOrThrow(ID)));
                if (cursor.getColumnIndexOrThrow(NOMBRE_REMITENTE) != -1)
                    object.put(NOMBRE_REMITENTE,cursor.getString(cursor.getColumnIndexOrThrow(NOMBRE_REMITENTE)));
                if (cursor.getColumnIndexOrThrow(ORIGEN) != -1)
                    object.put(ORIGEN,cursor.getString(cursor.getColumnIndexOrThrow(ORIGEN)));
                if (cursor.getColumnIndexOrThrow(CPO) != -1)
                    object.put(CPO,cursor.getString(cursor.getColumnIndexOrThrow(CPO)));
                if (cursor.getColumnIndexOrThrow(NOMBRE_DESTINATARIO) != -1)
                    object.put(NOMBRE_DESTINATARIO,cursor.getString(cursor.getColumnIndexOrThrow(NOMBRE_DESTINATARIO)));
                if (cursor.getColumnIndexOrThrow(DESTINO) != -1)
                    object.put(DESTINO,cursor.getString(cursor.getColumnIndexOrThrow(DESTINO)));
                if (cursor.getColumnIndexOrThrow(CPD) != -1)
                    object.put(CPD,cursor.getString(cursor.getColumnIndexOrThrow(CPD)));
                if (cursor.getColumnIndexOrThrow(TIPO_SERVICIO) != -1)
                    object.put(TIPO_SERVICIO,cursor.getString(cursor.getColumnIndexOrThrow(TIPO_SERVICIO)));
                if (cursor.getColumnIndexOrThrow(GARANTIA) != -1)
                    object.put(GARANTIA,cursor.getString(cursor.getColumnIndexOrThrow(GARANTIA)));
                if (cursor.getColumnIndexOrThrow(COSTO) != -1)
                    object.put(COSTO,cursor.getString(cursor.getColumnIndexOrThrow(COSTO)));
                if (cursor.getColumnIndexOrThrow(REFERENCIA) != -1)
                    object.put(REFERENCIA,cursor.getString(cursor.getColumnIndexOrThrow(REFERENCIA)));
                if (cursor.getColumnIndexOrThrow(DATE) != -1)
                    object.put(DATE,cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
                returned.add(object);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return returned;
    }

    public static void deleteByArguments(Context context,Map<String,String> arguments)
    {
        String selection="";
        String selectionArgs[]=new String[arguments.size()];
        int index=0;
        for(Map.Entry<String,String> entry : arguments.entrySet()) {
            if(index!=0)
                selection=selection + "AND ";
            selectionArgs[index]=entry.getValue();
            selection=selection+entry.getKey()+" = ? ";
            index++;
        }
        DataBaseAdapter.getDB(context).delete(TABLE_NAME, selection, selectionArgs);
    }

    public static long insert(Context context,Map<String,String> values)
    {
        ContentValues cv = new ContentValues();
        for(Map.Entry<String,String> entry : values.entrySet()) {
            cv.put(entry.getKey(),entry.getValue());
        }
        return DataBaseAdapter.getDB(context).insert(TABLE_NAME, null, cv);
    }

    public static List<Map<String,String>> getByArguments(Context context,Map<String,String> arguments)
    {
        List<Map<String,String>> returned=new ArrayList<>();
        Map<String,String> object;
        String selection="";
        String selectionArgs[]=new String[arguments.size()];
        int index=0;
        for(Map.Entry<String,String> entry : arguments.entrySet()) {
            if(index!=0)
                selection=selection + "AND ";
            selectionArgs[index]=entry.getValue();
            selection=selection+entry.getKey()+" = ? ";
            index++;
        }

        Cursor cursor= DataBaseAdapter.getDB(context).query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
        int counter;
        if(cursor.moveToFirst()){

            do{
                object=new HashMap<String,String>();
                if (cursor.getColumnIndexOrThrow(ID) != -1)
                    object.put(ID,cursor.getString(cursor.getColumnIndexOrThrow(ID)));
                if (cursor.getColumnIndexOrThrow(NOMBRE_REMITENTE) != -1)
                    object.put(NOMBRE_REMITENTE,cursor.getString(cursor.getColumnIndexOrThrow(NOMBRE_REMITENTE)));
                if (cursor.getColumnIndexOrThrow(ORIGEN) != -1)
                    object.put(ORIGEN,cursor.getString(cursor.getColumnIndexOrThrow(ORIGEN)));
                if (cursor.getColumnIndexOrThrow(CPO) != -1)
                    object.put(CPO,cursor.getString(cursor.getColumnIndexOrThrow(CPO)));
                if (cursor.getColumnIndexOrThrow(NOMBRE_DESTINATARIO) != -1)
                    object.put(NOMBRE_DESTINATARIO,cursor.getString(cursor.getColumnIndexOrThrow(NOMBRE_DESTINATARIO)));
                if (cursor.getColumnIndexOrThrow(DESTINO) != -1)
                    object.put(DESTINO,cursor.getString(cursor.getColumnIndexOrThrow(DESTINO)));
                if (cursor.getColumnIndexOrThrow(CPD) != -1)
                    object.put(CPD,cursor.getString(cursor.getColumnIndexOrThrow(CPD)));
                if (cursor.getColumnIndexOrThrow(TIPO_SERVICIO) != -1)
                    object.put(TIPO_SERVICIO,cursor.getString(cursor.getColumnIndexOrThrow(TIPO_SERVICIO)));
                if (cursor.getColumnIndexOrThrow(GARANTIA) != -1)
                    object.put(GARANTIA,cursor.getString(cursor.getColumnIndexOrThrow(GARANTIA)));
                if (cursor.getColumnIndexOrThrow(COSTO) != -1)
                    object.put(COSTO,cursor.getString(cursor.getColumnIndexOrThrow(COSTO)));
                if (cursor.getColumnIndexOrThrow(REFERENCIA) != -1)
                    object.put(REFERENCIA,cursor.getString(cursor.getColumnIndexOrThrow(REFERENCIA)));
                if (cursor.getColumnIndexOrThrow(DATE) != -1)
                    object.put(DATE,cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
                returned.add(object);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return returned;
    }

}
