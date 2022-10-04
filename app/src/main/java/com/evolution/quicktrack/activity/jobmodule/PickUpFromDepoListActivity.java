package com.evolution.quicktrack.activity.jobmodule;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.evolution.quicktrack.BaseActivity;
import com.evolution.quicktrack.Login_Activity;
import com.evolution.quicktrack.MainActivity;
import com.evolution.quicktrack.QuickTrackAplication;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.adapter.DeliveryToDepoJobList_Adepter;
import com.evolution.quicktrack.callback.Recycler_AcceptedJobClickCallBack;
import com.evolution.quicktrack.callback.Recycler_ItemClickCallBack;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.location.GetLocation;
import com.evolution.quicktrack.model.JobModel;
import com.evolution.quicktrack.network.ApiCallInterface;
import com.evolution.quicktrack.network.ApiClient;
import com.evolution.quicktrack.response.acceptedjobList.AcceptedJob;
import com.evolution.quicktrack.response.acceptedjobList.AcceptedJobListResponse;
import com.evolution.quicktrack.response.fleetres.AddToFleetRespose;
import com.evolution.quicktrack.util.PrefManager;
import com.evolution.quicktrack.util.RecyclerViewEmptySupport;
import com.evolution.quicktrack.util.Utils;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static br.com.zbra.androidlinq.Linq.stream;


public class PickUpFromDepoListActivity extends BaseActivity implements Recycler_ItemClickCallBack, Recycler_AcceptedJobClickCallBack {

    public static String TAG = PickUpFromDepoListActivity.class.getSimpleName();

    @BindView(R.id.recyclerview_manu)
    RecyclerViewEmptySupport recyclerview;
    @BindView(R.id.txt_nodata)
    TextView txtNodata;
    @BindView(R.id.emptyView)
    LinearLayout emptyView;
    private KProgressHUD pd;
    @BindView(R.id.btn_logout)
    ImageView btnLogout;

    int depoid = 0;

    @BindView(R.id.btn_move_to_depo)
    Button btnMoveToDepo;

    boolean isShowWaitingView = true;
    DeliveryToDepoJobList_Adepter custom_manu_adepter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        PrefManager prefManager = new PrefManager(this);
            initView();
            callApi();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_pickup_from_depo_joblist;
    }

    public void initView() {

        //  btnLogout.setVisibility(View.INVISIBLE);
        Intent i = getIntent();
        depoid = i.getIntExtra("selectedId",0);
        this.recyclerview.setHasFixedSize(true);
        this.recyclerview.setLayoutManager(new LinearLayoutManager(PickUpFromDepoListActivity.this, LinearLayoutManager.VERTICAL, false));
        this.recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setEmptyView(emptyView);
        GetLocation getLocation = new GetLocation(PickUpFromDepoListActivity.this);
    }

    public void callApi() {
        try {
            if (Utils.validInternet(getApplicationContext()))
                call_jobApi(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<AcceptedJob> responseData = new ArrayList<>();
    private void call_jobApi(boolean isFirstTime) throws Exception {
        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);

        if (isFirstTime) {
            pd = LogCustom.loderProgressDialog2(this);
            pd.show();

        }

        PrefManager prefManager = new PrefManager(getApplicationContext());


        Call<AcceptedJobListResponse> call = apiService.getAcceptedJob(prefManager.getLogin_Token(), Constants.API_KEY, prefManager.getTruckId(),
                Constants.Accepted + "," + Constants.Started + "," + Constants.PickUp + "," + Constants.OnRouteToPickup + "," + Constants.AtPickupLocation
                        + "," + Constants.OnRouteToDelivery + "," + Constants.AtDeliveryLocation, prefManager.getId());
        call.enqueue(new Callback<AcceptedJobListResponse>() {
            @Override
            public void onResponse(Call<AcceptedJobListResponse> call, Response<AcceptedJobListResponse> response) {

                if (pd != null && pd.isShowing())
                    pd.dismiss();
                if (response.body().getStatus().equals(Constants.API_STATUS)) {

                    responseData = response.body().getData();


                } else {
                    if (response.body().getMessage().equals("invalidToken")) {
                        prefManager.logout();

                        Intent intent = new Intent(PickUpFromDepoListActivity.this, Login_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }

                }

                setDataOrder();
            }

            @Override
            public void onFailure(Call<AcceptedJobListResponse> call, Throwable t) {
                LogCustom.logd(TAG, t.toString());
                if (pd != null && pd.isShowing())
                    pd.dismiss();
            }
        });
    }


    public void setAddapter() {

        /*List<JobModel> tempJobModelList = jobModelList;
        jobModelList.clear();
        for (int i = 0; i < tempJobModelList.size(); i++){
            if (tempJobModelList.get(i).isIspickup()){
                jobModelList.add(tempJobModelList.get(i));
            }
        }*/

         custom_manu_adepter = new DeliveryToDepoJobList_Adepter(PickUpFromDepoListActivity.this,
                 jobModelList, PickUpFromDepoListActivity.this,
                 Constants.Job.PICKED_JOB);
        recyclerview.setAdapter(custom_manu_adepter);

    }


    @OnClick({R.id.img_home, R.id.btn_logout,R.id.btn_move_to_depo})
    public void onViewClicked2(View view) {
        switch (view.getId()) {
            case R.id.img_home:
                onBackPressed();
                //homeDilog();

                break;
            case R.id.btn_logout:
                logOutDialog();
                break;

            case R.id.btn_move_to_depo:


                List<JobModel> acceptedJobs = stream(jobModelList).where(c -> c.isChecked() == true).toList();
                for(int i=0;i<acceptedJobs.size();i++) {

                    if(i == 0) {
                        try {
                            call_addJobToFleet(acceptedJobs.get(i), depoid, isShowWaitingView);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        try {
                            call_addJobToFleet(acceptedJobs.get(i), depoid, isShowWaitingView);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                new Handler().postDelayed(new Runnable() {
                     // Showing splash screen with a timer. This will be useful when you
                     //want to show case your app logo / company
                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        // Start your app main activity
                        Intent i = new Intent(PickUpFromDepoListActivity.this,AcceptedJobActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        // close this activity
                        finish();
                    }
                }, 5000);




                break;

        }
    }

    public void homeDilog() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PickUpFromDepoListActivity.this);
        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.wanttogohome));
        alertDialog.setCancelable(false);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(PickUpFromDepoListActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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


    public void logOutDialog() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PickUpFromDepoListActivity.this);

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure want to logout ?");
        alertDialog.setCancelable(true);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showOdometerDialog(PickUpFromDepoListActivity.this);
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
    public void OnItemClick(int position) {
        try {
            //call_jobDetailsApi(String.valueOf(jobModelList.get(position).getId()));
            if(jobModelList.get(position).isChecked())
            {
                jobModelList.get(position).setChecked(false);
            }
            else
            {
                jobModelList.get(position).setChecked(true);
            }

            custom_manu_adepter.notifyItemChanged(position);

            int count = stream(jobModelList).where(j -> j.isChecked() == true).count();
            if(count > 0)
            {
                btnMoveToDepo.setVisibility(View.VISIBLE);
            }
            else
            {
                btnMoveToDepo.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnStartJobItemClick(int position) {

    }


    private List<JobModel> jobModelList = new ArrayList<>();

    public void setDataOrder() {


        jobModelList.clear();

        List<JobModel> jobModels = new ArrayList<>();

        for (int i = 0; i < responseData.size(); i++) {

            JobModel jobModelpickup = new JobModel();
//            JobModel jobModeldelivery = new JobModel();


            if ((responseData.get(i).getStatus().equals(Constants.Accepted)
                    || responseData.get(i).getStatus().equals(Constants.Started)
                    || responseData.get(i).getStatus().equals(Constants.OnRouteToPickup)
                    || responseData.get(i).getStatus().equals(Constants.AtPickupLocation))
                    && responseData.get(i).getType().equals(Constants.Job.JOB_INT_1)) {

                jobModelpickup.setId(Long.parseLong(responseData.get(i).getId()));
                jobModelpickup.setTxtClientName(responseData.get(i).getCustomerName());
                jobModelpickup.setTxtDeliveryAddres(responseData.get(i).getDeliveryLocation());
                jobModelpickup.setTxtPickupAddres(responseData.get(i).getCollectionLocation());
                jobModelpickup.setDiscription(responseData.get(i).getDescription());
                jobModelpickup.setIspickup(true);
                jobModelpickup.setDeliveryName(responseData.get(i).getDeliveryName());
                jobModelpickup.setSorting("PickUp");
                jobModelpickup.setStatus(responseData.get(i).getStatus());
                jobModelpickup.setChecked(false);
                jobModelpickup.setType(responseData.get(i).getType());

                Log.e(TAG, "id : " + responseData.get(i).getId());
                Log.e(TAG, "getSubjobId : "+ responseData.get(i).getSubjobid());
                Log.e(TAG, "type : " + responseData.get(i).getType());

                if(QuickTrackAplication.IsNotNull(responseData.get(i).getSubjobid()))
                {
                    jobModelpickup.setSubjobId(responseData.get(i).getSubjobid());
                }

                 jobModelpickup.setEnable(true);

//                jobModeldelivery.setEnable(false);
                jobModels.add(jobModelpickup);
            } else {
//                jobModeldelivery.setEnable(true);
            }


           /* jobModeldelivery.setId(Long.parseLong(responseData.get(i).getId()));
            jobModeldelivery.setTxtClientName(responseData.get(i).getCustomerName());
            jobModeldelivery.setTxtDeliveryAddres(responseData.get(i).getDeliveryLocation());
            jobModeldelivery.setTxtPickupAddres(responseData.get(i).getCollectionLocation());
            jobModeldelivery.setDiscription(responseData.get(i).getDescription());
            jobModeldelivery.setDeliveryName(responseData.get(i).getDeliveryName());
            jobModeldelivery.setIspickup(false);
            jobModeldelivery.setSorting("Drop");
            jobModeldelivery.setChecked(false);
            jobModeldelivery.setStatus(responseData.get(i).getStatus());


            jobModels.add(jobModeldelivery);*/
        }
        jobModelList = jobModels;

        setAddapter();


    }

    @Override
    protected void onDestroy() {
        LogCustom.logd("xxxxxxxx allocatedjob","onDestroy");
        super.onDestroy();

    }

    private void call_addJobToFleet(JobModel jobModel, int dropid, boolean isFirstTime) throws Exception {

        if(QuickTrackAplication.IsNotNull(jobModel.getSubjobId())) {

            ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);

            if (isFirstTime) {
                isShowWaitingView = false;
                pd = LogCustom.loderProgressDialog2(this);
                pd.show();
            }

            PrefManager prefManager = new PrefManager(getApplicationContext());
            Log.e(TAG, "prefManager.getLogin_Token() : " + prefManager.getLogin_Token());
            Log.e(TAG, "Constants.API_KEY : " + Constants.API_KEY);
            Log.e(TAG, "prefManager.getId() : " + prefManager.getId());
            Log.e(TAG, "action : " + "addJobToFleet");
            Log.e(TAG, "String.valueOf(jobModel.getId()) : " + String.valueOf(jobModel.getSubjobId()));
            Log.e(TAG, "String.valueOf(prefManager.getTruckId()) : " + String.valueOf(prefManager.getTruckId()));
            Log.e(TAG, "jobModel.getType() : " + Constants.Job.PICK_UP_FORM_TYPE_1);//jobModel.getType()
            Log.e(TAG, "dropid : " + dropid);
            Log.e(TAG, "deliverFleetid : " + Constants.Job.DELIVER_FLEET_ID_0);

            Call<AddToFleetRespose> call = apiService.addJobToFleetPickup(
                    prefManager.getLogin_Token(),
                    Constants.API_KEY,
                    prefManager.getId(),
                    "addJobToFleet",
                    String.valueOf(jobModel.getSubjobId()),
                    String.valueOf(prefManager.getTruckId()),
                    Constants.Job.PICK_UP_FORM_TYPE_1,//jobModel.getType()
                    dropid,
                    Constants.Job.DELIVER_FLEET_ID_0
                    );
            call.enqueue(new Callback<AddToFleetRespose>() {
                @Override
                public void onResponse(Call<AddToFleetRespose> call, Response<AddToFleetRespose> response) {

                    Log.e(TAG, "add to fleet pickup from depo list response : " + response.body().toString());
                    Log.e(TAG, "response.body().getStatus() : " + response.body().getStatus());
                    Log.e(TAG, "response.body().getMessage() : " + response.body().getMessage());


                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                        isShowWaitingView = true;
                    }

                    if (response.body().getStatus().equals("true")) {
                        if (jobModel.getType().equals(Constants.Job.JOB_INT_1)) {
                            updatejobstatus(jobModel,isFirstTime);
                            updatejobStatus(jobModel,isFirstTime);
                        }
                    } else {
                        if (response.body().getMessage().equals("invalidToken")) {
                            prefManager.logout();

                            Intent intent = new Intent(PickUpFromDepoListActivity.this, Login_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }

                }

                @Override
                public void onFailure(Call<AddToFleetRespose> call, Throwable t) {
                    LogCustom.logd(TAG, t.toString());
                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                        isShowWaitingView = false;
                    }
                }
            });
        }
    }

    public void updatejobstatus(JobModel jobModel,boolean isFirstTime)
    {
        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);

        if (isFirstTime) {
            isShowWaitingView = false;
            pd = LogCustom.loderProgressDialog2(this);
            pd.show();
        }

        Log.e(TAG, "Update String.valueOf(jobModel.getId()) : " + String.valueOf(jobModel.getSubjobId()));
        PrefManager prefManager = new PrefManager(getApplicationContext());

        Log.e(TAG, "prefManager.getLogin_Token() : " + prefManager.getLogin_Token());
        Log.e(TAG, "Constants.API_KEY : " + Constants.API_KEY);
        Log.e(TAG, "prefManager.getId() : " + prefManager.getId());
        Log.e(TAG, "updateSubJob : " + "updateSubJob");
        Log.e(TAG, "String.valueOf(jobModel.getSubjobId()) : " + String.valueOf(jobModel.getSubjobId()));
        Log.e(TAG, "String.valueOf(prefManager.getTruckId()) : " + String.valueOf(prefManager.getTruckId()));
        Log.e(TAG, "Constants.PickUp : " + Constants.PickUp);
        RequestBody logintoken = RequestBody.create(MediaType.parse("multipart/form-data"),prefManager.getLogin_Token());
        RequestBody APIKEY = RequestBody.create(MediaType.parse("multipart/form-data"),Constants.API_KEY);
        RequestBody ID = RequestBody.create(MediaType.parse("multipart/form-data"),prefManager.getId());
        RequestBody status = RequestBody.create(MediaType.parse("multipart/form-data"),"updateSubJob");
        RequestBody subjob_id = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(jobModel.getSubjobId()));
        RequestBody truckid = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(prefManager.getTruckId()));
        RequestBody pickupstatus = RequestBody.create(MediaType.parse("multipart/form-data"),Constants.PickUp);

        Call<AddToFleetRespose> callupdate = apiService.updateFleetPickup(
                logintoken,
                APIKEY,
                ID,
                status,
                subjob_id,
                truckid,
                pickupstatus
        );  callupdate.enqueue(new Callback<AddToFleetRespose>() {
            @Override
            public void onResponse(Call<AddToFleetRespose> call, Response<AddToFleetRespose> response) {

                Log.e(TAG, " updatejobstatus API called and pickup from depo list response : " + response.body().getData().toString());
                Log.e(TAG, "response.body().getStatus() : " + response.body().getStatus());
                Log.e(TAG, "response.body().getMessage() : " + response.body().getMessage());

                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                    isShowWaitingView = true;
                }
                if (response.body().getStatus().equals("true")) {

                } else {
                    if (response.body().getMessage().equals("invalidToken")) {
                        prefManager.logout();

                        Intent intent = new Intent(PickUpFromDepoListActivity.this, Login_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddToFleetRespose> call, Throwable t) {
                LogCustom.logd(TAG, t.toString());
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                    isShowWaitingView = false;
                }
            }
        });
    }

    public void updatejobStatus(JobModel jobModel,boolean isFirstTime)
    {
        OkHttpClient client = new OkHttpClient();

        /*RequestBody body = new FormBody.Builder()
                .add("APIKEY", "d6Zwj9vVNqt9QZGj")
                .add("driverid", "105")
                .add("login_token", "IAgAxu4J08")
                .add("vehicleid", "36")
                .add("status", "6")
                .add("action", "updateSubJob")
                .add("subjobId", "8")
                .build();*/
        PrefManager prefManager = new PrefManager(getApplicationContext());
//        String subjobid = jobModel.getType()==0 ? ""+jobModel.getId():String.valueOf(jobModel.getSubjobId());
//        Log.e(TAG, "subjobid : " + subjobid);
        RequestBody body = new FormBody.Builder()
                .add("APIKEY", Constants.API_KEY)
                .add("driverid", prefManager.getId())
                .add("login_token", prefManager.getLogin_Token())
                .add("vehicleid", String.valueOf(prefManager.getTruckId()))
                .add("status", Constants.PickUp)
                .add("action", "updateSubJob")
                .add("subjobId", String.valueOf(jobModel.getSubjobId()))
                .build();

        Request request = new Request.Builder()
                .url("https://app.quicktrac.com.au/application/api/fleets.php")
                .post(body)
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                Log.e(TAG, "updateJobStatue Response : " + response.body().string());
            }
        });
    }
}