package com.evolution.quicktrack.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.evolution.quicktrack.BaseActivity;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.odometerreading.OdoMeterCheckRequestEntity;
import com.evolution.quicktrack.odometerreading.OdoMeterContractor;
import com.evolution.quicktrack.odometerreading.OdoMeterPresenter;
import com.evolution.quicktrack.util.Utility;
import com.evolution.quicktrack.util.Utils;
import com.kaopiz.kprogresshud.KProgressHUD;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 4/2/2018.
 */

public class OdometerReading_Activity extends BaseActivity implements OdoMeterContractor.View {

    private OdoMeterContractor.Presenter presenter;

    @BindView(R.id.edit_odometerreading)
    EditText editOdometerreading;
    @BindView(R.id.txt_trackName)
    TextView txt_trackName;
    @BindView(R.id.img_back_to)
    ImageView img_back;
    String back = "";
    public static String TAG = OdometerReading_Activity.class.getSimpleName();
    String truckId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new OdoMeterPresenter(this);
        ButterKnife.bind(this);
        getIntentData();
    }
    private void truckAssignApi(OdoMeterCheckRequestEntity odoMeterCheckRequestEntity) throws Exception {
        try {
            showProgressDialogue();
            presenter.addDriverToVehicle(odoMeterCheckRequestEntity);
        } catch (Exception e) {
            e.getMessage();
        }
    }
    public void getIntentData() {
        if (getIntent().getStringExtra(Constants.EXTA_TRUCKNAME) != null) {
            txt_trackName.setText(getIntent().getStringExtra(Constants.EXTA_TRUCKNAME));
            truckId = getIntent().getStringExtra(Constants.EXTA_TRUCKID);
        }
    }
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_odometer;
    }
    @OnClick({R.id.btn_verify, R.id.img_back_to})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_verify:
                if (validData()) {
                    try {
                        if (Utils.validInternet(getApplicationContext()))
                            call_checkOdometerAPi();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.img_back_to:
                back = "true";
                onBackPressed();
                break;
        }

    }
    public boolean validData() {
        if (Utility.isValidString(editOdometerreading.getText().toString().trim())) {
            return true;
        } else {
            editOdometerreading.requestFocus();
            showToast("Please enter Readings");
            return false;
        }
    }
    private void call_checkOdometerAPi() {
        showProgressDialogue();
        OdoMeterCheckRequestEntity odoMeterCheckRequestEntity=new OdoMeterCheckRequestEntity();
        odoMeterCheckRequestEntity.setApiKey(Constants.API_KEY);
        odoMeterCheckRequestEntity.setTruckId(truckId);
        odoMeterCheckRequestEntity.setDriverId(prefManager.getId());
        odoMeterCheckRequestEntity.setLoginToken(prefManager.getLogin_Token());
        odoMeterCheckRequestEntity.setOdoMeter(editOdometerreading.getText().toString().trim());
        presenter.checkOdoMeter(odoMeterCheckRequestEntity);
    }
    public void openDilog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(OdometerReading_Activity.this);
        alertDialog.setTitle("Error");
        alertDialog.setMessage("Odometer Reading Does Not Match");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                editOdometerreading.setText("");

            }
        });
        alertDialog.show();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && back.isEmpty()) {
            showToast(getString(R.string.not_allow_back));
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onCheckOdoMeterSuccess(OdoMeterCheckRequestEntity odoMeterCheckRequestEntity,String message) {
        hideProgressDialogue();
        try {
            showToast(message);
            prefManager.setOdometer(editOdometerreading.getText().toString());
            truckAssignApi(odoMeterCheckRequestEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onCheckOdoMeterError(String message) {
        hideProgressDialogue();
        if (message.equals("invalidToken")) {
            prefManager.logout();
            redirectToLogin();
        } else {
            openDilog();
        }
    }
    @Override
    public void addDriverToVehicleSuccess(String message) {
        hideProgressDialogue();
        OdometerReading_Activity.this.trackDriver.call_trackDriverAPi(Constants.BACK_SELECTTRUCK);
        Intent sender = new Intent(OdometerReading_Activity.this, QuestionListActivity.class);
        sender.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        sender.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sender.putExtra(Constants.EXTA_TRUCKID, truckId);
        startActivity(sender);
    }
    @Override
    public void addDriverToVehicleError(String message) {
        hideProgressDialogue();
        if (message.equals("invalidToken")) {
            prefManager.logout();
            redirectToLogin();
        }else{
            showToast(message);
        }
    }
}