package com.evolution.quicktrack.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.util.Utils;

/**
 * Created by user on 7/13/2018.
 */

public class NotificationRemoveReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        LogCustom.logd("xxxxxx","service NotificationRemoveReciver" );
        Utils.setNotificationResult(context,"");

    }

}