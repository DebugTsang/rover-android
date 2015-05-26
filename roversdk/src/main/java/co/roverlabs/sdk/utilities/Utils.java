package co.roverlabs.sdk.utilities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.roverlabs.sdk.RoverService;

/**
 * Created by SherryYang on 2015-01-27.
 */
public class Utils {
    
    public static final String TAG = Utils.class.getSimpleName();

    public static List subtractList(List list1, List list2) {

        List result = new ArrayList(list2);
        result.removeAll(list1);
        return result;
    }

    public static boolean isRoverServiceRunning(Activity activity) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (RoverService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String getRandomUuid() {
        return UUID.randomUUID().toString();
    }

}


