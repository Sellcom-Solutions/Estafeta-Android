package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by rebecalopezmartinez on 28/05/15.
 * Open and makes a new instance of the database.
 */
public class DataBaseAdapter {

    private static DataBaseHelper mDbHelper = null;
    private static SQLiteDatabase mDb = null;

    public static SQLiteDatabase getDB(Context context) {
        if (mDb == null)
            openDB(context);
        return mDb;
    }

    public static boolean openDB(Context context) {
        if (mDbHelper != null)
            mDbHelper.close();

        mDbHelper = new DataBaseHelper(context);

        try {
            mDb = mDbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


}
