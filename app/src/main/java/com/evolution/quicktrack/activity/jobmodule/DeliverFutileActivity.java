package com.evolution.quicktrack.activity.jobmodule;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.evolution.quicktrack.BaseActivity;
import com.evolution.quicktrack.Login_Activity;
import com.evolution.quicktrack.MainActivity;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.adapter.DeliveryToDepoFleetList_Adepter;
import com.evolution.quicktrack.callback.Recycler_AcceptedJobClickCallBack;
import com.evolution.quicktrack.callback.Recycler_ItemClickCallBack;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.location.GetLocation;
import com.evolution.quicktrack.model.JobModel;
import com.evolution.quicktrack.network.ApiCallInterface;
import com.evolution.quicktrack.network.ApiClient;
import com.evolution.quicktrack.response.common.CommonResponse;
import com.evolution.quicktrack.response.fleetres.AddToFleetRespose;
import com.evolution.quicktrack.response.fleetres.FleetListResponse;
import com.evolution.quicktrack.response.fleetres.FleetResponse;
import com.evolution.quicktrack.util.PrefManager;
import com.evolution.quicktrack.util.RecyclerViewEmptySupport;
import com.evolution.quicktrack.util.Utils;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static br.com.zbra.androidlinq.Linq.stream;


public class DeliverFutileActivity extends BaseActivity implements Recycler_ItemClickCallBack, Recycler_AcceptedJobClickCallBack {

    @BindView(R.id.recyclerview_manu)
    RecyclerViewEmptySupport recyclerview;
    @BindView(R.id.txt_nodata)
    TextView txtNodata;
    @BindView(R.id.emptyView)
    LinearLayout emptyView;
    private KProgressHUD pd;
    public static String TAG = DeliverFutileActivity.class.getSimpleName();
    @BindView(R.id.btn_logout)
    ImageView btnLogout;

    @BindView(R.id.btn_move_to_depo)
    Button btnMoveToDepo;

    private GetLocation getLocation;
    DeliveryToDepoFleetList_Adepter custom_manu_adepter;

    List<JobModel> jobModelsList;

    private String jobID;
    private String type;
    private String subjobid="0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        btnMoveToDepo.setText("Next");

        jobID = getIntent().getExtras().getString("jobid");
        type = getIntent().getExtras().getString("type","");
        subjobid = getIntent().getExtras().getString("subjobid","0");

        PrefManager prefManager = new PrefManager(this);
        initView();
        callApi();
    }


    private void openFutileDialogue(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        alert.setMessage("Enter Your Futile Comment");
        // alert.setTitle("");
        alert.setView(edittext);
        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String YouEditTextValue = edittext.getText().toString();
                if(YouEditTextValue!=null && YouEditTextValue.trim().length()>0){
                    ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);
                    PrefManager prefManager = new PrefManager(getApplicationContext());
                    pd = LogCustom.loderProgressDialog2(DeliverFutileActivity.this);
                    pd.show();
                    Call<CommonResponse> call = apiService.updateStatusFutileDelivery(prefManager.getLogin_Token(), Constants.API_KEY, prefManager.getId(),jobID,"9", String.valueOf(getLocation.getcurrntLataLong().getLatitude()), String.valueOf(getLocation.getcurrntLataLong().getLongitude()), prefManager.getTruckId(),YouEditTextValue,type,subjobid);
                    call.enqueue(new Callback<CommonResponse>() {
                        @Override
                        public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                            deliveryFultileAPI();
                        }
                        @Override
                        public void onFailure(Call<CommonResponse> call, Throwable t) {
                            pd.dismiss();
                            Log.w("Faild",t.getCause().getMessage());
                        }
                    });
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

    private void deliveryFultileAPI(){
        FleetResponse fb = stream(fleetResponseList).where(c -> c.isSelected() == true).first();
        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);
        PrefManager prefManager = new PrefManager(getApplicationContext());
        Call<CommonResponse> call = apiService.futileJobDelivery(prefManager.getLogin_Token(), Constants.API_KEY, prefManager.getId(),jobID,fb.getId()+"","delivery");
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.body().getStatus().equals("true")) {
                    pd.dismiss();
                    Intent intent = new Intent(DeliverFutileActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    DeliverFutileActivity.this.finish();
                    Log.w("Status",response.code()+"");
                }else{
                    deliveryFultileAPI();
                }

            }
            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                pd.dismiss();
                Log.w("Faild",t.getCause().getMessage());
            }
        });
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_deliverydepo_fleetlist;
    }


    public void initView() {
        this.recyclerview.setHasFixedSize(true);
        this.recyclerview.setLayoutManager(new LinearLayoutManager(DeliverFutileActivity.this, LinearLayoutManager.VERTICAL, false));
        this.recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setEmptyView(emptyView);
        getLocation = new GetLocation(DeliverFutileActivity.this);
    }
    public void callApi() {
        try {
            if (Utils.validInternet(getApplicationContext()))
                call_jobApi(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private List<FleetResponse> responseData = new ArrayList<>();

    private void call_jobApi(boolean isFirstTime) throws Exception {
        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);
        if (isFirstTime) {
            pd = LogCustom.loderProgressDialog2(this);
            pd.show();

        }
        PrefManager prefManager = new PrefManager(getApplicationContext());

        Call<FleetListResponse> call = apiService.getFleetList(prefManager.getLogin_Token(), Constants.API_KEY, prefManager.getId(), "list");
        call.enqueue(new Callback<FleetListResponse>() {
            @Override
            public void onResponse(Call<FleetListResponse> call, Response<FleetListResponse> response) {

                if (pd != null && pd.isShowing())
                    pd.dismiss();
                if (response.body().getStatus().equals("true")) {

                    responseData = response.body().getData();


                } else {
                    if (response.body().getMessage().equals("invalidToken")) {
                        prefManager.logout();

                        Intent intent = new Intent(DeliverFutileActivity.this, Login_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }

                }

                setDataOrder();
            }

            @Override
            public void onFailure(Call<FleetListResponse> call, Throwable t) {
                LogCustom.logd(TAG, t.toString());
                if (pd != null && pd.isShowing())
                    pd.dismiss();
            }
        });
    }


    public void setAddapter() {
        custom_manu_adepter = new DeliveryToDepoFleetList_Adepter(DeliverFutileActivity.this, fleetResponseList, DeliverFutileActivity.this);
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
                openFutileDialogue();
                break;
        }
    }

    public void homeDilog() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DeliverFutileActivity.this);
        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.wanttogohome));
        alertDialog.setCancelable(false);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(DeliverFutileActivity.this, MainActivity.class);
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

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DeliverFutileActivity.this);

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure want to logout ?");
        alertDialog.setCancelable(true);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showOdometerDialog(DeliverFutileActivity.this);
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
            if (fleetResponseList.get(position).isSelected()) {
                fleetResponseList.get(position).setSelected(false);
            } else {
                fleetResponseList.get(position).setSelected(true);
            }


            int count = stream(fleetResponseList).where(j -> j.isSelected() == true).count();
            if (count > 0) {
                btnMoveToDepo.setVisibility(View.VISIBLE);
                for (int i = 0; i < fleetResponseList.size(); i++) {
                    if (i != position) {
                        fleetResponseList.get(i).setSelected(false);
                    }
                }
            } else {
                btnMoveToDepo.setVisibility(View.GONE);
            }
            custom_manu_adepter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnStartJobItemClick(int position) {

    }


    private List<FleetResponse> fleetResponseList = new ArrayList<>();

    public void setDataOrder() {


        fleetResponseList.clear();

        fleetResponseList = responseData;

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

        }

        PrefManager prefManager = new PrefManager(getApplicationContext());


        Call<AddToFleetRespose> call = apiService.addJobToFleetDeliver(prefManager.getLogin_Token(), Constants.API_KEY, prefManager.getId(), "addJobToFleet", String.valueOf(jobModel.getId()), String.valueOf(prefManager.getTruckId()), String.valueOf(jobModel.getType()), dropid,String.valueOf(jobModel.getSubjobId()));
        call.enqueue(new Callback<AddToFleetRespose>() {
            @Override
            public void onResponse(Call<AddToFleetRespose> call, Response<AddToFleetRespose> response) {

                if (pd != null && pd.isShowing())
                    pd.dismiss();
                if (response.body().getStatus().equals("true")) {

                } else {
                    if (response.body().getMessage().equals("invalidToken")) {
                        prefManager.logout();

                        Intent intent = new Intent(DeliverFutileActivity.this, Login_Activity.class);
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
            }
        });
    }
}