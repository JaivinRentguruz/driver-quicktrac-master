package com.evolution.quicktrack.acceptedjob;

import com.evolution.quicktrack.base.BaseContractor;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.response.acceptedjobList.AcceptedJobListResponse;
import com.evolution.quicktrack.response.common.CommonResponse;
import com.evolution.quicktrack.response.jobDetails.JobDetailsData;
import com.evolution.quicktrack.response.jobDetails.JobDetailsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcceptedJobPresenter implements AcceptedJobContractor.Presenter, BaseContractor {

    private AcceptedJobContractor.View view;

    public AcceptedJobPresenter(AcceptedJobContractor.View view) {
        this.view = view;
    }

    @Override
    public void fetchAcceptedJobs(AcceptedJobEntity acceptedJobEntity) {

        Call<AcceptedJobListResponse> call = apiService.getAcceptedJobWithDate(acceptedJobEntity.getLogin_token(), acceptedJobEntity.getApikey(), acceptedJobEntity.getVehicleid(),
                acceptedJobEntity.getStatus(), acceptedJobEntity.getDriverid(), acceptedJobEntity.getDateFlag());
        call.enqueue(new Callback<AcceptedJobListResponse>() {
            @Override
            public void onResponse(Call<AcceptedJobListResponse> call, Response<AcceptedJobListResponse> response) {
                if (response.body().getStatus().equals(Constants.API_STATUS)) {
                    view.onSuccessAccepted(response.body());
                } else {
                    view.onErrorAccepted(response.body().getMessage());
                }

            }

            @Override
            public void onFailure(Call<AcceptedJobListResponse> call, Throwable t) {
                view.onErrorAccepted(t.toString());
            }
        });
    }

    @Override
    public void updateJob(AcceptedJobEntity acceptedJobEntity) {
        Call<CommonResponse> call = apiService.updateStatus(acceptedJobEntity.getLogin_token(), acceptedJobEntity.getApikey(),
                acceptedJobEntity.getDriverid(), acceptedJobEntity.getJobid(), acceptedJobEntity.getStatus(), acceptedJobEntity.getLatitude(),
                acceptedJobEntity.getLongitude(), acceptedJobEntity.getVehicleid(), acceptedJobEntity.getType(), acceptedJobEntity.getSubjobid() + "");
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.body().getStatus().equals(Constants.API_STATUS)) {

                    view.onSuccessUpdateJob(acceptedJobEntity);

                } else {
                    view.onErrorUpdateJob(response.body().getMessage());

                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                view.onErrorUpdateJob(t.toString());
            }
        });
    }

    @Override
    public void fetchDetails(AcceptedJobEntity acceptedJobEntity) {
        Call<JobDetailsResponse> call = apiService.getjobDetails(acceptedJobEntity.getLogin_token(), acceptedJobEntity.getApikey(), acceptedJobEntity.getJobid(), acceptedJobEntity.getDriverid());
        call.enqueue(new Callback<JobDetailsResponse>() {
            @Override
            public void onResponse(Call<JobDetailsResponse> call, Response<JobDetailsResponse> response) {
                if (response.body().getStatus().equals(Constants.API_STATUS)) {

                    JobDetailsData jobDetailsDatas = response.body().getData();
                    if (jobDetailsDatas != null) {
                        view.fetchDetailsSuccess(response.body());
                    } else {
                        view.fetchDetailsError(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<JobDetailsResponse> call, Throwable t) {
                view.fetchDetailsError(t.toString());
            }
        });
    }
}
