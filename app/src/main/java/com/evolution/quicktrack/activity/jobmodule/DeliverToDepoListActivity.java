package com.evolution.quicktrack.activity.jobmodule;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static br.com.zbra.androidlinq.Linq.stream;


public class DeliverToDepoListActivity extends BaseActivity implements Recycler_ItemClickCallBack, Recycler_AcceptedJobClickCallBack {

    @BindView(R.id.recyclerview_manu)
    RecyclerViewEmptySupport recyclerview;
    @BindView(R.id.txt_nodata)
    TextView txtNodata;
    @BindView(R.id.emptyView)
    LinearLayout emptyView;
    private KProgressHUD pd;
    public static String TAG = DeliverToDepoListActivity.class.getSimpleName();
    @BindView(R.id.btn_logout)
    ImageView btnLogout;
    boolean isShowWaitingView = true;

    @BindView(R.id.btn_move_to_depo)
    Button btnMoveToDepo;

    DeliveryToDepoJobList_Adepter custom_manu_adepter;


    @BindView(R.id.date_first)
    Button date_first;

    @BindView(R.id.date_second)
    Button date_second;

    @BindView(R.id.date_third)
    Button date_third;

    @BindView(R.id.old_jobs)
    Button old_jobs;

    int depoid = 0;

    String dateFlag = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        PrefManager prefManager = new PrefManager(this);
        initView();


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");


        date_first.setText("Today \n" + simpleDateFormat.format(calendar.getTime()));
        calendar.add(Calendar.DATE, 1);
        date_second.setText("Tomorrow \n" + simpleDateFormat.format(calendar.getTime()));
        calendar.add(Calendar.DATE, 1);
        date_third.setText("Day after \n Tomorrow");

        date_first.setOnClickListener(onClickListener);
        date_second.setOnClickListener(onClickListener);
        date_third.setOnClickListener(onClickListener);
        old_jobs.setOnClickListener(onClickListener);


        callApi();
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.date_first) {
                dateFlag = "1";
                date_second.setBackgroundColor(Color.WHITE);
                date_third.setBackgroundColor(Color.WHITE);
                date_first.setBackgroundColor(Color.BLACK);
                old_jobs.setBackgroundColor(Color.WHITE);

                date_second.setTextColor(Color.BLACK);
                date_third.setTextColor(Color.BLACK);
                date_first.setTextColor(Color.WHITE);
                old_jobs.setTextColor(Color.BLACK);

            } else if (view.getId() == R.id.date_second) {
                dateFlag = "2";
                date_second.setBackgroundColor(Color.BLACK);
                date_third.setBackgroundColor(Color.WHITE);
                date_first.setBackgroundColor(Color.WHITE);
                old_jobs.setBackgroundColor(Color.WHITE);

                date_second.setTextColor(Color.WHITE);
                date_third.setTextColor(Color.BLACK);
                date_first.setTextColor(Color.BLACK);
                old_jobs.setTextColor(Color.BLACK);

            } else if (view.getId() == R.id.date_third) {
                dateFlag = "3";
                date_second.setBackgroundColor(Color.WHITE);
                date_third.setBackgroundColor(Color.BLACK);
                date_first.setBackgroundColor(Color.WHITE);
                old_jobs.setBackgroundColor(Color.WHITE);

                date_second.setTextColor(Color.BLACK);
                date_third.setTextColor(Color.WHITE);
                date_first.setTextColor(Color.BLACK);
                old_jobs.setTextColor(Color.BLACK);

            } else {
                dateFlag = "0";
                old_jobs.setBackgroundColor(Color.BLACK);
                date_second.setBackgroundColor(Color.WHITE);
                date_third.setBackgroundColor(Color.WHITE);
                date_first.setBackgroundColor(Color.WHITE);

                date_second.setTextColor(Color.BLACK);
                date_third.setTextColor(Color.BLACK);
                date_first.setTextColor(Color.BLACK);
                old_jobs.setTextColor(Color.WHITE);
            }

            try {
                call_jobApi(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_deliverydepo_joblist;
    }


    public void initView() {

        //  btnLogout.setVisibility(View.INVISIBLE);
        Intent i = getIntent();
        depoid = i.getIntExtra("selectedId", 0);
        this.recyclerview.setHasFixedSize(true);
        this.recyclerview.setLayoutManager(new LinearLayoutManager(DeliverToDepoListActivity.this, LinearLayoutManager.VERTICAL, false));
        this.recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setEmptyView(emptyView);

        GetLocation getLocation = new GetLocation(DeliverToDepoListActivity.this);

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


        Call<AcceptedJobListResponse> call = apiService.getAcceptedJobWithDate(prefManager.getLogin_Token(), Constants.API_KEY, prefManager.getTruckId(),
                Constants.Accepted + "," + Constants.Started + "," + Constants.PickUp + "," + Constants.OnRouteToPickup + "," + Constants.AtPickupLocation
                        + "," + Constants.OnRouteToDelivery + "," + Constants.AtDeliveryLocation, prefManager.getId(), dateFlag);
        call.enqueue(new Callback<AcceptedJobListResponse>() {
            @Override
            public void onResponse(Call<AcceptedJobListResponse> call, Response<AcceptedJobListResponse> response) {
                if (responseData.size() > 0)
                    responseData.clear();
                if (pd != null && pd.isShowing())
                    pd.dismiss();
                if (response.body().getStatus().equals(Constants.API_STATUS)) {

                    responseData = response.body().getData();


                } else {
                    if (response.body().getMessage().equals("invalidToken")) {
                        prefManager.logout();

                        Intent intent = new Intent(DeliverToDepoListActivity.this, Login_Activity.class);
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

        custom_manu_adepter = new DeliveryToDepoJobList_Adepter(DeliverToDepoListActivity.this, jobModelList,
                DeliverToDepoListActivity.this, Constants.Job.DILEVERED_JOB);
        recyclerview.setAdapter(custom_manu_adepter);

    }


    @OnClick({R.id.img_home, R.id.btn_logout, R.id.btn_move_to_depo})
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
                btnMoveToDepo.setVisibility(View.GONE);
               /* Intent i = new Intent(DeliverToDepoListActivity.this,DeliverToFleetListActivity.class);
                List<JobModel> acceptedJobs = stream(jobModelList).where(c -> c.isChecked() == true).toList();
                Type listType = new TypeToken<ArrayList<JobModel>>(){}.getType();
                String selectedJObs = new Gson().toJson(acceptedJobs,listType);
                i.putExtra("selectedlist",selectedJObs);
                startActivity(i);
*/
                List<JobModel> acceptedJobs = stream(jobModelList).where(c -> c.isChecked() == true).toList();
                count = acceptedJobs.size() - 1;
                try {
                    call_addJobToFleet(acceptedJobs, depoid, isShowWaitingView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
               /* for (int i = 0; i < acceptedJobs.size(); i++) {

                    if (i == 0) {
                        try {
                            call_addJobToFleet(acceptedJobs.get(i), depoid, isShowWaitingView);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
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
                        Intent i = new Intent(DeliverToDepoListActivity.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        // close this activity
                        finish();
                    }
                }, 5000);*/

                break;

        }
    }

    public void homeDilog() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DeliverToDepoListActivity.this);
        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.wanttogohome));
        alertDialog.setCancelable(false);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(DeliverToDepoListActivity.this, MainActivity.class);
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

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DeliverToDepoListActivity.this);

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure want to logout ?");
        alertDialog.setCancelable(true);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showOdometerDialog(DeliverToDepoListActivity.this);
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
            if (jobModelList.get(position).isChecked()) {
                jobModelList.get(position).setChecked(false);
            } else {
                jobModelList.get(position).setChecked(true);
            }

            custom_manu_adepter.notifyItemChanged(position);

            int count = stream(jobModelList).where(j -> j.isChecked() == true).count();
            if (count > 0) {
                btnMoveToDepo.setVisibility(View.VISIBLE);
            } else {
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

//            JobModel jobModelpickup = new JobModel();
            JobModel jobModeldelivery = new JobModel();

            if (responseData.get(i).getStatus().equals(Constants.Accepted))
                Log.e(TAG, "responseData.get(i).getStatus() : " + responseData.get(i).getStatus());
            else
                Log.e(TAG, "responseData.get(i).getStatus() : " + responseData.get(i).getStatus());

            if ((responseData.get(i).getStatus().equals(Constants.OnRouteToDelivery)
                    || responseData.get(i).getStatus().equals(Constants.AtDeliveryLocation)
                    || responseData.get(i).getStatus().equals(Constants.PickUp))) {

                /*jobModelpickup.setId(Long.parseLong(responseData.get(i).getId()));
                jobModelpickup.setTxtClientName(responseData.get(i).getCustomerName());
                jobModelpickup.setTxtDeliveryAddres(responseData.get(i).getDeliveryLocation());
                jobModelpickup.setTxtPickupAddres(responseData.get(i).getCollectionLocation());
                jobModelpickup.setDiscription(responseData.get(i).getDescription());
                jobModelpickup.setIspickup(true);
                jobModelpickup.setDeliveryName(responseData.get(i).getDeliveryName());
                jobModelpickup.setSorting("PickUp");
                jobModelpickup.setStatus(responseData.get(i).getStatus());
                jobModelpickup.setChecked(false);

                 jobModelpickup.setEnable(true);*/

                jobModeldelivery.setEnable(true);

                jobModeldelivery.setId(Long.parseLong(responseData.get(i).getId()));
                jobModeldelivery.setTxtClientName(responseData.get(i).getCustomerName());
                jobModeldelivery.setTxtDeliveryAddres(responseData.get(i).getDeliveryLocation());
                jobModeldelivery.setTxtPickupAddres(responseData.get(i).getCollectionLocation());
                jobModeldelivery.setDiscription(responseData.get(i).getDescription());
                jobModeldelivery.setDeliveryName(responseData.get(i).getDeliveryName());
                jobModeldelivery.setIspickup(false);
                jobModeldelivery.setSorting("Drop");
                jobModeldelivery.setChecked(false);
                jobModeldelivery.setStatus(responseData.get(i).getStatus());
                jobModeldelivery.setSubjobId(responseData.get(i).getSubjobid());
                jobModels.add(jobModeldelivery);
            } else {
                jobModeldelivery.setEnable(false);
            }
        }
        jobModelList = jobModels;
        setAddapter();
    }

    @Override
    protected void onDestroy() {
        LogCustom.logd("xxxxxxxx allocatedjob", "onDestroy");
        super.onDestroy();

    }


    private void call_addJobToFleet(JobModel jobModel, int dropid, boolean isFirstTime) throws Exception {
        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);


        if (isFirstTime) {
            pd = LogCustom.loderProgressDialog2(this);
            pd.show();
            isShowWaitingView = false;

        }

        PrefManager prefManager = new PrefManager(getApplicationContext());

        Log.d("login_token : ", prefManager.getLogin_Token());
        Log.d("APIKEY : ", Constants.API_KEY);
        Log.d("driverid : ", prefManager.getId());
        Log.d("action : ", "addJobToFleet");
        Log.d("jobid ", String.valueOf(jobModel.getId()));
        Log.d("vehicle_id ", String.valueOf(prefManager.getTruckId()));
        Log.d("pickupFromType ", "0");
        Log.d("deliverFleetid ", "" + dropid);

        Call<AddToFleetRespose> call = apiService.addJobToFleetDeliver(prefManager.getLogin_Token(),
                Constants.API_KEY, prefManager.getId(), "addJobToFleet",
                String.valueOf(jobModel.getId()),
                String.valueOf(prefManager.getTruckId()),
                "0", dropid, String.valueOf(jobModel.getSubjobId()));
        call.enqueue(new Callback<AddToFleetRespose>() {
            @Override
            public void onResponse(Call<AddToFleetRespose> call, Response<AddToFleetRespose> response) {

                if (pd != null && pd.isShowing())
                    pd.dismiss();

                Log.e(TAG, "response.body().getData() : " + response.body().getData());
                isShowWaitingView = true;
                if (response.body().getStatus().equals("true")) {

                } else {
                    if (response.body().getMessage().equals("invalidToken")) {
                        prefManager.logout();

                        Intent intent = new Intent(DeliverToDepoListActivity.this, Login_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }

                }

                setDataOrder();
            }

            @Override
            public void onFailure(Call<AddToFleetRespose> call, Throwable t) {
                LogCustom.logd(TAG, t.toString());
                if (pd != null && pd.isShowing())
                    pd.dismiss();
                isShowWaitingView = false;
            }
        });
    }


    //New implementation
    private static int count = 0;

    private void call_addJobToFleet(List<JobModel> jobModel, int dropid, boolean isFirstTime) throws Exception {
        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);


        if (isFirstTime) {
            pd = LogCustom.loderProgressDialog2(this);
            pd.show();
            isShowWaitingView = false;
        }

        PrefManager prefManager = new PrefManager(getApplicationContext());

        Log.d("login_token : ", prefManager.getLogin_Token());
        Log.d("APIKEY : ", Constants.API_KEY);
        Log.d("driverid : ", prefManager.getId());
        Log.d("action : ", "addJobToFleet");
        Log.d("jobid ", String.valueOf(jobModel.get(count).getId()));
        Log.d("vehicle_id ", String.valueOf(prefManager.getTruckId()));
        Log.d("pickupFromType ", "0");
        Log.d("deliverFleetid ", "" + dropid);

        Call<AddToFleetRespose> call = apiService.addJobToFleetDeliver(prefManager.getLogin_Token(),
                Constants.API_KEY, prefManager.getId(), "addJobToFleet",
                String.valueOf(jobModel.get(count).getId()),
                String.valueOf(prefManager.getTruckId()),
                "0", dropid, String.valueOf(jobModel.get(count).getSubjobId()));
        call.enqueue(new Callback<AddToFleetRespose>() {
            @Override
            public void onResponse(Call<AddToFleetRespose> call, Response<AddToFleetRespose> response) {


                Log.e(TAG, "response.body().getData() : " + response.body().getData());
                if (response.body().getStatus().equals("true")) {

                } else {
                    if (response.body().getMessage().equals("invalidToken")) {
                        prefManager.logout();

                        Intent intent = new Intent(DeliverToDepoListActivity.this, Login_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }

                }
                try {
                    count--;
                    if (count < 0) {
                        Intent i = new Intent(DeliverToDepoListActivity.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        // close this activity
                        finish();
                        pd.dismiss();

                    } else {
                        call_addJobToFleet(jobModel, dropid, isFirstTime);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                setDataOrder();
            }

            @Override
            public void onFailure(Call<AddToFleetRespose> call, Throwable t) {
                count--;

                if (count == 0) {
                    Intent i = new Intent(DeliverToDepoListActivity.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    // close this activity
                    finish();
                    pd.dismiss();

                } else {
                    try {
                        call_addJobToFleet(jobModel, dropid, isFirstTime);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                LogCustom.logd(TAG, t.toString());

            }
        });
    }
}