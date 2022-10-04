package com.evolution.quicktrack.pickupsignature;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MultipartBody;

public class PickupSignatureRequestEntity {
    /*@Field("login_token") String login_token, @Field("apikey") String apikey, @Field("jobid") String jobid, @Field("driverid") String driverid*/

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public String getDriverid() {
        return driverid;
    }

    public void setDriverid(String driverid) {
        this.driverid = driverid;
    }
/*
    @Part("apikey") RequestBody apikey,
    @Part("driverid") RequestBody driverid,
    @Part("login_token") RequestBody login_token,
    @Part("jobid") RequestBody jobid,
    @Part("pickupslip_driver_name") RequestBody driverName,
    @Part("pickupslip_consignee_name") RequestBody consignName,
    @Part MultipartBody.Part pickupslip_consignee_sign,
    @Part List<MultipartBody.Part> pickup_item_picture*/

    private String loginToken;
    private String apikey;
    private String jobid;
    private String driverid;

    public String getPickupslip_driver_name() {
        return pickupslip_driver_name;
    }

    public void setPickupslip_driver_name(String pickupslip_driver_name) {
        this.pickupslip_driver_name = pickupslip_driver_name;
    }

    public String getPickupslip_consignee_name() {
        return pickupslip_consignee_name;
    }

    public void setPickupslip_consignee_name(String pickupslip_consignee_name) {
        this.pickupslip_consignee_name = pickupslip_consignee_name;
    }

    public MultipartBody.Part getPickupslip_consignee_sign() {
        return pickupslip_consignee_sign;
    }

    public void setPickupslip_consignee_sign(MultipartBody.Part pickupslip_consignee_sign) {
        this.pickupslip_consignee_sign = pickupslip_consignee_sign;
    }

    public ArrayList<MultipartBody.Part> getPickup_item_picture() {
        return pickup_item_picture;
    }

    public void setPickup_item_picture(ArrayList<MultipartBody.Part> pickup_item_picture) {
        this.pickup_item_picture = pickup_item_picture;
    }

    private String pickupslip_driver_name;
    private String pickupslip_consignee_name;
    private MultipartBody.Part pickupslip_consignee_sign;
    private ArrayList<MultipartBody.Part> pickup_item_picture;

}
