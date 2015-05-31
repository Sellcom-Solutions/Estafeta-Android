package database.model;

import android.content.ContentValues;
import android.content.Context;

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




}
