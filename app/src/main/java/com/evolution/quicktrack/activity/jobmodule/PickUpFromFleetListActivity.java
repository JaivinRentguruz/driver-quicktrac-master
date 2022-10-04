package com.evolution.quicktrack.activity.jobmodule;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.evolution.quicktrack.adapter.DeliveryToDepoFleetList_Adepter;
import com.evolution.quicktrack.callback.Recycler_AcceptedJobClickCallBack;
import com.evolution.quicktrack.callback.Recycler_ItemClickCallBack;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.location.GetLocation;
import com.evolution.quicktrack.model.JobModel;
import com.evolution.quicktrack.network.ApiCallInterface;
import com.evolution.quicktrack.network.ApiClient;
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


public class PickUpFromFleetListActivity extends BaseActivity implements Recycler_ItemClickCallBack, Recycler_AcceptedJobClickCallBack {

    @BindView(R.id.recyclerview_manu)
    RecyclerViewEmptySupport recyclerview;
    @BindView(R.id.txt_nodata)
    TextView txtNodata;
    @BindView(R.id.emptyView)
    LinearLayout emptyView;
    private KProgressHUD pd;
    public static String TAG = PickUpFromFleetListActivity.class.getSimpleName();
    @BindView(R.id.btn_logout)
    ImageView btnLogout;

    @BindView(R.id.btn_move_to_depo)
    Button btnMoveToDepo;

    DeliveryToDepoFleetList_Adepter custom_manu_adepter;



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
        return R.layout.activity_pickupdepo_fleetlist;
    }


    public void initView() {


        this.recyclerview.setHasFixedSize(true);
        this.recyclerview.setLayoutManager(new LinearLayoutManager(PickUpFromFleetListActivity.this, LinearLayoutManager.VERTICAL, false));
        this.recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setEmptyView(emptyView);

        GetLocation getLocation = new GetLocation(PickUpFromFleetListActivity.this);

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


        Call<FleetListResponse> call = apiService.getFleetList(prefManager.getLogin_Token(), Constants.API_KEY,prefManager.getId(),"list");
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

                        Intent intent = new Intent(PickUpFromFleetListActivity.this, Login_Activity.class);
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

         custom_manu_adepter = new DeliveryToDepoFleetList_Adepter(PickUpFromFleetListActivity.this, fleetResponseList, PickUpFromFleetListActivity.this);
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

                FleetResponse fb = stream(fleetResponseList).where(c-> c.isSelected() == true).first();

                Intent i = new Intent(PickUpFromFleetListActivity.this,PickUpFromDepoListActivity.class);
                i.putExtra("selectedId",fb.getId());
                startActivity(i);
                // close this activity
                finish();

                break;
        }
    }

    public void homeDilog() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PickUpFromFleetListActivity.this);
        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.wanttogohome));
        alertDialog.setCancelable(false);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(PickUpFromFleetListActivity.this, MainActivity.class);
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

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PickUpFromFleetListActivity.this);

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure want to logout ?");
        alertDialog.setCancelable(true);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showOdometerDialog(PickUpFromFleetListActivity.this);
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
            if(fleetResponseList.get(position).isSelected())
            {
                fleetResponseList.get(position).setSelected(false);
            }
            else
            {
                fleetResponseList.get(position).setSelected(true);
            }



            int count = stream(fleetResponseList).where(j -> j.isSelected() == true).count();
            if(count > 0)
            {
                btnMoveToDepo.setVisibility(View.VISIBLE);
                for(int i = 0; i<fleetResponseList.size();i++)
                {
                    if(i != position)
                    {
                        fleetResponseList.get(i).setSelected(false);
                    }
                }
            }
            else
            {
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
        LogCustom.logd("xxxxxxxx allocatedjob","onDestroy");
        super.onDestroy();

    }


    private void call_addJobToFleet(JobModel jobModel,int dropid,boolean isFirstTime) throws Exception {
        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);


        if (isFirstTime) {
            pd = LogCustom.loderProgressDialog2(this);
            pd.show();

        }

        PrefManager prefManager = new PrefManager(getApplicationContext());


        Call<AddToFleetRespose> call = apiService.addJobToFleetDeliver(prefManager.getLogin_Token(), Constants.API_KEY,prefManager.getId(),"addJobToFleet",String.valueOf(jobModel.getId()),String.valueOf(prefManager.getTruckId()),String.valueOf(jobModel.getType()),dropid,String.valueOf(jobModel.getSubjobId()));
        call.enqueue(new Callback<AddToFleetRespose>() {
            @Override
            public void onResponse(Call<AddToFleetRespose> call, Response<AddToFleetRespose> response) {

                if (pd != null && pd.isShowing())
                    pd.dismiss();
                if (response.body().getStatus().equals("true")) {



                } else {
                    if (response.body().getMessage().equals("invalidToken")) {
                        prefManager.logout();

                        Intent intent = new Intent(PickUpFromFleetListActivity.this, Login_Activity.class);
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