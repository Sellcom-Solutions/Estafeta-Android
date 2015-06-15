package database.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.security.acl.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import database.DataBaseAdapter;

/**
 * Created by rebecalopezmartinez on 28/05/15.
 */
public class Offices {
    public static final String TABLE_NAME = "offices";

    public static final String ID_OFFICE = "id_office";
    public static final String ENT = "ent";
    public static final String OPT = "opt";
    public static final String ESTADO = "estado";
    public static final String CALLE1 = "calle1";
    public static final String CALLE2 = "calle2";
    public static final String CIUDAD = "ciudad";
    public static final String CODIGO_POSTAL = "codigo_postal";
    public static final String COLONIA = "colonia";
    public static final String CORREO = "correo";
    public static final String ENTREGA_OFICINA = "entrega_oficina";
    public static final String EXT1 = "ext1";
    public static final String EXT2 = "ext2";
    public static final String HORARIO_ATENCION = "horario_atencion";
    public static final String HORARIO_COMIDA = "horario_comida";
    public static final String HORARIO_EXT = "horario_ext";
    public static final String HORARIO_RECOL = "horario_recol";
    public static final String HORARIO_SABATINO = "horario_sabatino";
    public static final String ID_CATALOGO = "idcatalogo";
    public static final String LATITUD = "latitud";
    public static final String LONGITUD = "longitud";
    public static final String NO_OFICINA = "no_oficina";
    public static final String NOMBRE = "nombre";
    public static final String TELEFONO1 = "telefono1";
    public static final String TELEFONO2 = "telefono2";
    public static final String TIPO_OFICINA = "tipo_oficina";
    public static final String TIPOS_PAGO = "tipos_pago";
    public static final String VERSION = "version";
    public static final String CIUDAD_N = "ciudad_n";
    public static final String COLONIA_N = "colonia_n";

    private int id_office;
    private String ent;
    private String opt;
    private String estado;
    private String calle1;
    private String calle2;
    private String ciudad;
    private String codigo_postal;
    private String colonia;
    private String correo;
    private String entrega_oficina;
    private String ext1;
    private String ext2;
    private String horario_atencion;
    private String horario_comida;
    private String horario_ext;
    private String horario_recol;
    private String horario_sabatino;
    private String idcatalogo;
    private String latitud;
    private String longitud;
    private String no_oficina;
    private String nombre;
    private String telefono1;
    private String telefono2;
    private String tipo_oficina;
    private String tipos_pago;
    private String version;
    private String ciudad_n;
    private String colonia_n;

    public Offices(int id_office, String ent, String opt, String estado, String calle1, String calle2,
                   String ciudad, String codigo_postal, String colonia, String correo, String entrega_oficina,
                   String ext1, String ext2, String horario_atencion, String horario_comida,
                   String horario_ext, String horario_recol, String horario_sabatino, String idcatalogo,
                   String latitud, String longitud, String no_oficina, String nombre, String telefono1,
                   String telefono2, String tipo_oficina, String version, String tipos_pago, String ciudad_n,
                   String colonia_n) {

        this.id_office = id_office;
        this.ent = ent;
        this.opt = opt;
        this.estado = estado;
        this.calle1 = calle1;
        this.calle2 = calle2;
        this.ciudad = ciudad;
        this.codigo_postal = codigo_postal;
        this.colonia = colonia;
        this.correo = correo;
        this.entrega_oficina = entrega_oficina;
        this.ext1 = ext1;
        this.ext2 = ext2;
        this.horario_atencion = horario_atencion;
        this.horario_comida = horario_comida;
        this.horario_ext = horario_ext;
        this.horario_recol = horario_recol;
        this.horario_sabatino = horario_sabatino;
        this.idcatalogo = idcatalogo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.no_oficina = no_oficina;
        this.nombre = nombre;
        this.telefono1 = telefono1;
        this.telefono2 = telefono2;
        this.tipo_oficina = tipo_oficina;
        this.version = version;
        this.tipos_pago = tipos_pago;
        this.ciudad_n = ciudad_n;
        this.colonia_n = colonia_n;
    }

    public static long insert(Context context, String ent, String opt, String estado, String calle1, String calle2,
                              String ciudad, String codigo_postal, String colonia, String correo, String entrega_oficina,
                              String ext1, String ext2, String horario_atencion, String horario_comida,
                              String horario_ext, String horario_recol, String horario_sabatino, String idcatalogo,
                              String latitud, String longitud, String no_oficina, String nombre, String telefono1,
                              String telefono2, String tipo_oficina, String version, String tipos_pago, String ciudad_n,
                              String colonia_n){

        ContentValues cv = new ContentValues();
        cv.put(ENT, ent);
        cv.put(OPT, opt);

        cv.put(CALLE1, calle1);
        cv.put(CALLE2, calle2);
        cv.put(CIUDAD, ciudad);
        cv.put(CODIGO_POSTAL, codigo_postal);
        cv.put(COLONIA, colonia);
        cv.put(CORREO, correo);
        cv.put(ENTREGA_OFICINA, entrega_oficina);
        cv.put(EXT1, ext1);
        cv.put(EXT2, ext2);
        cv.put(HORARIO_ATENCION, horario_atencion);
        cv.put(HORARIO_COMIDA, horario_comida);
        cv.put(HORARIO_EXT, horario_ext);
        cv.put(HORARIO_RECOL, horario_recol);
        cv.put(HORARIO_SABATINO, horario_sabatino);
        cv.put(ID_CATALOGO, idcatalogo);
        cv.put(LATITUD, latitud.trim());
        cv.put(LONGITUD, longitud.trim());
        cv.put(NO_OFICINA, no_oficina);
        cv.put(NOMBRE, nombre);
        cv.put(TELEFONO1, telefono1);
        cv.put(TELEFONO2, telefono2);
        cv.put(TIPO_OFICINA, tipo_oficina);
        cv.put(TIPOS_PAGO, tipos_pago);
        cv.put(VERSION, version);
        cv.put(CIUDAD_N, ciudad_n);
        cv.put(COLONIA_N, colonia_n);

        return DataBaseAdapter.getDB(context).insert(TABLE_NAME,null,cv);
    }

    public static long updateOffices(Context context, ArrayList<Map<String, String>> values){
        ContentValues cv = new ContentValues();
        long aux=0;

        for(int i = 0; i <values.size(); i++) {
            Map<String, String> item = new HashMap<>();
            item = values.get(i);

            //Log.d(TABLE_NAME,"status"+item.get("status"));
            if (item.get("status").equals("true")){
                int edo = States.getStateNumberByName(context, item.get("estado_name"));

                if (edo == 99) {
                    Log.d(TABLE_NAME, "error recuperando estado");
                } else
                    cv.put(ESTADO, edo);

                cv.put(CALLE1, item.get("calle1"));
                cv.put(CALLE2, item.get("calle2"));
                cv.put(CIUDAD, item.get("ciudad"));
                cv.put(CODIGO_POSTAL, item.get("codigoPostal"));
                cv.put(COLONIA, item.get("colonia"));
                cv.put(CORREO, item.get("correoE"));
                cv.put(ENTREGA_OFICINA, item.get("entregaOcurre"));
                cv.put(EXT1, item.get("ext1"));
                cv.put(EXT2, item.get("ext2"));
                cv.put(HORARIO_ATENCION, item.get("horariosAtencion"));
                cv.put(HORARIO_COMIDA, item.get("horarioComida"));
                cv.put(HORARIO_EXT, item.get("horarioExtendido"));

                cv.put(HORARIO_SABATINO, item.get("horarioSabatino"));
                cv.put(LATITUD, item.get("latitud").trim());
                cv.put(LONGITUD, item.get("longitud".trim()));
                cv.put(NO_OFICINA, item.get("idOficina"));
                cv.put(NOMBRE, item.get("nombreOficina"));
                cv.put(TELEFONO1, item.get("telefono1"));
                cv.put(TELEFONO2, item.get("telefono2"));
                cv.put(TIPO_OFICINA, item.get("idTipoOficina"));
                cv.put(TIPOS_PAGO, item.get("tiposPago"));
                cv.put(VERSION, item.get("ultimaAct"));

                String auxVersion = getVersionByOffice(context, item.get("idOficina"));
                if (auxVersion != null) {
                    if (auxVersion.equals(item.get("ultimaAct"))) {
                        Log.d(TABLE_NAME, "oficina actualizada");
                    } else {
                        try {
                            aux = DataBaseAdapter.getDB(context).update(TABLE_NAME, cv, NO_OFICINA + "=?", new String[]{item.get("idOficina")});
                            Log.d(TABLE_NAME, "actualizar aux: " + aux);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d(TABLE_NAME,"No actualizado");
                        }
                    }
                }else {
                    try {
                        aux = DataBaseAdapter.getDB(context).insert(TABLE_NAME, null,cv);
                        Log.d(TABLE_NAME, "insertar aux: " + aux);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TABLE_NAME,"No insertado");
                    }
                }
            }
            else{
                String auxId = item.get("idOficina");
                //Log.d(TABLE_NAME,"idOficina: "+auxId);
                delete(context,auxId);
            }
        }
        Log.d(TABLE_NAME,"Oficinas actualizadas");
      return aux;

    }

    public static String getVersion(Context context) {
        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String version = cursor.getString(cursor.getColumnIndexOrThrow(VERSION));
            cursor.close();
            return version;
        }
        return null;
    }

    public static String getVersionByOffice(Context context, String no_oficina){

        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME,
                new String[] {NO_OFICINA,VERSION},
                NO_OFICINA + "=?",
                new String[] {no_oficina}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String version = cursor.getString(cursor.getColumnIndexOrThrow(VERSION));
            cursor.close();
            return version;
        }
        return null;

    }


    public static ArrayList<Map<String,String>> getAllInMaps(Context context){

        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME, null, null, null, null ,null, null);
        if (cursor != null && cursor.getCount() > 0) {
            ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                Map<String,String> map  = new HashMap<String, String>();

                map.put(ENT,cursor.getString(cursor.getColumnIndexOrThrow(ENT)));
                map.put(OPT,cursor.getString(cursor.getColumnIndexOrThrow(OPT)));
                map.put(ESTADO,cursor.getString(cursor.getColumnIndexOrThrow(ESTADO)));
                map.put(CALLE1,cursor.getString(cursor.getColumnIndexOrThrow(CALLE1)));
                map.put(CALLE2,cursor.getString(cursor.getColumnIndexOrThrow(CALLE2)));
                map.put(CIUDAD,cursor.getString(cursor.getColumnIndexOrThrow(CIUDAD)));
                map.put(CODIGO_POSTAL,cursor.getString(cursor.getColumnIndexOrThrow(CODIGO_POSTAL)));
                map.put(COLONIA,cursor.getString(cursor.getColumnIndexOrThrow(COLONIA)));
                map.put(CORREO,cursor.getString(cursor.getColumnIndexOrThrow(CORREO)));
                map.put(ENTREGA_OFICINA,cursor.getString(cursor.getColumnIndexOrThrow(ENTREGA_OFICINA)));
                map.put(EXT1,cursor.getString(cursor.getColumnIndexOrThrow(EXT1)));
                map.put(EXT2,cursor.getString(cursor.getColumnIndexOrThrow(EXT2)));
                map.put(HORARIO_ATENCION,cursor.getString(cursor.getColumnIndexOrThrow(HORARIO_ATENCION)));
                map.put(HORARIO_COMIDA,cursor.getString(cursor.getColumnIndexOrThrow(HORARIO_COMIDA)));
                map.put(HORARIO_EXT,cursor.getString(cursor.getColumnIndexOrThrow(HORARIO_EXT)));
                map.put(HORARIO_RECOL,cursor.getString(cursor.getColumnIndexOrThrow(HORARIO_RECOL)));
                map.put(HORARIO_SABATINO,cursor.getString(cursor.getColumnIndexOrThrow(HORARIO_SABATINO)));
                map.put(ID_CATALOGO,cursor.getString(cursor.getColumnIndexOrThrow(ID_CATALOGO)));
                map.put(LATITUD,cursor.getString(cursor.getColumnIndexOrThrow(LATITUD)));
                map.put(LONGITUD,cursor.getString(cursor.getColumnIndexOrThrow(LONGITUD)));
                map.put(NO_OFICINA,cursor.getString(cursor.getColumnIndexOrThrow(NO_OFICINA)));
                map.put(NOMBRE,cursor.getString(cursor.getColumnIndexOrThrow(NOMBRE)));
                map.put(TELEFONO1,cursor.getString(cursor.getColumnIndexOrThrow(TELEFONO1)));
                map.put(TELEFONO2,cursor.getString(cursor.getColumnIndexOrThrow(TELEFONO2)));
                map.put(TIPO_OFICINA,cursor.getString(cursor.getColumnIndexOrThrow(TIPO_OFICINA)));
                map.put(TIPOS_PAGO,cursor.getString(cursor.getColumnIndexOrThrow(TIPOS_PAGO)));
                map.put(VERSION,cursor.getString(cursor.getColumnIndexOrThrow(VERSION)));
                map.put(CIUDAD_N,cursor.getString(cursor.getColumnIndexOrThrow(CIUDAD_N)));
                map.put(COLONIA_N,cursor.getString(cursor.getColumnIndexOrThrow(COLONIA_N)));

                list.add(map);
            }
            //Log.d("Campos recuperados: ", "" + list.size());
            cursor.close();
            return list;
        }
        return null;
    }


    public static ArrayList<Map<String,String>> getOfficesByCity(Context context, String sql, String[] args){
        Cursor cursor = DataBaseAdapter.getDB(context).rawQuery(sql,args);
       /* Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME, null, "estado LIKE ? AND ciudad_n LIKE ? AND colonia_n LIKE ? AND codigo_postal LIKE ?",
                new String[] { "%" +estado+ "%","%" +ciudad_n+ "%","%" +colonia_n+ "%",codigo_postal}, null, null, null);*/

        if (cursor != null && cursor.getCount() > 0) {
            ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                Map<String,String> map  = new HashMap<String, String>();

                map.put(ENT,cursor.getString(cursor.getColumnIndexOrThrow(ENT)));
                map.put(OPT,cursor.getString(cursor.getColumnIndexOrThrow(OPT)));
                map.put(ESTADO,cursor.getString(cursor.getColumnIndexOrThrow(ESTADO)));
                map.put(CALLE1,cursor.getString(cursor.getColumnIndexOrThrow(CALLE1)));
                map.put(CALLE2,cursor.getString(cursor.getColumnIndexOrThrow(CALLE2)));
                map.put(CIUDAD,cursor.getString(cursor.getColumnIndexOrThrow(CIUDAD)));
                map.put(CODIGO_POSTAL,cursor.getString(cursor.getColumnIndexOrThrow(CODIGO_POSTAL)));
                map.put(COLONIA,cursor.getString(cursor.getColumnIndexOrThrow(COLONIA)));
                map.put(CORREO,cursor.getString(cursor.getColumnIndexOrThrow(CORREO)));
                map.put(ENTREGA_OFICINA,cursor.getString(cursor.getColumnIndexOrThrow(ENTREGA_OFICINA)));
                map.put(EXT1,cursor.getString(cursor.getColumnIndexOrThrow(EXT1)));
                map.put(EXT2,cursor.getString(cursor.getColumnIndexOrThrow(EXT2)));
                map.put(HORARIO_ATENCION,cursor.getString(cursor.getColumnIndexOrThrow(HORARIO_ATENCION)));
                map.put(HORARIO_COMIDA,cursor.getString(cursor.getColumnIndexOrThrow(HORARIO_COMIDA)));
                map.put(HORARIO_EXT,cursor.getString(cursor.getColumnIndexOrThrow(HORARIO_EXT)));
                map.put(HORARIO_RECOL,cursor.getString(cursor.getColumnIndexOrThrow(HORARIO_RECOL)));
                map.put(HORARIO_SABATINO,cursor.getString(cursor.getColumnIndexOrThrow(HORARIO_SABATINO)));
                map.put(ID_CATALOGO,cursor.getString(cursor.getColumnIndexOrThrow(ID_CATALOGO)));
                map.put(LATITUD,cursor.getString(cursor.getColumnIndexOrThrow(LATITUD)));
                map.put(LONGITUD,cursor.getString(cursor.getColumnIndexOrThrow(LONGITUD)));
                map.put(NO_OFICINA,cursor.getString(cursor.getColumnIndexOrThrow(NO_OFICINA)));
                map.put(NOMBRE,cursor.getString(cursor.getColumnIndexOrThrow(NOMBRE)));
                map.put(TELEFONO1,cursor.getString(cursor.getColumnIndexOrThrow(TELEFONO1)));
                map.put(TELEFONO2,cursor.getString(cursor.getColumnIndexOrThrow(TELEFONO2)));
                map.put(TIPO_OFICINA,cursor.getString(cursor.getColumnIndexOrThrow(TIPO_OFICINA)));
                map.put(TIPOS_PAGO,cursor.getString(cursor.getColumnIndexOrThrow(TIPOS_PAGO)));
                map.put(VERSION,cursor.getString(cursor.getColumnIndexOrThrow(VERSION)));
                map.put(CIUDAD_N,cursor.getString(cursor.getColumnIndexOrThrow(CIUDAD_N)));
                map.put(COLONIA_N,cursor.getString(cursor.getColumnIndexOrThrow(COLONIA_N)));

                list.add(map);
            }
            Log.d("Campos recuperados: ", "" + list.size());
            cursor.close();
            return list;
        }
        return null;
    }


    public static void removeAll(Context context){
        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME, null, null, null, null ,null, NO_OFICINA);
        if (cursor != null && cursor.getCount() > 0) {

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String id              = cursor.getString(cursor.getColumnIndexOrThrow(NO_OFICINA));
                Offices.delete(context, id);
            }
        }
        cursor.close();
    }

    public static int delete(Context context, String id) {
        int resp = DataBaseAdapter.getDB(context).delete(TABLE_NAME, NO_OFICINA + "=?" , new String[]{ id });
        //Log.d(TABLE_NAME,"delete resp:"+resp);
        return resp;
    }

}
