package location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.estafeta.estafetamovilv1.Utils.DialogManager;

/**
 * Created by juan.guerra on 23/06/2015.
 * @deprecated
 */
public class GPS_AR implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location loc;
    private static GPS_AR gps;

    public synchronized static GPS_AR getInstance(Context context){
        if(gps==null){
           gps=new GPS_AR();
            gps.createGPSService(context);
        }
        return gps;
    }


    public void createGPSService(Context context){
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void finalize(){
        try {
            mGoogleApiClient.disconnect();
            gps=null;
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mostrarPosicion(location);
    }

    private void mostrarPosicion(Location loc) {
        if (loc!=null){
            Log.d("GPS Accuary: ",String.valueOf(loc.getAccuracy()));
            if (DialogManager.sharedInstance().isShowingDialog())
                DialogManager.sharedInstance().dismissDialog();
            loc.setAltitude(0);
            this.loc=loc;
        }
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

}
