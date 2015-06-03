package co.roverlabs.sdk;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.util.List;

import co.roverlabs.sdk.model.Touchpoint;
import co.roverlabs.sdk.ui.activities.CardListActivity;
/**
 * Created by ars on 15-06-03.
 */
public class UiManager {
    final Rover rover;

    private NotificationManager mNotificationManager;

    UiManager(Rover rover){
        this.rover = rover;
        mNotificationManager = (NotificationManager)rover.context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    void prepareViews(){
        Factory.getDefaultImageLoader(rover.context).fetchAll();
    }

    void showNotificationForTouchPoint(List<Touchpoint> touchpoints, Config config){
        //TODO: show notifications for all touchpoints
        Touchpoint touchpoint = touchpoints.get(0);

        //prepare notification title and message
        String touchPointId = touchpoint.getId();

        String numberOnlyId = touchPointId.replaceAll("[^0-9]", "");
        numberOnlyId = numberOnlyId.substring(Math.max(0, numberOnlyId.length() - 7));

        int notifiactionId = Integer.valueOf(numberOnlyId);
        String title = touchpoint.getTitle();
        String message = touchpoint.getNotification();

        //prepare the icon
        int headIcon = config.getRoverHeadIconId();
        int notificationIcon = config.getNotificationIconId();

        //start the notification
        Intent cardListIntent = new Intent(rover.context, CardListActivity.class);
        cardListIntent.setAction(Intent.ACTION_MAIN);

        cardListIntent.putExtra(CardListActivity.EXTRA_HEAD_ICON_ID, headIcon);
        cardListIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pendingIntent = PendingIntent.getActivity(rover.context, notifiactionId, cardListIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(rover.context)
                .setSmallIcon(notificationIcon)
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

        n.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        mNotificationManager.notify(notifiactionId, n);

    }

}
