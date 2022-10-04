package com.evolution.quicktrack.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.evolution.quicktrack.Login_Activity;
import com.evolution.quicktrack.QuickTrackAplication;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.location.GetLocation;
import com.evolution.quicktrack.network.ApiCallInterface;
import com.evolution.quicktrack.network.ApiClient;
import com.evolution.quicktrack.response.allocatedJobList.JobItem;
import com.evolution.quicktrack.response.common.CommonResponse;
import com.evolution.quicktrack.util.PrefManager;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobPopUpDialog extends AppCompatActivity  {

    private static final String TAG = "[JobPopUpDialog]";
    GetLocation getLocation;
    boolean status = false;
    int count = 0;
    private KProgressHUD pd;
    private TextToSpeech tts;
    JobItem jobItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.custom_allocated_joblist_popup);
            Intent intent = getIntent();
            String job = intent.getStringExtra("new_job");
            Gson gson = new Gson();
            jobItem = gson.fromJson(job, JobItem.class);
            getLocation = new GetLocation(JobPopUpDialog.this);
            speakOut("New Job Has Been Assigned to you");
            //ShowPopup(jobItem);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.e(TAG, "M OS");
                getOverlayPermission();
                //  ShowPopup(jobItem);
            } else {
                Log.e(TAG, "M OS");
                ShowPopup(jobItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static int REQUEST_CODE = -1010101;

    private void getOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_CODE);
            } else {
                ShowPopup(jobItem);
            }
        }
    }

    public void ShowPopup(JobItem dataItems) {
        try {

            LinearLayout main_layout = findViewById(R.id.allocated_mainLayout);
            TextView txtClientName = findViewById(R.id.txt_clientName);
            TextView txt_jobId = findViewById(R.id.txt_jobId);
            TextView txtCollectionCompany = findViewById(R.id.txtCollectionCompany);
            TextView txtPickupAddres = findViewById(R.id.txt_pickupAddres);
            TextView txtDeliveryCompany = findViewById(R.id.txtDeliveryCompany);
            TextView txtDeliveryAddres = findViewById(R.id.txt_deliveryAddres);
            TextView txtComment = findViewById(R.id.txt_comment);
            ImageView img_popUp = findViewById(R.id.img_popClose);
            //img_popUp.setVisibility(View.GONE);
            Button btnAccept = findViewById(R.id.btn_accept);
            Button btnDecline = findViewById(R.id.btn_decline);

            if (QuickTrackAplication.IsNotNull(dataItems.getDeliveryName())) {
                if (dataItems.getDeliveryName().equals("Hurricane Delivery")) {
                    main_layout.setBackgroundColor(Color.parseColor("#FF0000"));
                }
            }

            txtClientName.setText(dataItems.getCustomerName());
            txtCollectionCompany.setText(dataItems.getCollection_companyname());
            txtDeliveryAddres.setText(dataItems.getDeliveryLocation());
            txtPickupAddres.setText(dataItems.getCollectionLocation());
            txtDeliveryCompany.setText(dataItems.getDelivery_companyname());
            txtComment.setText(dataItems.getDescription());
            txt_jobId.setText("JOB ID : " + dataItems.getId());

            //getLocation = new GetLocation((Activity) getApplicationContext());

            btnAccept.setOnClickListener(v -> {
                //manager.removeView(layout);
                Toast.makeText(JobPopUpDialog.this, "Please Wait......", Toast.LENGTH_SHORT);
                count++;
                Log.e(TAG, "jobItem.getType() : " + jobItem.getType());
                if (getLocation.getcurrntLataLong() != null) {
                    //call_updatestaus(dataItems.getId(), Constants.Accepted, String.valueOf(getLocation.getcurrntLataLong().getLatitude()), String.valueOf(getLocation.getcurrntLataLong().getLongitude()));
                    if (dataItems.getType().equals(Constants.Job.JOB_1)) {

                        Log.e(TAG, "responseData.get(position).getSubJobid() : " + dataItems.getSubjobid());
                        Log.e(TAG, "responseData.get(position).getid() : " + dataItems.getId());
                        call_updatestaus(
                                dataItems.getSubjobid(),
                                Constants.Accepted,
                                String.valueOf(getLocation.getcurrntLataLong().getLatitude()),
                                String.valueOf(getLocation.getcurrntLataLong().getLongitude()),
                                Constants.Job.JOB_1,"");    //call karo

                    } else if (dataItems.getType().equals(Constants.Job.JOB_0)) {

                        call_updatestaus(
                                dataItems.getId(),
                                Constants.Accepted,
                                String.valueOf(getLocation.getcurrntLataLong().getLatitude()),
                                String.valueOf(getLocation.getcurrntLataLong().getLongitude()),
                                Constants.Job.JOB_0,"");
                    }

                } else {
                    getLocation = new GetLocation(JobPopUpDialog.this);
                    //call_updatestaus(dataItems.getId(), Constants.Accepted, String.valueOf(getLocation.getcurrntLataLong().getLatitude()), String.valueOf(getLocation.getcurrntLataLong().getLongitude()));
                    if (dataItems.getType().equals(Constants.Job.JOB_1)) {

                        Log.e(TAG, "responseData.get(position).getSubJobid() : " + dataItems.getSubjobid());
                        Log.e(TAG, "responseData.get(position).getid() : " + dataItems.getId());

                        call_updatestaus(
                                dataItems.getSubjobid(),
                                Constants.Accepted,
                                String.valueOf(getLocation.getcurrntLataLong().getLatitude()),
                                String.valueOf(getLocation.getcurrntLataLong().getLongitude()),
                                Constants.Job.JOB_1,"");    //call karo

                    } else if (dataItems.getType().equals(Constants.Job.JOB_0)) {

                        call_updatestaus(
                                dataItems.getId(),
                                Constants.Accepted,
                                String.valueOf(getLocation.getcurrntLataLong().getLatitude()),
                                String.valueOf(getLocation.getcurrntLataLong().getLongitude()),
                                Constants.Job.JOB_0,"");
                    }
                }
            });

            img_popUp.setOnClickListener(view -> {
                //manager.removeView(layout);
                finish();
            });

            btnDecline.setOnClickListener(v -> {
                //manager.removeView(layout);
                openFutileDialogue(dataItems);

            });
            //manager.addView(layout, layoutParams);
        } catch (Exception e) {
            e.getMessage();
        }

    }

    private boolean call_updatestaus(String jobid, String statusid, String latitude, String longitude, String jobType,String message) {

        pd = LogCustom.loderProgressDialog2(JobPopUpDialog.this);
        pd.show();

        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);
        PrefManager prefManager = new PrefManager(getApplicationContext());
        Call<CommonResponse> call;
        // = apiService.updateStatus(prefManager.getLogin_Token(),Constants.API_KEY, prefManager.getId(), jobid, statusid, latitude, longitude,prefManager.getTruckId());

        Log.e(TAG, "jobid : " + jobid);
        Log.e(TAG, "statusid : " + statusid);
        Log.e(TAG, "latitude : " + latitude);
        Log.e(TAG, "longitude : " + longitude);
        Log.e(TAG, "jobType : " + jobType);
        Log.e(TAG, "login_token : " + prefManager.getLogin_Token());
        Log.e(TAG, "APIKEY : " + Constants.API_KEY);
        Log.e(TAG, "APIKEY : " + prefManager.getId());

        if (jobType.equals(Constants.Job.JOB_1)) {
            call = apiService.updateSubJobStatus(//"fleets.php"
                    prefManager.getLogin_Token(),
                    Constants.API_KEY,
                    prefManager.getId(),
                    jobid,
                    statusid,
                    latitude,
                    longitude,
                    prefManager.getTruckId(),
                    Constants.Job.SUB_JOB_ACTION,message);
        } else {
            Log.e(TAG, "ready to change statues");
            call = apiService.acceptJobFromPopup(//"fleets.php"
                    prefManager.getLogin_Token(),
                    Constants.API_KEY,
                    prefManager.getId(),
                    jobid,
                    statusid,
                    latitude,
                    longitude,
                    prefManager.getTruckId(),message,jobType,jobid);
        }

        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                pd.dismiss();
                dismissDialog();
                try {
                    Log.e(TAG, "response.body().getStatus() : " + response.body().getStatus());
                    Log.e(TAG, "response.body().getMessage() : " + response.body().getMessage());
                    sendBroadcast(new Intent("jobAcceptedORDeclindReceiver"));
                    if (response.body().getStatus().equals("true") || response.body().getStatus().equals("success")) {
                        OutPut(true);
                    } else {
                        if (response.body().getMessage().equals("invalidToken")) {
                            prefManager.logout();

                            Intent intent = new Intent(JobPopUpDialog.this, Login_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                            OutPut(false);
                        } else {
                            OutPut(false);
                        }
                    }
                } catch (Exception e) {
                    OutPut(false);
                    dismissDialog();
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                LogCustom.logd(TAG, t.toString());
                dismissDialog();
            }

            private void dismissDialog() {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                    pd = null;
                }
            }
        });

        return status;
    }

    public void OutPut(boolean isSuccess) {
        Log.e(TAG, "isSuccess : " + isSuccess);
        if (isSuccess) {
            // manager.removeView(layout);
            finish();
        } else {
            if (count == 3) {
                // manager.removeView(layout);
                finish();
            } else {
                Toast.makeText(JobPopUpDialog.this, "You can try"
                        + (3 - count) + "more times", Toast.LENGTH_SHORT).show();
            }
            if (count >= 4) {
                // manager.removeView(layout);
                finish();
            }
        }
    }



    private void speakOut(String speakMessage) {
        ((QuickTrackAplication)getApplicationContext()).convertTextToSpeech(speakMessage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /** check if received result code
         is equal our requested code for draw permission  */
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            /** if so check once again if we have permission */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    ShowPopup(jobItem);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void openFutileDialogue(JobItem dataItems){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        alert.setMessage("Enter Your Decline Message");
        // alert.setTitle("");
        alert.setView(edittext);
        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String YouEditTextValue = edittext.getText().toString();
                if(YouEditTextValue!=null && YouEditTextValue.trim().length()>0){
                    ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);
                    PrefManager prefManager = new PrefManager(getApplicationContext());
                    pd = LogCustom.loderProgressDialog2(JobPopUpDialog.this);
                    // pd.show();

                    /*Call<CommonResponse> call = apiService.updateStatusDecline(prefManager.getLogin_Token(), Constants.API_KEY, prefManager.getId(),jobItem.getId(),"3", String.valueOf(getLocation.getcurrntLataLong().getLatitude()), String.valueOf(getLocation.getcurrntLataLong().getLongitude()), prefManager.getTruckId(),YouEditTextValue);
                    call.enqueue(new Callback<CommonResponse>() {
                        @Override
                        public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                            Log.w("Response ",response.body().toString());
                            pd.dismiss();
                            JobPopUpDialog.this.finish();
                            // decline(dataItems);
                            // deliveryFultileAPI();
                        }
                        @Override
                        public void onFailure(Call<CommonResponse> call, Throwable t) {
                            pd.dismiss();
                            Log.w("Faild",t.getCause().getMessage());
                        }
                    });*/



                    if (getLocation.getcurrntLataLong() != null) {
                        Log.e(TAG, "current location is not null");


                        if (dataItems.getType().equals(Constants.Job.JOB_1)) {
                            Log.e(TAG, "responseData.get(position).getSubJobid() : " + dataItems.getSubjobid());
                            Log.e(TAG, "responseData.get(position).getid() : " + dataItems.getId());

                            call_updatestaus(
                                    dataItems.getSubjobid(),
                                    Constants.Decline,
                                    String.valueOf(getLocation.getcurrntLataLong().getLatitude()),
                                    String.valueOf(getLocation.getcurrntLataLong().getLongitude()),
                                    Constants.Job.JOB_1,YouEditTextValue);    //call karo

                        } else if (dataItems.getType().equals(Constants.Job.JOB_0)) {

                            call_updatestaus(
                                    dataItems.getId(),
                                    Constants.Decline,
                                    String.valueOf(getLocation.getcurrntLataLong().getLatitude()),
                                    String.valueOf(getLocation.getcurrntLataLong().getLongitude()),
                                    Constants.Job.JOB_0,YouEditTextValue);
                        }

                        //call_updatestaus(dataItems.getId(), Constants.Decline, String.valueOf(getLocation.getcurrntLataLong().getLatitude()), String.valueOf(getLocation.getcurrntLataLong().getLongitude()));
                    } else {
                        Log.e(TAG, "current location is null");
                        getLocation = new GetLocation(JobPopUpDialog.this);
                        //call_updatestaus(dataItems.getId(), Constants.Decline, String.valueOf(getLocation.getcurrntLataLong().getLatitude()), String.valueOf(getLocation.getcurrntLataLong().getLongitude()));
                        if (dataItems.getType().equals(Constants.Job.JOB_1)) {

                            Log.e(TAG, "responseData.get(position).getSubJobid() : " + dataItems.getSubjobid());
                            Log.e(TAG, "responseData.get(position).getid() : " + dataItems.getId());

                            call_updatestaus(
                                    dataItems.getSubjobid(),
                                    Constants.Decline,
                                    String.valueOf(getLocation.getcurrntLataLong().getLatitude()),
                                    String.valueOf(getLocation.getcurrntLataLong().getLongitude()),
                                    Constants.Job.JOB_1,YouEditTextValue);    //call karo

                        } else if (dataItems.getType().equals(Constants.Job.JOB_0)) {

                            call_updatestaus(
                                    dataItems.getId(),
                                    Constants.Decline,
                                    String.valueOf(getLocation.getcurrntLataLong().getLatitude()),
                                    String.valueOf(getLocation.getcurrntLataLong().getLongitude()),
                                    Constants.Job.JOB_0,YouEditTextValue);
                        }
                    }
                }else {
                    edittext.setError("Required");
                    return;
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }

}


