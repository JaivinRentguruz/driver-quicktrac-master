package com.evolution.quicktrack.pickupsignature;

import com.evolution.quicktrack.response.jobDetails.JobDetailsData;

public interface PickupSignatureContractor {
    interface View{
        void onJobDetailsSuccess(JobDetailsData jobDetailsResponse);
        void onJobDetailsError(String error);

        void onSlipPickupSuccess();
        void onSlipPickupError(String error);
    }
    interface Presenter{
        void getJobDetails(PickupSignatureRequestEntity pickupSignatureRequestEntity);
        void sendSlipPickup(PickupSignatureRequestEntity pickupSignatureRequestEntity);
    }
}
