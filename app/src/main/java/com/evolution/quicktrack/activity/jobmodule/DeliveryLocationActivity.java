package com.evolution.quicktrack.activity.jobmodule;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.evolution.quicktrack.BaseActivity;
import com.evolution.quicktrack.Login_Activity;
import com.evolution.quicktrack.MainActivity;
import com.evolution.quicktrack.QuickTrackAplication;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.activity.SelectMultipleJobListActivity;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.location.GetLocation;
import com.evolution.quicktrack.model.DialogueButtonsModel;
import com.evolution.quicktrack.network.ApiCallInterface;
import com.evolution.quicktrack.network.ApiClient;
import com.evolution.quicktrack.response.acceptedjobList.AcceptedJob;
import com.evolution.quicktrack.response.common.CommonResponse;
import com.evolution.quicktrack.response.getcustomerAddress.CustomerAddressResponse;
import com.evolution.quicktrack.response.getcustomerAddress.CustomerDataItem;
import com.evolution.quicktrack.response.jobDetails.JobDetailsData;
import com.evolution.quicktrack.response.jobDetails.JobDetailsResponse;
import com.evolution.quicktrack.util.JSONParserTask;
import com.evolution.quicktrack.util.PrefManager;
import com.evolution.quicktrack.util.ShowCommonDialogu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryLocationActivity extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    @BindView(R.id.txt_pickupAddres)
    TextView txtPickupAddres;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_deliveryaddress)
    TextView txtDeliveryaddress;
    @BindView(R.id.btn_changeAddress)
    Button btnChangeAddress;
    @BindView(R.id.btn_loadTruck)
    Button btnLoadTruck;
    @BindView(R.id.img_gps)
    ImageView imgGps;

    @BindView(R.id.btn_deliveryarrived)
    Button btn_deliveryarrived;

    @BindView(R.id.liner_name)
    LinearLayout linerName;
    @BindView(R.id.liner_time)
    LinearLayout linerTime;
    private KProgressHUD pd;
    public static String TAG = Login_Activity.class.getSimpleName();
    @BindView(R.id.btn_logout)
    ImageView btnLogout;
    @BindView(R.id.img_home)
    ImageView imgHome;

    @BindView(R.id.btnMutliple)
    Button btnMutliple;

    @BindView(R.id.txt_delivery_company)
    TextView txt_delivery_company;

    @BindView(R.id.txt_contact_person)
    TextView txt_contact_person;

    @BindView(R.id.txt_contact_number)
    TextView txt_contact_number;

    @BindView(R.id.txtComment)
    TextView txtComment;

    Context mContext;
    SupportMapFragment map;
    GoogleMap mGoogleMap;
    ArrayList<LatLng> MarkerPoints;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;

    private GetLocation getLocation;


    String jobId = "";
    String type = "";
    String subjobid = "";

    @BindView(R.id.btn_delay_comment)
    Button btnDelayComment;

    private String destinationLatitude = "";
    private String destinationLongitude = "";

    ListView listView;
    double finaldestination = 0;
    double currentdestination = 0;
    PrefManager _prefManager;

    @BindView(R.id.btn_futile)
    Button btn_futile;

    @BindView(R.id.btnViewDetails)
    ImageView btnViewDetails;

    @BindView(R.id.txtJobID)
    TextView txtJobID;


    @BindView(R.id.clientName)
    LinearLayout clientName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        btnLogout.setVisibility(View.GONE);

        MarkerPoints = new ArrayList<>();
        map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.delivermap);
        map.getMapAsync(this);
        mContext = this;

        btnDelayComment.setOnClickListener(btnAddCommentClickListener);

        btn_futile.setOnClickListener(view -> {
            Intent intent = new Intent(DeliveryLocationActivity.this, DeliverFutileActivity.class);
            intent.putExtra("type", type);
            intent.putExtra("jobid", jobId);
            intent.putExtra("subjobid", subjobid);
            startActivity(intent);

        });


        btnMutliple.setOnClickListener(v -> {
            Intent intent = new Intent(DeliveryLocationActivity.this, SelectMultipleJobListActivity.class);
            intent.putExtra(Constants.EXTRA_TYPE, Constants.Job.DELIVER_FLEET_ID_0);
            intent.putExtra(Constants.EXTRA_TYPE, Constants.Job.DILEVERED_JOB);
            intent.putExtra("txtPickupAddres", txt_delivery_company.getText().toString().trim());
            intent.putExtra("txt_collection_company", txtDeliveryaddress.getText().toString().trim());
            intent.putExtra("selectedId", jobId);
            startActivityForResult(intent, 100);
        });

        btnViewDetails.setOnClickListener(v -> {
            try {
                call_jobDetailsApi(jobId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        showDialogue();
    }

    private void showDialogue() {
        showPlusButton();
        button.setOnClickListener(v -> {
            DialogueButtonsModel dialogueButtonsModel = new DialogueButtonsModel();
            dialogueButtonsModel.setNoOFButtonVisibility(5);
            dialogueButtonsModel.setBtnFiveText("Log Out");
            dialogueButtonsModel.setBtnFourText("Multiple job");
            dialogueButtonsModel.setBtnThreeText("Futile");
            dialogueButtonsModel.setBtnTwoText("Delay");
            dialogueButtonsModel.setBtnOneText("Take Me To Pickup");
            dialogueButtonsModel.setImgFive(R.drawable.logout);
            dialogueButtonsModel.setImgFour(R.drawable.ic_multiple_job);
            dialogueButtonsModel.setImgThree(R.drawable.ic_futile);
            dialogueButtonsModel.setImgTwo(R.drawable.ic_truck_delay);
            dialogueButtonsModel.setImgOne(R.drawable.ic_navigation);
            showCommonDialogue(dialog -> {
                switch (ShowCommonDialogu.btnClicked) {
                    case 1:
                        takeMeToPickUp();
                        break;
                    case 2:
                        delay();
                        break;
                    case 3:
                        openFutileDialogue();
                        break;
                    case 4:
                        multipleJobs();
                        break;
                    case 5:
                        logOutDialog();
                        break;
                }
            }, dialogueButtonsModel);

        });
    }

    private void multipleJobs() {
        Intent intent = new Intent(DeliveryLocationActivity.this, SelectMultipleJobListActivity.class);
        intent.putExtra(Constants.EXTRA_TYPE, Constants.Job.DELIVER_FLEET_ID_0);
        intent.putExtra(Constants.EXTRA_TYPE, Constants.Job.DILEVERED_JOB);
        intent.putExtra("txtPickupAddres", txt_delivery_company.getText().toString().trim());
        intent.putExtra("txt_collection_company", txtDeliveryaddress.getText().toString().trim());
        intent.putExtra("selectedId", jobId);
        startActivityForResult(intent, 100);
    }

    private void openFutileDialogue() {
        Intent intent = new Intent(DeliveryLocationActivity.this, DeliverFutileActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("jobid", jobId);
        intent.putExtra("subjobid", subjobid);
        startActivity(intent);

    }

    private void delay() {
        AlertDialog.Builder alert = new AlertDialog.Builder(DeliveryLocationActivity.this);
        final EditText edittext = new EditText(DeliveryLocationActivity.this);
        alert.setMessage("Enter Delay Comment");
        // alert.setTitle("");
        alert.setView(edittext);
        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String YouEditTextValue = edittext.getText().toString();
                if (YouEditTextValue != null && YouEditTextValue.trim().length() > 0) {
                    ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);
                    PrefManager prefManager = new PrefManager(getApplicationContext());
                    pd = LogCustom.loderProgressDialog2(DeliveryLocationActivity.this);
                    pd.show();
                    Call<CommonResponse> call = apiService.addDelayComment(Constants.API_KEY, prefManager.getId(), prefManager.getLogin_Token(), jobId, YouEditTextValue, prefManager.getTruckId(), "add");
                    call.enqueue(new Callback<CommonResponse>() {
                        @Override
                        public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                            dialog.dismiss();
                            pd.dismiss();
                            if (response.isSuccessful())
                                showToast(response.body().getMessage());
                        }

                        @Override
                        public void onFailure(Call<CommonResponse> call, Throwable t) {
                            pd.dismiss();
                            Log.w("Faild", t.getCause().getMessage());
                        }
                    });
                } else {
                    edittext.setError("Required");
                }
            }
        });
        alert.setNegativeButton("Cancel", (dialog, which) -> {
        });
        alert.show();
    }

    private void takeMeToPickUp() {
        if (getLocation.getcurrntLataLong() != null) {
            if (!destinationLatitude.equals("")) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + destinationLatitude + "," + destinationLongitude);
                Intent intent2 = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                intent2.setPackage("com.google.android.apps.maps");
                if (intent2.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent2);
                }
            }


        } else {
            LogCustom.logd("xxxxx", "getlocation");
            getLocation = new GetLocation(DeliveryLocationActivity.this);

            if (!destinationLatitude.equals("")) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + destinationLatitude + "," + destinationLongitude);
                Intent intent2 = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                intent2.setPackage("com.google.android.apps.maps");
                if (intent2.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent2);
                }
            }
        }
    }

    private void call_jobDetailsApi(String jobid) throws Exception {

        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);

        pd = LogCustom.loderProgressDialog2(this);
        pd.show();
        PrefManager prefManager = new PrefManager(getApplicationContext());


        Log.d("xxxx", "useerid:" + prefManager.getId());

        Call<JobDetailsResponse> call = apiService.getjobDetails(prefManager.getLogin_Token(), Constants.API_KEY, jobid, prefManager.getId());
        call.enqueue(new Callback<JobDetailsResponse>() {
            @Override
            public void onResponse(Call<JobDetailsResponse> call, Response<JobDetailsResponse> response) {

                Log.d(TAG, " : check status : " + response.body().toString());

                if (pd != null && pd.isShowing())
                    pd.dismiss();
                if (response.body().getStatus().equals(Constants.API_STATUS)) {

                    JobDetailsData jobDetailsDatas = response.body().getData();
                    if (jobDetailsDatas != null) {
                        showPopup(jobDetailsDatas);
                    } else {
                        if (response.body().getMessage().equals("invalidToken")) {
                            prefManager.logout();

                            Intent intent = new Intent(DeliveryLocationActivity.this, Login_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        showToast("Can't find job Details");
                    }
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

    private void showPopup(JobDetailsData jobDetailsData) {
        try {
            final Dialog layout = new Dialog(this);
            layout.requestWindowFeature(0);
            layout.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            layout.setCancelable(false);
            layout.setContentView(R.layout.popup_jobdetails);
            TextView txt_CompanyName = layout.findViewById(R.id.pop_companyname);
            TextView txt_DeliveryType = layout.findViewById(R.id.pop_deliverytype);
            TextView txt_RequestedBy = layout.findViewById(R.id.pop_requestby);
            ImageView img_close = layout.findViewById(R.id.pop_close_img);
            TextView txt_CollactionLocation = layout.findViewById(R.id.pop_collationlocation);
            TextView txt_ContactPerson = layout.findViewById(R.id.pop_contactperson);
            TextView txt_Pickupfromdate = layout.findViewById(R.id.pop_pickupfromdate);
            TextView txt_DeliveryLocation = layout.findViewById(R.id.pop_deliverylocation);
            TextView txt_DeliveryDateTime = layout.findViewById(R.id.pop_deliverydatetime);
            TextView txt_Description = layout.findViewById(R.id.pop_description);
            TextView txt_Quantity = layout.findViewById(R.id.pop_quantity);
            TextView txt_Weight = layout.findViewById(R.id.pop_weight);
            TextView txt_CubicWeight = layout.findViewById(R.id.pop_cubicweght);
            TextView txt_Distance = layout.findViewById(R.id.pop_distance);
            TextView txt_Dimension = layout.findViewById(R.id.pop_dimentation);
            Button btn_close = layout.findViewById(R.id.pop_close);
            TextView pop_collation_contact_person = layout.findViewById(R.id.pop_collation_contact_person);
            TextView pop_collation_contact_number = layout.findViewById(R.id.pop_collation_contact_number);


            if (QuickTrackAplication.IsNotNull(jobDetailsData)) {
                if (QuickTrackAplication.IsNotNull(jobDetailsData.getCompanyName())) {
                    txt_CompanyName.setText(jobDetailsData.getCompanyName());
                }
                if (QuickTrackAplication.IsNotNull(jobDetailsData.getDeliveryType())) {
                    txt_DeliveryType.setText(jobDetailsData.getDeliveryType());
                }
                if (QuickTrackAplication.IsNotNull(jobDetailsData.getRequestname())) {
                    txt_RequestedBy.setText(jobDetailsData.getRequestname());
                }
                if (QuickTrackAplication.IsNotNull(jobDetailsData.getCollectionLocation())) {
                    txt_CollactionLocation.setText(jobDetailsData.getCollectionLocation());
                }
                if (QuickTrackAplication.IsNotNull(jobDetailsData.getContactPerson())) {
                    txt_ContactPerson.setText(jobDetailsData.getContactPerson());
                }
                if (QuickTrackAplication.IsNotNull(jobDetailsData.getPickupFromDateTime())) {
                    txt_Pickupfromdate.setText(jobDetailsData.getPickupFromDateTime());
                }
                if (QuickTrackAplication.IsNotNull(jobDetailsData.getDeliveryLocation())) {
                    txt_DeliveryLocation.setText(jobDetailsData.getDeliveryLocation());
                }
                if (QuickTrackAplication.IsNotNull(jobDetailsData.getDeliveryToDateTime())) {
                    txt_DeliveryDateTime.setText(jobDetailsData.getDeliveryToDateTime());
                }
                if (QuickTrackAplication.IsNotNull(jobDetailsData.getDescription())) {
                    txt_Description.setText(jobDetailsData.getDescription());
                }

                if (QuickTrackAplication.IsNotNull(jobDetailsData.getQuantity())) {
                    txt_Quantity.setText(jobDetailsData.getQuantity());
                }

                if (QuickTrackAplication.IsNotNull(jobDetailsData.getWeight())) {
                    txt_Weight.setText(jobDetailsData.getWeight());
                }

                if (QuickTrackAplication.IsNotNull(jobDetailsData.getCubicWeight())) {
                    txt_CubicWeight.setText(jobDetailsData.getCubicWeight());
                }

                if (QuickTrackAplication.IsNotNull(jobDetailsData.getDistance())) {
                    txt_Distance.setText(jobDetailsData.getDistance());
                }

                if (QuickTrackAplication.IsNotNull(jobDetailsData.getDimension())) {
                    txt_Dimension.setText(jobDetailsData.getDimension());
                }

                if (QuickTrackAplication.IsNotNull(jobDetailsData.getCollectionSubrb())) {
                    txt_Dimension.setText(jobDetailsData.getDimension());
                }
                if (QuickTrackAplication.IsNotNull(jobDetailsData.getCollectionCompanyname())) {
                    ((TextView) layout.findViewById(R.id.pop_collationcompany)).setText(jobDetailsData.getCollectionCompanyname());
                }

                if (QuickTrackAplication.IsNotNull(jobDetailsData.getDeliveryCompanyname())) {
                    ((TextView) layout.findViewById(R.id.pop_deliverycompany)).setText(jobDetailsData.getDeliveryCompanyname());
                }

                if (QuickTrackAplication.IsNotNull(jobDetailsData.getDeliveryContactPerson())) {
                    pop_collation_contact_person.setText(jobDetailsData.getDeliveryContactPerson());
                }

                if (QuickTrackAplication.IsNotNull(jobDetailsData.getDeliveryContactNumner())) {
                    pop_collation_contact_number.setText(jobDetailsData.getDeliveryContactNumner());

                    pop_collation_contact_number.setTextColor(Color.parseColor("#357799"));
                    pop_collation_contact_number.setOnClickListener(view -> ((QuickTrackAplication) getApplicationContext()).openNumberPad(jobDetailsData.getDeliveryContactNumner()));
                }
            }

            btn_close.setOnClickListener(view -> layout.dismiss());
            img_close.setOnClickListener(v -> layout.dismiss());

            layout.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener btnAddCommentClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder alert = new AlertDialog.Builder(DeliveryLocationActivity.this);
            final EditText edittext = new EditText(DeliveryLocationActivity.this);
            alert.setMessage("Enter Delay Comment");
            // alert.setTitle("");
            alert.setView(edittext);
            alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String YouEditTextValue = edittext.getText().toString();
                    if (YouEditTextValue != null && YouEditTextValue.trim().length() > 0) {
                        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);
                        PrefManager prefManager = new PrefManager(getApplicationContext());
                        pd = LogCustom.loderProgressDialog2(DeliveryLocationActivity.this);
                        pd.show();
                        Call<CommonResponse> call = apiService.addDelayComment(Constants.API_KEY, prefManager.getId(), prefManager.getLogin_Token(), jobId, YouEditTextValue, prefManager.getTruckId(), "add");
                        call.enqueue(new Callback<CommonResponse>() {
                            @Override
                            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                                dialog.dismiss();
                                pd.dismiss();
                                if (response.isSuccessful())
                                    showToast(response.body().getMessage());
                            }

                            @Override
                            public void onFailure(Call<CommonResponse> call, Throwable t) {
                                pd.dismiss();
                                Log.w("Faild", t.getCause().getMessage());
                            }
                        });
                    } else {
                        edittext.setError("Required");
                    }
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert.show();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        getLocation = new GetLocation(DeliveryLocationActivity.this);
        _prefManager = new PrefManager(this);
        imgGps.setImageResource(R.drawable.img_drop);
        initView();


    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_delivery_location;
    }


    public void initView() {
        if (_prefManager.getKeyJobstatus().equals(Constants.AtDeliveryLocation)) {
           // btnLoadTruck.setVisibility(View.VISIBLE);
        }
        getIntendata();

    }


    public void getIntendata() {


        if (getIntent().getStringExtra(Constants.EXTRA_JOBID) != null) {

            jobId = getIntent().getStringExtra(Constants.EXTRA_JOBID);

            txtJobID.setText("Job ID: " + jobId);

        }
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey("type")) {
            type = getIntent().getExtras().getString("type", "");
        }
        subjobid = getIntent().getExtras().getString("subjobid", "");


        if (getIntent().getParcelableArrayListExtra(Constants.EXTRA_JOB_DATA) != null) {
            List<AcceptedJob> responseData = getIntent().getParcelableArrayListExtra(Constants.EXTRA_JOB_DATA);
        }
        if (!isFromSelectedMultiple)
            callApi();
    }

    public void callApi() {

        try {
            call_jobdetailsApi();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mapReady() {
        LatLng desti = new LatLng(Double.parseDouble(destinationLatitude), Double.parseDouble(destinationLongitude));
        mGoogleMap.addMarker(new MarkerOptions().position(desti).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Destination Point"));
        UpdateMap();
    }


    @OnClick({R.id.btn_changeAddress, R.id.btn_loadTruck, R.id.img_gps, R.id.btn_deliveryarrived})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_changeAddress:

                break;

            case R.id.btn_loadTruck:
                ShowDeliveryDocketActivity();
                break;

            case R.id.btn_deliveryarrived:

                if (getLocation.getcurrntLataLong() != null) {
                    call_updatestausfordeliverd(jobId, Constants.AtDeliveryLocation, String.valueOf(getLocation.getcurrntLataLong().getLatitude()), String.valueOf(getLocation.getcurrntLataLong().getLongitude()), true);

                } else {
                    getLocation = new GetLocation(DeliveryLocationActivity.this);
                    showToast("Try again");
                }

                ShowDeliveryDocketActivity();

                break;

            case R.id.img_gps:


                if (getLocation.getcurrntLataLong() != null) {
                    if (!destinationLatitude.equals("")) {
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + destinationLatitude + "," + destinationLongitude);
                        Intent intent2 = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        intent2.setPackage("com.google.android.apps.maps");
                        if (intent2.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent2);
                        }
                    }


                } else {
                    LogCustom.logd("xxxxx", "getlocation");
                    getLocation = new GetLocation(DeliveryLocationActivity.this);

                    if (!destinationLatitude.equals("")) {
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + destinationLatitude + "," + destinationLongitude);
                        Intent intent2 = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        intent2.setPackage("com.google.android.apps.maps");
                        if (intent2.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent2);
                        }
                    }
                }
                break;
        }
    }


    @OnClick({R.id.img_home, R.id.btn_logout})
    public void onViewClicked2(View view) {
        switch (view.getId()) {
            case R.id.img_home:
                onBackPressed();//homeDilog();
                break;
            case R.id.btn_logout:
                logOutDialog();
                break;
        }
    }

    private void call_jobdetailsApi() {
        pd = LogCustom.loderProgressDialog2(this);
        pd.show();

        PrefManager prefManager = new PrefManager(getApplicationContext());
        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);

        Call<JobDetailsResponse> call = apiService.getjobDetails(prefManager.getLogin_Token(), Constants.API_KEY, jobId, prefManager.getId());
        call.enqueue(new Callback<JobDetailsResponse>() {
            @Override
            public void onResponse(Call<JobDetailsResponse> call, Response<JobDetailsResponse> response) {
                try {
                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                    Log.d(TAG, " : check status : " + response.body().toString());


                    if (response.body().getStatus().equals(Constants.API_STATUS)) {

                        txtName.setText(response.body().getData().getCustomerId());
                        txtDeliveryaddress.setText(response.body().getData().getDeliveryLocation());
                        txtPickupAddres.setText(response.body().getData().getDeliveryLocation());

                        destinationLatitude = response.body().getData().getDeliveryLatitude();
                        destinationLongitude = response.body().getData().getDeliveryLongitude();
                        txt_delivery_company.setText(response.body().getData().getDeliveryCompanyname());


                        txtComment.setText(response.body().getData().getDescription());

                        SpannableString spannableString = new SpannableString(response.body().getData().getDeliveryContactNumner());
                        spannableString.setSpan(new UnderlineSpan(), 0, response.body().getData().getDeliveryContactNumner().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        txt_contact_number.setText(spannableString);
                        txt_contact_person.setText(response.body().getData().getDeliveryContactPerson());
                        txt_contact_number.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((QuickTrackAplication) getApplicationContext()).Call(DeliveryLocationActivity.this, response.body().getData().getDeliveryContactNumner());
                            }
                        });
                        try {
                            if (destinationLatitude != null && destinationLongitude != null) {
                                mapReady();
                            }
                        } catch (Exception e) {

                        }

                    } else {
                        if (response.body().getMessage().equals("invalidToken")) {
                            prefManager.logout();

                            Intent intent = new Intent(DeliveryLocationActivity.this, Login_Activity.class);
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
            public void onFailure(Call<JobDetailsResponse> call, Throwable t) {
                LogCustom.logd(TAG, t.toString());
                if (pd != null && pd.isShowing())
                    pd.dismiss();
            }
        });
    }

    private boolean isFromSelectedMultiple = false;
    String strIds = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            strIds = "";
            isFromSelectedMultiple = true;
            ArrayList<AcceptedJob> responseDataTemp = data.getParcelableArrayListExtra(Constants.EXTRA_JOB_DATA);
            findViewById(R.id.llCompanyNameDelivery).setVisibility(View.VISIBLE);
            findViewById(R.id.clientName).setVisibility(View.GONE);
            findViewById(R.id.llAddressDelivery).setVisibility(View.GONE);
            findViewById(R.id.llContactPerson).setVisibility(View.GONE);
            findViewById(R.id.llContactNumber).setVisibility(View.GONE);
            findViewById(R.id.llComments).setVisibility(View.GONE);
            findViewById(R.id.llDetails).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.comName)).setText("Job Ids:");
            btn_futile.setVisibility(View.GONE);

            for (int i = 0; i < responseDataTemp.size(); i++) {
                if (i == responseDataTemp.size() - 1) {
                    strIds += responseDataTemp.get(i).getId();
                } else {
                    strIds += responseDataTemp.get(i).getId() + ",";
                }
            }

            if (strIds != null && strIds.length() > 0) {
                btnMutliple.setEnabled(false);
            }

            txt_delivery_company.setText(strIds);
        }
    }

    public void OnDeliveryRoute() {


        if (!_prefManager.getKeyJobstatus().equals(Constants.Started) || !_prefManager.getKeyJobstatus().equals(Constants.OnRouteToDelivery) || !_prefManager.getKeyJobstatus().equals(Constants.AtDeliveryLocation)) {

            if (getLocation.getcurrntLataLong() != null) {
                call_updatestaus(jobId, Constants.OnRouteToDelivery, String.valueOf(getLocation.getcurrntLataLong().getLatitude()), String.valueOf(getLocation.getcurrntLataLong().getLongitude()), true);

            } else {
                getLocation = new GetLocation(DeliveryLocationActivity.this);
                OnDeliveryRoute();
            }
        }

    }

    List<CustomerDataItem> responseAddressdata = new ArrayList<>();
    List<String> Addressdata = new ArrayList<>();

    private void call_AddressApi() {

        PrefManager prefManager = new PrefManager(getApplicationContext());

        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);

        Call<CustomerAddressResponse> call = apiService.getcustomerAddress(prefManager.getLogin_Token(), Constants.API_KEY, jobId, prefManager.getId());


        call.enqueue(new Callback<CustomerAddressResponse>() {
            @Override
            public void onResponse(Call<CustomerAddressResponse> call, Response<CustomerAddressResponse> response) {


                try {

                    Log.d(TAG, " : check status : " + response.body().toString());


                    if (response.body().getStatus().equals(Constants.API_STATUS)) {

                        responseAddressdata = response.body().getData();


                    } else {
                        if (response.body().getMessage().equals("invalidToken")) {
                            prefManager.logout();

                            Intent intent = new Intent(DeliveryLocationActivity.this, Login_Activity.class);
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
            public void onFailure(Call<CustomerAddressResponse> call, Throwable t) {
                LogCustom.logd(TAG, t.toString());
                if (pd != null && pd.isShowing())
                    pd.dismiss();
            }
        });
    }


    public void setCategoryAdapter() {

        if (responseAddressdata.size() != 0) {
            Addressdata.clear();

            for (CustomerDataItem name : responseAddressdata) {
                Addressdata.add(name.getAddress());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (DeliveryLocationActivity.this,
                            R.layout.custom_radiobox_leftside,
                            android.R.id.text1, Addressdata);

            listView.setAdapter(adapter);
        }
    }


    private void call_updatestaus(final String jobid, final String statusid, final String latitude, final String longitude, final boolean isIntencall) {
        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);


        PrefManager prefManager = new PrefManager(getApplicationContext());


        Call<CommonResponse> call = apiService.updateStatus(prefManager.getLogin_Token(), Constants.API_KEY, prefManager.getId(), jobid, statusid, latitude, longitude, prefManager.getTruckId(), type, subjobid);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                try {

                    if (response.body().getStatus().equals(Constants.API_STATUS)) {
                        prefManager.setKEYJobstatus(Constants.OnRouteToDelivery);
                    } else {
                        if (response.body().getMessage().equals("invalidToken")) {
                            prefManager.logout();

                            Intent intent = new Intent(DeliveryLocationActivity.this, Login_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }

                        OnDeliveryRoute();
                    }

                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                LogCustom.logd(TAG, t.toString());

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        buildGoogleApiClient();
    }

    void UpdateMap() {
        LatLng source = new LatLng(getLocation.getcurrntLataLong().getLatitude(), getLocation.getcurrntLataLong().getLongitude());
        LatLng desti = new LatLng(Double.parseDouble(destinationLatitude), Double.parseDouble(destinationLongitude));
        String str_origin = "origin=" + source.latitude + "," + source.longitude;
        String str_dest = "destination=" + desti.latitude + "," + desti.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        FetchUrl FetchUrl = new FetchUrl();
        FetchUrl.execute(url);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(source, 16));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mGoogleMap.setMyLocationEnabled(true);
    }


    @Override
    public void onLocationChanged(Location location) {
        try {
            LatLng desti = new LatLng(Double.parseDouble(destinationLatitude), Double.parseDouble(destinationLongitude));
            getDistance(String.valueOf(getLocation.getcurrntLataLong().getLatitude()), String.valueOf(getLocation.getcurrntLataLong().getLongitude()), String.valueOf(desti.latitude), String.valueOf(desti.longitude));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(20000);
        mLocationRequest.setFastestInterval(20000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private class FetchUrl extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("result----", result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);

        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                JSONParserTask parser = new JSONParserTask();
                Log.d("ParserTask", parser.toString());
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = result.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                lineOptions.addAll(points);
                lineOptions.width(15);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }
            if (lineOptions != null) {
                mGoogleMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }

    private double getDistance(final String currentLat, final String currentLong, final String disLat, final String disLong) throws Exception {
        LatLng loc1 = new LatLng(Double.parseDouble(currentLat), Double.parseDouble(currentLong));
        LatLng loc2 = new LatLng(Double.parseDouble(disLat), Double.parseDouble(disLong));
        currentdestination = SphericalUtil.computeDistanceBetween(loc1, loc2);
        double matchdesti = 1000;
        try {
            if (currentdestination < matchdesti) {
                if (_prefManager.getKeyJobstatus().equals(Constants.AtDeliveryLocation)) {
                    btnLoadTruck.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showToast(e.getMessage());
        }
        return currentdestination;
    }

    private void call_updatestausfordeliverd(String jobid, String statusid, String latitude, String longitude, final boolean isIntencall) {
        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);
        PrefManager prefManager = new PrefManager(getApplicationContext());
        Call<CommonResponse> call = apiService.updateStatus(prefManager.getLogin_Token(), Constants.API_KEY, prefManager.getId(), jobid, statusid, latitude, longitude, prefManager.getTruckId(), type, subjobid);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                try {
                    if (response.body().getStatus().equals(Constants.API_STATUS)) {
                        _prefManager.setKEYJobstatus(Constants.AtDeliveryLocation);
                        if (isIntencall) {
                            //btnLoadTruck.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (response.body().getMessage().equals("invalidToken")) {
                            prefManager.logout();

                            Intent intent = new Intent(DeliveryLocationActivity.this, Login_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            showToast("Try again" + response.message());
                        }
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                LogCustom.logd(TAG, t.toString());

            }
        });
    }

    private void ShowDeliveryDocketActivity()
    {
        if (isFromSelectedMultiple) {
        Intent intent = new Intent(DeliveryLocationActivity.this, Delivery_SignatureActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("subjobid", subjobid);
        intent.putExtra("strIDs", strIds);
        intent.putExtra(Constants.EXTRA_JOBID, jobId);
        startActivity(intent);
        return;
    }
        Intent intent = new Intent(DeliveryLocationActivity.this, Delivery_SignatureActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("subjobid", subjobid);
        intent.putExtra(Constants.EXTRA_JOBID, jobId);
        startActivity(intent);
    }

    public void homeDilog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DeliveryLocationActivity.this);
        alertDialog.setMessage(getString(R.string.wanttogohome));
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
            dialog.dismiss();
            Intent intent = new Intent(DeliveryLocationActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        alertDialog.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    public void logOutDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DeliveryLocationActivity.this);
        alertDialog.setMessage("Are you sure want to logout ?");
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
            dialog.dismiss();
            showOdometerDialog(DeliveryLocationActivity.this);
        });
        alertDialog.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            showToast(getString(R.string.neverback));
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
