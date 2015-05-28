package co.roverlabs.sdk.managers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import co.roverlabs.sdk.events.RoverEventBus;
import co.roverlabs.sdk.ui.activity.CardListActivity;

/**
 * Created by SherryYang on 2015-01-26.
 */
public class RoverNotificationManager {
    
    public static final String TAG = RoverNotificationManager.class.getSimpleName();
    private static RoverNotificationManager sNotificationManagerInstance;
    private Context mContext;
    private NotificationManager mNotificationManager;
    private int mNotificationIconId;
    private int mHeadIconId;
    private Intent mCardListIntent;


    private RoverNotificationManager(Context con) {

        mContext = con;
        mNotificationManager = (NotificationManager)con.getSystemService(Context.NOTIFICATION_SERVICE);
        RoverEventBus.getInstance().register(this);
    }

    public static RoverNotificationManager getInstance(Context con) {

        if(sNotificationManagerInstance == null) {
            sNotificationManagerInstance = new RoverNotificationManager(con);
        }
        return sNotificationManagerInstance;
    }
    
    public void setNotificationIconId(int id) { mNotificationIconId = id; }
    public void setHeadIconId(int id) { mHeadIconId = id; }


    public void showStickyNotification(int id, String title, String message) {

        mCardListIntent = new Intent(mContext, CardListActivity.class);
        mCardListIntent.setAction(Intent.ACTION_MAIN);
        mCardListIntent.putExtra(CardListActivity.EXTRA_HEAD_ICON_ID, mHeadIconId);
        mCardListIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, id, mCardListIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(mNotificationIconId)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);

        Notification n;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            n = builder.build();
        } else {
            n = builder.getNotification();
        }

        //n.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        mNotificationManager.notify(id, n);
    }

    public void cancelNotification() {
        mNotificationManager.cancelAll();
    }


}
