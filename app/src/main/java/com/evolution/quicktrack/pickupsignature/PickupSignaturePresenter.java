package com.evolution.quicktrack.pickupsignature;

import com.evolution.quicktrack.base.BaseContractor;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.network.ApiCallInterface;
import com.evolution.quicktrack.network.ApiClient;
import com.evolution.quicktrack.response.common.CommonResponse;
import com.evolution.quicktrack.response.jobDetails.JobDetailsData;
import com.evolution.quicktrack.response.jobDetails.JobDetailsResponse;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PickupSignaturePresenter implements BaseContractor, PickupSignatureContractor.Presenter {

    private PickupSignatureContractor.View view;

    public PickupSignaturePresenter(PickupSignatureContractor.View view) {
        this.view = view;
    }

    @Override
    public void getJobDetails(PickupSignatureRequestEntity pickupSignatureRequestEntity) {
        Call<JobDetailsResponse> call = apiService.getjobDetails(pickupSignatureRequestEntity.getLoginToken(), pickupSignatureRequestEntity.getApikey(), pickupSignatureRequestEntity.getJobid(), pickupSignatureRequestEntity.getDriverid());
        call.enqueue(new Callback<JobDetailsResponse>() {
            @Override
            public void onResponse(Call<JobDetailsResponse> call, Response<JobDetailsResponse> response) {

                if (response.body().getStatus().equals(Constants.API_STATUS)) {

                    JobDetailsData jobDetailsDatas = response.body().getData();
                    if (jobDetailsDatas != null) {
                        view.onJobDetailsSuccess(jobDetailsDatas);
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

    @Override
    public void sendSlipPickup(PickupSignatureRequestEntity pickupSignatureRequestEntity) {
        RequestBody APIKEY = RequestBody.create(MediaType.parse("multipart/form-data"), pickupSignatureRequestEntity.getApikey());
        RequestBody jobid = RequestBody.create(MediaType.parse("multipart/form-data"), pickupSignatureRequestEntity.getJobid());
        RequestBody driverName = RequestBody.create(MediaType.parse("multipart/form-data"), pickupSignatureRequestEntity.getPickupslip_driver_name());
        RequestBody consignName = RequestBody.create(MediaType.parse("multipart/form-data"), pickupSignatureRequestEntity.getPickupslip_consignee_name());
        RequestBody token = RequestBody.create(MediaType.parse("multipart/form-data"), pickupSignatureRequestEntity.getLoginToken());
        RequestBody driverId = RequestBody.create(MediaType.parse("multipart/form-data"), pickupSignatureRequestEntity.getDriverid());

        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);
        Call<CommonResponse> call = apiService.sendSlipPickup(
                APIKEY,
                driverId,
                token,
                jobid,
                driverName,
                consignName,
                pickupSignatureRequestEntity.getPickupslip_consignee_sign(),
                pickupSignatureRequestEntity.getPickup_item_picture()
        );

        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.body().getStatus().equals(Constants.API_STATUS)) {
                    view.onSlipPickupSuccess();
                } else {

                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
            }
        });


    }
}
