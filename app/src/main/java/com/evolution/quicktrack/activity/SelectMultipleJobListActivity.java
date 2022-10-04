package com.evolution.quicktrack.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.evolution.quicktrack.BaseActivity;
import com.evolution.quicktrack.Login_Activity;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.adapter.SelectMultipleJobListAdapter;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.multiplejobselection.SelectMultipleJobsContractor;
import com.evolution.quicktrack.multiplejobselection.SelectMultipleJobsPresenter;
import com.evolution.quicktrack.multiplejobselection.SelectMultipleJobsRequestEntity;
import com.evolution.quicktrack.network.ApiCallInterface;
import com.evolution.quicktrack.network.ApiClient;
import com.evolution.quicktrack.response.acceptedjobList.AcceptedJob;
import com.evolution.quicktrack.response.acceptedjobList.AcceptedJobListResponse;
import com.evolution.quicktrack.util.PrefManager;
import com.evolution.quicktrack.util.RecyclerViewEmptySupport;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectMultipleJobListActivity extends BaseActivity implements SelectMultipleJobsContractor.View{
    ArrayList<AcceptedJob> responseDataTemp;
    RecyclerViewEmptySupport recyclerview_manu;
    private Button btn_move_to_depo;
    private SelectMultipleJobListAdapter selectMultipleJobListAdapter;
    private String selectedId;
    private KProgressHUD pd;
    private String comeFrom = "";
    private String txtPickupAddres = "";
    private String txt_collection_company = "";
    private SelectMultipleJobsContractor.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_select_multiple_job_list);

        presenter = new SelectMultipleJobsPresenter(this);


        recyclerview_manu = findViewById(R.id.recyclerview_manu);
        btn_move_to_depo = findViewById(R.id.btn_move_to_depo);
        ImageView img_home = findViewById(R.id.img_home);

        img_home.setOnClickListener(v -> finish());

        // responseDataTemp = getIntent().getExtras().getParcelableArrayList(Constants.EXTRA_JOB_DATA);
        comeFrom = getIntent().getExtras().getString(Constants.EXTRA_TYPE);
        selectedId = getIntent().getExtras().getString("selectedId");
        txtPickupAddres = getIntent().getExtras().getString("txtPickupAddres");
        txt_collection_company = getIntent().getExtras().getString("txt_collection_company");


        btn_move_to_depo.setOnClickListener(v -> {
            Intent intent = new Intent();

            ArrayList<AcceptedJob> responseDataTemp = new ArrayList<>();

            for (int i = 0; i < selectMultipleJobListAdapter.responseDataTemp.size(); i++) {
                if (selectMultipleJobListAdapter.responseDataTemp.get(i).isChecked())
                    responseDataTemp.add(selectMultipleJobListAdapter.responseDataTemp.get(i));
            }

            intent.putParcelableArrayListExtra(Constants.EXTRA_JOB_DATA, responseDataTemp);
            setResult(RESULT_OK, intent);
            finish();
        });
        try {
            call_jobApi(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_select_multiple_job_list;
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < selectMultipleJobListAdapter.responseDataTemp.size(); i++) {
                if (selectMultipleJobListAdapter.responseDataTemp.get(i).isChecked() && !selectMultipleJobListAdapter.responseDataTemp.get(i).getId().equalsIgnoreCase(selectedId)) {
                    btn_move_to_depo.setVisibility(View.VISIBLE);
                    break;
                } else {
                    btn_move_to_depo.setVisibility(View.GONE);
                }
            }
        }
    };


    private void call_jobApi(boolean isFirstTime) {

        SelectMultipleJobsRequestEntity selectMultipleJobsRequestEntity = new SelectMultipleJobsRequestEntity();
        selectMultipleJobsRequestEntity.setLogin_token(prefManager.getLogin_Token());
        selectMultipleJobsRequestEntity.setApikey(Constants.API_KEY);
        selectMultipleJobsRequestEntity.setVehicleid(prefManager.getTruckId());
        selectMultipleJobsRequestEntity.setStatus(Constants.Accepted + "," + Constants.Started + "," + Constants.PickUp + "," + Constants.OnRouteToPickup + "," + Constants.AtPickupLocation
                + "," + Constants.OnRouteToDelivery + "," + Constants.AtDeliveryLocation);
        selectMultipleJobsRequestEntity.setDriverid(prefManager.getId());
        selectMultipleJobsRequestEntity.setDateFlag("0");

        presenter.getAcceptedJobsWithDate(selectMultipleJobsRequestEntity);





        /*ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);
        if (isFirstTime) {
            pd = LogCustom.loderProgressDialog2(this);
            pd.show();
        }
        PrefManager prefManager = new PrefManager(getApplicationContext());
        Call<AcceptedJobListResponse> call = apiService.getAcceptedJobWithDate(prefManager.getLogin_Token(), Constants.API_KEY, prefManager.getTruckId(),
                Constants.Accepted + "," + Constants.Started + "," + Constants.PickUp + "," + Constants.OnRouteToPickup + "," + Constants.AtPickupLocation
                        + "," + Constants.OnRouteToDelivery + "," + Constants.AtDeliveryLocation, prefManager.getId(),"0");
        call.enqueue(new Callback<AcceptedJobListResponse>() {
            @Override
            public void onResponse(Call<AcceptedJobListResponse> call, Response<AcceptedJobListResponse> response) {
                if (pd != null && pd.isShowing())
                    pd.dismiss();
                if (response.body().getStatus().equals(Constants.API_STATUS)) {
                    ArrayList<AcceptedJob> responseData = (ArrayList<AcceptedJob>) response.body().getData();
                    if (responseData != null && responseData.size() > 0) {
                        responseDataTemp = new ArrayList<AcceptedJob>();
                        if (comeFrom.equalsIgnoreCase(Constants.Job.PICKED_JOB)) {
                            for (int i = 0; i < responseData.size(); i++) {
                                if (txtPickupAddres.toString().trim().equalsIgnoreCase(responseData.get(i).getCollectionLocation())
                                        && *//*txt_collection_company.toString().trim().equalsIgnoreCase(responseData.get(i).getCollectionCompanyname())
                                        &&*//* (responseData.get(i).getStatus().equalsIgnoreCase(Constants.Accepted)
                                        || responseData.get(i).getStatus().equalsIgnoreCase(Constants.OnRouteToPickup)
                                        ||responseData.get(i).getStatus().equalsIgnoreCase(Constants.AtPickupLocation)
                                )) {
                                    responseDataTemp.add(responseData.get(i));
                                }
                            }
                        } else {
                            for (int i = 0; i < responseData.size(); i++) {
                                if (*//*txtPickupAddres.toString().trim().equalsIgnoreCase(responseData.get(i).getDeliveryCompanyname())
                                        &&*//* txt_collection_company.toString().trim().equalsIgnoreCase(responseData.get(i).getDeliveryLocation())
                                        && ((responseData.get(i).getStatus().equalsIgnoreCase(Constants.OnRouteToDelivery)
                                        || responseData.get(i).getStatus().equalsIgnoreCase(Constants.AtDeliveryLocation)
                                        || responseData.get(i).getStatus().equalsIgnoreCase(Constants.PickUp)))) {
                                    responseDataTemp.add(responseData.get(i));
                                }
                            }
                        }
                        selectMultipleJobListAdapter = new SelectMultipleJobListAdapter(responseDataTemp, comeFrom, onClickListener, selectedId);
                        recyclerview_manu.setLayoutManager(new LinearLayoutManager(SelectMultipleJobListActivity.this));
                        recyclerview_manu.setAdapter(selectMultipleJobListAdapter);

                    }
                    try {
                        call_jobApiToday(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                   // responseDataTemp.clear();
                    if (response.body().getMessage().equals("invalidToken")) {
                        prefManager.logout();

                        Intent intent = new Intent(SelectMultipleJobListActivity.this, Login_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else{
                        try {
                            call_jobApiToday(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }

            }

            @Override
            public void onFailure(Call<AcceptedJobListResponse> call, Throwable t) {
                //LogCustom.logd(TAG, t.toString());
                if (pd != null && pd.isShowing())
                    pd.dismiss();

                try {
                    call_jobApiToday(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/
    }

    private void call_jobApiToday() {

        SelectMultipleJobsRequestEntity selectMultipleJobsRequestEntity = new SelectMultipleJobsRequestEntity();
        selectMultipleJobsRequestEntity.setLogin_token(prefManager.getLogin_Token());
        selectMultipleJobsRequestEntity.setApikey(Constants.API_KEY);
        selectMultipleJobsRequestEntity.setVehicleid(prefManager.getTruckId());
        selectMultipleJobsRequestEntity.setStatus(Constants.Accepted + "," + Constants.Started + "," + Constants.PickUp + "," + Constants.OnRouteToPickup + "," + Constants.AtPickupLocation
                + "," + Constants.OnRouteToDelivery + "," + Constants.AtDeliveryLocation);
        selectMultipleJobsRequestEntity.setDriverid(prefManager.getId());
        selectMultipleJobsRequestEntity.setDateFlag("1");

        presenter.getAcceptedJobsWithDate(selectMultipleJobsRequestEntity);

        /*ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);


        if (isFirstTime) {
            pd = LogCustom.loderProgressDialog2(this);
            pd.show();

        }

        PrefManager prefManager = new PrefManager(getApplicationContext());


        Call<AcceptedJobListResponse> call = apiService.getAcceptedJobWithDate(prefManager.getLogin_Token(), Constants.API_KEY, prefManager.getTruckId(),
                Constants.Accepted + "," + Constants.Started + "," + Constants.PickUp + "," + Constants.OnRouteToPickup + "," + Constants.AtPickupLocation
                        + "," + Constants.OnRouteToDelivery + "," + Constants.AtDeliveryLocation, prefManager.getId(),"1");
        call.enqueue(new Callback<AcceptedJobListResponse>() {
            @Override
            public void onResponse(Call<AcceptedJobListResponse> call, Response<AcceptedJobListResponse> response) {
//                Log.e(TAG, "Accepted Job List : " + response.body().toString());

                if (pd != null && pd.isShowing())
                    pd.dismiss();
                if (response.body().getStatus().equals(Constants.API_STATUS)) {
                    ArrayList<AcceptedJob> responseData = (ArrayList<AcceptedJob>) response.body().getData();
                    if (responseData != null && responseData.size() > 0) {
                        responseDataTemp = new ArrayList<AcceptedJob>();
                        if (comeFrom.equalsIgnoreCase(Constants.Job.PICKED_JOB)) {
                            for (int i = 0; i < responseData.size(); i++) {
                                if (txtPickupAddres.toString().trim().equalsIgnoreCase(responseData.get(i).getCollectionLocation())
                                        && txt_collection_company.toString().trim().equalsIgnoreCase(responseData.get(i).getCollectionCompanyname())
                                        && (responseData.get(i).getStatus().equalsIgnoreCase(Constants.Accepted)
                                        || responseData.get(i).getStatus().equalsIgnoreCase(Constants.OnRouteToPickup)
                                        ||responseData.get(i).getStatus().equalsIgnoreCase(Constants.AtPickupLocation)
                                )) {
                                    responseDataTemp.add(responseData.get(i));
                                }
                            }
                        } else {
                            for (int i = 0; i < responseData.size(); i++) {
                                if (txtPickupAddres.toString().trim().equalsIgnoreCase(responseData.get(i).getDeliveryCompanyname())
                                        && txt_collection_company.toString().trim().equalsIgnoreCase(responseData.get(i).getDeliveryLocation())
                                        && ((responseData.get(i).getStatus().equalsIgnoreCase(Constants.OnRouteToDelivery)
                                        || responseData.get(i).getStatus().equalsIgnoreCase(Constants.AtDeliveryLocation)
                                        || responseData.get(i).getStatus().equalsIgnoreCase(Constants.PickUp)))) {
                                    responseDataTemp.add(responseData.get(i));
                                }
                            }
                        }
                        if(selectMultipleJobListAdapter==null){
                            selectMultipleJobListAdapter = new SelectMultipleJobListAdapter(responseDataTemp, comeFrom, onClickListener, selectedId);
                            recyclerview_manu.setLayoutManager(new LinearLayoutManager(SelectMultipleJobListActivity.this));
                            recyclerview_manu.setAdapter(selectMultipleJobListAdapter);
                        }else{

                            selectMultipleJobListAdapter.addData(responseDataTemp);
                        }

                        *//*selectMultipleJobListAdapter = new SelectMultipleJobListAdapter(responseDataTemp, comeFrom, onClickListener, selectedId);
                        recyclerview_manu.setLayoutManager(new LinearLayoutManager(SelectMultipleJobListActivity.this));
                        recyclerview_manu.setAdapter(selectMultipleJobListAdapter);*//*
                    }
                } else {
                   // responseDataTemp.clear();
                    if (response.body().getMessage().equals("invalidToken")) {
                        prefManager.logout();

                        Intent intent = new Intent(SelectMultipleJobListActivity.this, Login_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }

                }

            }

            @Override
            public void onFailure(Call<AcceptedJobListResponse> call, Throwable t) {
                //LogCustom.logd(TAG, t.toString());
                if (pd != null && pd.isShowing())
                    pd.dismiss();
            }
        });*/
    }

    @Override
    public void onSuccessAcceptedJobWitDate(ArrayList<AcceptedJob> responseData,String dateFlag) {
        if(dateFlag.equalsIgnoreCase("0")){
            call_jobApiToday();
        }
        if (responseData != null && responseData.size() > 0) {
            responseDataTemp = new ArrayList<AcceptedJob>();
            if (comeFrom.equalsIgnoreCase(Constants.Job.PICKED_JOB)) {
                for (int i = 0; i < responseData.size(); i++) {
                    if (txtPickupAddres.toString().trim().equalsIgnoreCase(responseData.get(i).getCollectionLocation())
                            && txt_collection_company.toString().trim().equalsIgnoreCase(responseData.get(i).getCollectionCompanyname())
                            && (responseData.get(i).getStatus().equalsIgnoreCase(Constants.Accepted)
                            || responseData.get(i).getStatus().equalsIgnoreCase(Constants.OnRouteToPickup)
                            ||responseData.get(i).getStatus().equalsIgnoreCase(Constants.AtPickupLocation)
                    )) {
                        responseDataTemp.add(responseData.get(i));
                    }
                }
            } else {
                for (int i = 0; i < responseData.size(); i++) {
                    if (txtPickupAddres.toString().trim().equalsIgnoreCase(responseData.get(i).getDeliveryCompanyname())
                            && txt_collection_company.toString().trim().equalsIgnoreCase(responseData.get(i).getDeliveryLocation())
                            && ((responseData.get(i).getStatus().equalsIgnoreCase(Constants.OnRouteToDelivery)
                            || responseData.get(i).getStatus().equalsIgnoreCase(Constants.AtDeliveryLocation)
                            || responseData.get(i).getStatus().equalsIgnoreCase(Constants.PickUp)))) {
                        responseDataTemp.add(responseData.get(i));
                    }
                }
            }
            if(selectMultipleJobListAdapter==null){
                selectMultipleJobListAdapter = new SelectMultipleJobListAdapter(responseDataTemp, comeFrom, onClickListener, selectedId);
                recyclerview_manu.setLayoutManager(new LinearLayoutManager(SelectMultipleJobListActivity.this));
                recyclerview_manu.setAdapter(selectMultipleJobListAdapter);
            }else{
                selectMultipleJobListAdapter.addData(responseDataTemp);
            }
        }
    }

    @Override
    public void onErrorAcceptedJobwithDate(String error,String dateFlag) {
        if (error.equals("invalidToken")) {
            prefManager.logout();

            Intent intent = new Intent(SelectMultipleJobListActivity.this, Login_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }else if(dateFlag.equalsIgnoreCase("0")){
            call_jobApiToday();
        }
    }
}
