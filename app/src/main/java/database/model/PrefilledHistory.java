package database.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.estafeta.estafetamovilv1.Utils.DatesHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.DataBaseAdapter;

/**
 * Created by hugo.figueroa on 25/02/16.
 */
public class PrefilledHistory {

    public static final String TABLE_NAME = "prefilled_history";

    public static final String ID_PREFILLED_HISTORY = "id_prefilled_history";
    public static final String NAME_SENDER = "name_sender";
    public static final String ORIGIN_SENDER = "origin_sender";
    public static final String CP_ORIGIN_SENDER = "cp_origin_sender";
    public static final String CITY_SENDER = "city_sender";
    public static final String NAME_ADDRESSEE = "name_addressee";
    public static final String DESTINY_ADDRESSEE = "destiny_addressee";
    public static final String CP_DESTINY_ADDRESSEE = "cp_destiny_addressee";
    public static final String CITY_ADDRESSEE = "city_addressee";
    public static final String IMAGE_QR = "image_qr";
    public static final String TIMESTAMP = "timestamp";


    public static long insert(Context context, String name_sender, String origin_sender, String cp_origin_sender, String city_sender, String name_addressee,
                              String destiny_addressee, String cp_destiny_addressee, String city_addressee, String image_qr){

        if(getCountPrefilledHistoryContacts(context)==10){
            List<Map<String,String>> allPrefilledHistory = getAllInMaps(context);
            String id_old_prefilled_history="";
            Date oldDate = null,date1, date2;
            for(int i=0; i<allPrefilledHistory.size()-1; i++){

                if(oldDate != null){
                    date1 = oldDate;
                }else {
                    id_old_prefilled_history = allPrefilledHistory.get(i).get(ID_PREFILLED_HISTORY);
                    date1 = DatesHelper.stringToDateWithOtherFormat(allPrefilledHistory.get(i).get(TIMESTAMP));
                }
                date2 = DatesHelper.stringToDateWithOtherFormat(allPrefilledHistory.get((i+1)).get(TIMESTAMP));


                if(DatesHelper.comparateDateIsLess(date1,date2))
                    oldDate = date1;
                else {
                    oldDate = date2;
                    id_old_prefilled_history = allPrefilledHistory.get((i+1)).get(ID_PREFILLED_HISTORY);
                    i++;
                }
            }
            try {
                delete(context,id_old_prefilled_history);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        String date = DatesHelper.getStringDateWithoutSSS(new Date());

        ContentValues cv = new ContentValues();
        cv.put(NAME_SENDER,name_sender);
        cv.put(ORIGIN_SENDER,origin_sender);
        cv.put(CP_ORIGIN_SENDER,cp_origin_sender);
        cv.put(CITY_SENDER,city_sender);
        cv.put(NAME_ADDRESSEE,name_addressee);
        cv.put(DESTINY_ADDRESSEE,destiny_addressee);
        cv.put(CP_DESTINY_ADDRESSEE,cp_destiny_addressee);
        cv.put(CITY_ADDRESSEE,city_addressee);
        cv.put(IMAGE_QR,image_qr);
        cv.put(TIMESTAMP,date);


        return DataBaseAdapter.getDB(context).insert(TABLE_NAME,null,cv);
    }



    /**
     * It lets get all the information in this table within the database.
     * @param context
     * @return
     */
    public static ArrayList<Map<String,String>> getAllInMaps(Context context){

        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME, null, null, null, null ,null,TIMESTAMP +" DESC");
        if (cursor != null && cursor.getCount() > 0) {
            ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                Map<String,String> map  = new HashMap<String, String>();
                map.put(ID_PREFILLED_HISTORY,cursor.getString(cursor.getColumnIndexOrThrow(ID_PREFILLED_HISTORY)));
                map.put(NAME_SENDER,cursor.getString(cursor.getColumnIndexOrThrow(NAME_SENDER)));
                map.put(ORIGIN_SENDER,cursor.getString(cursor.getColumnIndexOrThrow(ORIGIN_SENDER)));
                map.put(CP_ORIGIN_SENDER,cursor.getString(cursor.getColumnIndexOrThrow(CP_ORIGIN_SENDER)));
                map.put(CITY_SENDER,cursor.getString(cursor.getColumnIndexOrThrow(CITY_SENDER)));
                map.put(NAME_ADDRESSEE,cursor.getString(cursor.getColumnIndexOrThrow(NAME_ADDRESSEE)));
                map.put(DESTINY_ADDRESSEE,cursor.getString(cursor.getColumnIndexOrThrow(DESTINY_ADDRESSEE)));
                map.put(CP_DESTINY_ADDRESSEE,cursor.getString(cursor.getColumnIndexOrThrow(CP_DESTINY_ADDRESSEE)));
                map.put(CITY_ADDRESSEE,cursor.getString(cursor.getColumnIndexOrThrow(CITY_ADDRESSEE)));
                map.put(IMAGE_QR,cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_QR)));
                map.put(TIMESTAMP,cursor.getString(cursor.getColumnIndexOrThrow(TIMESTAMP)));

                list.add(map);
            }
            //Log.d("Campos recuperados: ", "" + list.size());
            cursor.close();
            return list;
        }
        return null;
    }

    public static int getCountPrefilledHistoryContacts(Context context){

        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME, null, null, null, null, null,null);
        if (cursor != null && cursor.getCount() > 0) {

            int num = cursor.getCount();
            cursor.close();
            return num;

        }
        return 0;
    }

    /**
     * Delete items from the database.
     * @param context
     * @param id
     * @return
     */
    public static int delete(Context context, String id) {
        int resp = DataBaseAdapter.getDB(context).delete(TABLE_NAME, ID_PREFILLED_HISTORY + "=?", new String[]{id});
        //Log.d(TABLE_NAME,"delete resp:"+resp);
        return resp;
    }

}
