package com.evolution.quicktrack.activity.jobmodule;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.evolution.quicktrack.BaseActivity;
import com.evolution.quicktrack.Login_Activity;
import com.evolution.quicktrack.MainActivity;
import com.evolution.quicktrack.QuickTrackAplication;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.pickupsignature.PickupSignatureContractor;
import com.evolution.quicktrack.pickupsignature.PickupSignaturePresenter;
import com.evolution.quicktrack.pickupsignature.PickupSignatureRequestEntity;
import com.evolution.quicktrack.response.jobDetails.JobDetailsData;
import com.evolution.quicktrack.response.login.LoginData;
import com.evolution.quicktrack.util.PrefManager;
import com.evolution.quicktrack.util.Utility;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Pickup_SignatureActivity extends BaseActivity implements PickupSignatureContractor.View {


    @BindView(R.id.img_home)
    ImageView imgHome;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_pickupAddres)
    TextView txtPickupAddres;
    @BindView(R.id.txt_deliveryAddres)
    TextView txtDeliveryAddres;

    @BindView(R.id.txt_consignee)
    TextView txtConsignee;
    @BindView(R.id.txt_driver)
    TextView txtDriver;
    @BindView(R.id.edit_consignee)
    EditText editConsignee;
    @BindView(R.id.sign_consignee)
    SignaturePad signConsignee;
    @BindView(R.id.liner_consignee)
    LinearLayout linerConsignee;
    @BindView(R.id.edit_drivername)
    EditText editDrivername;
    @BindView(R.id.sign_driver)
    SignaturePad signDriver;
    @BindView(R.id.liner_driver)
    LinearLayout linerDriver;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
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
    private KProgressHUD pd;
    public static String TAG = Login_Activity.class.getSimpleName();
    @BindView(R.id.btn_logout)
    ImageView btnLogout;
    String jobId = "";
    boolean consignSign = false;
    boolean driverSign = false;
    LoginData loginData;


    @BindView(R.id.photo_pickup_sign)
    LinearLayout imgCamera;
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


    @BindView(R.id.txt_collection_company)
    TextView txt_collection_company;

    @BindView(R.id.txt_delivery_company)
    TextView txt_delivery_company;


    ArrayList<Image> images = new ArrayList<>();
    private String type = "", subJobId = "";
    private String strIDs = "";

    @BindView(R.id.btnViewDetails)
    ImageView btnViewDetails;

    @BindView(R.id.txtJobID)
    TextView txtJobID;

    private PickupSignatureContractor.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        presenter = new PickupSignaturePresenter(this);

        type = getIntent().getExtras().getString("type", "");
        subJobId = getIntent().getExtras().getString("subJobId", "0");
        ButterKnife.bind(this);
        PrefManager prefManager = new PrefManager(getApplicationContext());
        loginData = prefManager.getUserData();

        if (getIntent().getExtras().containsKey("strIDs")) {
            strIDs = getIntent().getExtras().getString("strIDs");
            findViewById(R.id.ll_coll_add).setVisibility(View.GONE);
            findViewById(R.id.ll_comp_wight).setVisibility(View.GONE);
            findViewById(R.id.ll_qty).setVisibility(View.GONE);
            findViewById(R.id.ll_cubic_weight).setVisibility(View.GONE);
            findViewById(R.id.ll_distance).setVisibility(View.GONE);
            findViewById(R.id.ll_dimension).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.txt_compny)).setText("Job Ids:");
            txt_collection_company.setText(strIDs);
            findViewById(R.id.llDetails).setVisibility(View.GONE);
        }

        btnViewDetails.setOnClickListener(v -> {
            try {
                call_jobDetailsApi(jobId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void call_jobDetailsApi(String jobid) {
        showProgressDialogue();

        PickupSignatureRequestEntity pickupSignatureRequestEntity = new PickupSignatureRequestEntity();
        pickupSignatureRequestEntity.setApikey(Constants.API_KEY);
        pickupSignatureRequestEntity.setDriverid(prefManager.getId());
        pickupSignatureRequestEntity.setJobid(jobid);
        pickupSignatureRequestEntity.setLoginToken(prefManager.getLogin_Token());

        presenter.getJobDetails(pickupSignatureRequestEntity);

    }

    private void showPopup(JobDetailsData jobDetailsData) {
        // PopupWindow pw = null;
        try {
            final Dialog layout = new Dialog(this);
            layout.requestWindowFeature(0);
            layout.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            layout.setCancelable(false);
            layout.setContentView(R.layout.popup_jobdetails);
            // LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //   View layout = inflater.inflate(R.layout.popup_jobdetails, (ViewGroup) findViewById(R.id.pop_main_layout));
            TextView txt_CompanyName = layout.findViewById(R.id.pop_companyname);
            TextView txt_DeliveryType = layout.findViewById(R.id.pop_deliverytype);
            TextView txt_RequestedBy = layout.findViewById(R.id.pop_requestby);
            ImageView img_close = layout.findViewById(R.id.pop_close_img);
            TextView txt_CollactionLocation = layout.findViewById(R.id.pop_collationlocation);
            TextView txt_ContactPerson = layout.findViewById(R.id.pop_contactperson);
            TextView txt_Pickupfromdate = layout.findViewById(R.id.pop_pickupfromdate);
            TextView txt_DeliveryLocation = layout.findViewById(R.id.pop_deliverylocation);
            TextView txt_DeliveryDateTime = layout.findViewById(R.id.pop_deliverydatetime);
            TextView txt_Description = layout.findViewById(R.id.pop_description);
            TextView txt_Quantity = layout.findViewById(R.id.pop_quantity);
            TextView txt_Weight = layout.findViewById(R.id.pop_weight);
            TextView txt_CubicWeight = layout.findViewById(R.id.pop_cubicweght);
            TextView txt_Distance = layout.findViewById(R.id.pop_distance);
            TextView txt_Dimension = layout.findViewById(R.id.pop_dimentation);
            Button btn_close = layout.findViewById(R.id.pop_close);
            TextView pop_collation_contact_person = layout.findViewById(R.id.pop_collation_contact_person);
            TextView pop_collation_contact_number = layout.findViewById(R.id.pop_collation_contact_number);


            if (QuickTrackAplication.IsNotNull(jobDetailsData)) {
                if (QuickTrackAplication.IsNotNull(jobDetailsData.getCompanyName())) {
                    txt_CompanyName.setText(jobDetailsData.getCompanyName());
                }
                if (QuickTrackAplication.IsNotNull(jobDetailsData.getDeliveryType())) {
                    txt_DeliveryType.setText(jobDetailsData.getDeliveryType());
                }
                if (QuickTrackAplication.IsNotNull(jobDetailsData.getRequestname())) {
                    txt_RequestedBy.setText(jobDetailsData.getRequestname());
                }
                if (QuickTrackAplication.IsNotNull(jobDetailsData.getCollectionLocation())) {
                    txt_CollactionLocation.setText(jobDetailsData.getCollectionLocation());
                }
                if (QuickTrackAplication.IsNotNull(jobDetailsData.getContactPerson())) {
                    txt_ContactPerson.setText(jobDetailsData.getContactPerson());
                }
                if (QuickTrackAplication.IsNotNull(jobDetailsData.getPickupFromDateTime())) {
                    txt_Pickupfromdate.setText(jobDetailsData.getPickupFromDateTime());
                }
                if (QuickTrackAplication.IsNotNull(jobDetailsData.getDeliveryLocation())) {
                    txt_DeliveryLocation.setText(jobDetailsData.getDeliveryLocation());
                }
                if (QuickTrackAplication.IsNotNull(jobDetailsData.getDeliveryToDateTime())) {
                    txt_DeliveryDateTime.setText(jobDetailsData.getDeliveryToDateTime());
                }
                if (QuickTrackAplication.IsNotNull(jobDetailsData.getDescription())) {
                    txt_Description.setText(jobDetailsData.getDescription());
                }

                if (QuickTrackAplication.IsNotNull(jobDetailsData.getQuantity())) {
                    txt_Quantity.setText(jobDetailsData.getQuantity());
                }

                if (QuickTrackAplication.IsNotNull(jobDetailsData.getWeight())) {
                    txt_Weight.setText(jobDetailsData.getWeight());
                }

                if (QuickTrackAplication.IsNotNull(jobDetailsData.getCubicWeight())) {
                    txt_CubicWeight.setText(jobDetailsData.getCubicWeight());
                }

                if (QuickTrackAplication.IsNotNull(jobDetailsData.getDistance())) {
                    txt_Distance.setText(jobDetailsData.getDistance());
                }

                if (QuickTrackAplication.IsNotNull(jobDetailsData.getDimension())) {
                    txt_Dimension.setText(jobDetailsData.getDimension());
                }

                if (QuickTrackAplication.IsNotNull(jobDetailsData.getCollectionSubrb())) {
                    txt_Dimension.setText(jobDetailsData.getDimension());
                }
                if (QuickTrackAplication.IsNotNull(jobDetailsData.getCollectionCompanyname())) {
                    ((TextView) layout.findViewById(R.id.pop_collationcompany)).setText(jobDetailsData.getCollectionCompanyname());
                }

                if (QuickTrackAplication.IsNotNull(jobDetailsData.getDeliveryCompanyname())) {
                    ((TextView) layout.findViewById(R.id.pop_deliverycompany)).setText(jobDetailsData.getDeliveryCompanyname());
                }

                if (QuickTrackAplication.IsNotNull(jobDetailsData.getCollectionContactPerson())) {
                    pop_collation_contact_person.setText(jobDetailsData.getCollectionContactPerson());
                }

                if (QuickTrackAplication.IsNotNull(jobDetailsData.getCollectionContactNumner())) {
                    pop_collation_contact_number.setText(jobDetailsData.getCollectionContactNumner());

                    pop_collation_contact_number.setTextColor(Color.parseColor("#357799"));
                    pop_collation_contact_number.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            ((QuickTrackAplication) getApplicationContext()).openNumberPad(jobDetailsData.getCollectionContactNumner());
                        }
                    });


                }
            }

            btn_close.setOnClickListener(view -> layout.dismiss());
            img_close.setOnClickListener(v -> layout.dismiss());

            layout.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        initView();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_pickup_signature_slip;
    }


    public void initView() {

        //  btnLogout.setVisibility(View.INVISIBLE);
        txtTitle.setText("Pickup slip");


        signConsignee.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                consignSign = true;
            }

            @Override
            public void onSigned() {

            }

            @Override
            public void onClear() {

                consignSign = false;
            }
        });


        signDriver.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                driverSign = true;
            }

            @Override
            public void onSigned() {

            }

            @Override
            public void onClear() {


            }
        });

        getIntendata();

    }


    public void getIntendata() {
        if (getIntent().getStringExtra(Constants.EXTRA_JOBID) != null) {

            jobId = getIntent().getStringExtra(Constants.EXTRA_JOBID);
            jobId = getIntent().getStringExtra(Constants.EXTRA_JOBID);
            txtJobID.setText("Job ID: " + jobId);
        }
        callApi();

    }

    public void callApi() {
        try {
            if (strIDs.trim().length() == 0)
                call_jobdetailsApi();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.img_home, R.id.btn_logout})
    public void onViewClicked2(View view) {
        switch (view.getId()) {
            case R.id.img_home:
                // homeDilog();
                onBackPressed();
                break;
            case R.id.btn_logout:

                logOutDialog();
                break;


        }
    }


    @OnClick({R.id.txt_consignee, R.id.txt_driver, R.id.btn_resign, R.id.btn_save, R.id.btn_cancel, R.id.photo_pickup_sign, R.id.img_close1, R.id.img_close2, R.id.img_close3, R.id.img_close4, R.id.img_close5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_consignee:

                if (linerConsignee.getVisibility() == View.GONE) {
                    txtDriver.setBackgroundColor(getResources().getColor(R.color.black_60));
                    txtConsignee.setBackgroundColor(getResources().getColor(R.color.black));
                    linerConsignee.setVisibility(View.VISIBLE);
                    linerDriver.setVisibility(View.GONE);
                }


                break;
            case R.id.txt_driver:

                if (linerDriver.getVisibility() == View.GONE) {
                    txtDriver.setBackgroundColor(getResources().getColor(R.color.black));
                    txtConsignee.setBackgroundColor(getResources().getColor(R.color.black_60));

                    linerConsignee.setVisibility(View.GONE);
                    linerDriver.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_resign:
                signConsignee.clear();
                consignSign = false;
                break;

            case R.id.btn_save:
                if (validdata()) {
                    callSlip_Api();
                }
                break;
            case R.id.btn_cancel:
                onBackPressed();
                break;

            case R.id.photo_pickup_sign:
                if (images.size() != 5) {
                    getImage();
                } else {
                    showToast("Ohh..You can't add more then 5 images..!!");
                }
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


    public boolean validdata() {
        if (Utility.isValidString(editConsignee.getText().toString().trim())) {
            if (consignSign) {
                if (images.size() > 0) {
                    return true;
                } else {
                    LogCustom.Toast(Pickup_SignatureActivity.this, "Please add Image");

                    return false;
                }
            } else {
                LogCustom.Toast(Pickup_SignatureActivity.this, "Please enter consignee signature");
                return false;
            }
        } else {
            LogCustom.Toast(Pickup_SignatureActivity.this, "Please enter consignee name");
            return false;
        }
    }


    private void call_jobdetailsApi() {
        showProgressDialogue();

        PickupSignatureRequestEntity pickupSignatureRequestEntity = new PickupSignatureRequestEntity();
        pickupSignatureRequestEntity.setApikey(Constants.API_KEY);
        pickupSignatureRequestEntity.setDriverid(prefManager.getId());
        pickupSignatureRequestEntity.setJobid(jobId);
        pickupSignatureRequestEntity.setLoginToken(prefManager.getLogin_Token());

        presenter.getJobDetails(pickupSignatureRequestEntity);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        signConsignee.clear();
    }


    public File getFile(Bitmap bitmap) {
        File filesDir = getFilesDir();
        File imageFile = new File(filesDir, "sign" + ".jpg");
        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;

    }


    public void callSlip_Api() {

        File filecosign = getFile(signConsignee.getSignatureBitmap());

        RequestBody mFile = RequestBody.create(MediaType.parse("image/jpeg"), filecosign);
        MultipartBody.Part file_Consignee_sign = MultipartBody.Part.createFormData("pickupslip_consignee_sign[]", filecosign.getName(), mFile);

        List<MultipartBody.Part> pickup_item_pictureImages = new ArrayList<>();
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
            pickup_item_pictureImages.add(MultipartBody.Part.createFormData("pickup_item_picture[]", filename.getName(), surveyBody));
        }
        // RequestBody mFile2 = RequestBody.create(MediaType.parse("image/jpeg"), filedriver);
        //MultipartBody.Part fileToUpload2 = MultipartBody.Part.createFormData("pickupslip_driver_sign[]", filedriver.getName(), mFile2);


        PickupSignatureRequestEntity pickupSignatureRequestEntity = new PickupSignatureRequestEntity();
        pickupSignatureRequestEntity.setLoginToken(prefManager.getLogin_Token());
        pickupSignatureRequestEntity.setApikey(Constants.API_KEY);
        pickupSignatureRequestEntity.setPickupslip_driver_name(loginData.getDFirstName() + " " + loginData.getDLastName());
        pickupSignatureRequestEntity.setJobid(jobId);
        pickupSignatureRequestEntity.setDriverid(prefManager.getId());
        pickupSignatureRequestEntity.setPickupslip_consignee_name(editConsignee.getText().toString());
        pickupSignatureRequestEntity.setPickupslip_consignee_sign(file_Consignee_sign);
        pickupSignatureRequestEntity.setPickup_item_picture((ArrayList<MultipartBody.Part>) pickup_item_pictureImages);

        if (strIDs.trim().length() > 0) {
            pickupSignatureRequestEntity.setJobid(strIDs);
        }

        showProgressDialogue();
        presenter.sendSlipPickup(pickupSignatureRequestEntity);
    }
    public void homeDilog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Pickup_SignatureActivity.this);
        alertDialog.setMessage(getString(R.string.wanttogohome));
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
            dialog.dismiss();
            Intent intent = new Intent(Pickup_SignatureActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        alertDialog.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }


    public void logOutDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Pickup_SignatureActivity.this);
        alertDialog.setMessage("Are you sure want to logout ?");
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
            dialog.dismiss();
            showOdometerDialog(Pickup_SignatureActivity.this);
        });
        alertDialog.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            showToast("" + getString(R.string.neverback));
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> imagesd = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            if (imagesd.size() > 0) {
                for (Image image : imagesd) {
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

    @Override
    public void onJobDetailsSuccess(JobDetailsData jobDetailsResponse) {
        hideProgressDialogue();
        if (strIDs.trim().length() == 0) {
            txtDeliveryAddres.setText(jobDetailsResponse.getDeliveryLocation());
            txtPickupAddres.setText(jobDetailsResponse.getCollectionLocation());

            txtPickupAddres.setText(jobDetailsResponse.getCollectionLocation());


            txt_collection_company.setText(jobDetailsResponse.getCollectionCompanyname());
            txt_delivery_company.setText(jobDetailsResponse.getDeliveryCompanyname());

            if (!jobDetailsResponse.getWeight().equals(""))
                txtWeight.setText(jobDetailsResponse.getWeight());

            if (!jobDetailsResponse.getDimension().equals(""))
                txtDimention.setText(jobDetailsResponse.getDimension());

            if (!jobDetailsResponse.getDistance().equals(""))
                txtDistance.setText(jobDetailsResponse.getDistance());

            if (!jobDetailsResponse.getCubicWeight().equals(""))
                txtQubicweight.setText(jobDetailsResponse.getCubicWeight());

            if (!jobDetailsResponse.getQuantity().equals(""))
                txtQuntity.setText(jobDetailsResponse.getQuantity());

        } else {
            showPopup(jobDetailsResponse);
        }
    }

    @Override
    public void onJobDetailsError(String error) {
        hideProgressDialogue();
        if (error.equals("invalidToken")) {
            prefManager.logout();

            Intent intent = new Intent(Pickup_SignatureActivity.this, Login_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        showToast("Can't find job Details");
    }

    @Override
    public void onSlipPickupSuccess() {
        hideProgressDialogue();
        Intent intent = new Intent(Pickup_SignatureActivity.this, PickupDocket_Activity.class);
        intent.putExtra("subJobId", subJobId);
        intent.putExtra("type", type);
        intent.putExtra("strIDs", strIDs);
        intent.putExtra(Constants.EXTRA_JOBID, jobId);
        startActivity(intent);
    }

    @Override
    public void onSlipPickupError(String error) {
        hideProgressDialogue();
        if (error.equals("invalidToken")) {
            prefManager.logout();
            redirectToLogin();
        }
    }
}
