package com.evolution.quicktrack.location;

import android.Manifest;
import android.app.Activity;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import com.evolution.quicktrack.QuickTrackAplication;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.network.ApiCallInterface;
import com.evolution.quicktrack.network.ApiClient;
import com.evolution.quicktrack.response.track_driver.TrackDriverResponse;
import com.evolution.quicktrack.util.PrefManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 7/16/2018.
 */

public class GetLocation {


    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    public static double  currentLatitude;
    public static double currentLongitude;
    static Location location=null;

    Activity context;

    public GetLocation(final Activity context) {
        this.context=context;
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {


                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            location  = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                            if (location != null) {
                                //If everything went fine lets get latitude and longitude
                                currentLatitude = location.getLatitude();
                                currentLongitude = location.getLongitude();
                                LogCustom.logd("xxxxxxx","xxx location onConnected");
                               // Toast.makeText(context, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error","Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(20 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(context, REQUEST_LOCATION);


                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
    }



    public  Location getcurrntLataLong(){
        Location curntlocation=null;
        curntlocation  =location;

       return curntlocation;
    }




    public void call_trackDriverAPi( String status)  {

        try {
            ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);

            Log.i("xxxxxxx", "call_trackDriverAPi:"+status);

            PrefManager prefManager  =new PrefManager(context);

            if(QuickTrackAplication.IsNotNull(prefManager.getUserData())) {
                Call<TrackDriverResponse> call = apiService.callBackGround(prefManager.getLogin_Token(), Constants.API_KEY, prefManager.getTruckId(), prefManager.getId(), status, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                call.enqueue(new Callback<TrackDriverResponse>() {
                    @Override
                    public void onResponse(Call<TrackDriverResponse> call, Response<TrackDriverResponse> response) {
                        Log.d("xxxxxx", " : check status : " + response.body().toString());

                        if (response.body().getStatus().equals(Constants.API_STATUS)) {

                        } else {
                            if (response.body().getMessage().equals("invalidToken")) {
//                            prefManager.logout();

/*
                            Intent intent =new Intent(context,Login_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
*/

                            }
                        }
                    }


                    @Override
                    public void onFailure(Call<TrackDriverResponse> call, Throwable t) {
                        LogCustom.loge("xxxxxx", t.toString());

                    }
                });
            }

        }catch (Exception e){e.printStackTrace();}
    }


}
