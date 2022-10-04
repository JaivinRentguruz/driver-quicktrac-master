package com.evolution.quicktrack.constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Constants {


    public static ArrayList<String> arrChatsUser = new ArrayList<>();
//    public static final String BASEURL = "https://dev.quicktrac.com.au/application/api/";
    public static final String BASEURL = "https://app.quicktrac.com.au/application/api/";
    public static final String DistanceCalculatorUrl = "https://maps.googleapis.com/maps/api/distancematrix/";

    //public static String DOWNLOAD_URL="https://dev.quicktrac.com.au/application/apks/Quicktrac-v";
    public static String DOWNLOAD_URL="https://app.quicktrac.com.au/application/apks/Quicktrac-v";

    public static class RestApi{
        public static final String FLEETS = BASEURL + "fleets.php";
    }
    public static class ApiAction {
        public static final String UPDATE_SUB_JOB = "updateSubJob";
        public static final String ADD_JOB_TO_FLEET = "addJobToFleet";
    }

    public static final String API_STATUS = "success";
    public static final String API_STATUS_Fail = "failed";
    public static final String API_KEY = "d6Zwj9vVNqt9QZGj";
    public static final String EXTA_TRUCK = "trackdata";
    public static final String EXTA_TRUCKNAME = "truckName";
    public static final String EXTA_TRUCKID = "truckId";
    public static final String EXTA_FRNDID = "to_id";
    public static final String EXTRA_TYPE="jobtype";
    public static final String EXTRA_JOB_DATA="job_data";


    public static final String isFail = "isFail";
    public static final String isNa = "isNa";
    public static final String isRight = "isRight";

    public static  String OPEN_ACTIVITYNAME = "";

    public static  String OPEN_to_USERID = "";

    public static final   SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");

    public static final String UnAssigned = "0";
    public static final String Assigned = "1";
    public static final String Accepted = "2";
    public static final String Decline = "3";
    public static final String Started = "4";
    public static final String OnRouteToPickup = "5";
    public static final String PickUp = "6";
    public static final String OnRouteToDelivery = "7";
    public static final String AtDeliveryLocation = "8";
    public static final String Delivered = "9";
    public static final String AtPickupLocation = "12";
    public static final String AddJobToFleet = "20";

    public static final String BACK_LOGIN = "0";
    public static final String BACK_SELECTTRUCK = "1";
    public static final String BACK_BRECK = "2";
    public static final String BACK_REFULE = "3";
    public static final String BACK_WASH = "4";
    public static final String BACK_LOCATION = "5";
    public static final String BACK_LOGOUT = "100";



    public static final String EXTRA_JOBID = "jobid";
    public static final String EXTRA_SUB_JOBID = "subjobid";
    public static final String EXTRA_SUBJOBID = "subjobid";


    public static  String JOBID = "";

    public static class Job{
        public static final String DILEVERED_JOB = "0";
        public static final String PICKED_JOB = "1";

        public static final String JOB_TYPE_KEY = "jobTypeKey";

        public static String JOB_0 = "0";
        public static String JOB_1 = "1";
        public static int JOB_INT_1 = 1;

        public static int DELIVER_FLEET_ID_0 = 0;
        public static int PICK_UP_FORM_TYPE_1 = 1;
        public static final String SUB_JOB_ACTION = "updateSubJob";
    }

    public static int SIZE_WIDTH = 1024;
    public static int SIZE_HEIGHT = 1024;



}
