package com.evolution.quicktrack;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.evolution.quicktrack.activity.TrackListActivity;
import com.evolution.quicktrack.base.BaseContractor;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.location.GetLocation;
import com.evolution.quicktrack.login.LoginContractor;
import com.evolution.quicktrack.login.LoginPresenter;
import com.evolution.quicktrack.login.LoginRequestEntity;
import com.evolution.quicktrack.response.login.LoginData;
import com.evolution.quicktrack.response.login.LoginResponse;
import com.evolution.quicktrack.util.PrefManager;
import com.evolution.quicktrack.util.Utility;
import com.evolution.quicktrack.util.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by user on 4/2/2018.
 */

public class Login_Activity extends BaseActivity implements EasyPermissions.PermissionCallbacks, BaseContractor, LoginContractor.View {

    @BindView(R.id.edit_licencenNumber)
    EditText editLicencenNumber;
    @BindView(R.id.edit_paswd)
    EditText editPaswd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.txt_deviceId)
    TextView txt_deviceId;
    @BindView(R.id.txt_imei)
    TextView txt_imei;
    private String imei = "00000";
    private GetLocation trackDriver;

    private LoginContractor.Presenter loginPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginPresenter = new LoginPresenter(this);

        ButterKnife.bind(this);
        findViewById(R.id.img_refresh).setOnClickListener(view -> ((QuickTrackAplication) getApplicationContext()).checkVersion());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            String[] perms = {
                    android.Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA};
            EasyPermissions.requestPermissions(Login_Activity.this, "Please Allow All Permissions", 101, perms);

        } else {
            trackDriver = new GetLocation(Login_Activity.this);
        }

        txt_deviceId.setText(String.format("Device ID : %s", ((QuickTrackAplication) getApplicationContext()).getAndroidID()));
        try {
            ((TextView) findViewById(R.id.txt_version)).setText("Version Code: " + ((QuickTrackAplication) getApplicationContext()).getPackageInfo().versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        imei = ((QuickTrackAplication) getApplicationContext()).getUniqueIMEIId(getApplicationContext());
        setemiText();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_login;
    }

    public void setemiText() {
        if (!imei.equals("")) {
            txt_imei.setVisibility(View.VISIBLE);
            txt_imei.setText("IMEI : " + imei);
        } else {
            txt_imei.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        if (validData()) {
            try {
                if (Utils.validInternet(getApplicationContext()))
                    call_loginAPi();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean validData() {
        if (Utility.isValidString(editLicencenNumber.getText().toString().trim())) {
            if (Utility.isValidString(editPaswd.getText().toString().trim())) {
                return true;
            } else {
                editPaswd.requestFocus();
                Toast.makeText(Login_Activity.this, "Please enter password", Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            editLicencenNumber.requestFocus();
            Toast.makeText(Login_Activity.this, "Please enter licence Number", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void call_loginAPi() {
        showProgressDialogue();
        String refreshedToken = "";
        refreshedToken = new PrefManager(this).getFMC_TOKEN();
        LoginRequestEntity loginRequestEntity = new LoginRequestEntity();
        loginRequestEntity.setApiKey(Constants.API_KEY);
        loginRequestEntity.setLicenceNumber(editLicencenNumber.getText().toString().trim());
        loginRequestEntity.setPassword(editPaswd.getText().toString().trim());
        loginRequestEntity.setDeviceId(((QuickTrackAplication) getApplicationContext()).getAndroidID());
        loginRequestEntity.setRefreshToken(refreshedToken);
        loginRequestEntity.setImei(imei);
        loginPresenter.login(loginRequestEntity);
    }


    public void forceLogoutDialog(LoginData loginData) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Login_Activity.this);
        alertDialog.setTitle("ERROR!");
        alertDialog.setMessage("You are already Logged in on other device, do you want to log out from first device?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
            dialog.dismiss();

            try {
                callForceLogoutAPI(loginData);
            } catch (Exception e) {
                Toast.makeText(Login_Activity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

        alertDialog.setNeutralButton("No", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }


    public void homeDilog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Login_Activity.this);
        alertDialog.setMessage(getString(R.string.areyousureexit));
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", (dialog, which) -> dialog.dismiss());
        alertDialog.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }


    private void callForceLogoutAPI(LoginData loginData) throws Exception {
        showProgressDialogue();
        loginPresenter.forceLogOut(loginData);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        imei = ((QuickTrackAplication) getApplicationContext()).getUniqueIMEIId(getApplicationContext());
        trackDriver = new GetLocation(Login_Activity.this);
        for (int i = 0; i < perms.size(); i++) {
            LogCustom.logd("xxxxxxx", ":permisstion:" + perms.get(i));
        }
        setemiText();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (perms.size() != 0) {
            String[] permsn = perms.toArray(new String[perms.size()]);
            if (ActivityCompat.shouldShowRequestPermissionRationale(Login_Activity.this, Manifest.permission.READ_PHONE_STATE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(Login_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(Login_Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
            ) {
                EasyPermissions.requestPermissions(Login_Activity.this, "Please Allow All Permissions", 101, permsn);
            }
        }
    }

    @Override
    public void onLoginSuccess(LoginResponse loginResponse) {
        hideProgressDialogue();
        if (loginResponse.getData().getLogin_flag() == 1) {
            forceLogoutDialog(loginResponse.getData());
        } else {
            PrefManager prefManager = new PrefManager(getApplicationContext());
            prefManager.setUserDetails(loginResponse.getData());
            prefManager.setLogin(true);
            trackDriver.call_trackDriverAPi(Constants.BACK_LOGIN);
            Intent sender = new Intent(Login_Activity.this, TrackListActivity.class);
            startActivity(sender);
        }
    }

    @Override
    public void onLoginError(String message) {
        hideProgressDialogue();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onForLogOutSuccess(LoginData loginData, String message) {
        hideProgressDialogue();
        showToast(message);
        PrefManager prefManager = new PrefManager(getApplicationContext());
        prefManager.setUserDetails(loginData);
        prefManager.setLogin(true);
        trackDriver.call_trackDriverAPi(Constants.BACK_LOGIN);
        Intent sender = new Intent(Login_Activity.this, TrackListActivity.class);
        startActivity(sender);

    }

    @Override
    public void onForLogOutError(String message) {
        hideProgressDialogue();
        showToast(message);
    }
}
