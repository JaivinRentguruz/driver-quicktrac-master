package com.evolution.quicktrack.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.evolution.quicktrack.response.login.LoginData;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PrefManager {
    private static final String KEY_LOGGED_STATUS = "logged";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_F_NAME = "firstname";
    private static final String KEY_L_NAME = "lastname";
    private static final String KEY_LICENCENUMBER = "licencenumber";
    private static final String KEY_EMAIL= "email";
    private static final String KEY_TRUCKID= "truckId";
    private static final String KEY_TRUCKNAME= "truckName";
    private static final String KEY_IMAGE= "userimage";
    private static final String KEY_JOBID= "jobid";
    private static final String KEY_JOBSTATUS= "jobstatus";
    private static final String KEY_DATE= "loginDate";
    private static final String KEY_TOKEN= "logintoken";
    private static final String PREF_NAME = "quicktrack";
    private static final String KEY_ODOMETER = "odometer";
    private static final String IS_LOGIN_PROCESS_COMPLETED="is_login_process_completed";
    private static final String FCM_TOKEN="fcm_token";
    private Editor editor;
    private SharedPreferences pref;

    public PrefManager(Context context) {
        this.pref = context.getSharedPreferences(PREF_NAME, 0);
    }





    public void setUserDetails(LoginData loginData) {
        this.editor = this.pref.edit();
        this.editor.putString(KEY_USER_ID, loginData.getDriverId());
        this.editor.putString(KEY_F_NAME, loginData.getDFirstName());
        this.editor.putString(KEY_L_NAME, loginData.getDLastName());
        this.editor.putString(KEY_MOBILE, loginData.getDContactNo());
        this.editor.putString(KEY_LICENCENUMBER, loginData.getDLicenceNumber());
        this.editor.putString(KEY_EMAIL, loginData.getDEmail());
        this.editor.putString(KEY_IMAGE, loginData.getImage());
        this.editor.putString(KEY_TOKEN, loginData.getLogin_token());
        this.editor.putString(FCM_TOKEN,loginData.getFcm_token());

        this.editor.commit();

        setDate();
    }


    public void setFCM_TOKEN(String token){
        this.editor = this.pref.edit();
        this.editor.putString(FCM_TOKEN,token);
        this.editor.commit();
    }

    public String getFMC_TOKEN(){
        return pref.getString(FCM_TOKEN,"");
    }




    public LoginData getUserData() {

        LoginData  loginData=new LoginData();
        loginData.setDFirstName(pref.getString(KEY_F_NAME, ""));
        loginData.setDLastName(pref.getString(KEY_L_NAME, ""));
        loginData.setDContactNo(pref.getString(KEY_MOBILE, ""));
        loginData.setDLicenceNumber(pref.getString(KEY_LICENCENUMBER, ""));
        loginData.setDEmail(pref.getString(KEY_EMAIL, ""));
        loginData.setLogin_token(pref.getString(KEY_TOKEN,""));
        loginData.setDriverId(pref.getString(KEY_USER_ID,""));
        loginData.setFcm_token(pref.getString(FCM_TOKEN,""));
        return loginData;


    }



    public void setDate(){

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        this.editor = this.pref.edit();
        this.editor.putString(KEY_DATE,sdf.format(cal.getTime()));

        this.editor.commit();
    }


    public void setTryckData(String truckId,String truckName) {
        this.editor = this.pref.edit();
        this.editor.putString(KEY_TRUCKID,truckId);
        this.editor.putString(KEY_TRUCKNAME, truckName);


        this.editor.commit();
    }

    public void setKEYJobId(String jobId)
    {
        this.editor = this.pref.edit();
        this.editor.putString(KEY_JOBID,jobId);
        this.editor.commit();

    }

    public void setOdometer(String odometer)
    {
        this.editor = this.pref.edit();
        this.editor.putString(KEY_ODOMETER,odometer);
        this.editor.commit();
    }
    public String getOdometer()
    {
        return this.pref.getString(KEY_ODOMETER, "");
    }


    public void setLoginProcess(Boolean odometer)
    {
        this.editor = this.pref.edit();
        this.editor.putBoolean(IS_LOGIN_PROCESS_COMPLETED,odometer);
        this.editor.commit();
    }
    public boolean getLoginProcess()
    {
        return this.pref.getBoolean(IS_LOGIN_PROCESS_COMPLETED, false);
    }


    public void setKEYJobstatus(String jobstatus)
    {
        this.editor = this.pref.edit();
        this.editor.putString(KEY_JOBSTATUS,jobstatus);
        this.editor.commit();
    }

    public String getKeyJobId()
    {
        return this.pref.getString(KEY_JOBID, "");
    }

    public String getKeyJobstatus()
    {
        return this.pref.getString(KEY_JOBSTATUS, "");
    }



    public String getDate() {

        return this.pref.getString(KEY_DATE, "");


    }


    public String getId() {

            return this.pref.getString(KEY_USER_ID, "");


    }
    public String getImage() {

        return this.pref.getString(KEY_IMAGE, "");


    }


    public String getTruckId() {

        return this.pref.getString(KEY_TRUCKID, "");


    }


    public String getLogin_Token() {
        return this.pref.getString(KEY_TOKEN, "");
    }

    public String getfullName() {

        return this.pref.getString(KEY_F_NAME, "")+" "+this.pref.getString(KEY_L_NAME, "");


    }




    public void logout() {
        this.editor = this.pref.edit();
        this.editor.clear();
        this.editor.commit();
    }

    public void setLogin(boolean status) {
        this.editor = this.pref.edit();
        this.editor.putBoolean(KEY_LOGGED_STATUS, status);
        this.editor.commit();
    }


    public String getValueByKey(String key) {
        return this.pref.getString(key, "");
    }

    public boolean isLogged() {
        return this.pref.getBoolean(KEY_LOGGED_STATUS, false);
    }
}
