package com.evolution.quicktrack.questionslist;

public class QuestionListSubmitRequestEntity {

    /*@Field("login_token") String login_token, @Field("data") String user_id, @Field("driverid") String driverid*/

    public String getLogin_token() {
        return login_token;
    }

    public void setLogin_token(String login_token) {
        this.login_token = login_token;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDriverid() {
        return driverid;
    }

    public void setDriverid(String driverid) {
        this.driverid = driverid;
    }

    private String login_token;
    private String data;
    private String driverid;
}
