package com.evolution.quicktrack.jobdetailspickup;

public class JobDetailsPickupRequestEntity {

    /*@Field("login_token") String login_token, @Field("apikey") String apikey, @Field("jobid") String jobid, @Field("driverid") String driverid*/

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

    private String login_token;
    private String apikey;
    private String jobid;
    private String driverid;

}
