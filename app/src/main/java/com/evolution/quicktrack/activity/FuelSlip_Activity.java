package com.evolution.quicktrack.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.evolution.quicktrack.BaseActivity;
import com.evolution.quicktrack.Login_Activity;
import com.evolution.quicktrack.MainActivity;
import com.evolution.quicktrack.QuickTrackAplication;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.network.ApiCallInterface;
import com.evolution.quicktrack.network.ApiClient;
import com.evolution.quicktrack.response.washtruckAndRefule.WashTruckResponse;
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

public class FuelSlip_Activity extends BaseActivity {

    @BindView(R.id.edit_odometer)
    EditText edit_odometer;

    @BindView(R.id.edit_quantity)
    EditText edit_quantity;

    @BindView(R.id.edit_price)
    EditText edit_price;

    @BindView(R.id.img_home)
    ImageView imgHome;
    @BindView(R.id.btn_logout)
    ImageView btnLogout;
    @BindView(R.id.btn_tackphoto)
    Button btn_tackphoto;

    @BindView(R.id.img_1)
    ImageView img1;
    @BindView(R.id.img_2)
    ImageView img2;
    @BindView(R.id.img_3)
    ImageView img3;

    @BindView(R.id.img_close1)
    ImageView img_close1;
    @BindView(R.id.img_close2)
    ImageView img_close2;
    @BindView(R.id.img_close3)
    ImageView img_close3;
    @BindView(R.id.rel_img1)
    RelativeLayout relImg1;
    @BindView(R.id.rel_img2)
    RelativeLayout relImg2;
    @BindView(R.id.rel_img3)
    RelativeLayout relImg3;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.img_4)
    ImageView img4;
    @BindView(R.id.rel_img4)
    RelativeLayout relImg4;
    @BindView(R.id.img_5)
    ImageView img5;
    @BindView(R.id.img_close5)
    ImageView imgClose5;
    @BindView(R.id.rel_img5)
    RelativeLayout relImg5;


    private KProgressHUD pd;

    String startDate,endDate;

    ArrayList<Image> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        Intent intent  = getIntent();


        startDate = intent.getStringExtra("startDate");
        endDate = intent.getStringExtra("endDate");
        edit_odometer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                edit_odometer.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edit_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                edit_price.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edit_quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                edit_quantity.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_fual_slip;
    }


    @OnClick({R.id.btn_submit, R.id.btn_tackphoto})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btn_submit:

                if(validation()) {
                    if (Utils.validInternet(getApplicationContext())) {

                        try {

                            call_RefuleTruckApi(edit_odometer.getText().toString(), edit_price.getText().toString(), edit_quantity.getText().toString(), images);
                        }
                        catch (Exception e)
                        {
                            e.getMessage();
                            showToast("Try again"+e.getMessage());
                            if (pd != null && pd.isShowing())
                                pd.dismiss();

                        }

                    }
                }

                break;
            case R.id.btn_tackphoto:

                getImage();

                break;
        }
    }

    @OnClick({R.id.img_home, R.id.btn_logout, R.id.img_1, R.id.img_2, R.id.img_3, R.id.img_close1, R.id.img_close2,
            R.id.img_close3,R.id.img_close4,R.id.img_close5})
    public void onViewClicked2(View view) {
        switch (view.getId()) {
            case R.id.img_home:
                onBackPressed();
                //homeDilog();

                break;
            case R.id.btn_logout:
                logOutDialog();

                break;

            case R.id.img_1:

                break;
            case R.id.img_2:

                break;
            case R.id.img_3:

                break;
            case R.id.img_close1:
                if (images.size() > 0) {
                    images.remove(0);
                    setImage();
                }

                break;
            case R.id.img_close2:
                if (images.size() > 1) {
                    images.remove(1);
                    setImage();
                }
                break;
            case R.id.img_close3:
                if (images.size() > 2) {
                    images.remove(2);
                    setImage();
                }



                break;


            case R.id.img_close4:
                if (images.size() > 3) {
                    images.remove(3);
                    setImage();
                }



                break;

            case R.id.img_close5:
                if (images.size() > 4) {
                    images.remove(4);
                    setImage();
                }



                break;


        }
    }


    public  void homeDilog(){

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(FuelSlip_Activity.this);
        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.wanttogohome));
        alertDialog.setCancelable(false);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(FuelSlip_Activity.this, MainActivity.class);
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

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(FuelSlip_Activity.this);

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure want to logout ?");
        alertDialog.setCancelable(true);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
       /*         trackDriver.call_trackDriverAPi(Constants.BACK_LOGOUT);


                PrefManager prefManager=new PrefManager(getApplicationContext());
                prefManager.logout();

                Intent   intent =new Intent(FuelSlip_Activity.this,Login_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
*/         showOdometerDialog(FuelSlip_Activity.this);
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                showToast(getString(R.string.not_allow_back));
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> imagesd = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            // do your logic here...
           // images = imagesd;

            if(imagesd.size() > 0)
            {
                for(Image image : imagesd)
                {
                    images.add(image);
                }
            }
            setImage();


        }
        super.onActivityResult(requestCode, resultCode, data);  // THIS METHOD SHOULD BE HERE so that ImagePicker works with fragment
    }


    public void setImage() {

        if (images.size() > 0) {
            Load_Image(images.get(0).getPath(), img1);
            relImg1.setVisibility(View.VISIBLE);

        } else {
            relImg1.setVisibility(View.GONE);
        }

        if (images.size() > 1) {

            Load_Image(images.get(1).getPath(), img2);
            relImg2.setVisibility(View.VISIBLE);
        } else {
            relImg2.setVisibility(View.GONE);
        }
        if (images.size() > 2) {

            Load_Image(images.get(2).getPath(), img3);
            relImg3.setVisibility(View.VISIBLE);
        } else {
            relImg3.setVisibility(View.GONE);
        }


        if (images.size() > 3) {

            Load_Image(images.get(3).getPath(), img4);
            relImg4.setVisibility(View.VISIBLE);
        } else {
            relImg4.setVisibility(View.GONE);
        }
        if (images.size() > 4) {

            Load_Image(images.get(4).getPath(), img5);
            relImg5.setVisibility(View.VISIBLE);
        } else {
            relImg5.setVisibility(View.GONE);
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
                .setMaxSize(5)                     //  Max images can be selected
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




    public boolean validation()
    {
        boolean isValid = true;

        if(!QuickTrackAplication.IsNotNull(edit_quantity.getText().toString()))
        {
            isValid = false;
            edit_quantity.setError("This field is required");
        }
        else
        {
            edit_quantity.setError(null);
        }

        if(!QuickTrackAplication.IsNotNull(edit_price.getText().toString()))
        {
            isValid = false;
            edit_price.setError("This field is required");
        }
        else
        {
            edit_price.setError(null);
        }

        if(!QuickTrackAplication.IsNotNull(edit_odometer.getText().toString()))
        {
            isValid = false;
            edit_odometer.setError("This field is required");
        }
        else
        {
            edit_odometer.setError(null);
        }

        if(images.size()== 0){

            isValid = false;
            LogCustom.Toast(FuelSlip_Activity.this,"Please select images.");
        }



        return isValid;
    }

    private void call_RefuleTruckApi(String sodometer, String sprice, String squantity, List<Image> imageList) throws Exception {

        PrefManager prefManager=new PrefManager(getApplicationContext());

        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);
        pd = LogCustom.loderProgressDialog2(this);
        pd.show();

        RequestBody token = RequestBody.create(MediaType.parse("multipart/form-data"),prefManager.getLogin_Token());
        RequestBody APIKEY = RequestBody.create(MediaType.parse("multipart/form-data"), Constants.API_KEY);
        RequestBody truckid = RequestBody.create(MediaType.parse("multipart/form-data"), prefManager.getTruckId());
        RequestBody driver_id = RequestBody.create(MediaType.parse("multipart/form-data"), prefManager.getId());
        RequestBody startdatetime = RequestBody.create(MediaType.parse("multipart/form-data"), startDate);
        RequestBody enddatetime = RequestBody.create(MediaType.parse("multipart/form-data"), endDate);
        RequestBody types = RequestBody.create(MediaType.parse("multipart/form-data"), "Refuel");
        RequestBody quantity = RequestBody.create(MediaType.parse("multipart/form-data"),squantity );
        RequestBody odometer = RequestBody.create(MediaType.parse("multipart/form-data"),sodometer );
        RequestBody price = RequestBody.create(MediaType.parse("multipart/form-data"),sprice );



        List<MultipartBody.Part> surveyImagesParts = new ArrayList<>();
        for (int i = 0; i < imageList.size(); i++) {

            File file2 = new File(imageList.get(i).getPath());


            Bitmap bitmapImage = BitmapFactory.decodeFile(file2.getAbsolutePath());
            int nh = (int) ( bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, Constants.SIZE_WIDTH, Constants.SIZE_HEIGHT, true);

            File filename = new File(getFilesDir(), Calendar.getInstance().getTimeInMillis()+".jpg");

            try (FileOutputStream out = new FileOutputStream(filename)) {
                scaled.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            } catch (IOException e) {
                e.printStackTrace();
            }

            int file_size = Integer.parseInt(String.valueOf(filename.length()/1024));
            Log.w("File Size ",file_size+"");


            RequestBody surveyBody = RequestBody.create(MediaType.parse("image/jpeg"), filename);
            surveyImagesParts.add( MultipartBody.Part.createFormData("files[]", filename.getName(), surveyBody));
        }

        Call<WashTruckResponse> call = apiService.RefuleTruck(token,APIKEY,truckid,driver_id,startdatetime,enddatetime,types,quantity,odometer,price,surveyImagesParts);
        call.enqueue(new Callback<WashTruckResponse>() {
            @Override
            public void onResponse(Call<WashTruckResponse> call, Response<WashTruckResponse> response) {

               // Log.d(TAG, " : check status : " + response.body());
                if (pd != null && pd.isShowing())
                    pd.dismiss();
                if (response.body().getStatus().equals(Constants.API_STATUS)) {
                 //   Log.d(TAG, " : check status : " + response.body().toString());
                    trackDriver.call_trackDriverAPi(Constants.BACK_REFULE);
                    Intent intent = new Intent(FuelSlip_Activity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    LogCustom.Toast(FuelSlip_Activity.this, response.body().getMessage());

                } else {
                    if(response.body().getMessage().equals("invalidToken"))
                    {
                        prefManager.logout();

                        Intent   intent =new Intent(FuelSlip_Activity.this,Login_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }

                    LogCustom.Toast(FuelSlip_Activity.this, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<WashTruckResponse> call, Throwable t) {
              //  LogCustom.logd(TAG, t.toString());
                if (pd != null && pd.isShowing())
                    pd.dismiss();
            }
        });
    }



}
