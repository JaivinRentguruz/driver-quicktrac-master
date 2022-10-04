package com.evolution.quicktrack.allocationjob;

public class AllocationJobUpdateStatusRequestEntity {


    /*@Field("login_token") String login_token, @Field("APIKEY") String apikey, @Field("driverid") String driverid,
    @Field("subjobId") String jobid, @Field("status") String status,
    @Field("latitude") String latitude, @Field("longitude")
    String longitude, @Field("vehicle_id") String vehicle_id, @Field("action") String action,@Field("comment") String pickupFutileComment*/

    public String getLogin_token() {
        return login_token;
    }

    public void setLogin_token(String login_token) {
        this.login_token = login_token;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getDriverid() {
        return driverid;
    }

    public void setDriverid(String driverid) {
        this.driverid = driverid;
    }

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPickupFutileComment() {
        return pickupFutileComment;
    }

    public void setPickupFutileComment(String pickupFutileComment) {
        this.pickupFutileComment = pickupFutileComment;
    }

    private String login_token;
    private String apikey;
    private String driverid;
    private String jobid;
    private String status;
    private String latitude;
    private String longitude;
    private String vehicle_id;
    private String action;
    private String pickupFutileComment;



}
