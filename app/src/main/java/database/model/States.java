package database.model;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import database.DataBaseAdapter;

/**
 * Created by rebecalopezmartinez on 28/05/15.
 */
public class States {
    public static final String TABLE_NAME = "estados";

    public static final String Z_PK = "Z_PK";
    public static final String ZNUMEROESTADO = "ZNUMEROESTADO";
    public static final String ZLATITUD = "ZLATITUD";
    public static final String ZLONGITUD = "ZLONGITUD";
    public static final String ZNOMBRE = "ZNOMBRE";

    private int z_pk;
    private int znumero_estado;
    private String zlatitud;
    private String zlongitud;
    private String znombre;

    public States(int z_pk, int znumero_estado, String zlatitud, String zlongitud, String znombre) {
        this.z_pk = z_pk;
        this.znumero_estado = znumero_estado;
        this.zlatitud = zlatitud;
        this.zlongitud = zlongitud;
        this.znombre = znombre;
    }

    public static long insert(Context context, int znumero_estado, String zlatitud, String zlongitud,
                              String znombre){
        ContentValues cv = new ContentValues();
        cv.put(ZNUMEROESTADO, znumero_estado);
        cv.put(ZLATITUD,zlatitud);
        cv.put(ZLONGITUD, zlongitud);
        cv.put(ZNOMBRE, znombre);

        return DataBaseAdapter.getDB(context).insert(TABLE_NAME,null,cv);
    }

    public static void setStates(Context context, String file_name){

        try {
            AssetManager assetManager = context.getAssets();
            InputStream json_file = assetManager.open("dbtxt/"+file_name);
            byte[] formArray = new byte[json_file.available()];

            json_file.read(formArray);
            json_file.close();

            String json = new String(formArray, "UTF-8");

            JSONObject obj = null;
            try {
                obj = new JSONObject(json);
                JSONArray jarray = obj.getJSONArray("states");

                for(int i=0;i<jarray.length();i++){
                    JSONObject stateObject = jarray.getJSONObject(i);

                    int number = Integer.parseInt(stateObject.getString("number"));
                    String latitud = stateObject.getString("latitud");
                    String longitud = stateObject.getString("longitud");
                    String name = stateObject.getString("name");

                    insert(context,number,latitud,longitud,name);
                    Log.d("Insertar",name);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
