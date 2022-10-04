package com.evolution.quicktrack.activity.jobmodule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.evolution.quicktrack.BaseActivity;
import com.evolution.quicktrack.Login_Activity;
import com.evolution.quicktrack.MainActivity;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.location.GetLocation;
import com.evolution.quicktrack.network.ApiCallInterface;
import com.evolution.quicktrack.network.ApiClient;
import com.evolution.quicktrack.response.common.CommonResponse;
import com.evolution.quicktrack.response.jobDetails.JobDetailsResponse;
import com.evolution.quicktrack.util.PrefManager;
import com.evolution.quicktrack.util.Utils;
import com.kaopiz.kprogresshud.KProgressHUD;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 4/2/2018.
 */

public class Pickup_StartJob_Activity extends BaseActivity {

    @BindView(R.id.img_home)
    ImageView imgHome;
    @BindView(R.id.btn_logout)
    ImageView btnLogout;

    private GetLocation getLocation;

    private KProgressHUD pd;
    public static String TAG = Pickup_StartJob_Activity.class.getSimpleName();
    String jobId="";

    private String destinationLatitude="";
    private String destinationLongitude="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView(R.layout.activity_odometer);
        ButterKnife.bind(this);


       // btnLogout.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void onStart() {
        super.onStart();

        getLocation  =new GetLocation(Pickup_StartJob_Activity.this);
        initView();


    }
    public void initView() {



        getIntendata();

    }

    public void getIntendata(){


        if(getIntent().getStringExtra(Constants.EXTRA_JOBID)!=null){

            jobId=getIntent().getStringExtra(Constants.EXTRA_JOBID);
        }
        callApi();
    }

    public void callApi(){

        if(Utils.validInternet(getApplicationContext())){
            try {

                call_jobdetailsApi();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }



    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_pickup_start_location;
    }


    @OnClick({R.id.img_home, R.id.btn_logout,R.id.img_start})
    public void onViewClicked2(View view) {
        switch (view.getId()) {
            case R.id.img_home:
                onBackPressed();// homeDilog();
                break;
            case R.id.btn_logout:
                logOutDialog();

                break;

            case R.id.img_start:

                validLocation();


                if(getLocation.getcurrntLataLong()!=null) {
                    call_updatestaus(jobId,Constants.AtPickupLocation,String.valueOf(getLocation.getcurrntLataLong().getLatitude()),String.valueOf(getLocation.getcurrntLataLong().getLongitude()),true);

                }else {
                    LogCustom.logd("xxxxx","getlocation");
                    getLocation  =new GetLocation(Pickup_StartJob_Activity.this);
                }
              /*  Intent intent=new Intent(Pickup_StartJob_Activity.this,JobDetailsPickup_Activity.class);
                intent.putExtra(Constants.EXTRA_JOBID,jobId);
                startActivity(intent);*/
                break;
        }
    }





    private void call_jobdetailsApi( )  {


        pd = LogCustom.loderProgressDialog2(this);
        pd.show();

        PrefManager prefManager=new PrefManager(getApplicationContext());
        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);

        Call<JobDetailsResponse> call = apiService.getjobDetails(prefManager.getLogin_Token(),Constants.API_KEY,jobId,prefManager.getId());
        call.enqueue(new Callback<JobDetailsResponse>() {
            @Override
            public void onResponse(Call<JobDetailsResponse> call, Response<JobDetailsResponse> response) {

                try {
                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                    Log.d(TAG, " : check status : " + response.body().toString());


                    if (response.body().getStatus().equals(Constants.API_STATUS)) {

                        destinationLatitude=response.body().getData().getCollectionLatitude();
                        destinationLongitude=response.body().getData().getDeliveryLongitude();



                    }
                    else
                    {

                        if(response.body().getMessage().equals("invalidToken"))
                        {
                            prefManager.logout();

                            Intent   intent =new Intent(Pickup_StartJob_Activity.this,Login_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                }catch (Exception e){}
            }

            @Override
            public void onFailure(Call<JobDetailsResponse> call, Throwable t) {
                LogCustom.logd(TAG, t.toString());
                if (pd != null && pd.isShowing())
                    pd.dismiss();
            }
        });
    }



    public boolean validLocation(){
        if(getLocation.getcurrntLataLong()!=null){
            Location startPoint=getLocation.getcurrntLataLong();
            Location endPoint=new Location("locationA");
            endPoint.setLatitude(Double.parseDouble(destinationLatitude));
            endPoint.setLongitude(Double.parseDouble(destinationLongitude));
            return distance(startPoint.getLatitude(),startPoint.getLongitude(),endPoint.getLatitude(),endPoint.getLongitude())<1.5;
        }else {
            getLocation  =new GetLocation(Pickup_StartJob_Activity.this);
            return false;
        }

    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }










    private void call_updatestaus(String jobid, String statusid, String latitude, String longitude , final boolean isIntencall )  {
        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);
        PrefManager prefManager = new PrefManager(getApplicationContext());
        Call<CommonResponse> call = apiService.updateStatus(prefManager.getLogin_Token(),Constants.API_KEY, prefManager.getId(),jobid,statusid,latitude,longitude,prefManager.getTruckId(),"","0");
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                try {


                    Log.d(TAG, " : check status : " + response.body().toString());


                    if (response.body().getStatus().equals(Constants.API_STATUS)) {

                        if(isIntencall){
                            Intent intent=new Intent(Pickup_StartJob_Activity.this,JobDetailsPickup_Activity.class);
                            intent.putExtra(Constants.EXTRA_JOBID,jobId);
                            startActivity(intent);
                        }

                    }
                    else
                    {
                        if(response.body().getMessage().equals("invalidToken"))
                        {
                            prefManager.logout();

                            Intent   intent =new Intent(Pickup_StartJob_Activity.this,Login_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }

                    }
                }catch (Exception e){}
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                LogCustom.logd(TAG, t.toString());

            }
        });
    }


    public  void homeDilog(){

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Pickup_StartJob_Activity.this);
        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.wanttogohome));
        alertDialog.setCancelable(false);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(Pickup_StartJob_Activity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


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


    public  void logOutDialog(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Pickup_StartJob_Activity.this);
        // Setting Dialog Message
        alertDialog.setMessage("Are you sure want to logout ?");
        alertDialog.setCancelable(true);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showOdometerDialog(Pickup_StartJob_Activity.this);
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            showToast(""+getString(R.string.neverback));
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }


}
