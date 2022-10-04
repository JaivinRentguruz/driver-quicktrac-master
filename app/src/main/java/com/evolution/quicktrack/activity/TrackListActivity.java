package com.evolution.quicktrack.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.evolution.quicktrack.BaseActivity;
import com.evolution.quicktrack.Login_Activity;
import com.evolution.quicktrack.MainActivity;
import com.evolution.quicktrack.QuickTrackAplication;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.adapter.Custom_TrackList_Adepter;
import com.evolution.quicktrack.base.BaseContractor;
import com.evolution.quicktrack.callback.Recycler_ItemClickCallBack;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.response.vehicle.VehicleDataItem;
import com.evolution.quicktrack.response.vehicle.VehicleResponse;
import com.evolution.quicktrack.tracklist.TrackListContractor;
import com.evolution.quicktrack.tracklist.TrackListPresenter;
import com.evolution.quicktrack.tracklist.TrackListRequestEntity;
import com.evolution.quicktrack.util.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TrackListActivity extends BaseActivity implements Recycler_ItemClickCallBack, BaseContractor,TrackListContractor.View {


    private TrackListContractor.Presenter trackListPresenter;
    @BindView(R.id.recyclerview_manu)
    RecyclerView recyclerviewManu;
    public static String TAG = Login_Activity.class.getSimpleName();

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trackListPresenter = new TrackListPresenter(this);

        ButterKnife.bind(this);
        try {
            initView();
            ((QuickTrackAplication) getApplicationContext()).checkOverlayDraw(this);
            callApi();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_tracklist;
    }

    public void initView() {
        this.recyclerviewManu.setHasFixedSize(true);
        this.recyclerviewManu.setLayoutManager(new LinearLayoutManager(TrackListActivity.this, LinearLayoutManager.VERTICAL, false));
        this.recyclerviewManu.setNestedScrollingEnabled(false);
        swipeContainer.setOnRefreshListener(() -> {
            try {
                callGetTrackApi();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    public void callApi() {
        try {
            if (Utils.validInternet(getApplicationContext()))
                callGetTrackApi();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<VehicleDataItem> responseData;

    private void callGetTrackApi() {
        showProgressDialogue();
        TrackListRequestEntity trackListRequestEntity = new TrackListRequestEntity();
        trackListRequestEntity.setApiKey(Constants.API_KEY);
        trackListRequestEntity.setDriverId(prefManager.getId());
        trackListRequestEntity.setLoginToken(prefManager.getLogin_Token());
        trackListPresenter.getVehicle(trackListRequestEntity);
    }


    @Override
    public void OnItemClick(int position) {
        try {
            prefManager.setTryckData(responseData.get(position).getId(), responseData.get(position).getIdentifier());
            Intent intent = new Intent(TrackListActivity.this, OdometerReading_Activity.class);
            intent.putExtra(Constants.EXTA_TRUCKNAME, responseData.get(position).getIdentifier());
            intent.putExtra(Constants.EXTA_TRUCKID, responseData.get(position).getId());
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.img_home, R.id.btn_logout})
    public void onViewClicked2(View view) {
        switch (view.getId()) {
            case R.id.img_home:
                Intent intent = new Intent(TrackListActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_logout:
                onBackPressed();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Toast.makeText(TrackListActivity.this, getString(R.string.not_allow_back), Toast.LENGTH_LONG).show();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onGetVehicleSuccess(VehicleResponse vehicleResponse) {
        hideProgressDialogue();
        swipeContainer.setRefreshing(false);
        try {
            responseData = vehicleResponse.getData();
            Custom_TrackList_Adepter custom_manu_adepter = new Custom_TrackList_Adepter(TrackListActivity.this, responseData, TrackListActivity.this);
            recyclerviewManu.setAdapter(custom_manu_adepter);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void onGetVehicleError(String message) {
        hideProgressDialogue();
        swipeContainer.setRefreshing(false);
        if (message.equals("invalidToken")) {
            prefManager.logout();
            Intent intent = new Intent(TrackListActivity.this, Login_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }else{
            showToast(message);
        }

    }
}
