package database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sellcom.apps.tracker_material.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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


        //readInsertsFile("insert_estados.txt");
        readInsertsFile("insert_paises.txt");
        readInsertsFile("insert_codigos.txt");
        readInsertsFile("insert_oficinas.txt");
    }

    private void readInsertsFile(String filename) {
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    mContext.getAssets().open("dbtxt/" + filename)));
            String strLine;
            // Read File Line By Line
            db.beginTransaction();
            while ((strLine = br.readLine()) != null) {
                db.execSQL(strLine);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {// Catch exception if any
            System.err.println("Error: " + e.getMessage());
        } finally{
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
