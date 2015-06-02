package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import database.model.Offices;
import database.model.States;

/**
 * Created by rebecalopezmartinez on 02/06/15.
 */
public class DataBaseManager {

    public static DataBaseManager dbM;
    private SQLiteDatabase db;
    private Context mContext;


    public static synchronized DataBaseManager sharedInstance(){
        if (dbM== null)
            dbM = new DataBaseManager();
        return dbM;
    }


    public void insert(Context context){
        mContext = context;
        Log.d("DataBaseHelper", "insert");
        readInsertsFile("insert_estados.txt");
        readInsertsFile("insert_paises.txt");
        readInsertsFile("insert_codigos.txt");
        readInsertsFile("insert_oficinas.txt");
    }

    public void readInsertsFile(String filename) {
        if(filename.equals("insert_oficinas.txt")){
            if(Offices.getAllInMaps(mContext)!= null){
                Offices.removeAll(mContext);
            }
        }

        if(filename.equals("insert_estados.txt")){
            if(States.getAllInMaps(mContext)!= null){
               States.removeAll(mContext);
            }
        }

        try {


            BufferedReader br = new BufferedReader(new InputStreamReader(
                    mContext.getAssets().open("dbtxt/" + filename)));
            String strLine;
            // Read File Line By Line
            DataBaseAdapter.getDB(mContext).beginTransaction();
            while ((strLine = br.readLine()) != null) {
                DataBaseAdapter.getDB(mContext).execSQL(strLine);
            }
            DataBaseAdapter.getDB(mContext).setTransactionSuccessful();
        } catch (Exception e) {// Catch exception if any
            System.err.println("Error: " + e.getMessage());
        } finally{
            DataBaseAdapter.getDB(mContext).endTransaction();
        }
    }



}
