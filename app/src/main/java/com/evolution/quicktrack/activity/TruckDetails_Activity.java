package com.evolution.quicktrack.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.evolution.quicktrack.BaseActivity;
import com.evolution.quicktrack.Login_Activity;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.network.ApiCallInterface;
import com.evolution.quicktrack.network.ApiClient;
import com.evolution.quicktrack.response.truckdetails.TruckDetailsResponse;
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

public class TruckDetails_Activity extends BaseActivity {

    @BindView(R.id.img_home)
    ImageView imgHome;
    @BindView(R.id.btn_logout)
    ImageView btnLogout;
    @BindView(R.id.txt_identifier)
    TextView txtIdentifier;
    @BindView(R.id.txt_truck_company)
    TextView txtTruckCompany;
    @BindView(R.id.txt_licence_plate)
    TextView txtLicencePlate;


    private KProgressHUD pd;
    public static String TAG = TruckDetails_Activity.class.getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView(R.layout.activity_odometer);
        ButterKnife.bind(this);
        btnLogout.setVisibility(View.INVISIBLE);
        initView();
    }


    public void initView() {



        callApi();

    }


    public void callApi(){

        if(Utils.validInternet(getApplicationContext())){
            try {

                getTruckDetails();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }



    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_truckdetail;
    }


    @OnClick({R.id.img_home, R.id.btn_logout})
    public void onViewClicked2(View view) {
        switch (view.getId()) {
            case R.id.img_home:
                Log.d("xxxxx", "xxx");

                exitDilog();

                break;
            case R.id.btn_logout:


                break;
        }
    }


    public void exitDilog() {
        onBackPressed();
       /* final AlertDialog.Builder alertDialog = new AlertDialog.Builder(TruckDetails_Activity.this);
        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.areyousureexit));
        alertDialog.setCancelable(false);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onBackPressed();

            }
        });

        alertDialog.setNeutralButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // Showing Alert Message
        alertDialog.show();*/

    }


    private void getTruckDetails() throws Exception {


        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);

        PrefManager prefManager = new PrefManager(getApplicationContext());

        pd = LogCustom.loderProgressDialog2(this);
        pd.show();

        Call<TruckDetailsResponse> call = apiService.getTruckDetails(prefManager.getLogin_Token(),Constants.API_KEY, prefManager.getTruckId(),prefManager.getId());
        call.enqueue(new Callback<TruckDetailsResponse>() {
            @Override
            public void onResponse(Call<TruckDetailsResponse> call, Response<TruckDetailsResponse> response) {

                Log.d(TAG, " : check status : " + response.body().toString());

                if (pd != null && pd.isShowing())
                    pd.dismiss();
                if (response.body().getStatus().equals(Constants.API_STATUS)) {


                    txtIdentifier.setText(response.body().getData().get(0).getIdentifier());
                    txtLicencePlate.setText(response.body().getData().get(0).getLicencePlate());
                    txtTruckCompany.setText(response.body().getData().get(0).getTruckCompany());

                }
                else
                {


                    if(response.body().getMessage().equals("invalidToken"))
                    {
                        prefManager.logout();

                        Intent intent =new Intent(TruckDetails_Activity.this,Login_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<TruckDetailsResponse> call, Throwable t) {
                LogCustom.logd(TAG, t.toString());
                if (pd != null && pd.isShowing())
                    pd.dismiss();
            }
        });
    }


}
