package location;

import android.content.Context;
import android.location.Location;

/**
 * Created by juan.guerra on 09/07/2015.
 * @deprecated
 */
public class GPSHandler {


    private static GPSTracker gpsTracker;
    private static GPS_AR gpsAR;

    public static GPSTracker getGpsTrackerInstance(Context context){
        if (gpsTracker == null){
            gpsTracker = new GPSTracker(context);
        }
        return gpsTracker;
    }

    public static GPS_AR getGpsARInstance(Context context){
        if (gpsAR == null){
            gpsAR = GPS_AR.getInstance(context);
        }
        return gpsAR;
    }

    public static Location getMyCurrentLocation(Context context){
        Location fromAndroid,fromGoogle;
        fromAndroid = getGpsTrackerInstance(context).getCurrentLocation();
        fromGoogle = getGpsARInstance(context).getLoc();

        if (fromAndroid == null)
            return fromGoogle;

        if (fromGoogle == null)
            return fromAndroid;

        if (fromAndroid.getAccuracy() < fromGoogle.getAccuracy())
            return fromAndroid;

        else
            return fromGoogle;

    }
}
