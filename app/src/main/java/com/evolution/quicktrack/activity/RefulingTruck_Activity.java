package com.evolution.quicktrack.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.evolution.quicktrack.BaseActivity;
import com.evolution.quicktrack.MainActivity;
import com.evolution.quicktrack.QuickTrackAplication;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.util.Utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 4/2/2018.
 */

public class RefulingTruck_Activity extends BaseActivity{


    public static String TAG = RefulingTruck_Activity.class.getSimpleName();

    @BindView(R.id.btn_done)
    Button btnDone;
    @BindView(R.id.btn_cancel)    Button btnCancel;
    @BindView(R.id.img_home)
    ImageView imgHome;
    @BindView(R.id.btn_logout)    ImageView btnLogout;
    @BindView(R.id.txt_hours)
    TextView txtHours;
    @BindView(R.id.txt_minutes)    TextView txtMinutes;
    @BindView(R.id.txt_second)    TextView txtSecond;
    Handler handler;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    int Seconds, Minutes, MilliSeconds,Hours ;
    String startDate,endDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        handler = new Handler() ;
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);
        startDate = giveDate();
        speakOut();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_refuling_truck;
    }

    @OnClick({R.id.btn_done, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_done:
                openDilog();
                break;
            case R.id.btn_cancel:
                exitDilog();
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
    public  void exitDilog(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(RefulingTruck_Activity.this);
        alertDialog.setMessage(getString(R.string.wanttogohome));
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onBackPressed();

            }
        });
        alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
    public  void openDilog(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(RefulingTruck_Activity.this);
        alertDialog.setMessage("Total Refueling Time is :"
                +txtHours.getText().toString()+":"+txtMinutes.getText().toString()+":"+txtSecond.getText().toString());
        alertDialog.setCancelable(false);
        endDate = giveDate();
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                stoptimer();
                try {
                    if(Utils.validInternet(RefulingTruck_Activity.this))
                    {
                        Intent intent = new Intent(RefulingTruck_Activity.this,FuelSlip_Activity.class);
                        intent.putExtra("startDate",startDate);
                        intent.putExtra("endDate",endDate);
                        startActivity(intent);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
               // startTimer();

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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            Toast.makeText(RefulingTruck_Activity.this,getString(R.string.not_allow_back),Toast.LENGTH_LONG).show();


            return false;
        }

        return super.onKeyDown(keyCode, event);
    }
    private void speakOut() {
        String text = "your total Refueling time is  "+txtHours.getText().toString()+"hours"+txtMinutes.getText().toString()+"minutes"+txtSecond.getText().toString()+"seconds";
        ((QuickTrackAplication)getApplicationContext()).convertTextToSpeech(text);
    }


    public  void homeDilog(){

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(RefulingTruck_Activity.this);
        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.wanttogohome));
        alertDialog.setCancelable(false);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(RefulingTruck_Activity.this, MainActivity.class);
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

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(RefulingTruck_Activity.this);

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure want to logout ?");
        alertDialog.setCancelable(true);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
/*
                trackDriver.call_trackDriverAPi(Constants.BACK_LOGOUT);


                PrefManager prefManager=new PrefManager(getApplicationContext());
                prefManager.logout();

                Intent   intent =new Intent(RefulingTruck_Activity.this,Login_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
*/showOdometerDialog(RefulingTruck_Activity.this);

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
