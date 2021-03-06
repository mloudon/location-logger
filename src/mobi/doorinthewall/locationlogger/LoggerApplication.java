package mobi.doorinthewall.locationlogger;

import android.app.Application;
import android.util.Log;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;

public class LoggerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        
        Log.d("LoggerApplication", "onCreate()");

        // output debug to LogCat, uses tag LittleFluffyLocationLibrary
        LocationLibrary.showDebugOutput(true);

        try {
            
            // every 1 minute, and force a location update if there hasn't been one for 2 minutes.
            LocationLibrary.initialiseLibrary(getBaseContext(),
            			Config.locQueryInterval * 1000,
            			Config.locForceUpdateInterval * 1000,
            			"mobi.doorinthewall.locationlogger");
        }
        catch (UnsupportedOperationException ex) {
            Log.d("LoggerApplication", "UnsupportedOperationException thrown - the device doesn't have any location providers");
        }
    }
}
