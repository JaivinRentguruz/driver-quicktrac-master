package com.evolution.quicktrack;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;

import com.evolution.quicktrack.activity.BreackDownTruck_Activity;
import com.evolution.quicktrack.activity.BreakTruck_Activity;
import com.evolution.quicktrack.activity.MyProfile_Activity;
import com.evolution.quicktrack.activity.RefulingTruck_Activity;
import com.evolution.quicktrack.activity.TruckDetails_Activity;
import com.evolution.quicktrack.activity.UserListActivity;
import com.evolution.quicktrack.activity.WashTruck_Activity;
import com.evolution.quicktrack.activity.jobmodule.AcceptedJobActivity;
import com.evolution.quicktrack.activity.jobmodule.AllocatedJobActivity;
import com.evolution.quicktrack.util.PrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @BindView(R.id.img_home)
    ImageView imgHome;
    @BindView(R.id.btn_logout)
    ImageView btnLogout;
    @BindView(R.id.img_manu1)
    ImageView imgManu1;
    @BindView(R.id.img_manu2)
    ImageView imgManu2;
    @BindView(R.id.img_man3)
    ImageView imgMan3;
    @BindView(R.id.img_manu4)
    ImageView imgManu4;
    @BindView(R.id.img_manu5)
    ImageView imgManu5;
    @BindView(R.id.img_man6)
    ImageView imgMan6;
    @BindView(R.id.img_man7)
    ImageView imgMan7;
    @BindView(R.id.img_manu9)
    ImageView imgManu9;
    @BindView(R.id.img_manu10)
    ImageView imgManu10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new PrefManager(this).setLoginProcess(true);
        ButterKnife.bind(this);
        imgHome.setVisibility(View.INVISIBLE);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.btn_logout, R.id.img_manu1, R.id.img_manu2, R.id.img_man3, R.id.img_manu4, R.id.img_manu5, R.id.img_man6, R.id.img_man7, R.id.img_manu9, R.id.img_manu10})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_logout:
                logOutDialog();
                break;
            case R.id.img_manu1:
                intent = new Intent(MainActivity.this, AllocatedJobActivity.class);
                startActivity(intent);
                break;
            case R.id.img_manu2:
                intent = new Intent(MainActivity.this, AcceptedJobActivity.class);
                startActivity(intent);
                break;
            case R.id.img_man3:
                intent = new Intent(MainActivity.this, BreakTruck_Activity.class);
                startActivity(intent);
                break;
            case R.id.img_manu4:
                intent = new Intent(MainActivity.this, RefulingTruck_Activity.class);
                startActivity(intent);
                break;
            case R.id.img_manu5:
                intent = new Intent(MainActivity.this, WashTruck_Activity.class);
                startActivity(intent);
                break;
            case R.id.img_man6:
                intent = new Intent(MainActivity.this, BreackDownTruck_Activity.class);
                startActivity(intent);
                break;
            case R.id.img_man7:
                intent = new Intent(MainActivity.this, MyProfile_Activity.class);
                startActivity(intent);
                break;
            case R.id.img_manu9:
                intent = new Intent(MainActivity.this, TruckDetails_Activity.class);
                startActivity(intent);
                break;
            case R.id.img_manu10:
                intent = new Intent(MainActivity.this, UserListActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        homeDilog();
    }

    public void homeDilog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setMessage(getString(R.string.areyousureexit));
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
            dialog.dismiss();
            System.exit(0);
        });
        alertDialog.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());
        alertDialog.show();

    }
}
