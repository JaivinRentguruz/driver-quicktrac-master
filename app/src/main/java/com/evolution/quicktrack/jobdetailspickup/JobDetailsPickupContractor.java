package com.evolution.quicktrack.jobdetailspickup;

import com.evolution.quicktrack.response.jobDetails.JobDetailsResponse;

public interface JobDetailsPickupContractor {
    interface View{
        void onJobDetalsSuccess(JobDetailsResponse jobDetailsResponse);
        void onJobDetailsError(String error);
    }
    interface Presenter{
        void getJobDetails(JobDetailsPickupRequestEntity jobDetailsPickupRequestEntity);
    }
}
