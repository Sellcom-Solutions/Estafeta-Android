package database.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.estafeta.estafetamovilv1.Utils.DatesHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.DataBaseAdapter;

/**
 * Created by hugo.figueroa on 05/02/16.
 */
public class FrequentlyContacts {

    public static final String TABLE_NAME = "frequently_contacts";

    public static final String ID_FREQUENTLY = "id_frequently";
    public static final String NAME_CONTACT = "name_contact";
    public static final String BUSINESS_NAME_CONTACT = "business_name_contact";
    public static final String STREET_CONTACT = "street_contact";
    public static final String NO_EXT_CONTACT = "no_ext_contact";
    public static final String NO_INT_CONTACT = "no_int_contact";
    public static final String CP_CONTACT = "cp_contact";
    public static final String PHONE_CONTACT = "phone_contact";
    public static final String EMAIL_CONTACT = "email_contact";
    public static final String REFERENCE_CONTACT = "reference_contact";
    public static final String NAVE_CONTACT = "nave_contact";
    public static final String PLATFORM_CONTACT = "platform_contact";
    public static final String STATUS_CONTACT = "status_contact";
    public static final String TIMESTAMP = "timestamp";

    public static long insert(Context context, String name_contact, String business_name_contact, String street_contact, String no_ext_contact,
                              String no_int_contact, String cp_contact, String phone_contact, String email_contact, String reference_contact,
                              String nave_contact, String platform_contact){

        if(getCountFrequentlyContacts(context)==10){
            List<Map<String,String>> allFrequentlyContacts = getAllFrequentlyContactsNoFavorites(context);
            String id_old_frequently_contact="";
            Date oldDate = null,date1, date2;
            for(int i=0; i<allFrequentlyContacts.size()-1; i++){

                if(oldDate != null){
                    date1 = oldDate;
                }else {
                    id_old_frequently_contact = allFrequentlyContacts.get(i).get(ID_FREQUENTLY);
                    date1 = DatesHelper.stringToDate(allFrequentlyContacts.get(i).get(TIMESTAMP));
                }
                date2 = DatesHelper.stringToDate(allFrequentlyContacts.get((i+1)).get(TIMESTAMP));



                if(DatesHelper.comparateDateIsLess(date1,date2))
                    oldDate = date1;
                else {
                    oldDate = date2;
                    id_old_frequently_contact = allFrequentlyContacts.get((i+1)).get(ID_FREQUENTLY);
                    i++;
                }
            }
            try {
                delete(context,id_old_frequently_contact);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        String date = DatesHelper.getStringDate(new Date());

        ContentValues cv = new ContentValues();
        cv.put(NAME_CONTACT,name_contact);
        cv.put(BUSINESS_NAME_CONTACT,business_name_contact);
        cv.put(STREET_CONTACT,street_contact);
        cv.put(NO_EXT_CONTACT,no_ext_contact);
        cv.put(NO_INT_CONTACT,no_int_contact);
        cv.put(CP_CONTACT,cp_contact);
        cv.put(PHONE_CONTACT,phone_contact);
        cv.put(EMAIL_CONTACT,email_contact);
        cv.put(REFERENCE_CONTACT,reference_contact);
        cv.put(NAVE_CONTACT,nave_contact);
        cv.put(PLATFORM_CONTACT,platform_contact);
        cv.put(TIMESTAMP,date);
        cv.put(STATUS_CONTACT,"FALSE");


        return DataBaseAdapter.getDB(context).insert(TABLE_NAME,null,cv);
    }

    /**
     * It lets get all the information in this table within the database.
     * @param context
     * @return
     */
    public static ArrayList<Map<String,String>> getAllInMaps(Context context){

        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME, null, null, null, null ,null, null);
        if (cursor != null && cursor.getCount() > 0) {
            ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                Map<String,String> map  = new HashMap<String, String>();
                map.put(ID_FREQUENTLY,cursor.getString(cursor.getColumnIndexOrThrow(ID_FREQUENTLY)));
                map.put(NAME_CONTACT,cursor.getString(cursor.getColumnIndexOrThrow(NAME_CONTACT)));
                map.put(BUSINESS_NAME_CONTACT,cursor.getString(cursor.getColumnIndexOrThrow(BUSINESS_NAME_CONTACT)));
                map.put(STREET_CONTACT,cursor.getString(cursor.getColumnIndexOrThrow(STREET_CONTACT)));
                map.put(NO_EXT_CONTACT,cursor.getString(cursor.getColumnIndexOrThrow(NO_EXT_CONTACT)));
                map.put(NO_INT_CONTACT,cursor.getString(cursor.getColumnIndexOrThrow(NO_INT_CONTACT)));
                map.put(CP_CONTACT,cursor.getString(cursor.getColumnIndexOrThrow(CP_CONTACT)));
                map.put(PHONE_CONTACT,cursor.getString(cursor.getColumnIndexOrThrow(PHONE_CONTACT)));
                map.put(EMAIL_CONTACT,cursor.getString(cursor.getColumnIndexOrThrow(EMAIL_CONTACT)));
                map.put(REFERENCE_CONTACT,cursor.getString(cursor.getColumnIndexOrThrow(REFERENCE_CONTACT)));
                map.put(NAVE_CONTACT,cursor.getString(cursor.getColumnIndexOrThrow(NAVE_CONTACT)));
                map.put(PLATFORM_CONTACT,cursor.getString(cursor.getColumnIndexOrThrow(PLATFORM_CONTACT)));
                map.put(TIMESTAMP,cursor.getString(cursor.getColumnIndexOrThrow(TIMESTAMP)));
                map.put(STATUS_CONTACT,cursor.getString(cursor.getColumnIndexOrThrow(STATUS_CONTACT)));

                list.add(map);
            }
            //Log.d("Campos recuperados: ", "" + list.size());
            cursor.close();
            return list;
        }
        return null;
    }

    public static ArrayList<Map<String,String>> getAllFrequentlyContactsNoFavorites(Context context){

        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME, null, STATUS_CONTACT + " =? ", new String[]{ "FALSE"}, null ,null, null);
        if (cursor != null && cursor.getCount() > 0) {
            ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                Map<String,String> map  = new HashMap<String, String>();
                map.put(ID_FREQUENTLY,cursor.getString(cursor.getColumnIndexOrThrow(ID_FREQUENTLY)));
                map.put(NAME_CONTACT,cursor.getString(cursor.getColumnIndexOrThrow(NAME_CONTACT)));
                map.put(BUSINESS_NAME_CONTACT,cursor.getString(cursor.getColumnIndexOrThrow(BUSINESS_NAME_CONTACT)));
                map.put(STREET_CONTACT,cursor.getString(cursor.getColumnIndexOrThrow(STREET_CONTACT)));
                map.put(NO_EXT_CONTACT,cursor.getString(cursor.getColumnIndexOrThrow(NO_EXT_CONTACT)));
                map.put(NO_INT_CONTACT,cursor.getString(cursor.getColumnIndexOrThrow(NO_INT_CONTACT)));
                map.put(CP_CONTACT,cursor.getString(cursor.getColumnIndexOrThrow(CP_CONTACT)));
                map.put(PHONE_CONTACT,cursor.getString(cursor.getColumnIndexOrThrow(PHONE_CONTACT)));
                map.put(EMAIL_CONTACT,cursor.getString(cursor.getColumnIndexOrThrow(EMAIL_CONTACT)));
                map.put(REFERENCE_CONTACT,cursor.getString(cursor.getColumnIndexOrThrow(REFERENCE_CONTACT)));
                map.put(NAVE_CONTACT,cursor.getString(cursor.getColumnIndexOrThrow(NAVE_CONTACT)));
                map.put(PLATFORM_CONTACT,cursor.getString(cursor.getColumnIndexOrThrow(PLATFORM_CONTACT)));
                map.put(TIMESTAMP,cursor.getString(cursor.getColumnIndexOrThrow(TIMESTAMP)));
                map.put(STATUS_CONTACT,cursor.getString(cursor.getColumnIndexOrThrow(STATUS_CONTACT)));

                list.add(map);
            }
            //Log.d("Campos recuperados: ", "" + list.size());
            cursor.close();
            return list;
        }
        return null;
    }

    public static long updateStatusById(Context context, String id_frequently, boolean status){

        ContentValues cv = new ContentValues();
        if(status) {
            cv.put(STATUS_CONTACT, "TRUE");
        }else{
            cv.put(STATUS_CONTACT, "FALSE");
        }

        long response = DataBaseAdapter.getDB(context).update(TABLE_NAME, cv, ID_FREQUENTLY + "=?", new String[]{id_frequently});

        Log.d("UpdateStatusFrequently", "" + response);

        return response;
    }



    public static int getCountFrequentlyContactsFavorites(Context context){

        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME, null, STATUS_CONTACT + " =? ", new String[]{ "TRUE"}, null, null,null);
        if (cursor != null && cursor.getCount() > 0) {

            int num = cursor.getCount();
            cursor.close();
            return num;

        }
        return 0;
    }

    public static int getCountFrequentlyContacts(Context context){

        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME, null, null, null, null, null,null);
        if (cursor != null && cursor.getCount() > 0) {

            int num = cursor.getCount();
            cursor.close();
            return num;

        }
        return 0;
    }

    public static boolean checkIfExistContactInDB(Context context,String name_contact, String business_name_contact, String street_contact, String no_ext_contact,
                                              String no_int_contact, String cp_contact, String phone_contact, String email_contact, String reference_contact,
                                              String nave_contact, String platform_contact){

        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE_NAME, null, NAME_CONTACT + "=? and "+ BUSINESS_NAME_CONTACT+ "=? and "+STREET_CONTACT+ "=? and "
                +NO_EXT_CONTACT+ "=? and "+NO_INT_CONTACT+ "=? and "+CP_CONTACT+ "=? and "+PHONE_CONTACT+ "=? and "+EMAIL_CONTACT+ "=? and "+REFERENCE_CONTACT+ "=? and "
                +NAVE_CONTACT+ "=? and "+PLATFORM_CONTACT+ "=?", new String[]{ name_contact,business_name_contact,street_contact,no_ext_contact,no_int_contact,
        cp_contact,phone_contact,email_contact,reference_contact,nave_contact,platform_contact},null,null,null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true;

        }
        return false;
    }

    /**
     * Delete items from the database.
     * @param context
     * @param id
     * @return
     */
    public static int delete(Context context, String id) {
        int resp = DataBaseAdapter.getDB(context).delete(TABLE_NAME, ID_FREQUENTLY + "=?", new String[]{id});
        //Log.d(TABLE_NAME,"delete resp:"+resp);
        return resp;
    }

}
