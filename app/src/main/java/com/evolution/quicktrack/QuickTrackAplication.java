package com.evolution.quicktrack;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.evolution.quicktrack.activity.DownloadActivity;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.network.ApiCallInterface;
import com.evolution.quicktrack.network.ApiClient;
import com.evolution.quicktrack.response.common.CommonResponse;
import com.evolution.quicktrack.response.login.LoginData;
import com.evolution.quicktrack.util.DateUtility;
import com.evolution.quicktrack.util.PrefManager;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.evolution.quicktrack.BaseActivity.ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE;
import static com.evolution.quicktrack.constants.Constants.OPEN_ACTIVITYNAME;

/**
 * Created by user on 7/7/2018.
 */
@ReportsCrashes(mailTo = "ketan.icreate@gmail.com",
        mode = ReportingInteractionMode.TOAST, resToastText = R.string.crash_toast_text, formKey = "")
public class QuickTrackAplication extends Application implements Application.ActivityLifecycleCallbacks,TextToSpeech.OnInitListener {

    public static OkHttpClient okHttpClient;


    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);
        registerActivityLifecycleCallbacks(this);
        DateUtility.initFormat();
        LogCustom.logd("xxxxxxxx", "Application create");
        initOkHttp();
        tts = new TextToSpeech(this, this);
       checkVersion();

    }

    public void openNumberPad(String number){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+number));
        startActivity(intent);
    }

    public void Call(Activity activity, String phoneNumber) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.CALL_PHONE},
                    123);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + phoneNumber));
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(callIntent);
        }
    }

    private void initOkHttp() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    public OkHttpClient okHttpClient() {
        return okHttpClient;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        LogCustom.logd("xxxxxxxx", "onActivityCreated");

    }

    @Override
    public void onActivityStarted(Activity activity) {
        LogCustom.logd("xxxxxxxx", "onActivityStarted");

    }

    @Override
    public void onActivityResumed(Activity activity) {
        LogCustom.logd("xxxxxxxx", "onActivityResumed");

        OPEN_ACTIVITYNAME = activity.getClass().getSimpleName();
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public static boolean IsNotNull(Object object) {
        return object != null && !object.equals("null") && !object.equals("");
    }

     void checkVersion(){
        try{
            PackageInfo pInfo = null;
            try {
                pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            String version = pInfo.versionName;
            ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);
            apiService.checkUpdateVersion("latest",version).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if(jsonObject.optBoolean("status")){
                           /* apiService.downloadFileWithDynamicUrlSync(Constants.DOWNLOAD_URL+jsonObject.optString("version")+".apk").enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    boolean writtenToDisk = writeResponseBodyToDisk(response.body(),jsonObject.optString("version")+".apk");

                                    Log.d("Download", "file download was a success? " + writtenToDisk);
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.e("Download",call.toString());
                                }
                            });*/

                           Intent  i = new Intent(getApplicationContext(),DownloadActivity.class);
                           i.putExtra("version",jsonObject.optString("version"));
                           startActivity(i);


                        }else{
                            Toast.makeText(getApplicationContext(),"You have already updated version.",Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("Splash",call.toString());
                }
            });
        }catch (Exception e){
            Log.e("SplashScreen",e.toString());
        }
    }


    public void logoutForce(Activity activity){
        PrefManager prefManager = new PrefManager(getApplicationContext());
        LoginData loginData = prefManager.getUserData();
        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);
        Call<CommonResponse> call = apiService.force_logout_api(Constants.API_KEY, loginData.getDriverId(), loginData.getLogin_token(), loginData.getFcm_token());
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if(response.isSuccessful()){
                    Intent i = new Intent(activity, Login_Activity.class);
                    startActivity(i);
                    activity.finish();
                }
            }
            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Log.w("error force logout",call.toString());
            }
        });
    }

    public String getAndroidID(){ return Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID); }

    public PackageInfo getPackageInfo() throws PackageManager.NameNotFoundException { return getPackageManager().getPackageInfo(getPackageName(), 0);}

    public String getUniqueIMEIId(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                return "";
            }
            String imei = telephonyManager.getDeviceId();
            Log.e("imei", "=" + imei);
            if (imei != null && !imei.isEmpty()) {
                return imei;
            } else {
                return android.os.Build.SERIAL;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private TextToSpeech tts;
    private String text = "";
    public void convertTextToSpeech(String text){
        this.text = text;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH,null,null);
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                convertTextToSpeech(text);
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    public void checkOverlayDraw(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(activity)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                activity. startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }
        }
    }
}
