package com.evolution.quicktrack.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evolution.quicktrack.QuickTrackAplication;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.location.GetLocation;
import com.evolution.quicktrack.notification.NotificationHelper;

import java.util.Locale;

public class ChatPopupDialog extends Activity{

    private static final String TAG = "ChatPopupDialog";
    GetLocation getLocation;
    boolean status = false;
   // WindowManager manager;
   // View layout;
    int count = 0;
    String spekMessage = "";
    private LinearLayout linearLayout;
    private String fromUser;
    private TextView popup_txt_user;
    private String id;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_popup_dialog);
        linearLayout = findViewById(R.id.llChats);
        popup_txt_user = findViewById(R.id.popup_txt_user);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        fromUser = intent.getStringExtra("chatuser");
        ((TextView)findViewById(R.id.popup_title)).setText("Message from "+fromUser);
        String chatmessage = intent.getStringExtra("chatmessage");
        spekMessage = intent.getStringExtra("speakmessage");
        ShowPopup(fromUser,chatmessage);
        registerReceiver(chatMessages,new IntentFilter(id));
    }

    BroadcastReceiver chatMessages = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            fromUser = intent.getStringExtra("chatuser");
            String chatmessage = intent.getStringExtra("chatmessage");
            spekMessage = intent.getStringExtra("speakmessage");
            speakOut(spekMessage);
            TextView txtuser = (TextView) LayoutInflater.from(ChatPopupDialog.this).inflate(R.layout.multiple_chat_item,null,false);
            txtuser.setText(chatmessage);
            linearLayout.addView(txtuser);

        }
    };


    public void ShowPopup(String fromUser,String chatmessage) {

        try {
            popup_txt_user.setText("From : "+fromUser);
            TextView txtuser = (TextView) LayoutInflater.from(this).inflate(R.layout.multiple_chat_item,null,false);
            txtuser.setText(chatmessage);
            linearLayout.addView(txtuser);
            Button btnOk = findViewById(R.id.popup_btn_ok_chat);
            btnOk.setOnClickListener(v -> {
                NotificationHelper.dismiss();
                Constants.arrChatsUser.remove(id);
                finish();

            });
        } catch (Exception e) {
            e.getMessage();
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Constants.arrChatsUser.remove(id);
        NotificationHelper.dismiss();
    }
    private void speakOut(String speakMessage) {
        ((QuickTrackAplication)getApplicationContext()).convertTextToSpeech(speakMessage);
    }
    @Override
    protected void onStop() {
        super.onStop();
        Constants.arrChatsUser.remove(id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constants.arrChatsUser.remove(id);
    }
}


