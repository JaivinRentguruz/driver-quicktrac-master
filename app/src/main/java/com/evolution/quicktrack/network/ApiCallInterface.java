package com.evolution.quicktrack.network;


import com.evolution.quicktrack.response.fleetres.AddToFleetRespose;
import com.evolution.quicktrack.response.fleetres.FleetListResponse;
import com.evolution.quicktrack.response.acceptedjobList.AcceptedJobListResponse;
import com.evolution.quicktrack.response.allocatedJobList.AllocatedJobListResponse;
import com.evolution.quicktrack.response.breckdown.BreckDownResponse;
import com.evolution.quicktrack.response.chatlist.ChatListResponse;
import com.evolution.quicktrack.response.checkodometer.OdometerResponse;
import com.evolution.quicktrack.response.common.CommonResponse;
import com.evolution.quicktrack.response.getcustomerAddress.CustomerAddressResponse;
import com.evolution.quicktrack.response.jobDetails.JobDetailsResponse;
import com.evolution.quicktrack.response.login.LoginResponse;
import com.evolution.quicktrack.response.qustionlist.QuestionListResponse;
import com.evolution.quicktrack.response.sentmsg.SentMsgResponse;
import com.evolution.quicktrack.response.track_driver.TrackDriverResponse;
import com.evolution.quicktrack.response.truckdetails.TruckDetailsResponse;
import com.evolution.quicktrack.response.userlist.UserListResponse;
import com.evolution.quicktrack.response.vehicle.VehicleResponse;
import com.evolution.quicktrack.response.washtruckAndRefule.WashTruckResponse;
import com.google.gson.JsonElement;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiCallInterface {


    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> login_api(@Field("apikey") String apikey, @Field("licencenumber") String licencenumber,
                                  @Field("password") String password, @Field("device_id") String deviceid,
                                  @Field("fcm_token") String fcm_token, @Field("imei_number") String imei_number);

    @FormUrlEncoded
    @POST("force_logout.php")
    Call<CommonResponse> force_logout_api(@Field("apikey") String apikey,
                                          @Field("driverid") String driverid,
                                          @Field("login_token") String login_token,
                                          @Field("fcm_token") String fcm_token);

    @FormUrlEncoded
    @POST("vehicle.php")
    Call<VehicleResponse> getVehicle(@Field("login_token") String login_token, @Field("apikey") String apikey, @Field("driverid") String driverid);

    @FormUrlEncoded
    @POST("adddrivertovehicle.php")
    Call<CommonResponse> adddrivertovehicle(@Field("login_token") String login_token, @Field("apikey") String apikey, @Field("driverid") String driverid, @Field("truckid") String truckid);


    @FormUrlEncoded
    @POST("checkodometer.php")
    Call<OdometerResponse> checkOdometer(@Field("login_token") String login_token, @Field("apikey") String apikey, @Field("truckid") String truckid, @Field("driverid") String driverid, @Field("odometer") String odometer);

    @FormUrlEncoded
    @POST("updateodometer.php")
    Call<OdometerResponse> updateOdometer(@Field("login_token") String login_token, @Field("apikey") String apikey, @Field("truckid") String truckid, @Field("driverid") String driverid, @Field("odometer") String odometer);


    @FormUrlEncoded
    @POST("checklist.php")
    Call<QuestionListResponse> getQuestion(@Field("login_token") String login_token, @Field("apikey") String apikey, @Field("truckid") String truckid, @Field("driverid") String driverid);


    @FormUrlEncoded
    @POST("fatigue.php")
    Call<WashTruckResponse> washTruckAndRefuleTruck(@Field("login_token") String login_token, @Field("apikey") String apikey, @Field("truckid") String truckid,
                                                    @Field("driverid") String driverid, @Field("startdatetime") String startdatetime,
                                                    @Field("enddatetime") String enddatetime, @Field("types") String types);


    @FormUrlEncoded
    @POST("truckdetail.php")
    Call<TruckDetailsResponse> getTruckDetails(@Field("login_token") String login_token, @Field("apikey") String apikey, @Field("truckid") String truckid, @Field("driverid") String driverid);


    @FormUrlEncoded
    @POST("checklistsubmit.php")
    Call<CommonResponse> uploadData3(@Field("login_token") String login_token, @Field("data") String user_id, @Field("driverid") String driverid);


    @FormUrlEncoded
    @POST("jobs.php")
    Call<AllocatedJobListResponse> getAllocatedJob(@Field("login_token") String login_token, @Field("apikey") String apikey, @Field("vehicleid") String vehicleid,
                                                   @Field("status") String status, @Field("driverid") String driverid, @Field("dateFlag") String dateFlag);


    @FormUrlEncoded
    @POST("jobs.php")
    Call<AcceptedJobListResponse> getAcceptedJobWithDate(@Field("login_token") String login_token, @Field("apikey") String apikey, @Field("vehicleid") String vehicleid,
                                                         @Field("status") String status, @Field("driverid") String driverid, @Field("dateFlag") String dateFlag);

    @FormUrlEncoded
    @POST("jobs.php")
    Call<AcceptedJobListResponse> getAcceptedJob(@Field("login_token") String login_token, @Field("apikey") String apikey, @Field("vehicleid") String vehicleid,
                                                 @Field("status") String status, @Field("driverid") String driverid);


    @FormUrlEncoded
    @POST("chat.php")
    Call<UserListResponse> getUserList(@Field("login_token") String login_token, @Field("APIKEY") String apikey, @Field("from_user_id") String from_user_id, @Field("action") String action, @Field("driverid") String driverid);


    @FormUrlEncoded
    @POST("chat.php")
    Call<ChatListResponse> getChat(@Field("login_token") String login_token, @Field("APIKEY") String apikey, @Field("from_user_id") String from_user_id, @Field("action") String action, @Field("to_user_id") String to_user_id, @Field("firstmessageid") String firstmessageid, @Field("driverid") String driverid);

    @FormUrlEncoded
    @POST("chat.php")
    Call<SentMsgResponse> sendMsg(@Field("login_token") String login_token, @Field("APIKEY") String apikey, @Field("action") String action,
                                  @Field("from_user_id") String from_user_id,
                                  @Field("to_user_id") String to_user_id,
                                  @Field("from_user_role") String from_user_role, @Field("to_user_role") String to_user_role,
                                  @Field("message") String message
            , @Field("driverid") String driverid);


    //profileadd
    @Multipart
    @POST("chat.php")
    Call<SentMsgResponse> sendFile(@Part("login_token") RequestBody login_token, @Part("APIKEY") RequestBody APIKEY, @Part("action") RequestBody action,
                                   @Part("from_user_id") RequestBody email, @Part("from_user_role") RequestBody mobile,
                                   @Part("to_user_id") RequestBody city, @Part("to_user_role") RequestBody state,
                                   @Part MultipartBody.Part file
            , @Part("driverid") RequestBody driverid);


    @FormUrlEncoded
    @POST("jobupdate.php")
    Call<CommonResponse> acceptJobFromPopup(@Field("login_token") String login_token, @Field("apikey") String apikey, @Field("driverid") String driverid,
                                            @Field("jobid") String jobid, @Field("status") String status,
                                            @Field("latitude") String latitude, @Field("longitude") String longitude, @Field("vehicle_id") String vehicle_id
            , @Field("comment") String comment, @Field("type") String type, @Field("subjobid") String subjobid

    );

    @FormUrlEncoded
    @POST("jobupdate.php")
    Call<CommonResponse> updateStatus(@Field("login_token") String login_token, @Field("apikey") String apikey, @Field("driverid") String driverid,
                                      @Field("jobid") String jobid, @Field("status") String status,
                                      @Field("latitude") String latitude, @Field("longitude") String longitude, @Field("vehicle_id") String vehicle_id
            , @Field("type") String type, @Field("subjobid") String subjobid);

    @FormUrlEncoded
    @POST("jobupdate.php")
    Call<CommonResponse> updateStatusWithMessage(@Field("login_token") String login_token, @Field("apikey") String apikey, @Field("driverid") String driverid,
                                                 @Field("jobid") String jobid, @Field("status") String status,
                                                 @Field("latitude") String latitude, @Field("longitude") String longitude, @Field("vehicle_id") String vehicle_id, @Field("comment") String comment
            , @Field("type") String type, @Field("subjobid") String subjobid

    );


    @FormUrlEncoded
    @POST("jobupdate.php")
    Call<CommonResponse> updateStatusFutile(@Field("login_token") String login_token, @Field("apikey") String apikey, @Field("driverid") String driverid,
                                            @Field("jobid") String jobid, @Field("status") String status,
                                            @Field("latitude") String latitude, @Field("longitude") String longitude,
                                            @Field("vehicle_id") String vehicle_id, @Field("pickupFutileComment") String pickupFutileComment
            , @Field("type") String type, @Field("subjobid") String subjobid

    );


    //https://dev.quicktrac.com.au/application/api/job_comment.php

    @FormUrlEncoded
    @POST("job_comment.php")
    Call<CommonResponse> addDelayComment(@Field("APIKEY") String APIKey,
                                         @Field("driverid") String driverId,
                                         @Field("login_token") String loginToken,
                                         @Field("jobid") String jobid,
                                         @Field("comment") String comment,
                                         @Field("vehicle_id") String vehicleId,
                                         @Field("action") String action);


    @FormUrlEncoded
    @POST("jobupdate.php")
    Call<CommonResponse> updateStatusFutileDelivery(@Field("login_token") String login_token, @Field("apikey") String apikey, @Field("driverid") String driverid,
                                                    @Field("jobid") String jobid, @Field("status") String status,
                                                    @Field("latitude") String latitude, @Field("longitude") String longitude,
                                                    @Field("vehicle_id") String vehicle_id, @Field("deliveryFutileComment") String pickupFutileComment
            , @Field("type") String type, @Field("subjobid") String subjobid //small ma pass karvanu che ..subjobid

    );

 /*   @FormUrlEncoded
    @POST("jobupdate.php")
    Call<CommonResponse> updateStatusDecline(@Field("login_token") String login_token, @Field("apikey") String apikey, @Field("driverid") String driverid,
                                                    @Field("jobid") String jobid, @Field("status") String status,
                                                    @Field("latitude") String latitude, @Field("longitude") String longitude,
                                             @Field("vehicle_id") String vehicle_id,@Field("comment") String pickupFutileComment


    );*/


    @FormUrlEncoded
    @POST("futileJobDelivery.php")
    Call<CommonResponse> futileJobDelivery(@Field("login_token") String login_token, @Field("APIKEY") String apikey, @Field("driverid") String driverid,
                                           @Field("id") String jobid, @Field("fleet") String fleet,
                                           @Field("action") String action

    );


    @FormUrlEncoded
    @POST("fleets.php")
    Call<CommonResponse> updateSubJobStatus(@Field("login_token") String login_token, @Field("APIKEY") String apikey, @Field("driverid") String driverid,
                                            @Field("subjobId") String jobid, @Field("status") String status,
                                            @Field("latitude") String latitude, @Field("longitude") String longitude, @Field("vehicle_id") String vehicle_id, @Field("action") String action, @Field("comment") String pickupFutileComment

    );


    @FormUrlEncoded
    @POST("track_driver.php")
    Call<TrackDriverResponse> callBackGround(@Field("login_token") String login_token, @Field("apikey") String apikey, @Field("vehicle_id") String vehicle_id,
                                             @Field("driver_id") String driver_id, @Field("status") String status,
                                             @Field("latitude") String latitude, @Field("longitude") String longitude);


    @Multipart
    @POST("incident.php")
    Call<BreckDownResponse> uploadMultipleFilesDynamic(
            @Part("login_token") RequestBody login_token,
            @Part("APIKEY") RequestBody description,
            @Part("device_id") RequestBody device_id,
            @Part("driver_id") RequestBody driver_id,
            @Part("vehicle_id") RequestBody vehicle_id,
            @Part("action") RequestBody action,
            // @Part MultipartBody.Part[] files
            @Part List<MultipartBody.Part> files);


    @Multipart
    @POST("fatigue.php")
    Call<WashTruckResponse> RefuleTruck(
            @Part("login_token") RequestBody login_token,
            @Part("apikey") RequestBody apikey,
            @Part("truckid") RequestBody truckid,
            @Part("driverid") RequestBody driverid,
            @Part("startdatetime") RequestBody startdatetime,
            @Part("enddatetime") RequestBody enddatetime,
            @Part("types") RequestBody types,
            @Part("quantity") RequestBody quantity,
            @Part("odometer") RequestBody odometer,
            @Part("price") RequestBody price,
            @Part List<MultipartBody.Part> files);


    @FormUrlEncoded
    @POST("jobdetail.php")
    Call<JobDetailsResponse> getjobDetails(@Field("login_token") String login_token, @Field("apikey") String apikey, @Field("jobid") String jobid, @Field("driverid") String driverid);

    @FormUrlEncoded
    @POST("subjobdetails.php")
    Call<JobDetailsResponse> getSubJobDetails(@Field("login_token") String login_token, @Field("apikey") String apikey, @Field("subjobid") String jobid, @Field("driverid") String driverid);

    @FormUrlEncoded
    @POST("subjobdetails.php")
    Call<JobDetailsResponse> getsubjobDetails(@Field("login_token") String login_token, @Field("apikey") String apikey, @Field("subjobid") String subjobid, @Field("driverid") String driverid);


    @FormUrlEncoded
    @POST("getcustomeraddress.php")
    Call<CustomerAddressResponse> getcustomerAddress(@Field("login_token") String login_token, @Field("apikey") String apikey, @Field("customerid") String customerid, @Field("driverid") String driverid);


    @FormUrlEncoded
    @POST("changeaddress.php")
    Call<CommonResponse> changeAddress(@Field("login_token") String login_token, @Field("apikey") String apikey, @Field("jobid") String jobid, @Field("address") String address, @Field("driverid") String driverid);


    //sendSlipPickup
    @Multipart
    @POST("jobpickup.php")
    Call<CommonResponse> sendSlipPickup(
            @Part("apikey") RequestBody apikey,
            @Part("driverid") RequestBody driverid,
            @Part("login_token") RequestBody login_token,
            @Part("jobid") RequestBody jobid,
            @Part("pickupslip_driver_name") RequestBody driverName,
            @Part("pickupslip_consignee_name") RequestBody consignName,
            @Part MultipartBody.Part pickupslip_consignee_sign,
            @Part List<MultipartBody.Part> pickup_item_picture
    );

/*


    @Multipart
    @POST("jobpickup.php")
    Call<CommonResponse> sendSlipPickup(@Part("apikey") RequestBody apikey, @Part("jobid") RequestBody jobid,
                                   @Part("pickupslip_driver_name") RequestBody driverName,@Part("pickupslip_consignee_name") RequestBody consignName,
                                   @Part MultipartBody.Part filecosign,
                                   @Part MultipartBody.Part filedriver);

 */


    //sendSlipPickup
    @Multipart
    @POST("jobdelivery.php")
    Call<CommonResponse> sendSlipDelivery(
            @Part("apikey") RequestBody apikey,
            @Part("driverid") RequestBody driverid,
            @Part("login_token") RequestBody login_token,
            @Part("jobid") RequestBody jobid,
            @Part("deliveryslip_driver_name") RequestBody driverName,
            @Part("deliveryslip_consignee_name") RequestBody consignName,
            @Part MultipartBody.Part deliveryslip_consignee_sign,
            @Part List<MultipartBody.Part> deliveryslip_item_picture
    );


    //senddocketpickupslip
    @Multipart
    @POST("docketpickupslip.php")
    Call<CommonResponse> docketpickupslip(
            @Part("apikey") RequestBody apikey,
            @Part("driverid") RequestBody driverid,
            @Part("login_token") RequestBody login_token,
            @Part("jobid") RequestBody jobid,
            @Part("docketpickupslip_number") RequestBody docketpickupslip_number,
            @Part List<MultipartBody.Part> docketpickupslip_picture);

    //senddocketdeliveryslip
    @Multipart
    @POST("docketdeliveryslip.php")
    Call<CommonResponse> docketdeliveryslip(
            @Part("apikey") RequestBody apikey,
            @Part("driverid") RequestBody driverid,
            @Part("login_token") RequestBody login_token,
            @Part("jobid") RequestBody jobid,
            @Part("docketdeliveryslip_number") RequestBody docketdeliveryslip_number,
            @Part List<MultipartBody.Part> docketdeliveryslip_picture
    );


    @POST("json")
    Call<JsonElement> getDistance(@Query("origins") String origin, @Query("destinations") String destinations);


    ///fleet
    @FormUrlEncoded
    @POST("fleets.php")
    Call<FleetListResponse> getFleetList(@Field("login_token") String login_token, @Field("APIKEY") String apikey,
                                         @Field("driverid") String driver_id, @Field("action") String list);

    @FormUrlEncoded
    @POST("fleets.php")
    Call<AddToFleetRespose> addJobToFleetDeliver(@Field("login_token") String login_token,
                                                 @Field("APIKEY") String apikey,
                                                 @Field("driverid") String driver_id,
                                                 @Field("action") String list,
                                                 @Field("jobid") String jobid,
                                                 @Field("vehicle_id") String vehicle_id,
                                                 @Field("pickupFromType") String pickupFromType,
                                                 @Field("deliverFleetid") int deliverFleetid,
                                                 @Field("subjobid") String subJobId);

    @FormUrlEncoded
    @POST("fleets.php")
    Call<AddToFleetRespose> addJobToFleetPickup(@Field("login_token") String login_token,
                                                @Field("APIKEY") String apikey,
                                                @Field("driverid") String driver_id,
                                                @Field("action") String action,
                                                @Field("jobid") String jobid,
                                                @Field("vehicle_id") String vehicle_id,
                                                @Field("pickupFromType") int pickupFromType,
                                                @Field("pickupFleetid") int pickupFleetid,
                                                @Field("deliverFleetid") int deliverFleetid);

    @Multipart
    @POST("fleets.php")
    Call<AddToFleetRespose> updateFleetPickup(@Part("login_token") RequestBody login_token,
                                              @Part("APIKEY") RequestBody apikey,
                                              @Part("driverid") RequestBody driver_id,
                                              @Part("action") RequestBody action,
                                              @Part("subjobid") RequestBody subjobid,
                                              @Part("vehicleid") RequestBody vehicle_id,
                                              @Part("status") RequestBody status);

    @FormUrlEncoded
    @POST("downloadApplication.php")
    Call<ResponseBody> checkUpdateVersion(@Field("action") String action,
                                          @Field("version") String version);

    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);


}

