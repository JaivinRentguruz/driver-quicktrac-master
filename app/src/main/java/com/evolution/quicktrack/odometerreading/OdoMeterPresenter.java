package com.evolution.quicktrack.odometerreading;

import android.content.Intent;
import android.util.Log;

import com.evolution.quicktrack.Login_Activity;
import com.evolution.quicktrack.activity.OdometerReading_Activity;
import com.evolution.quicktrack.activity.QuestionListActivity;
import com.evolution.quicktrack.base.BaseContractor;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.network.ApiCallInterface;
import com.evolution.quicktrack.response.checkodometer.OdometerResponse;
import com.evolution.quicktrack.response.common.CommonResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OdoMeterPresenter implements BaseContractor, OdoMeterContractor.Presenter {

    private OdoMeterContractor.View view;
    public OdoMeterPresenter(OdoMeterContractor.View view){
        this.view = view;
    }

    @Override
    public void checkOdoMeter(OdoMeterCheckRequestEntity odoMeterCheckRequestEntity) {
        Call<OdometerResponse> call = apiService.checkOdometer(odoMeterCheckRequestEntity.getLoginToken(),odoMeterCheckRequestEntity.getApiKey(), odoMeterCheckRequestEntity.getTruckId(),odoMeterCheckRequestEntity.getDriverId(),odoMeterCheckRequestEntity.getOdoMeter());
        call.enqueue(new Callback<OdometerResponse>() {
            @Override
            public void onResponse(Call<OdometerResponse> call, Response<OdometerResponse> response) {
                if (response.body().getStatus().equals(Constants.API_STATUS)) {
                        view.onCheckOdoMeterSuccess(odoMeterCheckRequestEntity,response.body().getMessage());
                } else {
                    view.onCheckOdoMeterError(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<OdometerResponse> call, Throwable t) {
                view.onCheckOdoMeterError(t.getMessage());
            }
        });
    }

    @Override
    public void addDriverToVehicle(OdoMeterCheckRequestEntity odoMeterCheckRequestEntity) {
        Call<CommonResponse> call = apiService.adddrivertovehicle(odoMeterCheckRequestEntity.getLoginToken(), odoMeterCheckRequestEntity.getApiKey(), odoMeterCheckRequestEntity.getDriverId(), odoMeterCheckRequestEntity.getTruckId());
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.body().getStatus().equals(Constants.API_STATUS)) {
                  view.addDriverToVehicleSuccess(response.body().getMessage());

                } else {
                    view.addDriverToVehicleError(response.body().getMessage());
                }
            }
            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                view.addDriverToVehicleError(t.getMessage());
            }
        });
    }

}
