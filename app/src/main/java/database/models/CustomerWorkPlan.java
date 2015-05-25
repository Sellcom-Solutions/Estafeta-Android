package database.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import database.DataBaseAdapter;

/**
 * Created by hugo.figueroa on 19/01/15.
 *
 */
public class CustomerWorkPlan extends Table{

    public static final String TABLE = "customer_workplan";

    public static final String ID = "id";
    public static final String CUSTOMER_NAME = "customer_name";
    public static final String CP = "cp";
    public static final String COUNTRY = "country";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String ADDRESS = "address";
    public static final String WORKPLAN_DATE = "workplan_date";
    public static final String OPENING_TIME = "opening_time";
    public static final String CLOSING_TIME = "closing_time";

    private int id;
    private String customerName;
    private int cp;
    private String country;
    private String city;
    private String state;
    private String address;
    private String workplanDate;
    private String openingTime;
    private String closingTime;

    public CustomerWorkPlan(int id, String customerName, int cp, String country, String city, String state, String address, String workplanDate, String openingTime, String closingTime){
        this.id = id;
        this.customerName = customerName;
        this.cp = cp;
        this. country = country;
        this.city = city;
        this.state = state;
        this.address = address;
        this.workplanDate = workplanDate;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public static long insert(Context context, String customerName, int cp, String country, String city, String state, String address, String workplanDate, String openingTime, String closingTime) {
        ContentValues cv = new ContentValues();
        cv.put(CUSTOMER_NAME, customerName);
        cv.put(CP, cp);
        cv.put(COUNTRY, country);
        cv.put(CITY, city);
        cv.put(STATE, state);
        cv.put(ADDRESS, address);
        cv.put(WORKPLAN_DATE, workplanDate);
        cv.put(OPENING_TIME, openingTime);
        cv.put(CLOSING_TIME, closingTime);

        return DataBaseAdapter.getDB(context).insert(TABLE, null, cv);
    }

    public int getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getCp() {
        return cp;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getAddress() {
        return address;
    }

    public String getWorkplanDate() {
        return workplanDate;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public static CustomerWorkPlan getCustomerWorkPlan(Context context, int id) {

        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE, null, ID + "=" + id, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {

            String customerName = cursor.getString(cursor.getColumnIndexOrThrow(CUSTOMER_NAME));
            int cp = cursor.getInt(cursor.getColumnIndexOrThrow(CP));
            String country = cursor.getString(cursor.getColumnIndexOrThrow(COUNTRY));
            String city = cursor.getString(cursor.getColumnIndexOrThrow(CITY));
            String state = cursor.getString(cursor.getColumnIndexOrThrow(STATE));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(ADDRESS));
            String workplanDate = cursor.getString(cursor.getColumnIndexOrThrow(WORKPLAN_DATE));
            String openingTime = cursor.getString(cursor.getColumnIndexOrThrow(OPENING_TIME));
            String closingTime = cursor.getString(cursor.getColumnIndexOrThrow(CLOSING_TIME));

            cursor.close();

            return new CustomerWorkPlan(id, customerName, cp, country, city, state, address, workplanDate, openingTime, closingTime);
        }
        return null;
    }

}
