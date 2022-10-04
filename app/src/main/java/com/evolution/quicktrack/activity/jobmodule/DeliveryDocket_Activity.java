package com.evolution.quicktrack.activity.jobmodule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.evolution.quicktrack.BaseActivity;
import com.evolution.quicktrack.Login_Activity;
import com.evolution.quicktrack.MainActivity;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.location.GetLocation;
import com.evolution.quicktrack.network.ApiCallInterface;
import com.evolution.quicktrack.network.ApiClient;
import com.evolution.quicktrack.response.common.CommonResponse;
import com.evolution.quicktrack.response.jobDetails.JobDetailsResponse;
import com.evolution.quicktrack.util.PrefManager;
import com.evolution.quicktrack.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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

/**
 * Created by user on 4/2/2018.
 */

public class DeliveryDocket_Activity extends BaseActivity {


    @BindView(R.id.edit_docketnumber)
    EditText editDocketnumber;

    @BindView(R.id.img_details)
    ImageView imgDetails;
    @BindView(R.id.txt_pickupAddres)
    TextView txtPickupAddres;
    @BindView(R.id.txt_deliveryAddres)
    TextView txtDeliveryAddres;

    @BindView(R.id.txt_dc_title)
    TextView txt_dc_title;

    @BindView(R.id.img_camera)
    ImageView imgCamera;

    // For Multiple Image
    @BindView(R.id.img_1)
    ImageView img1;
    @BindView(R.id.img_close1)
    ImageView imgClose1;
    @BindView(R.id.rel_img1)
    RelativeLayout relImg1;

    @BindView(R.id.img_2)
    ImageView img2;
    @BindView(R.id.img_close2)
    ImageView imgClose2;
    @BindView(R.id.rel_img2)
    RelativeLayout relImg2;

    @BindView(R.id.img_3)
    ImageView img3;
    @BindView(R.id.img_close3)
    ImageView imgClose3;
    @BindView(R.id.rel_img3)
    RelativeLayout relImg3;

    @BindView(R.id.img_4)
    ImageView img4;
    @BindView(R.id.img_close4)
    ImageView imgClose4;
    @BindView(R.id.rel_img4)
    RelativeLayout relImg4;

    @BindView(R.id.img_5)
    ImageView img5;
    @BindView(R.id.img_close5)
    ImageView imgClose5;
    @BindView(R.id.rel_img5)
    RelativeLayout relImg5;

    // Multiple Image Over
    @BindView(R.id.txt_weight)
    TextView txtWeight;
    @BindView(R.id.txt_quntity)
    TextView txtQuntity;
    @BindView(R.id.txt_qubicweight)
    TextView txtQubicweight;
    @BindView(R.id.txt_distance)
    TextView txtDistance;
    @BindView(R.id.txt_dimention)
    TextView txtDimention;

    @BindView(R.id.txt_jobId)
    TextView txt_jobId;

    /* @BindView(R.id.sign_consignee)
     LinearLayout signConsignee;*/
    @BindView(R.id.liner_consignee)
    LinearLayout linerConsignee;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private KProgressHUD pd;
    public static String TAG = DeliveryDocket_Activity.class.getSimpleName();
    @BindView(R.id.img_home)
    ImageView imgHome;
    @BindView(R.id.btn_logout)
    ImageView btnLogout;

    String jobId = "";
    String type = "";
    String subjobID = "";
    ArrayList<Image> images = new ArrayList<>();
    private GetLocation getLocation;

    private String strIDs = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView(R.layout.activity_odometer);
        ButterKnife.bind(this);

        type = getIntent().getExtras().getString("type", "");
        subjobID = getIntent().getExtras().getString("subjobid", "");

        // btnLogout.setVisibility(View.INVISIBLE);

    }


    @Override
    protected void onStart() {
        super.onStart();

        getLocation = new GetLocation(DeliveryDocket_Activity.this);
        initView();


    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_pickup_docket_slip;
    }

    public void initView() {


        getIntendata();

    }

    public void getIntendata() {


        if (getIntent().getStringExtra(Constants.EXTRA_JOBID) != null) {

            jobId = getIntent().getStringExtra(Constants.EXTRA_JOBID);
        }
        txt_dc_title.setText("Proof Of Delivery");

        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey("strIDs") && getIntent().getExtras().getString("strIDs").trim().length() > 0) {
            strIDs = getIntent().getExtras().getString("strIDs");
            txt_jobId.setText("JOB ID : " + strIDs);
            findViewById(R.id.ll_details).setVisibility(View.GONE);

            findViewById(R.id.ll_col_comp).setVisibility(View.GONE);
            findViewById(R.id.ll_col_add).setVisibility(View.GONE);
            findViewById(R.id.ll_del_comp).setVisibility(View.GONE);
            findViewById(R.id.ll_del_add).setVisibility(View.GONE);
        } else {
            callApi();
        }
        // callApi();
    }

    public void callApi() {

        if (Utils.validInternet(getApplicationContext())) {
            try {

                call_jobdetailsApi();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @OnClick({R.id.img_camera, R.id.img_close1, R.id.img_close2, R.id.img_close3, R.id.img_close4, R.id.img_close5, R.id.btn_submit, R.id.img_details})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.img_camera:

                if (images.size() != 1) {
                    getImage();
                } else {
                    showToast("Ohh..You can't add more then 1 images..!!");
                }
                break;
            case R.id.btn_submit:

                //if(validdata()){

                if (!apicall) {
                    callSlip_Api();

                } else {

                    if (getLocation.getcurrntLataLong() != null) {
                        call_updatestaus(jobId, Constants.Delivered, String.valueOf(getLocation.getcurrntLataLong().getLatitude()), String.valueOf(getLocation.getcurrntLataLong().getLongitude()));
                    } else {
                        LogCustom.logd("xxxxx", "getlocation");
                        getLocation = new GetLocation(DeliveryDocket_Activity.this);
                    }

                }

                //}


                break;
            case R.id.img_details:

                Intent intent = new Intent(DeliveryDocket_Activity.this, JobDetailsView_Activity.class);
                intent.putExtra(Constants.EXTRA_JOBID, jobId);
                startActivity(intent);

                break;
            case R.id.img_close1:
                if (images.size() > 0) {
                    images.remove(0);
                    setImage(0, true);
                }
                break;
            case R.id.img_close2:
                if (images.size() > 1) {
                    images.remove(1);
                    setImage(1, true);
                }
                break;
            case R.id.img_close3:
                if (images.size() > 2) {
                    images.remove(2);
                    setImage(2, true);
                }
                break;
            case R.id.img_close4:
                if (images.size() > 3) {
                    images.remove(3);
                    setImage(3, true);
                }
                break;
            case R.id.img_close5:
                if (images.size() > 4) {
                    images.remove(4);
                    setImage(4, true);
                }
                break;
        }
    }


    @OnClick({R.id.img_home, R.id.btn_logout})
    public void onViewClicked2(View view) {
        switch (view.getId()) {
            case R.id.img_home:
                onBackPressed();
//homeDilog();
                break;
            case R.id.btn_logout:

                logOutDialog();
                break;
        }
    }


    private void call_jobdetailsApi() {


        pd = LogCustom.loderProgressDialog2(this);
        pd.show();

        PrefManager prefManager = new PrefManager(getApplicationContext());
        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);

        Call<JobDetailsResponse> call = apiService.getjobDetails(prefManager.getLogin_Token(), Constants.API_KEY, jobId, prefManager.getId());
        call.enqueue(new Callback<JobDetailsResponse>() {
            @Override
            public void onResponse(Call<JobDetailsResponse> call, Response<JobDetailsResponse> response) {


                try {
                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                    Log.d(TAG, " : check status : " + response.body().toString());


                    if (response.body().getStatus().equals(Constants.API_STATUS)) {
                        txtPickupAddres.setText(response.body().getData().getCollectionLocation());
                        txtDeliveryAddres.setText(response.body().getData().getDeliveryLocation());
                        txt_jobId.setText("JOB ID : " + response.body().getData().getId());


                        if (!response.body().getData().getWeight().equals(""))
                            txtWeight.setText(response.body().getData().getWeight());

                        if (!response.body().getData().getDimension().equals(""))
                            txtDimention.setText(response.body().getData().getDimension());

                        if (!response.body().getData().getDistance().equals(""))
                            txtDistance.setText(response.body().getData().getDistance());

                        if (!response.body().getData().getCubicWeight().equals(""))
                            txtQubicweight.setText(response.body().getData().getCubicWeight());

                        if (!response.body().getData().getQuantity().equals(""))
                            txtQuntity.setText(response.body().getData().getQuantity());

                    } else {
                        if (response.body().getMessage().equals("invalidToken")) {
                            prefManager.logout();

                            Intent intent = new Intent(DeliveryDocket_Activity.this, Login_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }


                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<JobDetailsResponse> call, Throwable t) {
                LogCustom.logd(TAG, t.toString());
                if (pd != null && pd.isShowing())
                    pd.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> imagesd = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            // do your logic here...
            // images = imagesd;

            if (imagesd.size() > 0) {
                for (Image image : imagesd) {
                    images.add(image);
                }
            }

            for (int i = 0; i < images.size(); i++) {
                setImage(i, false);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);  // THIS METHOD SHOULD BE HERE so that ImagePicker works with fragment
    }


    public void setImage(int position, boolean isRemove) {

        if (!isRemove) {
            if (position == 0) {
                Load_Image(images.get(position).getPath(), img1);
                relImg1.setVisibility(View.VISIBLE);
            }
            if (position == 1) {
                Load_Image(images.get(position).getPath(), img2);
                relImg2.setVisibility(View.VISIBLE);
            }
            if (position == 2) {
                Load_Image(images.get(position).getPath(), img3);
                relImg3.setVisibility(View.VISIBLE);
            }
            if (position == 3) {
                Load_Image(images.get(position).getPath(), img4);
                relImg4.setVisibility(View.VISIBLE);
            }
            if (position == 4) {
                Load_Image(images.get(position).getPath(), img5);
                relImg5.setVisibility(View.VISIBLE);
            }

        } else {
            if (position == 0) {
                relImg1.setVisibility(View.GONE);
            }
            if (position == 1) {

                relImg2.setVisibility(View.GONE);
            }
            if (position == 2) {

                relImg3.setVisibility(View.GONE);
            }
            if (position == 3) {

                relImg4.setVisibility(View.GONE);
            }
            if (position == 4) {
                relImg5.setVisibility(View.GONE);
            }
        }


    }


    public void getImage() {

        ImagePicker.with(this)                         //  Initialize ImagePicker with activity or fragment context
                .setToolbarColor("#212121")         //  Toolbar color
                .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
                .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                .setProgressBarColor("#4CAF50")     //  ProgressBar color
                .setBackgroundColor("#212121")      //  Background color
                .setCameraOnly(true)               //  Camera mode
                .setMultipleMode(false)              //  Select multiple images or single image
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

    private void Load_Image(String Url, final ImageView img) {

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.sample_user);
        requestOptions.fitCenter();


        Glide.with(getApplicationContext())
                .applyDefaultRequestOptions(requestOptions)
                .load(Url)
                .into(img);
    }


    boolean apicall = false;

    public void callSlip_Api() {


        //File filecosign=new File(images.get(0).getPath());


        pd = LogCustom.loderProgressDialog2(this);
        pd.show();

        PrefManager prefManager = new PrefManager(getApplicationContext());
        //RequestBody mFile2 = RequestBody.create(MediaType.parse("image/jpeg"), filecosign);
        //MultipartBody.Part fileToUpload2 = MultipartBody.Part.createFormData("docketdeliveryslip_sign[]", filecosign.getName(), mFile2);
        List<MultipartBody.Part> deliveryLocationImages = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {

            File file2 = new File(images.get(i).getPath());
            Bitmap bitmapImage = BitmapFactory.decodeFile(file2.getAbsolutePath());
            int nh = (int) (bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()));
            Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, Constants.SIZE_WIDTH, Constants.SIZE_HEIGHT, true);

            File filename = new File(getFilesDir(), Calendar.getInstance().getTimeInMillis() + ".jpg");

            try (FileOutputStream out = new FileOutputStream(filename)) {
                scaled.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            } catch (IOException e) {
                e.printStackTrace();
            }

            int file_size = Integer.parseInt(String.valueOf(filename.length() / 1024));
            Log.w("File Size ", file_size + "");

            RequestBody surveyBody = RequestBody.create(MediaType.parse("image/jpeg"), filename);
            deliveryLocationImages.add(MultipartBody.Part.createFormData("docketdeliveryslip_picture[]", filename.getName(), surveyBody));
        }


        RequestBody APIKEY = RequestBody.create(MediaType.parse("multipart/form-data"), Constants.API_KEY);
        RequestBody jobid = RequestBody.create(MediaType.parse("multipart/form-data"), jobId);
        RequestBody driverid = RequestBody.create(MediaType.parse("multipart/form-data"), prefManager.getId());
        RequestBody token = RequestBody.create(MediaType.parse("multipart/form-data"), prefManager.getLogin_Token());
        String docateSlipNumber = "";
        docateSlipNumber = "" + editDocketnumber.getText().toString();
       /* if(docateSlipNumber.equals(""))
        {
            docateSlipNumber = "text";
        }*/

        if (strIDs.trim().length() > 0) {
            jobid = RequestBody.create(MediaType.parse("multipart/form-data"), strIDs);
        }

        RequestBody number = RequestBody.create(MediaType.parse("multipart/form-data"), docateSlipNumber);


        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);

        Call<CommonResponse> call = apiService.docketdeliveryslip(
                APIKEY,
                driverid,
                token,
                jobid,
                number,
                deliveryLocationImages
        );

        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {


                if (pd != null && pd.isShowing())
                    pd.dismiss();

                try {

                    if (response != null) {
                        if (response.body().getStatus().equals(Constants.API_STATUS)) {

                            LogCustom.loge("xxxxxxx", "response::" + response.body());

                            apicall = true;


                            if (getLocation.getcurrntLataLong() != null) {
                                call_updatestaus(jobId, Constants.Delivered, String.valueOf(getLocation.getcurrntLataLong().getLatitude()), String.valueOf(getLocation.getcurrntLataLong().getLongitude()));

                            } else {
                                LogCustom.logd("xxxxx", "getlocation");
                                getLocation = new GetLocation(DeliveryDocket_Activity.this);
                                call_updatestaus(jobId, Constants.Delivered, String.valueOf(getLocation.getcurrntLataLong().getLatitude()), String.valueOf(getLocation.getcurrntLataLong().getLongitude()));
                            }


                        } else {
                            showToast("Try again" + response.body().getMessage());
                        }
                    } else {
                        showToast("Try again");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                LogCustom.loge(TAG, t.toString());
                if (pd != null && pd.isShowing())
                    pd.dismiss();
            }
        });


    }


    public boolean validdata() {


        // if(Utility.isValidString(editDocketnumber.getText().toString().trim())){
        if (images.size() > 0) {
            return true;
        } else {
            LogCustom.Toast(DeliveryDocket_Activity.this, "Please add Image");

            return false;
        }

        /*}else {
            LogCustom.Toast(DeliveryDocket_Activity.this,"Please enter Number");

            return false;
        }
*/


    }


    private void call_updatestaus(String jobid, String statusid, String latitude, String longitude) {

        if (strIDs.trim().length() > 0) {
            jobid = strIDs;
        }

        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);


        PrefManager prefManager = new PrefManager(getApplicationContext());


        Call<CommonResponse> call = apiService.updateStatus(prefManager.getLogin_Token(), Constants.API_KEY, prefManager.getId(), jobid, statusid, latitude, longitude, prefManager.getTruckId(), type, subjobID);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                try {


                    Log.d(TAG, " : check status : " + response.body().toString());


                    if (response.body().getStatus().equals(Constants.API_STATUS)) {

                        Intent intent2 = new Intent(DeliveryDocket_Activity.this, JobDetails_Complate_Activity.class);
                        intent2.putExtra(Constants.EXTRA_JOBID, jobId);
                        startActivity(intent2);

                    } else {
                        if (response.body().getMessage().equals("invalidToken")) {
                            prefManager.logout();

                            Intent intent = new Intent(DeliveryDocket_Activity.this, Login_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                LogCustom.logd(TAG, t.toString());

            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            showToast(getString(R.string.neverback));
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void homeDilog() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DeliveryDocket_Activity.this);
        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.wanttogohome));
        alertDialog.setCancelable(false);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(DeliveryDocket_Activity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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


    public void logOutDialog() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DeliveryDocket_Activity.this);

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

                Intent   intent =new Intent(DeliveryDocket_Activity.this,Login_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();*/
                showOdometerDialog(DeliveryDocket_Activity.this);
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
