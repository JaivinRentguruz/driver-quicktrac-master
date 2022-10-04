package com.evolution.quicktrack.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.evolution.quicktrack.BaseActivity;
import com.evolution.quicktrack.MainActivity;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.response.vehicle.VehicleDataItem;
import com.evolution.quicktrack.util.PrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 4/2/2018.
 */

public class Welcome_Activity extends BaseActivity {


    public static String TAG = Welcome_Activity.class.getSimpleName();
    @BindView(R.id.txt_driverName)    TextView txtDriverName;
    @BindView(R.id.txt_trackName)    TextView txtTrackName;
    private VehicleDataItem trackData;
    PrefManager prefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        prefManager =new PrefManager(getApplicationContext());


        getIntentData();
    }


    public void getIntentData() {

        if (getIntent().getSerializableExtra(Constants.EXTA_TRUCK) != null) {
            trackData =(VehicleDataItem)getIntent().getSerializableExtra(Constants.EXTA_TRUCK);

            txtDriverName.setText(prefManager.getfullName());
            txtTrackName.setText(trackData.getIdentifier());
        }

    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_welcome;
    }

    @OnClick(R.id.btn_verify)
    public void onViewClicked() {
        Log.d("xxxx", "click");


        Intent intent = new Intent(Welcome_Activity.this, OdometerReading_Activity.class);
        intent.putExtra(Constants.EXTA_TRUCKNAME,trackData.getIdentifier());
        intent.putExtra(Constants.EXTA_TRUCKID,trackData.getId());

        startActivity(intent);

    }


    @OnClick({R.id.img_home, R.id.btn_logout})
    public void onViewClicked2(View view) {
        switch (view.getId()) {
            case R.id.img_home:
                Log.d("xxxxx", "xxx");

                Intent intent = new Intent(Welcome_Activity.this, MainActivity.class);
                startActivity(intent);

                break;
            case R.id.btn_logout:
                onBackPressed();
                break;
        }
    }


}
