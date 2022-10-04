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
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.network.ApiCallInterface;
import com.evolution.quicktrack.network.ApiClient;
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

public class JobDetailsDelivery_Activity extends BaseActivity {


    @BindView(R.id.txt_collection_contact_person)
    TextView collectionContactPerson;

    @BindView(R.id.txt_collection_contact_number)
    TextView collectionContactNumber;


    @BindView(R.id.txtPersonNameLabel)
    TextView txtPersonNameLabel;

    @BindView(R.id.txtNumberLabel)
    TextView txtNumberLabel;


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
    public static String TAG = JobDetailsDelivery_Activity.class.getSimpleName();
    @BindView(R.id.img_home)
    ImageView imgHome;
    @BindView(R.id.btn_logout)
    ImageView btnLogout;

    String jobId = "";
    String type= "";
    String subjobid="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView(R.layout.activity_odometer);
        ButterKnife.bind(this);


        //btnLogout.setVisibility(View.INVISIBLE);
        initView();

        txtPersonNameLabel.setText("Delivery contact person");
        txtNumberLabel.setText("Delivery contact number");
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
        }
        if(getIntent()!=null && getIntent().getExtras()!=null && getIntent().getExtras().containsKey("type")){
            type = getIntent().getExtras().getString("type","");
        }
        subjobid = getIntent().getExtras().getString("subjobid","");
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


        Intent intent=new Intent(JobDetailsDelivery_Activity.this,Delivery_SignatureActivity.class);
        intent.putExtra("type",type);
        intent.putExtra("subjobid",subjobid);
        intent.putExtra(Constants.EXTRA_JOBID,jobId);
        startActivity(intent);


    }

    @OnClick({R.id.img_home, R.id.btn_logout})
    public void onViewClicked2(View view) {
        switch (view.getId()) {
            case R.id.img_home:
                onBackPressed();// homeDilog();
                break;
            case R.id.btn_logout:

                logOutDialog();

                break;
        }
    }


    private void call_jobdetailsApi() {


        pd = LogCustom.loderProgressDialog2(this);
        pd.show();


        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);
        PrefManager prefManager=new PrefManager(getApplicationContext());
        Call<JobDetailsResponse> call = apiService.getjobDetails(prefManager.getLogin_Token(),Constants.API_KEY, jobId,prefManager.getId());
        call.enqueue(new Callback<JobDetailsResponse>() {
            @Override
            public void onResponse(Call<JobDetailsResponse> call, Response<JobDetailsResponse> response) {


                try {
                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                    Log.d(TAG, " : check status : " + response.body().toString());


                    if (response.body().getStatus().equals(Constants.API_STATUS)) {

                        txtIdentifier.setText(response.body().getData().getCompanyName());
                        txtDeliveryAddres.setText(response.body().getData().getDeliveryLocation());
                        txtPickupAddres.setText(response.body().getData().getCollectionLocation());
                        txtDesription.setText(response.body().getData().getDescription());
                        txtSpecialInstruction.setText(response.body().getData().getSpecialinstructions());


                        txt_delivery_type.setText(response.body().getData().getDeliveryType());
                        txt_requested_by.setText(response.body().getData().getRequested_by());
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


                    }
                    else
                    {

                        if(response.body().getMessage().equals("invalidToken"))
                        {
                            prefManager.logout();

                            Intent   intent =new Intent(JobDetailsDelivery_Activity.this,Login_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }


                    }


                } catch (Exception e) {
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


    public  void homeDilog(){

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(JobDetailsDelivery_Activity.this);
        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.wanttogohome));
        alertDialog.setCancelable(false);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(JobDetailsDelivery_Activity.this, MainActivity.class);
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

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(JobDetailsDelivery_Activity.this);

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure want to logout ?");
        alertDialog.setCancelable(true);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                /*trackDriver.call_trackDriverAPi(Constants.BACK_LOGOUT);


                PrefManager prefManager=new PrefManager(getApplicationContext());
                prefManager.logout();

                Intent   intent =new Intent(JobDetailsDelivery_Activity.this,Login_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
*/showOdometerDialog(JobDetailsDelivery_Activity.this);
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
