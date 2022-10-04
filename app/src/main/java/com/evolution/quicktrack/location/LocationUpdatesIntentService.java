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

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.network.ApiCallInterface;
import com.evolution.quicktrack.network.ApiClient;
import com.evolution.quicktrack.response.track_driver.TrackDriverResponse;
import com.evolution.quicktrack.util.PrefManager;
import com.evolution.quicktrack.util.Utils;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles incoming location updates and displays a notification with the location data.
 *
 * For apps targeting API level 25 ("Nougat") or lower, location updates may be requested
 * using {@link android.app.PendingIntent#getService(Context, int, Intent, int)} or
 * {@link android.app.PendingIntent#getBroadcast(Context, int, Intent, int)}. For apps targeting
 * API level O, only {@code getBroadcast} should be used.
 *
 *  Note: Apps running on "O" devices (regardless of targetSdkVersion) may receive updates
 *  less frequently than the interval specified in the
 *  {@link com.google.android.gms.location.LocationRequest} when the app is no longer in the
 *  foreground.
 */
public class LocationUpdatesIntentService extends IntentService {
    PrefManager prefManager;
    private static final String ACTION_PROCESS_UPDATES =
            "com.google.android.gms.location.sample.locationupdatespendingintent.action" +
                    ".PROCESS_UPDATES";
    private static final String TAG = LocationUpdatesIntentService.class.getSimpleName();

    private static Location location;


    public LocationUpdatesIntentService() {
        // Name the worker thread.
        super(TAG);
        mHandler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {




        Log.i(TAG, "onReceive:onHandleIntent");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PROCESS_UPDATES.equals(action)) {
                LocationResult result = LocationResult.extractResult(intent);
                if (result != null) {
                    List<Location> locations = result.getLocations();
                    if(locations!=null && locations.get(0)!=null){
                        Utils.setLocationUpdatesResult(this, locations);
                        // Utils.sendNotification(this, Utils.getLocationResultTitle(this, locations));
                        Log.i(TAG, Utils.getLocationUpdatesResult(this));

                        try {
                            prefManager  =new PrefManager(getApplicationContext());
                            if(!prefManager.getId().equals("")){

                                if(location==null){
                                    location = locations.get(0);
                                }

                                /*double  head = SphericalUtil.computeHeading(new LatLng(location.getLatitude(),location.getLongitude()),new LatLng(locations.get(0).getLatitude(),locations.get(0).getLongitude()));

                                double kmhSpeed = location.getSpeed()* 18 / 5;


                                double distance = SphericalUtil.computeDistanceBetween(new LatLng(location.getLatitude(),location.getLongitude()),new LatLng(locations.get(0).getLatitude(),locations.get(0).getLongitude())) ;//CalculationByDistance(locations.get(0).getLatitude(), locations.get(0).getLongitude(), location.getLatitude(), location.getLongitude());*/
                                /*double kmhSpeed = 0.0;
                                if(distance>1){
                                   kmhSpeed = distance/(2);
                               }*/

                                location = locations.get(0);



                                // Toast.makeText(getApplicationContext(),"Head "+head+" speed "+speed,Toast.LENGTH_LONG).show();

                             //   mHandler.post(new DisplayToast(this, "Head "+head+" speed "+kmhSpeed));

                                call_trackDriverAPi(String.valueOf(locations.get(0).getLatitude()),String.valueOf(locations.get(0).getLongitude()));

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }
    }
    Handler mHandler;
    public class DisplayToast implements Runnable {
        private final Context mContext;
        String mText;

        public DisplayToast(Context mContext, String text){
            this.mContext = mContext;
            mText = text;
        }

        public void run(){
            Toast.makeText(mContext, mText, Toast.LENGTH_SHORT).show();
        }
    }


    private void call_trackDriverAPi(String latitude,String longitude) throws Exception {
        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);

        Log.i("xxxxxxx"+TAG, "onReceive:latitude"+latitude);
        Log.i("xxxxxxx"+TAG, "onReceive:latitude"+longitude);

        PrefManager prefManager=new PrefManager(getApplicationContext());

        Call<TrackDriverResponse> call = apiService.callBackGround(prefManager.getLogin_Token(),Constants.API_KEY,prefManager.getTruckId() , prefManager.getId(), "5", latitude,longitude);
        call.enqueue(new Callback<TrackDriverResponse>() {
            @Override
            public void onResponse(Call<TrackDriverResponse> call, Response<TrackDriverResponse> response) {
                Log.d(TAG, " : check status : " + response.body().toString());
                if(response.body().getStatus().equals(Constants.API_STATUS))
                {

                }
                else
                {
                    if(response.body().getMessage().equals("invalidToken"))
                    {
/*
                        prefManager.logout();

                        Intent   intent =new Intent(LocationUpdatesIntentService.this,Login_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
*/

                    }

                }
            }

            @Override
            public void onFailure(Call<TrackDriverResponse> call, Throwable t) {
                LogCustom.loge(TAG, t.toString());

            }
        });
    }

    static final Double EARTH_RADIUS = 6371.00;
    public double CalculationByDistance(double lat1, double lon1, double lat2, double lon2) {
        double Radius = EARTH_RADIUS;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return Radius * c;
    }

}
