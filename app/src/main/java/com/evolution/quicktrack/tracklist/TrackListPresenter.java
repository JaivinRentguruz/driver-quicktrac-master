package com.evolution.quicktrack.tracklist;

import com.evolution.quicktrack.base.BaseContractor;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.response.vehicle.VehicleResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackListPresenter implements BaseContractor,TrackListContractor.Presenter{
    private TrackListContractor.View view;

    public TrackListPresenter(TrackListContractor.View view){
        this.view = view;
    }

    @Override
    public void getVehicle(TrackListRequestEntity trackListRequestEntity) {
        Call<VehicleResponse> call = apiService.getVehicle(trackListRequestEntity.getLoginToken(),trackListRequestEntity.getApiKey(),trackListRequestEntity.getDriverId());
        call.enqueue(new Callback<VehicleResponse>() {
            @Override
            public void onResponse(Call<VehicleResponse> call, Response<VehicleResponse> response) {
                if (response.body().getStatus().equals(Constants.API_STATUS)) {
                    view.onGetVehicleSuccess(response.body());
                } else {
                    view.onGetVehicleError(response.body().getMessage());
                }
            }
            @Override
            public void onFailure(Call<VehicleResponse> call, Throwable t) {
                view.onGetVehicleError(t.getMessage());
            }
        });
    }
}
