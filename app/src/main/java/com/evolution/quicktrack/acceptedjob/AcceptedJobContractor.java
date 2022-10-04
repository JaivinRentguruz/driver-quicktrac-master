package com.evolution.quicktrack.acceptedjob;

import com.evolution.quicktrack.response.acceptedjobList.AcceptedJobListResponse;
import com.evolution.quicktrack.response.common.CommonResponse;
import com.evolution.quicktrack.response.jobDetails.JobDetailsResponse;

public interface AcceptedJobContractor {

    interface View {
        void onSuccessAccepted(AcceptedJobListResponse acceptedJobListResponse);

        void onErrorAccepted(String error);

        void onSuccessUpdateJob(AcceptedJobEntity commonResponse);

        void onErrorUpdateJob(String error);

        void fetchDetailsSuccess(JobDetailsResponse jobDetailsResponse);

        void fetchDetailsError(String error);

    }

    interface Presenter {
        void fetchAcceptedJobs(AcceptedJobEntity acceptedJobEntity);

        void updateJob(AcceptedJobEntity acceptedJobEntity);

        void fetchDetails(AcceptedJobEntity acceptedJobEntity);
    }
}
