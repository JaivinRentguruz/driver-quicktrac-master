package com.evolution.quicktrack.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.evolution.quicktrack.BaseActivity;
import com.evolution.quicktrack.MainActivity;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.response.login.LoginData;
import com.evolution.quicktrack.util.PrefManager;
import com.kaopiz.kprogresshud.KProgressHUD;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 4/2/2018.
 */

public class MyProfile_Activity extends BaseActivity {

    @BindView(R.id.img_home)
    ImageView imgHome;
    @BindView(R.id.btn_logout)
    ImageView btnLogout;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_contact)
    TextView txtContact;
    @BindView(R.id.txt_licence)
    TextView txtLicence;
    @BindView(R.id.txt_email)
    TextView txtEmail;
    private KProgressHUD pd;
    public static String TAG = MyProfile_Activity.class.getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView(R.layout.activity_odometer);
        ButterKnife.bind(this);
       // btnLogout.setVisibility(View.INVISIBLE);
        initView();
    }




    public void initView(){

        PrefManager prefManager=new PrefManager(getApplicationContext());
        LoginData loginData= prefManager.getUserData();

        txtEmail.setText(loginData.getDEmail());
        txtContact.setText(loginData.getDContactNo());
        txtName.setText(loginData.getDFirstName()+" "+loginData.getDLastName() );
        txtLicence.setText(loginData.getDLicenceNumber());

    }



    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_myprofile;
    }


    @OnClick({R.id.img_home, R.id.btn_logout})
    public void onViewClicked2(View view) {
        switch (view.getId()) {
            case R.id.img_home:
                //homeDilog();
                onBackPressed();
                break;
            case R.id.btn_logout:

                logOutDialog();
                break;
        }
    }

    public  void homeDilog(){

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyProfile_Activity.this);
        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.wanttogohome));
        alertDialog.setCancelable(false);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(MyProfile_Activity.this, MainActivity.class);
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

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyProfile_Activity.this);

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure want to logout ?");
        alertDialog.setCancelable(true);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
             /*   trackDriver.call_trackDriverAPi(Constants.BACK_LOGOUT);


                PrefManager prefManager=new PrefManager(getApplicationContext());
                prefManager.logout();

                Intent   intent =new Intent(MyProfile_Activity.this,Login_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
*/              showOdometerDialog(MyProfile_Activity.this);
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
