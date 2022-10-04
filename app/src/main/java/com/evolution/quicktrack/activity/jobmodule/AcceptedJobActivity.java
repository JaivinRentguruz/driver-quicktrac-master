package com.evolution.quicktrack.activity.jobmodule;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.evolution.quicktrack.BaseActivity;
import com.evolution.quicktrack.Login_Activity;
import com.evolution.quicktrack.MainActivity;
import com.evolution.quicktrack.QuickTrackAplication;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.acceptedjob.AcceptedJobContractor;
import com.evolution.quicktrack.acceptedjob.AcceptedJobEntity;
import com.evolution.quicktrack.acceptedjob.AcceptedJobPresenter;
import com.evolution.quicktrack.adapter.Custom_AcceptedJobList_Adepter;
import com.evolution.quicktrack.callback.Recycler_AcceptedJobClickCallBack;
import com.evolution.quicktrack.callback.Recycler_ItemClickCallBack;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.location.GetLocation;
import com.evolution.quicktrack.model.DialogueButtonsModel;
import com.evolution.quicktrack.model.JobModel;
import com.evolution.quicktrack.response.acceptedjobList.AcceptedJob;
import com.evolution.quicktrack.response.acceptedjobList.AcceptedJobListResponse;
import com.evolution.quicktrack.response.jobDetails.JobDetailsData;
import com.evolution.quicktrack.response.jobDetails.JobDetailsResponse;
import com.evolution.quicktrack.util.RecyclerViewEmptySupport;
import com.evolution.quicktrack.util.ShowCommonDialogu;
import com.evolution.quicktrack.util.Utils;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.zbra.androidlinq.Linq.stream;


public class AcceptedJobActivity extends BaseActivity implements Recycler_ItemClickCallBack, Recycler_AcceptedJobClickCallBack, AcceptedJobContractor.View {

    @BindView(R.id.recyclerview_manu)
    RecyclerViewEmptySupport recyclerview;
    @BindView(R.id.txt_nodata)
    TextView txtNodata;
    @BindView(R.id.emptyView)
    LinearLayout emptyView;
    private KProgressHUD pd;
    public static String TAG = Login_Activity.class.getSimpleName();
    @BindView(R.id.btn_logout)
    ImageView btnLogout;
    private GetLocation getLocation;
    @BindView(R.id.date_first)
    Button date_first;
    @BindView(R.id.date_second)
    Button date_second;
    @BindView(R.id.date_third)
    Button date_third;
    @BindView(R.id.old_jobs)
    Button old_jobs;
    @BindView(R.id.btn_subjob)
    Button btn_subjob;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    private CountDownTimer countDownTimer = null;
    String dateFlag = "1";
    private AcceptedJobContractor.Presenter presenter;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.date_first) {
                dateFlag = "1";
                date_second.setBackgroundColor(Color.WHITE);
                date_third.setBackgroundColor(Color.WHITE);
                date_first.setBackgroundColor(Color.BLACK);
                old_jobs.setBackgroundColor(Color.WHITE);

                date_second.setTextColor(Color.BLACK);
                date_third.setTextColor(Color.BLACK);
                date_first.setTextColor(Color.WHITE);
                old_jobs.setTextColor(Color.BLACK);

            } else if (view.getId() == R.id.date_second) {
                dateFlag = "2";
                date_second.setBackgroundColor(Color.BLACK);
                date_third.setBackgroundColor(Color.WHITE);
                date_first.setBackgroundColor(Color.WHITE);
                old_jobs.setBackgroundColor(Color.WHITE);

                date_second.setTextColor(Color.WHITE);
                date_third.setTextColor(Color.BLACK);
                date_first.setTextColor(Color.BLACK);
                old_jobs.setTextColor(Color.BLACK);

            } else if (view.getId() == R.id.date_third) {
                dateFlag = "3";
                date_second.setBackgroundColor(Color.WHITE);
                date_third.setBackgroundColor(Color.BLACK);
                date_first.setBackgroundColor(Color.WHITE);
                old_jobs.setBackgroundColor(Color.WHITE);

                date_second.setTextColor(Color.BLACK);
                date_third.setTextColor(Color.WHITE);
                date_first.setTextColor(Color.BLACK);
                old_jobs.setTextColor(Color.BLACK);

            } else {
                dateFlag = "0";
                old_jobs.setBackgroundColor(Color.BLACK);
                date_second.setBackgroundColor(Color.WHITE);
                date_third.setBackgroundColor(Color.WHITE);
                date_first.setBackgroundColor(Color.WHITE);

                date_second.setTextColor(Color.BLACK);
                date_third.setTextColor(Color.BLACK);
                date_first.setTextColor(Color.BLACK);
                old_jobs.setTextColor(Color.WHITE);
            }

            try {
                call_jobApi();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        presenter = new AcceptedJobPresenter(this);

        btnLogout.setVisibility(View.GONE);
        showPlusButton();
        button.setOnClickListener(v -> {
            DialogueButtonsModel dialogueButtonsModel = new DialogueButtonsModel();
            dialogueButtonsModel.setNoOFButtonVisibility(2);
            dialogueButtonsModel.setBtnFiveText("5");
            dialogueButtonsModel.setBtnFourText("4");
            dialogueButtonsModel.setBtnThreeText("3");
            dialogueButtonsModel.setBtnTwoText("Log Out");
            dialogueButtonsModel.setBtnOneText("Delivery To Depo");
            dialogueButtonsModel.setImgFive(R.drawable.ic_plus_menu);
            dialogueButtonsModel.setImgFour(R.drawable.ic_plus_menu);
            dialogueButtonsModel.setImgThree(R.drawable.ic_plus_menu);
            dialogueButtonsModel.setImgTwo(R.drawable.logout);
            dialogueButtonsModel.setImgOne(R.drawable.ic_delivery_to_depo);
            showCommonDialogue(dialog -> {
                switch (ShowCommonDialogu.btnClicked) {
                    case 1:
                        Intent intent = new Intent(AcceptedJobActivity.this, DeliverToFleetListActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        logOutDialog();
                        break;
                }
            }, dialogueButtonsModel);

        });

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");


        date_first.setText("Today \n" + simpleDateFormat.format(calendar.getTime()));
        calendar.add(Calendar.DATE, 1);
        date_second.setText("Tomorrow \n" + simpleDateFormat.format(calendar.getTime()));
        calendar.add(Calendar.DATE, 1);
        date_third.setText("Day after \n Tomorrow");

        date_first.setOnClickListener(onClickListener);
        date_second.setOnClickListener(onClickListener);
        date_third.setOnClickListener(onClickListener);
        old_jobs.setOnClickListener(onClickListener);


        initView();
        callApi();
        swipeContainer.setOnRefreshListener(() -> callApi());
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_accepted_job;
    }


    public void initView() {
        this.recyclerview.setHasFixedSize(true);
        this.recyclerview.setLayoutManager(new LinearLayoutManager(AcceptedJobActivity.this, LinearLayoutManager.VERTICAL, false));
        this.recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setEmptyView(emptyView);
        getLocation = new GetLocation(AcceptedJobActivity.this);
    }

    public void callApi() {
        if (Utils.validInternet(getApplicationContext()))
            call_jobApi();

        startTimer();
    }

    public void startTimer() {
        countDownTimer = new CountDownTimer(200000, 60000) {
            @Override
            public void onTick(long l) {
                try {
                    call_jobApi();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {

            }
        };

        countDownTimer.start();

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(jobAcceptedORDeclindReceiver, new IntentFilter("jobAcceptedORDeclindReceiver"));
    }

    BroadcastReceiver jobAcceptedORDeclindReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            call_jobApi();
        }
    };


    private List<AcceptedJob> responseData = new ArrayList<>();


    private void call_jobApi() {
        showProgressDialogue();
        AcceptedJobEntity acceptedJobEntity = new AcceptedJobEntity();
        acceptedJobEntity.setLogin_token(prefManager.getLogin_Token());
        acceptedJobEntity.setApikey(Constants.API_KEY);
        acceptedJobEntity.setVehicleid(prefManager.getTruckId());
        acceptedJobEntity.setStatus(Constants.Accepted + "," + Constants.Started + "," + Constants.PickUp + "," + Constants.OnRouteToPickup + "," + Constants.AtPickupLocation
                + "," + Constants.OnRouteToDelivery + "," + Constants.AtDeliveryLocation);
        acceptedJobEntity.setDriverid(prefManager.getId());
        acceptedJobEntity.setDateFlag(dateFlag);
        presenter.fetchAcceptedJobs(acceptedJobEntity);
    }


    public void setAddapter() {
        Custom_AcceptedJobList_Adepter custom_manu_adepter = new Custom_AcceptedJobList_Adepter(AcceptedJobActivity.this, jobModelList, AcceptedJobActivity.this);
        recyclerview.setAdapter(custom_manu_adepter);
    }

    @Override
    public void onBackPressed() {
        startHomeAsFresh();
    }

    private void startHomeAsFresh() {
        Intent intent2 = new Intent(AcceptedJobActivity.this, MainActivity.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent2);
        finish();
    }

    @OnClick({R.id.img_home, R.id.btn_logout, R.id.img_refresh, R.id.btn_subjob})
    public void onViewClicked2(View view) {
        switch (view.getId()) {
            case R.id.img_home:
                startHomeAsFresh();
                break;
            case R.id.btn_logout:
                logOutDialog();
                break;
            case R.id.img_refresh:

                callApi();

                break;

            case R.id.btn_subjob:
                Intent intent = new Intent(AcceptedJobActivity.this, DeliverToFleetListActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void homeDilog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AcceptedJobActivity.this);
        alertDialog.setMessage(getString(R.string.wanttogohome));
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
            dialog.dismiss();
            Intent intent = new Intent(AcceptedJobActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        alertDialog.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    @Override
    public void OnItemClick(int position) {
        call_jobDetailsApi(String.valueOf(jobModelList.get(position).getId()));
    }

    @Override
    public void OnStartJobItemClick(int position) {
        if (getLocation.getcurrntLataLong() != null) {
            if (jobModelList.get(position).isIspickup()) {
                call_updatestaus(String.valueOf(jobModelList.get(position).getId()), jobModelList.get(position), Constants.Started, String.valueOf(getLocation.getcurrntLataLong().getLatitude()), String.valueOf(getLocation.getcurrntLataLong().getLongitude()), "pickup", jobModelList.get(position).getType(), jobModelList.get(position).getSubjobId() + "");
            } else {
                call_updatestaus(String.valueOf(jobModelList.get(position).getId()), jobModelList.get(position), Constants.OnRouteToDelivery, String.valueOf(getLocation.getcurrntLataLong().getLatitude()), String.valueOf(getLocation.getcurrntLataLong().getLongitude()), "deliver", jobModelList.get(position).getType(), jobModelList.get(position).getSubjobId() + "");
            }
        } else {
            getLocation = new GetLocation(AcceptedJobActivity.this);
        }
    }

    private String pickupordeliver;

    private void call_updatestaus(String jobid, JobModel jobModel, String statusid, String latitude, String longitude, String pickupordeliver, String type, String subjobid) {
        this.pickupordeliver = pickupordeliver;
        showProgressDialogue();
        AcceptedJobEntity  acceptedJobEntity = new AcceptedJobEntity();
        acceptedJobEntity.setDateFlag(dateFlag);
        acceptedJobEntity.setDriverid(prefManager.getId());
        acceptedJobEntity.setStatus(statusid);
        acceptedJobEntity.setVehicleid(prefManager.getTruckId());
        acceptedJobEntity.setLogin_token(prefManager.getLogin_Token());
        acceptedJobEntity.setJobid(jobid);
        acceptedJobEntity.setLatitude(latitude);
        acceptedJobEntity.setLongitude(longitude);
        acceptedJobEntity.setType(type);
        acceptedJobEntity.setSubjobid(subjobid);
        acceptedJobEntity.setApikey(Constants.API_KEY);

        presenter.updateJob(acceptedJobEntity);
    }

    private List<JobModel> jobModelList = new ArrayList<>();

    public void setDataOrder() {
        jobModelList.clear();
        List<JobModel> jobModels = new ArrayList<>();
        LatLng loc1 = null;
        if (getLocation != null && getLocation.getcurrntLataLong() != null)
            loc1 = new LatLng(getLocation.getcurrntLataLong().getLatitude(), getLocation.getcurrntLataLong().getLongitude());
        for (int i = 0; i < responseData.size(); i++) {

            JobModel jobModelpickup = new JobModel();
            JobModel jobModeldelivery = new JobModel();

            jobModeldelivery.setDeliveryCompany(responseData.get(i).getDeliveryCompanyname());
            jobModelpickup.setCollectionCompany(responseData.get(i).getCollectionCompanyname());


            if (responseData.get(i).getStatus().equals(Constants.Accepted) || responseData.get(i).getStatus().equals(Constants.Started)
                    || responseData.get(i).getStatus().equals(Constants.OnRouteToPickup) || responseData.get(i).getStatus().equals(Constants.AtPickupLocation)) {

                jobModelpickup.setId(Long.parseLong(responseData.get(i).getId()));
                jobModelpickup.setTxtClientName(responseData.get(i).getCustomerName());
                jobModelpickup.setTxtDeliveryAddres(responseData.get(i).getDeliveryLocation());
                jobModelpickup.setTxtPickupAddres(responseData.get(i).getCollectionLocation());
                jobModelpickup.setDiscription(responseData.get(i).getDescription());
                jobModelpickup.setIspickup(true);
                jobModelpickup.setDeliveryName(responseData.get(i).getDeliveryName());
                jobModelpickup.setSorting("PickUp");
                jobModelpickup.setStatus(responseData.get(i).getStatus());
                jobModelpickup.setType(responseData.get(i).getType());
                jobModelpickup.setSubjobId(responseData.get(i).getSubjobid());
                if (loc1 != null) {
                    LatLng loc2 = new LatLng(Double.parseDouble(responseData.get(i).getCollectionLatitude()), Double.parseDouble(responseData.get(i).getCollectionLongitude()));
                    jobModelpickup.setDistance(SphericalUtil.computeDistanceBetween(loc1, loc2));
                }
                jobModelpickup.setEnable(true);
                jobModeldelivery.setEnable(false);
                jobModels.add(jobModelpickup);
            } else {
                jobModeldelivery.setEnable(true);
            }
            jobModeldelivery.setId(Long.parseLong(responseData.get(i).getId()));
            jobModeldelivery.setTxtClientName(responseData.get(i).getCustomerName());
            jobModeldelivery.setTxtDeliveryAddres(responseData.get(i).getDeliveryLocation());
            jobModeldelivery.setTxtPickupAddres(responseData.get(i).getCollectionLocation());
            jobModeldelivery.setDiscription(responseData.get(i).getDescription());
            jobModeldelivery.setDeliveryName(responseData.get(i).getDeliveryName());
            jobModeldelivery.setIspickup(false);
            jobModeldelivery.setSorting("Drop");
            jobModeldelivery.setStatus(responseData.get(i).getStatus());
            jobModeldelivery.setType(responseData.get(i).getType());
            jobModeldelivery.setSubjobId(responseData.get(i).getSubjobid());
            if (loc1 != null) {
                LatLng loc2 = new LatLng(Double.parseDouble(responseData.get(i).getDeliveryLatitude()), Double.parseDouble(responseData.get(i).getDeliveryLongitude()));
                jobModeldelivery.setDistance(SphericalUtil.computeDistanceBetween(loc1, loc2));
            }
            jobModels.add(jobModeldelivery);
        }
        List<JobModel> MainJobModelList = new ArrayList<>();
        List<JobModel> tempList = new ArrayList<>();
        try {
            tempList = stream(jobModels).orderBy(j -> j.getDistance()).toList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<JobModel> SkipList = new ArrayList<>();
        for (int i = 0; i < tempList.size(); i++) {
            if (tempList.get(i).getSorting().equals("Drop")) {
                if (MainJobModelList.size() > 0) {
                    if (tempList.get(i).isEnable()) {
                        MainJobModelList.add(tempList.get(i));
                    } else {
                        final long id = tempList.get(i).getId();
                        JobModel jobModel = stream(MainJobModelList).where(j -> j.getId() == id && j.getSorting().equals("PickUp")).firstOrNull();
                        if (jobModel != null) {
                            MainJobModelList.add(tempList.get(i));
                        } else {
                            SkipList.add(tempList.get(i));
                        }
                    }
                } else {
                    if (tempList.get(i).isEnable()) {
                        MainJobModelList.add(tempList.get(i));
                    } else {

                        SkipList.add(tempList.get(i));
                    }
                }
            } else {
                if (MainJobModelList.size() > 0) {

                    if (SkipList.size() > 0) {
                        List<Integer> out = new ArrayList<>();
                        int count = 0;
                        for (JobModel skipmodel : SkipList) {
                            final long id = skipmodel.getId();
                            JobModel jobModel = stream(MainJobModelList).where(j -> j.getId() == id && j.getSorting().equals("PickUp")).firstOrNull();
                            if (jobModel != null) {
                                MainJobModelList.add(skipmodel);
                                out.add(count);
                            }
                            count++;
                        }
                        for (int temp : out) {
                            SkipList.remove(temp);
                        }
                    }
                    MainJobModelList.add(tempList.get(i));
                } else {
                    MainJobModelList.add(tempList.get(i));
                }
            }
        }

        if (SkipList.size() > 0) {
            for (JobModel jb : SkipList) {
                JobModel jobModel = stream(MainJobModelList).where(j -> j.getId() == jb.getId() && j.getSorting().equals("Drop")).firstOrNull();
                if (jobModel == null) {
                    MainJobModelList.add(jb);
                }
            }
        }
        jobModelList = MainJobModelList;
        setAddapter();
    }


    private void showPopup(JobDetailsData jobDetailsData) {
        try {
            final Dialog layout = new Dialog(this);
            layout.requestWindowFeature(0);
            layout.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            layout.setCancelable(false);
            layout.setContentView(R.layout.popup_jobdetails);
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

                            ((QuickTrackAplication) getApplicationContext()).Call(
                                    AcceptedJobActivity.this,
                                    jobDetailsData.getDeliveryContactNumner());
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

    private void call_jobDetailsApi(String jobid) {
        showProgressDialogue();
        AcceptedJobEntity acceptedJobEntity = new AcceptedJobEntity();
        acceptedJobEntity.setLogin_token(prefManager.getLogin_Token());
        acceptedJobEntity.setApikey(Constants.API_KEY);
        acceptedJobEntity.setJobid(jobid);
        acceptedJobEntity.setDriverid(prefManager.getId());
        presenter.fetchDetails(acceptedJobEntity);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }


    public void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.onFinish();
            countDownTimer.cancel();

        }
    }

    @Override
    public void onSuccessAccepted(AcceptedJobListResponse acceptedJobListResponse) {
        hideProgressDialogue();
        swipeContainer.setRefreshing(false);
        responseData = acceptedJobListResponse.getData();
        setDataOrder();
    }

    @Override
    public void onErrorAccepted(String error) {
        hideProgressDialogue();
        swipeContainer.setRefreshing(false);
        responseData.clear();
        if (error.equals("invalidToken")) {
            prefManager.logout();
            Intent intent = new Intent(AcceptedJobActivity.this, Login_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        }
        setAddapter();
    }

    @Override
    public void onSuccessUpdateJob(AcceptedJobEntity commonResponse) {
        hideProgressDialogue();
        if (pickupordeliver.equals("deliver")) {
            Constants.JOBID = commonResponse.getJobid();
            prefManager.setKEYJobId(commonResponse.getJobid());
            prefManager.setKEYJobstatus(Constants.OnRouteToDelivery);
            Intent intent = new Intent(AcceptedJobActivity.this, DeliveryLocationActivity.class);
            intent.putExtra("type", commonResponse.getType());
            intent.putExtra("subjobid", commonResponse.getSubjobid());
            intent.putExtra(Constants.EXTRA_JOBID, commonResponse.getJobid());
            ArrayList responseDataTemp = (ArrayList) responseData;
            intent.putParcelableArrayListExtra(Constants.EXTRA_JOB_DATA, responseDataTemp);
            startActivity(intent);
        } else if (pickupordeliver.equals("pickup")) {
            if (getLocation.getcurrntLataLong() != null) {
                call_updatestaus(commonResponse.getJobid(), commonResponse.getJobModel(), Constants.OnRouteToPickup, String.valueOf(getLocation.getcurrntLataLong().getLatitude()), String.valueOf(getLocation.getcurrntLataLong().getLongitude()), "onroutepickup", commonResponse.getType(), commonResponse.getSubjobid());

            } else {
                getLocation = new GetLocation(AcceptedJobActivity.this);
                call_updatestaus(commonResponse.getJobid(), commonResponse.getJobModel(),
                        Constants.OnRouteToPickup, String.valueOf(getLocation.getcurrntLataLong().getLatitude()),
                        String.valueOf(getLocation.getcurrntLataLong().getLongitude()), "onroutepickup", commonResponse.getJobid(), commonResponse.getSubjobid());
            }

        } else if (pickupordeliver.equals("onroutepickup")) {
            prefManager.setKEYJobId(commonResponse.getJobid());
            prefManager.setKEYJobstatus(Constants.OnRouteToPickup);
            Intent intent = new Intent(AcceptedJobActivity.this, PickupLocationActivity.class);
            intent.putExtra(Constants.EXTRA_JOBID, commonResponse.getJobid());
            intent.putExtra(Constants.EXTRA_SUBJOBID, String.valueOf(commonResponse.getSubjobid()));
            intent.putExtra(Constants.EXTRA_TYPE, commonResponse.getType());
            ArrayList responseDataTemp = (ArrayList) responseData;
            intent.putParcelableArrayListExtra(Constants.EXTRA_JOB_DATA, responseDataTemp);
            startActivity(intent);
        }
        call_jobApi();
    }

    @Override
    public void onErrorUpdateJob(String error) {
        hideProgressDialogue();
        if (error.equals("invalidToken")) {
            callApi();
            prefManager.logout();
            Intent intent = new Intent(AcceptedJobActivity.this, Login_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void fetchDetailsSuccess(JobDetailsResponse jobDetailsResponse) {
        hideProgressDialogue();
        JobDetailsData jobDetailsDatas = jobDetailsResponse.getData();
        if (jobDetailsDatas != null) {
            showPopup(jobDetailsDatas);
        }
    }

    @Override
    public void fetchDetailsError(String error) {
        hideProgressDialogue();
        if (error.equals("invalidToken")) {
            prefManager.logout();
            Intent intent = new Intent(AcceptedJobActivity.this, Login_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        Toast.makeText(AcceptedJobActivity.this, "Can't find job Details", Toast.LENGTH_SHORT).show();
    }
}