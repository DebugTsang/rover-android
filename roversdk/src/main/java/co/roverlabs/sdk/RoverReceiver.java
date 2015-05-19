package co.roverlabs.sdk;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by SherryYang on 2015-03-18.
 */
public class RoverReceiver extends BroadcastReceiver {

    public static final String TAG = RoverReceiver.class.getSimpleName();
    private final int STATE_OFF = 10;
    private final int STATE_ON = 12;
    private final int STATE_TURNING_OFF = 13;
    private final int STATE_TURNING_ON = 11;

    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();

        Log.d(TAG, "The action received is " + action);

        if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            final int extraStateInt = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
            String extraStateString;
            if(extraStateInt == STATE_OFF) {
                extraStateString = "STATE_OFF";
            }
            else if(extraStateInt == STATE_ON) {
                extraStateString = "STATE_ON";
            }
            else if(extraStateInt == STATE_TURNING_OFF) {
                extraStateString = "STATE_TURNING_OFF";
            }
            else if(extraStateInt == STATE_TURNING_ON) {
                extraStateString = "STATE_TURNING_ON";
            }
            else {
                extraStateString = "STATE_ERROR";
            }
            Log.d(TAG, "The bluetooth state is " + extraStateString);
            switch(extraStateInt) {
                case BluetoothAdapter.STATE_ON:
                    break;
                default:
                    return;
            }
        }

        Rover rover = Rover.getInstance(context.getApplicationContext());

        if(rover.getCustomer() != null && !rover.isMonitoring()) {
            rover.startMonitoring();
        }
    }
}