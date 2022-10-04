package com.evolution.quicktrack.questionslist;

public class QuestionsListRequestEntity {


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

    public String getTruckid() {
        return truckid;
    }

    public void setTruckid(String truckid) {
        this.truckid = truckid;
    }

    public String getDriverid() {
        return driverid;
    }

    public void setDriverid(String driverid) {
        this.driverid = driverid;
    }

    /* @Field("login_token") String login_token, @Field("apikey") String apikey, @Field("truckid") String truckid, @Field("driverid") String driverid*/
            private String login_token;
            private String apikey;
            private String truckid;
            private String driverid;

}
