package com.evolution.quicktrack.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.evolution.quicktrack.Login_Activity;

public class ForceLogoutDialog extends Activity {

    private static final String TAG = "ForceLogoutDialog";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {

            String message = getIntent().getStringExtra("Message");
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Force Logout");
            alertDialog.setMessage(message);
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    try {

                        Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            });

            AlertDialog dialog = alertDialog.create();
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog.show();

        }
        catch (Exception e)
        {
            e.getMessage();
        }

    }



}


