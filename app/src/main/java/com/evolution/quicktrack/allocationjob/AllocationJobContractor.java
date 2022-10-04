package com.evolution.quicktrack.allocationjob;

import com.evolution.quicktrack.response.allocatedJobList.AllocatedJobListResponse;
import com.evolution.quicktrack.response.common.CommonResponse;

public interface AllocationJobContractor {

    interface View{
        void onSuccessAllocationJob(AllocatedJobListResponse allocatedJobListResponse);
        void onErrorAllocationJob(String error);

        void onSuccessAllocationStustus(CommonResponse commonResponse);
        void onErrorAllocationStustus(String error);
    }

    interface Presenter{
        void getAllocationJobs(AllocationJobRequestEntity allocationJobRequestEntity);

        void updateAllocationStustus(AllocationJobUpdateStatusRequestEntity allocationJobUpdateStatusRequestEntity);
    }
}
