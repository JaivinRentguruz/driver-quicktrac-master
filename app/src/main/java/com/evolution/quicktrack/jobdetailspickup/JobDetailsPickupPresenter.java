package com.evolution.quicktrack.jobdetailspickup;

import com.evolution.quicktrack.base.BaseContractor;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.response.jobDetails.JobDetailsData;
import com.evolution.quicktrack.response.jobDetails.JobDetailsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobDetailsPickupPresenter implements JobDetailsPickupContractor.Presenter, BaseContractor {

    private JobDetailsPickupContractor.View view;

    public JobDetailsPickupPresenter(JobDetailsPickupContractor.View view){
        this.view = view;
    }


    @Override
    public void getJobDetails(JobDetailsPickupRequestEntity jobDetailsPickupRequestEntity) {
        Call<JobDetailsResponse> call = apiService.getjobDetails(jobDetailsPickupRequestEntity.getLogin_token(), jobDetailsPickupRequestEntity.getApikey(),
                jobDetailsPickupRequestEntity.getJobid(), jobDetailsPickupRequestEntity.getDriverid());
        call.enqueue(new Callback<JobDetailsResponse>() {
            @Override
            public void onResponse(Call<JobDetailsResponse> call, Response<JobDetailsResponse> response) {

                if (response.body().getStatus().equals(Constants.API_STATUS)) {
                    if (response.body() != null) {
                        view.onJobDetalsSuccess(response.body());
                    } else {
                        view.onJobDetailsError(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<JobDetailsResponse> call, Throwable t) {
                view.onJobDetailsError(t.toString());
            }
        });
    }
}
