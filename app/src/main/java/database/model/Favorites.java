package database.model;

import android.content.ContentValues;
import android.content.Context;

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
}
