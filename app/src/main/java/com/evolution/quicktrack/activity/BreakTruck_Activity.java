package com.evolution.quicktrack.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.evolution.quicktrack.util.CustomCountDownTimer;
import com.evolution.quicktrack.util.PrefManager;
import com.evolution.quicktrack.util.Utils;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user on 4/2/2018.
 */

public class BreakTruck_Activity extends BaseActivity implements CustomCountDownTimer.TimerTickListener {

    public static String TAG = BreakTruck_Activity.class.getSimpleName();

    @BindView(R.id.btn_cancel)    Button btnCancel;
    @BindView(R.id.img_home)    ImageView imgHome;
    @BindView(R.id.btn_logout)    ImageView btnLogout;
    @BindView(R.id.txt_hours)    TextView txtHours;
    @BindView(R.id.txt_minutes)    TextView txtMinutes;
    @BindView(R.id.txt_second)    TextView txtSecond;

    DecimalFormat dFormat=new DecimalFormat("00");

    String startDate,endDate;
    @BindView(R.id.btn_add)    Button btn_add;


    private KProgressHUD pd;

    CustomCountDownTimer.TimerTickListener timerTickListener;
    CustomCountDownTimer customCountDownTimer;

    long milliSeconds =1000*15*60;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        startDate=giveDate();
        timerTickListener  = BreakTruck_Activity.this;
        customCountDownTimer =new CustomCountDownTimer(milliSeconds,1000,timerTickListener);
        customCountDownTimer.start();



    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_break_truck;
    }

    @OnClick({R.id.img_home, R.id.btn_logout,R.id.btn_add,R.id.btn_minus,R.id.btn_done,R.id.btn_cancel})
    public void onViewClicked2(View view) {
        switch (view.getId()) {
            case R.id.img_home:
                onBackPressed();
                break;
            case R.id.btn_logout:
                logOutDialog();
                break;
            case R.id.btn_add:
                milliSeconds = milliSeconds +1000*5*60;
                customCountDownTimer.extendTime(1000*5*60);
                break;
            case R.id.btn_minus:
                if(milliSeconds >1000*5*60){
                    milliSeconds = milliSeconds -1000*5*60;
                    customCountDownTimer.minusTime(1000*5*60);
                }else {
                    milliSeconds = milliSeconds -1000*60;
                    customCountDownTimer.minusTime(1000*60);
                }
                break;

            case R.id.btn_cancel:

                    homeDilog();
                break;
            case R.id.btn_done:

                openDialog(false);

                break;
        }
    }



    public  void openDialog(final boolean isAddSecond){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(BreakTruck_Activity.this);
        alertDialog.setMessage(getString(R.string.txt_your_break_is_over));
        endDate=giveDate();
        alertDialog.setCancelable(false);
        speakOut();
        if(isAddSecond) {
            customCountDownTimer.cancel();
        }
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                try {
                    if(Utils.validInternet(BreakTruck_Activity.this))
                    {

                        call_BreckTruckApi();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(isAddSecond){
                    milliSeconds = milliSeconds +1000*15*60;
                    customCountDownTimer =new CustomCountDownTimer(milliSeconds,1000,timerTickListener);
                    customCountDownTimer.start();
                }
            }
        });
        alertDialog.show();
    }


    public String giveDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(cal.getTime());
    }





    @Override
    public void onTick(long millisLeft) {

        txtHours.setText(dFormat.format( TimeUnit.MILLISECONDS.toHours(millisLeft)));
        txtMinutes.setText(dFormat.format(TimeUnit.MILLISECONDS.toMinutes(millisLeft) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisLeft))));
        txtSecond.setText(dFormat.format(TimeUnit.MILLISECONDS.toSeconds(millisLeft) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisLeft))));

    }




    @Override
    public void onFinish() {
        openDialog(true);
    }

    @Override
    public void onCancel() {

    }
    private void call_BreckTruckApi() throws Exception {
        PrefManager prefManager=new PrefManager(getApplicationContext());

        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);
        pd = LogCustom.loderProgressDialog2(this);
        pd.show();

        LogCustom.logd("xxxx","start time:"+startDate+":end:"+endDate);
        LogCustom.logd("xxxx","start time:"+prefManager.getTruckId()+":getId:"+prefManager.getId());

        Call<WashTruckResponse> call = apiService.washTruckAndRefuleTruck(prefManager.getLogin_Token(),Constants.API_KEY,prefManager.getTruckId(),prefManager.getId(),startDate,endDate,"Break" );
        call.enqueue(new Callback<WashTruckResponse>() {
            @Override
            public void onResponse(Call<WashTruckResponse> call, Response<WashTruckResponse> response) {

                Log.d(TAG, " : check status : " + response.body());
                if (pd != null && pd.isShowing())
                    pd.dismiss();
                if (response.body().getStatus().equals(Constants.API_STATUS)) {
                    Log.d(TAG, " : check status : " + response.body().toString());

                    trackDriver.call_trackDriverAPi(Constants.BACK_BRECK);

                    LogCustom.Toast(BreakTruck_Activity.this, response.body().getMessage());
                    onBackPressed();
                } else {
                    if(response.body().getMessage().equals("invalidToken"))
                    {
                        prefManager.logout();

                        Intent   intent =new Intent(BreakTruck_Activity.this,Login_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    LogCustom.Toast(BreakTruck_Activity.this, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<WashTruckResponse> call, Throwable t) {
                LogCustom.loge(TAG, t.toString());
                if (pd != null && pd.isShowing())
                    pd.dismiss();
            }
        });
    }
    private void speakOut() {
        ((QuickTrackAplication)getApplicationContext()).convertTextToSpeech(getString(R.string.txt_your_break_is_over));

    }
    public  void homeDilog(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(BreakTruck_Activity.this);
        alertDialog.setMessage(getString(R.string.wanttogohome));
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
            dialog.dismiss();
            Intent intent = new Intent(BreakTruck_Activity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        alertDialog.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }
    public  void logOutDialog(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(BreakTruck_Activity.this);
        alertDialog.setMessage("Are you sure want to logout ?");
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
            dialog.dismiss();
            showOdometerDialog(BreakTruck_Activity.this);
        });
        alertDialog.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            showToast(getString(R.string.not_allow_back));
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
