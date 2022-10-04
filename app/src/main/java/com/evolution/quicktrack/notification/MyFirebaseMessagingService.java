package com.evolution.quicktrack.notification;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.evolution.quicktrack.QuickTrackAplication;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.SplashScreen;
import com.evolution.quicktrack.activity.ChatPopupDialog;
import com.evolution.quicktrack.activity.ForceLogoutDialog;
import com.evolution.quicktrack.activity.JobPopUpDialog;
import com.evolution.quicktrack.activity.UserListActivity;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.response.chatlist.MessagesItem;
import com.evolution.quicktrack.util.PrefManager;
import com.evolution.quicktrack.util.Utils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

import static com.evolution.quicktrack.constants.Constants.OPEN_ACTIVITYNAME;
import static com.evolution.quicktrack.constants.Constants.OPEN_to_USERID;

/**
 * Created by evolution on 1/7/17.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";




    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.w("new token ",s);
        new PrefManager(this).setFCM_TOKEN(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


            // TODO(developer): Handle FCM messages here.
            Log.d(TAG, "From: " + remoteMessage.getFrom());

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());
                Log.d(TAG, "Message Notification notification_message: " + remoteMessage.getData().get("body"));
                Log.d(TAG, "OPEN_ACTIVITYNAME: " + OPEN_ACTIVITYNAME);
                Log.d(TAG, "type: " + remoteMessage.getData().get("notification_type"));

               /* if(remoteMessage.getData().get("notification_type").equals("activityRefresh")){
                    sendBroadcast(new Intent("activityRefresh"));
                }*/

                if(remoteMessage.getData().get("notification_type").equals("chat")){


                     if(OPEN_to_USERID.equals(remoteMessage.getData().get("from_id"))&& OPEN_ACTIVITYNAME.equals("ChatActivity")){
                         Intent intent1 = new Intent("MSG");
                         intent1.putExtra("MessageModel", getJasonToString(remoteMessage));
                         getApplicationContext().sendBroadcast(intent1);

                    }else {
                         //showChatNotifications(remoteMessage);
                         try {
                             MessagesItem messagesItem = getJasonToString(remoteMessage);
                             if(QuickTrackAplication.IsNotNull(messagesItem)){
                                 NotificationHelper.setContect(this);
                                 NotificationHelper.createNotification(messagesItem.getName(),messagesItem.getMessage());
                             }

                             if (QuickTrackAplication.IsNotNull(messagesItem) && isAppRunning(this) ) {
                                 if (QuickTrackAplication.IsNotNull(messagesItem.getMessage()) && QuickTrackAplication.IsNotNull(messagesItem.getFromUserRole())) {
                                     if(Constants.arrChatsUser.size()>0){
                                         int i=0;
                                         for(i=0;i<Constants.arrChatsUser.size();i++){
                                             if(messagesItem.getFromUserId().equals(Constants.arrChatsUser.get(i))){
                                                 Intent intent = new Intent(messagesItem.getFromUserId());
                                                 String speakMessage = messagesItem.getMessage();
                                                 intent.putExtra("chatuser", messagesItem.getName());
                                                 intent.putExtra("chatmessage", messagesItem.getMessage());
                                                 intent.putExtra("speakmessage", speakMessage);
                                                 intent.putExtra("id",messagesItem.getFromUserId());
                                                 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                 sendBroadcast(intent);
                                                 break;
                                             }
                                         }
                                         if(i==Constants.arrChatsUser.size()){
                                             Constants.arrChatsUser.add(messagesItem.getFromUserId());
                                             Intent intent = new Intent(getApplicationContext(), ChatPopupDialog.class);
                                             //String speakMessage = "Message from" + messagesItem.getFromUserRole() + messagesItem.getMessage();
                                             String speakMessage = messagesItem.getMessage();
                                             intent.putExtra("chatuser", messagesItem.getName());
                                             intent.putExtra("chatmessage", messagesItem.getMessage());
                                             intent.putExtra("speakmessage", speakMessage);
                                             intent.putExtra("id",messagesItem.getFromUserId());
                                             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                             startActivity(intent);
                                         }
                                     }else{
                                         if(isActivityRunning(ChatPopupDialog.class)){
                                             Intent intent = new Intent(messagesItem.getFromUserId());
                                             String speakMessage = messagesItem.getMessage();
                                             intent.putExtra("chatuser", messagesItem.getName());
                                             intent.putExtra("chatmessage", messagesItem.getMessage());
                                             intent.putExtra("speakmessage", speakMessage);
                                             intent.putExtra("id",messagesItem.getFromUserId());
                                             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                             sendBroadcast(intent);

                                         }else{
                                             Constants.arrChatsUser.add(messagesItem.getFromUserId());
                                             Intent intent = new Intent(getApplicationContext(), ChatPopupDialog.class);
                                             //String speakMessage = "Message from" + messagesItem.getFromUserRole() + messagesItem.getMessage();
                                             String speakMessage = messagesItem.getMessage();
                                             intent.putExtra("chatuser", messagesItem.getName());
                                             intent.putExtra("chatmessage", messagesItem.getMessage());
                                             intent.putExtra("speakmessage", speakMessage);
                                             intent.putExtra("id",messagesItem.getFromUserId());
                                             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                             startActivity(intent);
                                         }
                                     }
                                 }
                             }
                         }
                         catch (Exception e)
                         {
                             e.getMessage();
                         }

                     }

                }
                else if(remoteMessage.getData().get("notification_type").equals("new_job"))
                {
                    if(remoteMessage.getData().get("job_details") != null)
                    {
                        try {
                            Log.e(TAG, "Remote message : " + remoteMessage.getData().get("job_details"));
                            Intent i = new Intent(getApplicationContext(), JobPopUpDialog.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //i.setAction("com.avinash.quicktrack.android.intent.action.Pop");
                            i.putExtra("new_job", remoteMessage.getData().get("job_details"));
                            startActivity(i);
                        }
                        catch (Exception e)
                        {
                            e.getMessage();
                        }
                    }
                }
                else if(remoteMessage.getData().get("notification_type").equals("force_logout"))
                {
                    logout(remoteMessage.getData().get("message"));
                }
                else {
                    sendNotification(remoteMessage );
                }

            }

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
                Log.d(TAG, "Message Notification getTitle: " + remoteMessage.getNotification().getTitle());

            }

        }


    public  void logout(String message){

        PrefManager prefManager = new PrefManager(getApplicationContext());
        prefManager.logout();

        Intent intent = new Intent(getApplicationContext(), ForceLogoutDialog.class);
        intent.putExtra("Message",message);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }


    private void sendNotification( RemoteMessage remoteMessage ) {
        Intent intent = new Intent(this, SplashScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification summaryNotification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(remoteMessage.getData().get("message"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .build();
        summaryNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, summaryNotification);
    }



    public void showChatNotifications(RemoteMessage remoteMessage) {

        Log.d(TAG, "Message Notification showChatNotifications: " + Integer.parseInt(remoteMessage.getData().get("from_id")));

        String ss=  Utils.getNotificationId(this);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap bitmapMila = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher_round);
        Intent intent = new Intent(this, UserListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        // Nuke all previous notifications and generate unique ids
       // NotificationManagerCompat.from(this).cancelAll();


        // String to represent the group all the notifications will be a part of
        final String GROUP_KEY_MESSAGES = "group_key_messages";

        Intent intentdelete = new Intent(this, NotificationRemoveReciver.class);
        PendingIntent pendingIntentdelete = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intentdelete, 0);

        // Group notification that will be visible on the phone
        Notification summaryNotification = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(remoteMessage.getData().get("message"))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(bitmapMila)
                .setGroup(GROUP_KEY_MESSAGES)
                .setGroupSummary(ss.equals(""))
                .setContentIntent(pendingIntent)
                .setSound(defaultSoundUri)
                .setDeleteIntent(pendingIntentdelete)
                .build();
        summaryNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        // Issue the group notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(Integer.parseInt(remoteMessage.getData().get("from_id")), summaryNotification);

        Utils.setNotificationResult(this,remoteMessage.getData().get("from_id"));
       // showStackNotifications();
    }







    public MessagesItem getJasonToString(RemoteMessage remoteMessage ){

      //  remoteMessage.getData()

        MessagesItem messagesItem =new MessagesItem();


            messagesItem.setId((String) remoteMessage.getData().get("id"));
            messagesItem.setFromUserId(remoteMessage.getData().get("from_id"));
            messagesItem.setFromUserRole(remoteMessage.getData().get("from_user_role"));
            messagesItem.setToUserId(remoteMessage.getData().get("to"));
            messagesItem.setToUserRole(remoteMessage.getData().get("to_user_role"));
            messagesItem.setDateTimestamp(remoteMessage.getData().get("date_timestamp"));
            messagesItem.setMessage(remoteMessage.getData().get("message"));
            messagesItem.setName(remoteMessage.getData().get("from_user_name"));

            LogCustom.logd("xxxxxx","service recive msg" +messagesItem.toString());


        return messagesItem;
    }

    protected Boolean isActivityRunning(Class activityClass)
    {
        ActivityManager activityManager = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (activityClass.getCanonicalName().equalsIgnoreCase(task.topActivity.getClassName()))
                return true;
        }

        return false;
    }
    public static boolean isAppRunning(final Context context) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null)
        {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(context.getApplicationContext().getPackageName()) && processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND ) {
                    return true;
                }
            }
        }
        return false;
    }
}
