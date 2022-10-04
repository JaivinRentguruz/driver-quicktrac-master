package com.evolution.quicktrack;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.evolution.quicktrack.activity.UserListActivity;
import com.evolution.quicktrack.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DemoNotification extends AppCompatActivity {
    @BindView(R.id.btn_snd)
    Button btnSnd;

    // Splash screen timer


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // MultiDex.install(this);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_demonotification);
        ButterKnife.bind(this);


    }

    int count=0;


    @OnClick(R.id.btn_snd)
    public void onViewClicked() {

        showStackNotifications();
        count++;

        Utils.setNotificationResult(getApplicationContext(),Utils.getNotificationId(getApplicationContext()));

    }




    public void showStackNotifications() {
        Bitmap bitmapMila = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);

        // Nuke all previous notifications and generate unique ids
        // NotificationManagerCompat.from(this).cancelAll();
        int notificationId = 0;

        // String to represent the group all the notifications will be a part of
        final String GROUP_KEY_MESSAGES = "group_key_messages";


        Intent intent = new Intent(this, UserListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);



        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);



            Intent viewIntent1 = new Intent(this, MainActivity.class);
            PendingIntent viewPendingIntent1 =
                    PendingIntent.getActivity(this, notificationId+1, viewIntent1, 0);
            Notification notification1 = new NotificationCompat.Builder(this)
                    // .addAction(R.drawable.ic_person, "Treat Fed", viewPendingIntent1)
                    .setContentTitle("Message from Mila")
                    .setContentText("What's for dinner? "
                            + "Can we have steak?")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setGroup(GROUP_KEY_MESSAGES)
                    .setGroupSummary(true) .build();


          //  notificationManager.notify(count, groupBuilder.build());
            notificationManager.notify(count, notification1);


        if(count==2){
            showStackNotifications2();
        }

      //  notificationManager.notify(notificationId + 1, notification1);

    }



    public void showStackNotifications2() {
        Bitmap bitmapMila = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);

        // Nuke all previous notifications and generate unique ids
        // NotificationManagerCompat.from(this).cancelAll();
        int notificationId = 4;

        // String to represent the group all the notifications will be a part of
        final String GROUP_KEY_MESSAGES = "group_key_messages";

        // Group notification that will be visible on the phone
        Notification summaryNotification = new NotificationCompat.Builder(this)
                .setContentTitle("2 Pet Notificationswwwwppppp")
                .setContentText("Mila and Dylan both sent messageswwwwpppppp")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(bitmapMila)
                .setGroup(GROUP_KEY_MESSAGES)
                .setGroupSummary(false).build();


        Notification notification1 = new NotificationCompat.Builder(this)
                // .addAction(R.drawable.ic_person, "Treat Fed", viewPendingIntent1)
                .setContentTitle("Message from Milawwwwww")
                .setContentText("What's for dinnerwwww? "
                        + "Can we have steak?")
                .setLargeIcon(bitmapMila).setSmallIcon(R.mipmap.ic_launcher_round)
                .setGroup(GROUP_KEY_MESSAGES)
                .setGroupSummary(false).build();


        Notification notification2 = new NotificationCompat.Builder(this)
                // .addAction(R.drawable.ic_person, "Water Filled", viewPendingIntent2)
                .setContentTitle("Message from Dylaneeeee")
                .setContentText("Can you refill our water bowleeeee?")
                .setLargeIcon(bitmapMila).setSmallIcon(R.mipmap.ic_launcher_round)
                .setGroup(GROUP_KEY_MESSAGES)
                .setGroupSummary(false).build();
        notification2.flags |= Notification.FLAG_AUTO_CANCEL;

        // Issue the group notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
       notificationManager.notify(notificationId+3, summaryNotification);

        // Issue the separate wear notifications
        notificationManager.notify(notificationId+2, notification2);
       notificationManager.notify(notificationId+1, notification1);
    }


}