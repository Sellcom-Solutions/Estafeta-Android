package database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sellcom.apps.tracker_material.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.acl.Permission;


import database.model.Offices;
import database.model.States;

/**
 * Created by rebecalopezmartinez on 27/05/15.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "estafeta.db";

    private static final int VER_1            = 1;  // 1.1
    private static final int DATABASE_VERSION = VER_1;
    public static final int NOT_UPDATE        = -1;
    private final Context mContext;

    private SQLiteDatabase db;


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.i("DataBaseHelper","onCreate");
        this.db=db;

        try {
            Log.d("DataBaseHelper","onCreate favoritos");
            db.execSQL(mContext.getString(R.string.db_ctlfavoritos));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Log.d("DataBaseHelper","onCreate trackdata");
            db.execSQL(mContext.getString(R.string.db_trackdata));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Log.d("DataBaseHelper","onCreate history");
            db.execSQL(mContext.getString(R.string.db_history));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Log.d("DataBaseHelper","onCreate codigos");
            db.execSQL(mContext.getString(R.string.db_codigos));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.execSQL(mContext.getString(R.string.db_estados));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.execSQL(mContext.getString(R.string.db_offices));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.execSQL(mContext.getString(R.string.db_paises));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /*try {
            db.execSQL(mContext.getString(R.string.db_rastreo_tmp));
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
