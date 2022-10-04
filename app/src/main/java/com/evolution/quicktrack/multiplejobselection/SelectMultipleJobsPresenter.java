package com.evolution.quicktrack.multiplejobselection;

import com.evolution.quicktrack.base.BaseContractor;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.response.acceptedjobList.AcceptedJob;
import com.evolution.quicktrack.response.acceptedjobList.AcceptedJobListResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectMultipleJobsPresenter implements SelectMultipleJobsContractor.Presenter, BaseContractor {

    private SelectMultipleJobsContractor.View view;

    public SelectMultipleJobsPresenter(SelectMultipleJobsContractor.View view) {
        this.view = view;
    }

    @Override
    public void getAcceptedJobsWithDate(SelectMultipleJobsRequestEntity selectMultipleJobsRequestEntity) {

        Call<AcceptedJobListResponse> call = apiService.getAcceptedJobWithDate(selectMultipleJobsRequestEntity.getLogin_token(), selectMultipleJobsRequestEntity.getApikey(),
                selectMultipleJobsRequestEntity.getVehicleid(),
                selectMultipleJobsRequestEntity.getStatus(), selectMultipleJobsRequestEntity.getDriverid(), selectMultipleJobsRequestEntity.getDateFlag());
        call.enqueue(new Callback<AcceptedJobListResponse>() {
            @Override
            public void onResponse(Call<AcceptedJobListResponse> call, Response<AcceptedJobListResponse> response) {
                if (response.body().getStatus().equals(Constants.API_STATUS)) {
                    ArrayList<AcceptedJob> responseData = (ArrayList<AcceptedJob>) response.body().getData();
                    if (responseData != null && responseData.size() > 0) {
                        view.onSuccessAcceptedJobWitDate(responseData,selectMultipleJobsRequestEntity.getDateFlag());
                    }
                } else {
                    view.onErrorAcceptedJobwithDate(response.body().getMessage(),selectMultipleJobsRequestEntity.getDateFlag());
                }
            }

            @Override
            public void onFailure(Call<AcceptedJobListResponse> call, Throwable t) {
                view.onErrorAcceptedJobwithDate(t.toString(),selectMultipleJobsRequestEntity.getDateFlag());
            }
        });
    }
}
