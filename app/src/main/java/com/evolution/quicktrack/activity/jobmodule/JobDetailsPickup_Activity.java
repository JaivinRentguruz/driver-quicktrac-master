package com.evolution.quicktrack.activity.jobmodule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.evolution.quicktrack.BaseActivity;
import com.evolution.quicktrack.Login_Activity;
import com.evolution.quicktrack.MainActivity;
import com.evolution.quicktrack.QuickTrackAplication;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.jobdetailspickup.JobDetailsPickupContractor;
import com.evolution.quicktrack.jobdetailspickup.JobDetailsPickupPresenter;
import com.evolution.quicktrack.jobdetailspickup.JobDetailsPickupRequestEntity;
import com.evolution.quicktrack.network.ApiCallInterface;
import com.evolution.quicktrack.network.ApiClient;
import com.evolution.quicktrack.response.jobDetails.JobDetailsData;
import com.evolution.quicktrack.response.jobDetails.JobDetailsResponse;
import com.evolution.quicktrack.util.PrefManager;
import com.evolution.quicktrack.util.Utils;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 4/2/2018.
 */

public class JobDetailsPickup_Activity extends BaseActivity implements JobDetailsPickupContractor.View {

    public static String TAG = "[JobDetailsPickup_Activity]";

    @BindView(R.id.txt_collection_contact_person)
    TextView collectionContactPerson;

    @BindView(R.id.txt_collection_contact_number)
    TextView collectionContactNumber;

    @BindView(R.id.txt_delivery_type)
    TextView txt_delivery_type;

    @BindView(R.id.txt_requested_by)
    TextView txt_requested_by;

    @BindView(R.id.txt_collection_company)
    TextView txt_collection_company;

    @BindView(R.id.txt_contact_person)
    TextView txt_contact_person;

    @BindView(R.id.txt_delivery_company)
    TextView txt_delivery_company;

    @BindView(R.id.txt_identifier)
    TextView txtIdentifier;
    @BindView(R.id.txt_pickupAddres)
    TextView txtPickupAddres;
    @BindView(R.id.txt_deliveryAddres)
    TextView txtDeliveryAddres;
    @BindView(R.id.txt_weight)
    TextView txtWeight;
    @BindView(R.id.txt_quntity)
    TextView txtQuntity;
    @BindView(R.id.txt_desription)
    TextView txtDesription;
    @BindView(R.id.txt_special_instruction)
    TextView txtSpecialInstruction;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.txt_qubicweight)
    TextView txtQubicweight;
    @BindView(R.id.txt_dimention)
    TextView txtDimention;
    @BindView(R.id.txt_distance)
    TextView txtDistance;
    private KProgressHUD pd;
    @BindView(R.id.img_home)
    ImageView imgHome;
    @BindView(R.id.btn_logout)
    ImageView btnLogout;

    String jobId = "", subJobId = "0", jobType = "";
    PrefManager prefManager;
    ApiCallInterface apiService;
    JobDetailsData jobDetailsData;

    private JobDetailsPickupContractor.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_odometer);
        presenter = new JobDetailsPickupPresenter(this);
        prefManager = new PrefManager(getApplicationContext());
        apiService = ApiClient.getClient().create(ApiCallInterface.class);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_jobdetail;
    }

    public void initView() {
        getIntendata();
    }

    public void getIntendata() {
        if (getIntent().getStringExtra(Constants.EXTRA_JOBID) != null) {
            jobId = getIntent().getStringExtra(Constants.EXTRA_JOBID);
            subJobId = getIntent().getStringExtra(Constants.EXTRA_SUB_JOBID);
            jobType = getIntent().getStringExtra(Constants.Job.JOB_TYPE_KEY);
        }
        callApi();
    }

    public void callApi() {
        if (Utils.validInternet(getApplicationContext())) {
            try {
                call_jobdetailsApi();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        if (jobType.equals(Constants.Job.JOB_0)) {
            Intent intent = new Intent(JobDetailsPickup_Activity.this, Pickup_SignatureActivity.class);
            intent.putExtra("type",jobType);
            intent.putExtra(Constants.EXTRA_JOBID, jobId);
            intent.putExtra("subJobId",subJobId);
            startActivity(intent);
        } else {
            addJobToFleets();
            updateSubJobStatus();
        }
    }

    @OnClick({R.id.img_home, R.id.btn_logout})
    public void onViewClicked2(View view) {
        switch (view.getId()) {
            case R.id.img_home:
                onBackPressed();
                break;
            case R.id.btn_logout:
                logOutDialog();
                break;
        }
    }

    private void call_jobdetailsApi() {

        pd = LogCustom.loderProgressDialog2(this);
        pd.show();

        Call<JobDetailsResponse> call = null;
        if (jobType.equals(Constants.Job.JOB_0)) {
            call = apiService.getjobDetails(prefManager.getLogin_Token(), Constants.API_KEY,
                    jobId, prefManager.getId());
        } else if (jobType.equals(Constants.Job.JOB_1)) {
            call = apiService.getSubJobDetails(prefManager.getLogin_Token(), Constants.API_KEY,
                    subJobId, prefManager.getId());
        }
        call.enqueue(new Callback<JobDetailsResponse>() {
            @Override
            public void onResponse(Call<JobDetailsResponse> call, Response<JobDetailsResponse> response) {
                try {
                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                    Log.d(TAG, " : check status : " + response.body().toString());

                    if (response.body().getStatus().equals(Constants.API_STATUS)) {
                        jobDetailsData = response.body().getData();
                        txtIdentifier.setText(response.body().getData().getCompanyName());
                        txtDeliveryAddres.setText(response.body().getData().getDeliveryLocation());
                        txtPickupAddres.setText(response.body().getData().getCollectionLocation());
                        txtDesription.setText(response.body().getData().getDescription());
                        txtSpecialInstruction.setText(response.body().getData().getSpecialinstructions());

                        txt_delivery_type.setText(response.body().getData().getDeliveryType());
                        txt_requested_by.setText(response.body().getData().getRequestname());
                        txt_collection_company.setText(response.body().getData().getCollectionCompanyname());
                        txt_contact_person.setText(response.body().getData().getContactPerson());
                        txt_delivery_company.setText(response.body().getData().getDeliveryCompanyname());

                        if (!response.body().getData().getWeight().equals(""))
                            txtWeight.setText(response.body().getData().getWeight());

                        if (!response.body().getData().getDimension().equals(""))
                            txtDimention.setText(response.body().getData().getDimension());

                        if (!response.body().getData().getDistance().equals(""))
                            txtDistance.setText(response.body().getData().getDistance());

                        if (!response.body().getData().getCubicWeight().equals(""))
                            txtQubicweight.setText(response.body().getData().getCubicWeight());

                        if (!response.body().getData().getQuantity().equals(""))
                            txtQuntity.setText(response.body().getData().getQuantity());
                    } else {
                        if (response.body().getMessage().equals("invalidToken")) {
                            prefManager.logout();

                            Intent intent = new Intent(JobDetailsPickup_Activity.this, Login_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JobDetailsResponse> call, Throwable t) {
                LogCustom.logd(TAG, t.toString());
                if (pd != null && pd.isShowing())
                    pd.dismiss();
            }
        });
    }

    private void updateSubJobStatus() {
        pd = LogCustom.loderProgressDialog2(this);
        pd.show();
        RequestBody body = new FormBody.Builder()
                .add("APIKEY", Constants.API_KEY)
                .add("driverid", prefManager.getId())
                .add("login_token", prefManager.getLogin_Token())
                .add("vehicleid", String.valueOf(prefManager.getTruckId()))
                .add("status", Constants.PickUp)
                .add("action", Constants.ApiAction.UPDATE_SUB_JOB)
                .add("subjobId", subJobId)

                .build();

        Request request = new Request.Builder()
                .url(Constants.RestApi.FLEETS)
                .post(body)
                .build();

        QuickTrackAplication.okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                disMissDialog();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                disMissDialog();
                Intent intent = new Intent(JobDetailsPickup_Activity.this, AcceptedJobActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            private void disMissDialog() {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                    pd = null;
                }
            }
        });
    }

    private void addJobToFleets() {
        RequestBody body = new FormBody.Builder()
                .add("APIKEY", Constants.API_KEY)
                .add("action", Constants.ApiAction.ADD_JOB_TO_FLEET)
                .add("login_token", prefManager.getLogin_Token())
                .add("driverid", prefManager.getId())
                .add("subjobid", subJobId)
                .add("jobid", jobId)
                .add("vehicle_id", String.valueOf(prefManager.getTruckId()))
                .add("pickupFleetid", jobDetailsData.getCollection_location_id())
                .add("pickupFromType", "1")
//                .add("status", Constants.AddJobToFleet)
                .build();

        Request request = new Request.Builder()
                .url(Constants.RestApi.FLEETS)
                .post(body)
                .build();

        QuickTrackAplication.okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

            }
        });
    }

    public void homeDilog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(JobDetailsPickup_Activity.this);
        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.wanttogohome));
        alertDialog.setCancelable(false);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(JobDetailsPickup_Activity.this, MainActivity.class);
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

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(JobDetailsPickup_Activity.this);

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure want to logout ?");
        alertDialog.setCancelable(true);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showOdometerDialog(JobDetailsPickup_Activity.this);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Toast.makeText(this, "" + getString(R.string.neverback), Toast.LENGTH_SHORT).show();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onJobDetalsSuccess(JobDetailsResponse jobDetailsResponse) {
        hideProgressDialogue();
        jobDetailsData = jobDetailsResponse.getData();
        txtIdentifier.setText(jobDetailsResponse.getData().getCompanyName());
        txtDeliveryAddres.setText(jobDetailsResponse.getData().getDeliveryLocation());
        txtPickupAddres.setText(jobDetailsResponse.getData().getCollectionLocation());
        txtDesription.setText(jobDetailsResponse.getData().getDescription());
        txtSpecialInstruction.setText(jobDetailsResponse.getData().getSpecialinstructions());

        txt_delivery_type.setText(jobDetailsResponse.getData().getDeliveryType());
        txt_requested_by.setText(jobDetailsResponse.getData().getRequestname());
        txt_collection_company.setText(jobDetailsResponse.getData().getCollectionCompanyname());
        txt_contact_person.setText(jobDetailsResponse.getData().getContactPerson());
        txt_delivery_company.setText(jobDetailsResponse.getData().getDeliveryCompanyname());

        collectionContactNumber.setText(jobDetailsResponse.getData().getCollectionContactNumner());
        collectionContactPerson.setText(jobDetailsResponse.getData().getCollectionContactPerson());

        if (!jobDetailsResponse.getData().getWeight().equals(""))
            txtWeight.setText(jobDetailsResponse.getData().getWeight());

        if (!jobDetailsResponse.getData().getDimension().equals(""))
            txtDimention.setText(jobDetailsResponse.getData().getDimension());

        if (!jobDetailsResponse.getData().getDistance().equals(""))
            txtDistance.setText(jobDetailsResponse.getData().getDistance());

        if (!jobDetailsResponse.getData().getCubicWeight().equals(""))
            txtQubicweight.setText(jobDetailsResponse.getData().getCubicWeight());

        if (!jobDetailsResponse.getData().getQuantity().equals(""))
            txtQuntity.setText(jobDetailsResponse.getData().getQuantity());
    }

    @Override
    public void onJobDetailsError(String error) {
        hideProgressDialogue();
        if (error.equals("invalidToken")) {
            prefManager.logout();
        }
    }
}
