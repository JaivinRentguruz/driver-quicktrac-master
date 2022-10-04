/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.evolution.quicktrack.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.evolution.quicktrack.util.Utils;
import com.google.android.gms.location.LocationResult;

import java.util.List;


public class LocationUpdatesBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "LUBroadcastReceiver";

    public static final String ACTION_PROCESS_UPDATES =
            "com.google.android.gms.location.sample.locationupdatespendingintent.action" +
                    ".PROCESS_UPDATES";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive");

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PROCESS_UPDATES.equals(action)) {
                LocationResult result = LocationResult.extractResult(intent);
                if (result != null) {
                    List<Location> locations = result.getLocations();
                    Utils.setLocationUpdatesResult(context, locations);
                   // Utils.sendNotification(context, Utils.getLocationResultTitle(context, locations));
                    Log.i(TAG, Utils.getLocationUpdatesResult(context));
                }
            }
        }
    }






}
