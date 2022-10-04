package com.evolution.quicktrack.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.evolution.quicktrack.Login_Activity;
import com.evolution.quicktrack.MainActivity;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.adapter.Chat_Adapter;
import com.evolution.quicktrack.callback.Chat_ItemClickCallBack;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.location.GetLocation;
import com.evolution.quicktrack.model.UserNameImageModel;
import com.evolution.quicktrack.network.ApiCallInterface;
import com.evolution.quicktrack.network.ApiClient;
import com.evolution.quicktrack.response.chatlist.ChatListResponse;
import com.evolution.quicktrack.response.chatlist.MessagesItem;
import com.evolution.quicktrack.response.chatlist.UserChatData;
import com.evolution.quicktrack.response.sentmsg.SentMsgResponse;
import com.evolution.quicktrack.util.CircleImageView;
import com.evolution.quicktrack.util.PrefManager;
import com.evolution.quicktrack.util.RecyclerViewEmptySupport;
import com.evolution.quicktrack.util.Utility;
import com.evolution.quicktrack.util.Utils;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.evolution.quicktrack.constants.Constants.OPEN_to_USERID;

public class ChatActivity extends AppCompatActivity implements  Chat_ItemClickCallBack {

    @BindView(R.id.recyclerview_manu)
    RecyclerViewEmptySupport recyclerview;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshView;



    @BindView(R.id.txt_nodata)
    TextView txtNodata;
    @BindView(R.id.emptyView)
    LinearLayout emptyView;
    @BindView(R.id.edt_msg)
    EditText edtMsg;
    @BindView(R.id.img_send)
    ImageView imgSend;
    private KProgressHUD pd;
    public static String TAG = Login_Activity.class.getSimpleName();
    @BindView(R.id.btn_logout)
    ImageView btnLogout;
    LinearLayoutManager layoutManager;

    PrefManager prefManager;
    String frndId = "";
    boolean isApicall = false;

    public  GetLocation trackDriver=null;

    private List<MessagesItem> responseData = new ArrayList<>();
    UserChatData userChatData=new UserChatData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        trackDriver  =new GetLocation(ChatActivity.this);
        initView();

        prefManager = new PrefManager(getApplicationContext());

        getIntentData();


    }

    public void getIntentData() {

        if (getIntent().getStringExtra(Constants.EXTA_FRNDID) != null) {
            frndId = getIntent().getStringExtra(Constants.EXTA_FRNDID);
            OPEN_to_USERID=frndId;
            callApi();

        }


    }





    public void initView() {

        btnLogout.setVisibility(View.INVISIBLE);

        this.recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.VERTICAL, true);
        this.recyclerview.setLayoutManager(layoutManager);
        this.recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setEmptyView(emptyView);


        refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                lodMore();

            }
        });

    }


    public void lodMore() {

        try {
            getChat_Api(responseData.get(responseData.size()-1).getId(), false,"loadMoreMessages");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void callApi() {

        try {

            if (Utils.validInternet(getApplicationContext()))
                getChat_Api("", true,"openChatbox");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getChat_Api(String firstmessageid, final boolean isFirsttime,String action) throws Exception {


        LogCustom.logd("xxxxx","firstmessageid"+firstmessageid);
        LogCustom.logd("xxxxx","isFirsttime"+isFirsttime);
        LogCustom.logd("xxxxx","action"+action);


        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);


        PrefManager prefManager = new PrefManager(getApplicationContext());

//        Toast.makeText(TrackListActivity.this,"id :"+prefManager.getId(),Toast.LENGTH_LONG).show();

        Log.d("xxxx", "useerid:" + prefManager.getId());

        //  Call<ChatListResponse> call = apiService.getChat(Constants.API_KEY, prefManager.getId(),"loadFriends",frndId,"");
        Call<ChatListResponse> call = null;

        if (!isApicall) {

            isApicall = true;
            call = apiService.getChat(prefManager.getLogin_Token(),Constants.API_KEY,prefManager.getId() , action,frndId , firstmessageid,prefManager.getId());


            call.enqueue(new Callback<ChatListResponse>() {
                @Override
                public void onResponse(Call<ChatListResponse> call, Response<ChatListResponse> response) {

                    Log.d(TAG, " : check status : " + response.body().toString());
                    isApicall = false;

                    refreshView.setRefreshing(false);

                    if (pd != null && pd.isShowing())
                        pd.dismiss();




                    if (response.body().isStatus())
                    {

                        userChatData=response.body().getData();
                         List<MessagesItem> responseDatatemp = new ArrayList<>();
                        responseDatatemp=response.body().getData().getMessages();

                        if(responseDatatemp!=null && responseDatatemp.size()>0){

                            if (isFirsttime) {


                                 responseData.clear();
                                Collections.reverse(responseDatatemp);
                                responseData=responseDatatemp;

                            } else {
                               // Collections.reverse(responseDatatemp);
                                responseData.addAll(responseData.size(),responseDatatemp);
                               // responseData.addAll(0,response.body().getData().getMessages() );
                               /// Collections.reverse(response.body().getData().getMessages());
                            }
                        }else {

                            refreshView.setEnabled(false);
                        }

                        setUserModel();
                        setAddapter(isFirsttime);

                    }
                    else
                    {
                        if(response.body().getMessage().equals("invalidToken"))
                        {
                            prefManager.logout();

                            Intent   intent =new Intent(ChatActivity.this,Login_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }

                    }
                }

                @Override
                public void onFailure(Call<ChatListResponse> call, Throwable t) {
                    LogCustom.logd(TAG, t.toString());

                }
            });

        }
    }

    Chat_Adapter custom_manu_adepter;

    public void setAddapter(boolean isFirstTime) {
        if (isFirstTime) {
            custom_manu_adepter = new Chat_Adapter(ChatActivity.this, responseData,userNameImageModel, ChatActivity.this);
            recyclerview.setAdapter(custom_manu_adepter);
            recyclerview.getLayoutManager().scrollToPosition(0);

        } else {
            custom_manu_adepter.notifyDataSetChanged();
        }

    }
    UserNameImageModel userNameImageModel=new UserNameImageModel();


    public void setUserModel(){
        userNameImageModel.setMyId(prefManager.getId());
        userNameImageModel.setMyImage(prefManager.getImage());
        userNameImageModel.setMyName(prefManager.getfullName());

        userNameImageModel.setSenderImage(userChatData.getPicture());
        userNameImageModel.setSenderName(userChatData.getName());
    }




    @OnClick({R.id.img_home, R.id.btn_logout,R.id.img_pictur})
    public void onViewClicked2(View view) {
        switch (view.getId()) {
            case R.id.img_home:

                //homeDilog();
                onBackPressed();

                break;
            case R.id.btn_logout:

                logOutDialog();

                break;

            case R.id.img_pictur:

                getImage();

                break;
        }
    }



    @OnClick(R.id.img_send)
    public void onViewClicked() {

        if(Utils.validInternet(getApplicationContext())){

            if(Utility.isValidString(edtMsg.getText().toString().trim())) {

                    sentTextMsg();

            }
        }


    }






    private void sentTextMsg() {


        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);


        Call<SentMsgResponse> call = apiService.sendMsg(prefManager.getLogin_Token(),Constants.API_KEY, "sendMsg",prefManager.getId(),frndId,"14","12", edtMsg.getText().toString().trim(),prefManager.getId());
        call.enqueue(new Callback<SentMsgResponse>() {
            @Override
            public void onResponse(Call<SentMsgResponse> call, Response<SentMsgResponse> response) {
                try {
                Log.d(TAG, " : check status : " + response.body().toString());

                if (pd != null && pd.isShowing())
                    pd.dismiss();
                if (response.body().isStatus()) {

                    edtMsg.setText("");
                    getChat_Api("",true,"openChatbox");

                }
                else
                {

                    if(response.body().getMessage().equals("invalidToken"))
                    {
                        prefManager.logout();

                        Intent   intent =new Intent(ChatActivity.this,Login_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SentMsgResponse> call, Throwable t) {
                LogCustom.logd(TAG, t.toString());
                if (pd != null && pd.isShowing())
                    pd.dismiss();
            }
        });
    }





    public void sendImage(){



        File file=new File(images.get(0).getPath());

        pd = LogCustom.loderProgressDialog2(this);
        pd.show();

        RequestBody mFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);

        RequestBody APIKEY = RequestBody.create(MediaType.parse("multipart/form-data"),Constants.API_KEY);
        RequestBody action = RequestBody.create(MediaType.parse("multipart/form-data"), "sendMsg");
        RequestBody from_user_id = RequestBody.create(MediaType.parse("multipart/form-data"),prefManager.getId());
        RequestBody from_user_role = RequestBody.create(MediaType.parse("multipart/form-data"),"14");
        RequestBody to_user_id = RequestBody.create(MediaType.parse("multipart/form-data"), frndId);
        RequestBody to_user_role = RequestBody.create(MediaType.parse("multipart/form-data"),"12");
        RequestBody token = RequestBody.create(MediaType.parse("multipart/form-data"),prefManager.getLogin_Token());
        RequestBody driverId = RequestBody.create(MediaType.parse("multipart/form-data"),prefManager.getId());
        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);

        Call<SentMsgResponse> call = apiService.sendFile(token,APIKEY, action,from_user_id,from_user_role,to_user_id,to_user_role, fileToUpload,driverId);

        call.enqueue(new Callback<SentMsgResponse>() {
            @Override
            public void onResponse(Call<SentMsgResponse> call, Response<SentMsgResponse> response) {


                if (pd != null && pd.isShowing())
                    pd.dismiss();

                try{

                    images.remove(0);

                        if (response.body().isStatus()) {

                            LogCustom.loge("xxxxxxx","response::"+response.body());
                            getChat_Api("",true,"openChatbox");

                        } else {

                            if(response.body().getMessage().equals("invalidToken"))
                            {
                                prefManager.logout();

                                Intent   intent =new Intent(ChatActivity.this,Login_Activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SentMsgResponse> call, Throwable t) {
                LogCustom.loge(TAG, t.toString());
                if (pd != null && pd.isShowing())
                    pd.dismiss();
            }
        });


    }

    ArrayList<Image> images = new ArrayList<>();

    public void getImage() {

        ImagePicker.with(this)                         //  Initialize ImagePicker with activity or fragment context
                .setToolbarColor("#212121")         //  Toolbar color
                .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
                .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                .setProgressBarColor("#4CAF50")     //  ProgressBar color
                .setBackgroundColor("#212121")      //  Background color
                .setCameraOnly(false)               //  Camera mode
                .setMultipleMode(true)              //  Select multiple images or single image
                .setFolderMode(true)                //  Folder mode
                .setShowCamera(true)                //  Show camera button
                .setFolderTitle("Albums")           //  Folder title (works with FolderMode = true)
                .setImageTitle("Galleries")         //  Image title (works with FolderMode = false)
                .setDoneTitle("Done")               //  Done button title
                .setLimitMessage("You have reached selection limit")    // Selection limit message
                .setMaxSize(1)                     //  Max images can be selected
                .setSavePath("ImagePicker")         //  Image capture folder name
                .setSelectedImages(images)          //  Selected images
                .setKeepScreenOn(true)              //  Keep screen on when selecting images
                .start();


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> imagesd = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            // do your logic here...
            images = imagesd;

            if(images.size()==1){
                sendImage();
            }



        }
        super.onActivityResult(requestCode, resultCode, data);  // THIS METHOD SHOULD BE HERE so that ImagePicker works with fragment
    }



    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction("MSG");

        registerReceiver(mHandleMessageReceiver, filter);
    }


    BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


          MessagesItem messagesItem= (MessagesItem) intent.getSerializableExtra("MessageModel");

          LogCustom.loge("xxxxxxx","recive brodcast"+messagesItem.toString());
            responseData.add(0,messagesItem);
            custom_manu_adepter.notifyDataSetChanged();

        }
    };

    @Override
    public void OnItemClick(int position) {

    }

    @Override
    public void OnImageClick(int position) {


        Intent intent=new Intent(ChatActivity.this,ImageFullViewActivity.class);
        intent.putExtra("Image",responseData.get(position).getImage());
        startActivity(intent);


    }

    @Override
    public void OnUserImageClick(int position, boolean isMy) {

        if (responseData.get(position).getFromUserId().equals(userNameImageModel.getMyId())){
            userDetails_Dailog(userNameImageModel.getMyName(),userNameImageModel.getMyImage());
        }else {
            userDetails_Dailog(userNameImageModel.getSenderName(),userNameImageModel.getSenderImage());

        }

    }







    private AlertDialog userDetailDialog;

    public void userDetails_Dailog(String name,String imgUrl) {

        LayoutInflater factory = LayoutInflater.from(ChatActivity.this);
        final View dialogView = factory.inflate(R.layout.friends_details_dialog, null);
        userDetailDialog = new AlertDialog.Builder(ChatActivity.this).create();
        userDetailDialog.setView(dialogView);
        userDetailDialog.setCancelable(false);

        final ImageView img_close = (ImageView) dialogView.findViewById(R.id.img_close);
        final ImageView img_profiletxt = (ImageView) dialogView.findViewById(R.id.img_profiletxt);

        CircleImageView img_profile = (CircleImageView) dialogView.findViewById(R.id.img_profile);
        TextView txt_name = (TextView) dialogView.findViewById(R.id.txt_name);


        txt_name.setText(name);


        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRound(Utils.printFirstCharacter(name), Utils.getRandomMaterialColor(getApplicationContext()));
        img_profiletxt.setImageDrawable(drawable);

        RequestOptions options = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(getApplicationContext())
                .load(imgUrl)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        img_profiletxt.setImageDrawable(getResources().getDrawable(R.drawable.circle_shape));
                        return false;
                    }
                })
                .into(img_profile);


        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDetailDialog.dismiss();
            }
        });





        userDetailDialog.show();


    }


    public  void homeDilog(){

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChatActivity.this);
        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.wanttogohome));
        alertDialog.setCancelable(false);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(ChatActivity.this, MainActivity.class);
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

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChatActivity.this);

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

                Intent   intent =new Intent(ChatActivity.this,Login_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();*/

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
