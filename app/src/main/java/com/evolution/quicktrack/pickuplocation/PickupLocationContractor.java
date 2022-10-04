package com.evolution.quicktrack.pickuplocation;

import com.evolution.quicktrack.response.jobDetails.JobDetailsData;

public interface PickupLocationContractor {
    interface View{
        void onSuccessJobDetails(JobDetailsData jobDetailsData,boolean isFromMultipleJob);
        void onErrorJobDetails(String error);

        void onSuccessUpdateJob();
        void onErrorUpdateJob(String error);
    }
    interface Presenter{
            void getJobDetails(PickupLocationRequestEntity pickupLocationRequestEntity, String jobType);

            void updateJob(PickupLocationUpdateRequestEntity pickupLocationUpdateRequestEntity);
    }
}
