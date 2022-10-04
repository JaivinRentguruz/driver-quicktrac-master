package com.evolution.quicktrack.pickuplocation;

import com.evolution.quicktrack.base.BaseContractor;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.response.common.CommonResponse;
import com.evolution.quicktrack.response.jobDetails.JobDetailsData;
import com.evolution.quicktrack.response.jobDetails.JobDetailsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PickupLocationPresenter implements PickupLocationContractor.Presenter, BaseContractor {

    private PickupLocationContractor.View view;

    public PickupLocationPresenter(PickupLocationContractor.View view) {
        this.view = view;
    }


    @Override
    public void getJobDetails(PickupLocationRequestEntity pickupLocationRequestEntity, String jobType) {

        Call<JobDetailsResponse> call;
        if(jobType.equals(Constants.Job.JOB_1)) {
            call = apiService.getSubJobDetails(pickupLocationRequestEntity.getLogin_token(), pickupLocationRequestEntity.getApikey(), pickupLocationRequestEntity.getJobid(), pickupLocationRequestEntity.getDriverid());
        }
        else {
            call = apiService.getjobDetails(pickupLocationRequestEntity.getLogin_token(), pickupLocationRequestEntity.getApikey(), pickupLocationRequestEntity.getJobid(), pickupLocationRequestEntity.getDriverid());
        }

        call.enqueue(new Callback<JobDetailsResponse>() {
            @Override
            public void onResponse(Call<JobDetailsResponse> call, Response<JobDetailsResponse> response) {

                if (response.body().getStatus().equals(Constants.API_STATUS)) {

                    JobDetailsData jobDetailsData = response.body().getData();
                    if (jobDetailsData != null) {
                        view.onSuccessJobDetails(jobDetailsData, pickupLocationRequestEntity.isFromMultipleJob());
                    } else {
                        view.onErrorJobDetails(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<JobDetailsResponse> call, Throwable t) {
                view.onErrorJobDetails(t.toString());
            }
        });
    }

    @Override
    public void updateJob(PickupLocationUpdateRequestEntity pickupLocationUpdateRequestEntity) {


        Call<CommonResponse> call = apiService.updateStatus(pickupLocationUpdateRequestEntity.getLogin_token(),
                pickupLocationUpdateRequestEntity.getApikey(), pickupLocationUpdateRequestEntity.getDriverid(), pickupLocationUpdateRequestEntity.getJobid(),
                pickupLocationUpdateRequestEntity.getStatus(), pickupLocationUpdateRequestEntity.getLatitude(),
                pickupLocationUpdateRequestEntity.getLongitude(), pickupLocationUpdateRequestEntity.getVehicle_id(),
                pickupLocationUpdateRequestEntity.getType(), pickupLocationUpdateRequestEntity.getSubjobid());
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.body().getStatus().equals(Constants.API_STATUS)) {
                    view.onSuccessUpdateJob();
                } else {
                    view.onErrorUpdateJob(response.body().getMessage());

                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                LogCustom.logd("Error", t.toString());
            }
        });
    }
}
