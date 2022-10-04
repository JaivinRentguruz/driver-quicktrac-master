package com.evolution.quicktrack.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.evolution.quicktrack.BaseActivity;
import com.evolution.quicktrack.Login_Activity;
import com.evolution.quicktrack.MainActivity;
import com.evolution.quicktrack.QuickTrackAplication;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.network.ApiCallInterface;
import com.evolution.quicktrack.network.ApiClient;
import com.evolution.quicktrack.response.washtruckAndRefule.WashTruckResponse;
import com.evolution.quicktrack.util.PrefManager;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 4/2/2018.
 */

public class WashTruck_Activity extends BaseActivity  {


    public static String TAG = WashTruck_Activity.class.getSimpleName();
    @BindView(R.id.btn_done)    Button btnDone;
    @BindView(R.id.btn_cancel)    Button btnCancel;



    @BindView(R.id.img_home)    ImageView imgHome;
    @BindView(R.id.btn_logout)    ImageView btnLogout;
    @BindView(R.id.txt_hours)    TextView txtHours;
    @BindView(R.id.txt_minutes)    TextView txtMinutes;
    @BindView(R.id.txt_second)    TextView txtSecond;
    int totalsecond=0;

    Handler handler;

    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;

    int Seconds, Minutes, MilliSeconds,Hours ;

    String startDate,endDate;

    private KProgressHUD pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);


        btnLogout.setVisibility(View.INVISIBLE);

        handler = new Handler() ;
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);
        startDate = giveDate();


    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_wash_truck;
    }



    @OnClick({R.id.btn_done, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_done:
                openDilog();
                break;
            case R.id.btn_cancel:
                    homeDilog();
                break;
        }
    }

    @OnClick({R.id.img_home, R.id.btn_logout})
    public void onViewClicked2(View view) {
        switch (view.getId()) {
            case R.id.img_home:
                onBackPressed();
                break;
            case R.id.btn_logout:
                logOutDialog();
                break;
        }
    }
    public Runnable runnable = new Runnable() {
        public void run() {
            DecimalFormat dFormat=new DecimalFormat("00");
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;
            Hours=Minutes / (60 * 60 * 1000) % 24;
            MilliSeconds = (int) (UpdateTime % 1000);

            txtHours.setText(""+dFormat.format(Hours));
            txtMinutes.setText(""+dFormat.format(Minutes));
            txtSecond.setText(""+String.format("%02d", Seconds));
            handler.postDelayed(this, 0);
        }

    };



    public void stoptimer(){

        TimeBuff += MillisecondTime;
        handler.removeCallbacks(runnable);


    }
    public void startTimer(){


        StartTime = SystemClock.uptimeMillis();

        handler.postDelayed(runnable, 0);
    }





    public  void openDilog(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(WashTruck_Activity.this);
        alertDialog.setMessage("Total Wash Time is :"
                +txtHours.getText().toString()+":"+txtMinutes.getText().toString()+":"+txtSecond.getText().toString());

        alertDialog.setCancelable(false);
        speakOut();
        endDate = giveDate();
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                stoptimer();
                try {
                    call_washTruckApi();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
              //  startTimer();
            }
        });

        // Showing Alert Message
        alertDialog.show();


    }


    public String giveDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }


    private void call_washTruckApi() throws Exception {

        PrefManager prefManager=new PrefManager(getApplicationContext());

        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);
        pd = LogCustom.loderProgressDialog2(this);
        pd.show();

        LogCustom.logd("xxxx","start time:"+startDate+":end:"+endDate);
        LogCustom.logd("xxxx","start time:"+prefManager.getTruckId()+":getId:"+prefManager.getId());

        Call<WashTruckResponse> call = apiService.washTruckAndRefuleTruck(prefManager.getLogin_Token(),Constants.API_KEY,prefManager.getTruckId(),prefManager.getId(),startDate,endDate,"Wash" );
        call.enqueue(new Callback<WashTruckResponse>() {
            @Override
            public void onResponse(Call<WashTruckResponse> call, Response<WashTruckResponse> response) {


                if (pd != null && pd.isShowing())
                    pd.dismiss();
                if (response.body().getStatus().equals(Constants.API_STATUS)) {
                    Log.d(TAG, " : check status : " + response.body().toString());
                    trackDriver.call_trackDriverAPi(Constants.BACK_WASH);

                    LogCustom.Toast(WashTruck_Activity.this, response.body().getMessage());
                        onBackPressed();
                } else {

                    if(response.body().getMessage().equals("invalidToken"))
                    {
                        prefManager.logout();

                        Intent   intent =new Intent(WashTruck_Activity.this,Login_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    LogCustom.Toast(WashTruck_Activity.this, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<WashTruckResponse> call, Throwable t) {
                LogCustom.logd(TAG, t.toString());
                if (pd != null && pd.isShowing())
                    pd.dismiss();
            }
        });
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            Toast.makeText(WashTruck_Activity.this,getString(R.string.not_allow_back),Toast.LENGTH_LONG).show();


            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void speakOut() {
        String text = "your total wash time is "+txtHours.getText().toString()+"hours"+txtMinutes.getText().toString()+"minutes"+txtSecond.getText().toString()+"seconds";
        ((QuickTrackAplication)getApplicationContext()).convertTextToSpeech(text);
    }

    public  void homeDilog(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(WashTruck_Activity.this);
        alertDialog.setMessage(getString(R.string.wanttogohome));
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(WashTruck_Activity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });

        alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // Showing Alert Message
        alertDialog.show();

    }


    public  void logOutDialog(){

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(WashTruck_Activity.this);

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure want to logout ?");
        alertDialog.setCancelable(true);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();/*
                trackDriver.call_trackDriverAPi(Constants.BACK_LOGOUT);


                PrefManager prefManager=new PrefManager(getApplicationContext());
                prefManager.logout();

                Intent   intent =new Intent(WashTruck_Activity.this,Login_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
*/              showOdometerDialog(WashTruck_Activity.this);
            }
        });



        alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        // Showing Alert Message
        alertDialog.show();


    }

}
