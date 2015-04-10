package co.roverlabs.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by SherryYang on 2015-03-18.
 */
public class RoverReceiver extends BroadcastReceiver {

    public static final String TAG = RoverReceiver.class.getSimpleName();
    private Rover mRover;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "The action received is " + intent.getAction());
        mRover = Rover.getInstance(context.getApplicationContext());
        mRover.startMonitoring();
    }
}