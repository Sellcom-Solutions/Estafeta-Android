package database.model;

import android.content.ContentValues;
import android.content.Context;

import database.DataBaseAdapter;

/**
 * Created by rebecalopezmartinez on 28/05/15.
 */
public class History {
    public static final String TABLE_NAME = "history";

    public static final String ID_HISTORY = "id_history";
    public static final String EVENT_DATE = "evnt_date";
    public static final String EVENT_ID = "evnt_id";
    public static final String EVENT_DESC_SPA = "evnt_desc_spa";
    public static final String EVENT_DESC_ENG = "evnt_desc_eng";
    public static final String EVENT_PLACE_ACRONYM = "evnt_place_acronym";
    public static final String EVENT_PLACE_NAME = "evnt_place_name";
    public static final String EXCEPT_CODE = "except_code";
    public static final String EXCEPT_DESCSPA = "except_descspa";
    public static final String EXCEPT_DESCENG = "except_desceng";
    public static final String EXCEPT_DETAILS = "except_details";
    public static final String ID_TRACKDATA = "id_trackdata";

    private int id_history;
    private String evnt_date;
    private String evnt_id;
    private String evnt_desc_spa;
    private String evnt_desc_eng;
    private String evnt_place_acronym;
    private String evnt_place_name;
    private String except_code;
    private String except_descspa;
    private String except_desceng;
    private String except_details;
    private int id_trackdata;

    public History(int id_history, String evnt_date, String evnt_id, String evnt_desc_spa,
                   String evnt_desc_eng, String evnt_place_acronym, String evnt_place_name,
                   String except_code, String except_descspa, String except_desceng,
                   String except_details, int id_trackdata) {

        this.id_history = id_history;
        this.evnt_date = evnt_date;
        this.evnt_id = evnt_id;
        this.evnt_desc_spa = evnt_desc_spa;
        this.evnt_desc_eng = evnt_desc_eng;
        this.evnt_place_acronym = evnt_place_acronym;
        this.evnt_place_name = evnt_place_name;
        this.except_code = except_code;
        this.except_descspa = except_descspa;
        this.except_desceng = except_desceng;
        this.except_details = except_details;
        this.id_trackdata = id_trackdata;
    }

    public static long insert(Context context,String evnt_date, String evnt_id, String evnt_desc_spa,
                              String evnt_desc_eng, String evnt_place_acronym, String evnt_place_name,
                              String except_code, String except_descspa, String except_desceng,
                              String except_details, int id_trackdata){
        ContentValues cv = new ContentValues();
        cv.put(EVENT_DATE, evnt_date);
        cv.put(EVENT_ID, evnt_id);
        cv.put(EVENT_DESC_SPA, evnt_desc_spa);
        cv.put(EVENT_DESC_ENG, evnt_desc_eng);
        cv.put(EVENT_PLACE_ACRONYM,evnt_place_acronym);
        cv.put(EVENT_PLACE_NAME, evnt_place_name);
        cv.put(EXCEPT_CODE, except_code);
        cv.put(EXCEPT_DESCSPA, except_descspa);
        cv.put(EXCEPT_DESCENG, except_desceng);
        cv.put(EXCEPT_DETAILS, except_details);
        cv.put(ID_TRACKDATA, id_trackdata);

        return DataBaseAdapter.getDB(context).insert(TABLE_NAME,null,cv);

    }
}
