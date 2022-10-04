package com.evolution.quicktrack.activity.jobmodule;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.evolution.quicktrack.BaseActivity;
import com.evolution.quicktrack.Login_Activity;
import com.evolution.quicktrack.MainActivity;
import com.evolution.quicktrack.QuickTrackAplication;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.adapter.Custom_AllocatedJobList_Adepter;
import com.evolution.quicktrack.allocationjob.AllocationJobContractor;
import com.evolution.quicktrack.allocationjob.AllocationJobRequestEntity;
import com.evolution.quicktrack.allocationjob.AllocationPresenter;
import com.evolution.quicktrack.callback.Recycler_AllocatedJobClickCallBack;
import com.evolution.quicktrack.callback.Recycler_ItemClickCallBack;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.location.GetLocation;
import com.evolution.quicktrack.network.ApiCallInterface;
import com.evolution.quicktrack.network.ApiClient;
import com.evolution.quicktrack.response.allocatedJobList.AllocatedJobListResponse;
import com.evolution.quicktrack.response.allocatedJobList.JobItem;
import com.evolution.quicktrack.response.common.CommonResponse;
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

public class AllocatedJobActivity extends BaseActivity
        implements Recycler_ItemClickCallBack, Recycler_AllocatedJobClickCallBack, AllocationJobContractor.View {

    @BindView(R.id.recyclerview_manu)
    RecyclerViewEmptySupport recyclerview;
    @BindView(R.id.txt_nodata)
    TextView txtNodata;
    @BindView(R.id.emptyView)
    LinearLayout emptyView;
    private KProgressHUD pd;
    public static String TAG = Login_Activity.class.getSimpleName();
    @BindView(R.id.btn_logout)
    ImageView btnLogout;

    @BindView(R.id.date_first)
    Button date_first;

    @BindView(R.id.date_second)
    Button date_second;

    @BindView(R.id.date_third)
    Button date_third;

    @BindView(R.id.old_jobs)
    Button old_jobs;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    private GetLocation getLocation;
    private PrefManager prefManager;
    private CountDownTimer countDownTimer = null;
    private List<JobItem> responseData = new ArrayList<>();

    String dateFlag = "1";

    private AllocationJobContractor.Presenter presenter;

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
                callGetTrackApi();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        presenter = new AllocationPresenter(this);

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


        initView();
        callApi();

        swipeContainer.setOnRefreshListener(() -> callApi());
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_allocated_job;
    }


    public void initView() {
        btnLogout.setVisibility(View.INVISIBLE);
        this.recyclerview.setHasFixedSize(true);
        this.recyclerview.setLayoutManager(new LinearLayoutManager(AllocatedJobActivity.this, LinearLayoutManager.VERTICAL, false));
        this.recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setEmptyView(emptyView);
        getLocation = new GetLocation(AllocatedJobActivity.this);
        prefManager = new PrefManager(getApplicationContext());
    }


    public void callApi() {
        try {
            if (Utils.validInternet(getApplicationContext()))
                callGetTrackApi();
        } catch (Exception e) {
            e.printStackTrace();
        }

        startTimer();
    }

    private void callGetTrackApi() {
        showProgressDialogue();
        AllocationJobRequestEntity allocationJobRequestEntity = new AllocationJobRequestEntity();
        allocationJobRequestEntity.setApikey(Constants.API_KEY);
        allocationJobRequestEntity.setDateFlag(dateFlag);
        allocationJobRequestEntity.setDriverid(prefManager.getId());
        allocationJobRequestEntity.setLogin_token(prefManager.getLogin_Token());
        allocationJobRequestEntity.setVehicleid(prefManager.getTruckId());
        allocationJobRequestEntity.setStatus(Constants.Assigned);
        presenter.getAllocationJobs(allocationJobRequestEntity);
    }


    public void setAddapter() {
        Custom_AllocatedJobList_Adepter custom_manu_adepter = new Custom_AllocatedJobList_Adepter(AllocatedJobActivity.this, responseData, AllocatedJobActivity.this);
        recyclerview.setAdapter(custom_manu_adepter);
    }


    @Override
    public void OnItemClick(int position) {
    }

    @Override
    public void OnAcceptItemClick(int position) {
        if (getLocation.getcurrntLataLong() != null) {
            if (responseData.get(position).getType().equals(Constants.Job.JOB_1)) {
                call_updatestaus(
                        responseData.get(position).getSubjobid(),
                        Constants.Accepted,
                        String.valueOf(getLocation.getcurrntLataLong().getLatitude()),
                        String.valueOf(getLocation.getcurrntLataLong().getLongitude()),
                        Constants.Job.JOB_1, "");    //call karo

            } else if (responseData.get(position).getType().equals(Constants.Job.JOB_0)) {
                call_updatestaus(
                        responseData.get(position).getId(),
                        Constants.Accepted,
                        String.valueOf(getLocation.getcurrntLataLong().getLatitude()),
                        String.valueOf(getLocation.getcurrntLataLong().getLongitude()),
                        Constants.Job.JOB_0, "");
            }
        } else {
            getLocation = new GetLocation(AllocatedJobActivity.this);
        }
    }

    private void openFutileDialogue(int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        alert.setMessage("Enter Your Decline Message");
        // alert.setTitle("");
        alert.setView(edittext);
        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String YouEditTextValue = edittext.getText().toString();
                if (YouEditTextValue != null && YouEditTextValue.trim().length() > 0) {
                    ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);
                    PrefManager prefManager = new PrefManager(getApplicationContext());
                  /*  pd = LogCustom.loderProgressDialog2(AllocatedJobActivity.this);
                    pd.show();*/


                    if (getLocation.getcurrntLataLong() != null) {
                        if (responseData.get(position).getType().equals(Constants.Job.JOB_1)) {
                            call_updatestaus(responseData.get(position).getSubjobid(),
                                    Constants.Decline,
                                    String.valueOf(getLocation.getcurrntLataLong().getLatitude()),
                                    String.valueOf(getLocation.getcurrntLataLong().getLongitude()),
                                    Constants.Job.JOB_1, YouEditTextValue);
                        } else if (responseData.get(position).getType().equals(Constants.Job.JOB_0)) {
                            call_updatestaus(responseData.get(position).getId(),
                                    Constants.Decline,
                                    String.valueOf(getLocation.getcurrntLataLong().getLatitude()),
                                    String.valueOf(getLocation.getcurrntLataLong().getLongitude()),
                                    Constants.Job.JOB_0, YouEditTextValue);
                        }
                    } else {
                        getLocation = new GetLocation(AllocatedJobActivity.this);
                        if (responseData.get(position).getId().equals(Constants.Job.JOB_1)) {
                            call_updatestaus(responseData.get(position).getSubjobid(),
                                    Constants.Decline,
                                    String.valueOf(getLocation.getcurrntLataLong().getLatitude()),
                                    String.valueOf(getLocation.getcurrntLataLong().getLongitude()),
                                    Constants.Job.JOB_1, YouEditTextValue);
                        } else if (responseData.get(position).getId().equals(Constants.Job.JOB_0)) {
                            call_updatestaus(responseData.get(position).getId(),
                                    Constants.Decline,
                                    String.valueOf(getLocation.getcurrntLataLong().getLatitude()),
                                    String.valueOf(getLocation.getcurrntLataLong().getLongitude()),
                                    Constants.Job.JOB_0, YouEditTextValue);
                        }
                    }


                } else {
                    edittext.setError("Required");
                    return;
                }
            }
        });

        alert.setNegativeButton("Cancel", (dialog, whichButton) -> {
            // what ever you want to do with No option.
        });

        alert.show();
    }

    @Override
    public void OnDeclineItemClick(int position) {

        openFutileDialogue(position);


    }

    @OnClick({R.id.img_home, R.id.btn_logout, R.id.img_refresh})
    public void onViewClicked2(View view) {
        switch (view.getId()) {
            case R.id.img_home:

                //homeDilog();
                if (QuickTrackAplication.IsNotNull(responseData)) {
                    if (responseData.size() == 0) {
                        Intent intent = new Intent(AllocatedJobActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "You can't go outside until you accept or decline the job", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent intent = new Intent(AllocatedJobActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                break;
            case R.id.btn_logout:

                logOutDialog();

                break;

            case R.id.img_refresh:

                callApi();

                break;


        }
    }

    private void call_updatestaus(String jobid, String statusid, String latitude, String longitude, String jobType, String message) {
        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);
        PrefManager prefManager = new PrefManager(getApplicationContext());

        if (true) {
            pd = LogCustom.loderProgressDialog2(this);
            pd.show();
        }

        Call<CommonResponse> call;
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
                    Constants.Job.SUB_JOB_ACTION, message);
        } else {
            Log.e(TAG, "ready to change statues");
            call = apiService.updateStatusWithMessage(//"jobupdate.php"
                    prefManager.getLogin_Token(),
                    Constants.API_KEY,
                    prefManager.getId(),
                    jobid,
                    statusid,
                    latitude,
                    longitude,
                    prefManager.getTruckId(), message, jobType, jobid);
        }
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                try {
                    Log.d(TAG, " : check status : " + response.body().toString());
                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                    if (response.body().getStatus().equals(Constants.API_STATUS)) {
                        callGetTrackApi();
                    } else {
                        callGetTrackApi();
                        if (response.body().getMessage().equals("invalidToken")) {
                            prefManager.logout();

                            Intent intent = new Intent(AllocatedJobActivity.this, Login_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }

                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                LogCustom.logd(TAG, t.toString());
                if (pd != null && pd.isShowing())
                    pd.dismiss();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            Toast.makeText(AllocatedJobActivity.this, "You are not allow to go back if you want to go back press home", Toast.LENGTH_LONG).show();

            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void startTimer() {
        countDownTimer = new CountDownTimer(200000, 60000) {
            @Override
            public void onTick(long l) {
                try {
                    callGetTrackApi();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    public void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.onFinish();
            countDownTimer.cancel();
        }
    }

    @Override
    public void onSuccessAllocationJob(AllocatedJobListResponse allocatedJobListResponse) {
        hideProgressDialogue();
        swipeContainer.setRefreshing(false);
        responseData.clear();
        try {
            responseData = allocatedJobListResponse.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setAddapter();
    }

    @Override
    public void onErrorAllocationJob(String error) {
        hideProgressDialogue();
        if (error.equals("invalidToken")) {
            prefManager.logout();

            Intent intent = new Intent(AllocatedJobActivity.this, Login_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        responseData.clear();
        setAddapter();
    }

    @Override
    public void onSuccessAllocationStustus(CommonResponse commonResponse) {

    }

    @Override
    public void onErrorAllocationStustus(String error) {

    }
}
