package co.roverlabs.sdk.managers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import co.roverlabs.sdk.Rover;

/**
 * Created by SherryYang on 2015-01-26.
 */
public class RoverNotificationManager {
    
    private static final String TAG = RoverNotificationManager.class.getName();
    private Context mContext;
    private NotificationCompat.Builder mNotificationBuilder;
    private NotificationManager mNotificationManager;

    public RoverNotificationManager(Context con) {

        mContext = con;
        mNotificationManager = (NotificationManager)con.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void sendNotification(int id, String title, String message, Class intentClass) {

        Intent intent = new Intent(mContext, intentClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //intent.addCategory(Intent.CATEGORY_LAUNCHER);
        //intent.setAction(Intent.ACTION_MAIN);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotificationBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(Rover.getInstance(mContext).getNotificationIconId())
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setAutoCancel(true);
        mNotificationManager.notify(id, mNotificationBuilder.build());
    }
}
