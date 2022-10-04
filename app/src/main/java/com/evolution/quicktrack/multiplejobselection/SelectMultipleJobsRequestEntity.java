package com.evolution.quicktrack.multiplejobselection;

public class SelectMultipleJobsRequestEntity {

    /*@Field("login_token") String login_token,
    @Field("apikey") String apikey,
     @Field("vehicleid") String vehicleid,
    @Field("status") String status,
     @Field("driverid") String driverid,
     @Field("dateFlag") String dateFlag*/

    private String login_token;
    private String apikey;
    private String vehicleid;

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

    public String getVehicleid() {
        return vehicleid;
    }

    public void setVehicleid(String vehicleid) {
        this.vehicleid = vehicleid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDriverid() {
        return driverid;
    }

    public void setDriverid(String driverid) {
        this.driverid = driverid;
    }

    public String getDateFlag() {
        return dateFlag;
    }

    public void setDateFlag(String dateFlag) {
        this.dateFlag = dateFlag;
    }

    private String status;
    private String driverid;
    private String dateFlag;


}
