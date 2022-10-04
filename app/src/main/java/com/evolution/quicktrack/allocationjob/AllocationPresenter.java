package com.evolution.quicktrack.allocationjob;

import com.evolution.quicktrack.base.BaseContractor;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.response.allocatedJobList.AllocatedJobListResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllocationPresenter implements AllocationJobContractor.Presenter, BaseContractor {

    private AllocationJobContractor.View view;

    public AllocationPresenter(AllocationJobContractor.View view) {
        this.view = view;
    }

    @Override
    public void getAllocationJobs(AllocationJobRequestEntity allocationJobRequestEntity) {
        Call<AllocatedJobListResponse> call = apiService.getAllocatedJob(
                allocationJobRequestEntity.getLogin_token(),
                allocationJobRequestEntity.getApikey(),
                allocationJobRequestEntity.getVehicleid()
                , allocationJobRequestEntity.getStatus(),
                allocationJobRequestEntity.getDriverid(), allocationJobRequestEntity.getDateFlag());
        call.enqueue(new Callback<AllocatedJobListResponse>() {
            @Override
            public void onResponse(Call<AllocatedJobListResponse> call, Response<AllocatedJobListResponse> response) {
                if (response.body().getStatus().equals(Constants.API_STATUS)) {
                    view.onSuccessAllocationJob(response.body());
                } else {
                    view.onErrorAllocationJob(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<AllocatedJobListResponse> call, Throwable t) {
                view.onErrorAllocationJob(t.toString());
            }
        });
    }

    @Override
    public void updateAllocationStustus(AllocationJobUpdateStatusRequestEntity allocationJobUpdateStatusRequestEntity) {

    }
}
