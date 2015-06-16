package database.model;

import android.content.ContentValues;
import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import database.DataBaseAdapter;

/**
 * Created by rebecalopezmartinez on 28/05/15.
 */
public class History {
    public static final String TABLE_NAME = "history";

    public static final String ID_HISTORY = "id_history";
    public static final String ADD_DATE = "add_date";
    public static final String CODE_NUMBER = "code_number";
    public static final String MOV_COMS = "mov_coms";
    public static final String MOV_DATE = "mov_date";
    public static final String MOV_PLACE = "mov_place";

    public static long insertMap(Context context, Map<String, String> values){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String date = df.format(Calendar.getInstance().getTime());

        ContentValues cv = new ContentValues();
        cv.put(ADD_DATE,date);
        cv.put(CODE_NUMBER, values.get("shortWayBillId"));
        cv.put(MOV_COMS, values.get(""));
        cv.put(MOV_DATE, values.get("H_eventDateTime"));
        cv.put(MOV_PLACE, values.get("H_eventPlaceName"));

        return DataBaseAdapter.getDB(context).insert(TABLE_NAME,null,cv);
    }


}
