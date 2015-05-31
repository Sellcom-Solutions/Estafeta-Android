package database.model;

import android.content.ContentValues;
import android.content.Context;

import database.DataBaseAdapter;

/**
 * Created by rebecalopezmartinez on 28/05/15.
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

    public static long insert(Context context, String idpais, String nombrepais_esp,
                              String nombrepais_ing){
        ContentValues cv = new ContentValues();
        cv.put(IDPAIS, idpais);
        cv.put(NOMBREPAIS_ESP, nombrepais_esp);
        cv.put(NOMBREPAIS_ING, nombrepais_ing);

        return DataBaseAdapter.getDB(context).insert(TABLE_NAME,null,cv);

    }
}
