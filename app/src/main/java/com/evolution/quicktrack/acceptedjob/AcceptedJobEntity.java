package com.evolution.quicktrack.acceptedjob;

import com.evolution.quicktrack.model.JobModel;

public class AcceptedJobEntity {

    /*@Field("login_token") String login_token, @Field("apikey") String apikey, @Field("vehicleid") String vehicleid,
    @Field("status") String status, @Field("driverid") String driverid,@Field("dateFlag")String dateFlag*/

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

    private String login_token;
    private String apikey;
    private String vehicleid;
    private String status;
    private String driverid;
    private String dateFlag;
    private String jobid;
    private JobModel jobModel;

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubjobid() {
        return subjobid;
    }

    public void setSubjobid(String subjobid) {
        this.subjobid = subjobid;
    }

    private String latitude;
    private String longitude;
    private String comment;
    private String type;
    private String subjobid;

    public JobModel getJobModel() {
        return jobModel;
    }

    public void setJobModel(JobModel jobModel) {
        this.jobModel = jobModel;
    }
}
