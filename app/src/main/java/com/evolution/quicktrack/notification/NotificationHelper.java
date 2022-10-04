package com.evolution.quicktrack.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

import com.evolution.quicktrack.R;

import java.util.Random;

public class NotificationHelper {

    private static NotificationManager mNotificationManager;
    private static NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    public static void setContect(Context context) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context,"NOTIFICATION_CHANNEL_NAME");
    }

    /**
     * Create and push the notification
     */
    public static void createNotification(String title, String message)
    {
        /**Creates an explicit intent for an Activity in your app**/
       /* Intent resultIntent = new Intent(mContext , SomeOtherActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
                0 *//* Request code *//*, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);*/


        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
               // .setContentIntent(resultPendingIntent);



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(new Random().nextInt() /* Request Code */, mBuilder.build());
    }

    public static void dismiss(){
        if(mNotificationManager!=null)
          mNotificationManager.cancelAll();
    }

}