package com.evolution.quicktrack.multiplejobselection;

import com.evolution.quicktrack.response.acceptedjobList.AcceptedJob;

import java.util.ArrayList;

public interface SelectMultipleJobsContractor {

    interface View{
        void onSuccessAcceptedJobWitDate(ArrayList<AcceptedJob> acceptedJobs,String dateFlag);
        void onErrorAcceptedJobwithDate(String error,String dateFlag);
    }

    interface Presenter{
        void getAcceptedJobsWithDate(SelectMultipleJobsRequestEntity selectMultipleJobsRequestEntity);
    }

}
