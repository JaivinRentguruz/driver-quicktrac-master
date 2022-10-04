package com.evolution.quicktrack;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.evolution.quicktrack.util.PrefManager;

import java.text.ParseException;

import butterknife.ButterKnife;

import static com.evolution.quicktrack.util.DateUtility.isValidDateDifference;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG = "SplashScreen";
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        moveToLogin();
    }

    private void moveToLogin(){
        prefManager=new PrefManager(getApplicationContext());
        // Splash screen timer
        int SPLASH_TIME_OUT = 1000;
        new Handler().postDelayed(() -> {
            if( !prefManager.getLoginProcess() && prefManager.isLogged()){
               ((QuickTrackAplication)getApplicationContext()).logoutForce(this);
                return;
            }
            try {
                if(!prefManager.getDate().equals("") && isValidDateDifference(prefManager.getDate())){
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                    finish();

                }else {
                    Intent i = new Intent(SplashScreen.this, Login_Activity.class);
                    startActivity(i);
                    finish();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }, SPLASH_TIME_OUT);
    }
}