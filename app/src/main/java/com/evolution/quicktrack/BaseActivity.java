package com.evolution.quicktrack;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.location.GetLocation;
import com.evolution.quicktrack.location.LocationUpdatesBroadcastReceiver;
import com.evolution.quicktrack.location.LocationUpdatesIntentService;
import com.evolution.quicktrack.model.DialogueButtonsModel;
import com.evolution.quicktrack.network.ApiCallInterface;
import com.evolution.quicktrack.network.ApiClient;
import com.evolution.quicktrack.response.checkodometer.OdometerResponse;
import com.evolution.quicktrack.util.PlusButton;
import com.evolution.quicktrack.util.PrefManager;
import com.evolution.quicktrack.util.ShowCommonDialogu;
import com.evolution.quicktrack.util.Utility;
import com.evolution.quicktrack.util.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.kaopiz.kprogresshud.KProgressHUD;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 4/21/2018.
 */

public abstract class BaseActivity extends FragmentActivity {



    protected  PrefManager prefManager;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 5469;
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private  KProgressHUD pd;
    private static final long UPDATE_INTERVAL = 2000;//10000; // Every 60 seconds.

    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value, but they may be less frequent.
     */
    private static final long FASTEST_UPDATE_INTERVAL = 2000; //10000; // Every 30 seconds

    /**
     * The max time before batched results are delivered by location services. Results may be
     * delivered sooner than this interval.
     */
    private static final long MAX_WAIT_TIME = UPDATE_INTERVAL * 2; // Every 5 minutes.

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    public GetLocation trackDriver = null;
    public static Dialog dialog;
    public PlusButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        pd = LogCustom.loderProgressDialog2(this);
        prefManager = new PrefManager(getApplicationContext());
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();
        trackDriver = new GetLocation(BaseActivity.this);

        if (Utils.checkPermission(BaseActivity.this)) {

            LogCustom.logd("xxxxx", "locstion start");
            requestLocationUpdates();
            trackDriver = new GetLocation(BaseActivity.this);


        }


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

         button = new PlusButton(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(80,80);

        layoutParams.setMargins(width-150,height-150,50,50);
        addContentView(button,layoutParams);


        button.setVisibility(View.GONE);
    }

    public void showPlusButton(){
        button.setVisibility(View.VISIBLE);
    }

    protected abstract int getLayoutResourceId();


    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        // Note: apps running on "O" devices (regardless of targetSdkVersion) may receive updates
        // less frequently than this interval when the app is no longer in the foreground.
        mLocationRequest.setInterval(UPDATE_INTERVAL);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Sets the maximum time when batched location updates are delivered. Updates may be
        // delivered sooner than this interval.
        mLocationRequest.setMaxWaitTime(MAX_WAIT_TIME);
    }

    private PendingIntent getPendingIntent() {
        // Note: for apps targeting API level 25 ("Nougat") or lower, either
        // PendingIntent.getService() or PendingIntent.getBroadcast() may be used when requesting
        // location updates. For apps targeting API level O, only
        // PendingIntent.getBroadcast() should be used. This is due to the limits placed on services
        // started in the background in "O".

        // TODO(developer): uncomment to use PendingIntent.getService().
//        Intent intent = new Intent(this, LocationUpdatesIntentService.class);
//        intent.setAction(LocationUpdatesIntentService.ACTION_PROCESS_UPDATES);
//        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intent = new Intent(this, LocationUpdatesIntentService.class);
        intent.setAction(LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    public void requestLocationUpdates() {
        try {
            Log.i(TAG, "Starting location updates");
            Utils.setRequestingLocationUpdates(this, true);
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, getPendingIntent());
        } catch (SecurityException e) {
            Utils.setRequestingLocationUpdates(this, false);
            e.printStackTrace();
        }
    }

    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    public void showOdometerDialog(final Activity activity) {
        try {
            PrefManager prefManager = new PrefManager(activity);
            dialog = new Dialog(activity);
            dialog.requestWindowFeature(1);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.odometer_popup);
            EditText edittext_odometer = (EditText) dialog.findViewById(R.id.edit_pop_odometerreading);
            ImageView btn_cancel = (ImageView) dialog.findViewById(R.id.img_pop_Close);
            Button btn_ok = (Button) dialog.findViewById(R.id.btn_pop_verify);

            btn_cancel.setOnClickListener(view -> dialog.dismiss());

            btn_ok.setOnClickListener(view -> {
                if (Utility.isValidString(edittext_odometer.getText().toString().trim())) {
                    String temp_odometer = prefManager.getOdometer();
                    if (!temp_odometer.isEmpty()) {
                        long odometer = Long.parseLong(temp_odometer);
                        if (Long.parseLong(edittext_odometer.getText().toString()) < odometer) {
                            showToast("  New odometer number must be    greater than previous odometer number");
                        } else {
                            try {
                                call_updateOdometerAPi(edittext_odometer.getText().toString().trim());
                            } catch (Exception e) {
                                showToast(e.getMessage());
                            }
                        }
                    } else {
                        dialog.dismiss();
                    }

                } else {
                    edittext_odometer.requestFocus();
                    showToast("Please enter Readings");
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void call_updateOdometerAPi(String odometerreading) throws Exception {
        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);

        pd.show();

        PrefManager prefManager = new PrefManager(getApplicationContext());

        Call<OdometerResponse> call = apiService.updateOdometer(prefManager.getLogin_Token(), Constants.API_KEY, prefManager.getTruckId(), prefManager.getId(), odometerreading);
        call.enqueue(new Callback<OdometerResponse>() {
            @Override
            public void onResponse(Call<OdometerResponse> call, Response<OdometerResponse> response) {

                Log.d(TAG, " : check status : " + response.body().toString());

                if (pd != null && pd.isShowing())
                    pd.dismiss();
                if (response.body().getStatus().equals(Constants.API_STATUS)) {
                    trackDriver.call_trackDriverAPi(Constants.BACK_LOGOUT);
                    prefManager.logout();
                    dialog.dismiss();
                    Intent intent = new Intent(BaseActivity.this, Login_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                } else {

                    if (response.body().getMessage().equals("invalidToken")) {
                        trackDriver.call_trackDriverAPi(Constants.BACK_LOGOUT);
                        prefManager.logout();
                        Intent intent = new Intent(BaseActivity.this, Login_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }

                    LogCustom.Toast(BaseActivity.this, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<OdometerResponse> call, Throwable t) {
                LogCustom.logd(TAG, t.toString());
                if (pd != null && pd.isShowing())
                    pd.dismiss();
            }
        });
    }

    public void showCommonDialogue(DialogInterface.OnDismissListener onDismissListener, DialogueButtonsModel dialogueButtonsModel){
        FragmentManager fragmentManager = getSupportFragmentManager();
        ShowCommonDialogu showCommonDialogu = ShowCommonDialogu.getInstance(dialogueButtonsModel);
        showCommonDialogu.show(fragmentManager,"dialogue");
        fragmentManager.executePendingTransactions();
        showCommonDialogu.getDialog().setOnDismissListener(onDismissListener);
    }

    protected void showProgressDialogue(){
        pd.show();
    }

    protected void hideProgressDialogue(){
        pd.dismiss();
    }

    protected void redirectToLogin(){
        Intent intent = new Intent(this, Login_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    protected void logOutDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(BaseActivity.this);
        // Setting Dialog Message
        alertDialog.setMessage("Are you sure want to logout ?");
        alertDialog.setCancelable(true);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                new PrefManager(BaseActivity.this).setLoginProcess(false);
                dialog.dismiss();
                showOdometerDialog(BaseActivity.this);
            }
        });
        alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        // Showing Alert Message
        alertDialog.show();


    }
}