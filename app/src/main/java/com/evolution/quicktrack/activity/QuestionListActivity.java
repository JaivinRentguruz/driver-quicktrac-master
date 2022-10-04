package com.evolution.quicktrack.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evolution.quicktrack.BaseActivity;
import com.evolution.quicktrack.Login_Activity;
import com.evolution.quicktrack.MainActivity;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.adapter.Question_Adapter;
import com.evolution.quicktrack.callback.QuestionClickCallBack;
import com.evolution.quicktrack.callback.Recycler_ItemClickCallBack;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.model.QuestionModel;
import com.evolution.quicktrack.questionslist.QuestionListSubmitRequestEntity;
import com.evolution.quicktrack.questionslist.QuestionsListContractor;
import com.evolution.quicktrack.questionslist.QuestionsListPresenter;
import com.evolution.quicktrack.questionslist.QuestionsListRequestEntity;
import com.evolution.quicktrack.response.common.CommonResponse;
import com.evolution.quicktrack.response.qustionlist.QuestionDataItem;
import com.evolution.quicktrack.response.qustionlist.QuestionListResponse;
import com.evolution.quicktrack.util.PrefManager;
import com.evolution.quicktrack.util.Utils;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuestionListActivity extends BaseActivity implements Recycler_ItemClickCallBack, QuestionClickCallBack, QuestionsListContractor.View {

    @BindView(R.id.recyclerview_manu)
    RecyclerView recyclerviewManu;
    @BindView(R.id.btn_submit)
    Button btn_submit;

    public static String TAG = QuestionListActivity.class.getSimpleName();
    Question_Adapter custom_manu_adepter;

    List<QuestionDataItem> responseData = new ArrayList<>();


    int selectedId = 0;
    String truckId = "";

    private QuestionsListContractor.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionlist);
        ButterKnife.bind(this);
        presenter = new QuestionsListPresenter(this);

        initView();
        getIntentData();
    }


    public void getIntentData() {
        if (getIntent().getStringExtra(Constants.EXTA_TRUCKID) != null) {
            truckId = getIntent().getStringExtra(Constants.EXTA_TRUCKID);
        }
        callApi();
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }


    public void initView() {
        this.recyclerviewManu.setHasFixedSize(true);
        this.recyclerviewManu.setLayoutManager(new LinearLayoutManager(QuestionListActivity.this, LinearLayoutManager.VERTICAL, false));
        this.recyclerviewManu.setNestedScrollingEnabled(false);
    }


    public void callApi() {
        if (Utils.validInternet(getApplicationContext())) {
            try {
                call_getQuestion_APi();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @OnClick({R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:

                if (btn_submit.getText().toString().toLowerCase().equals("next")) {
                    Intent intent = new Intent(QuestionListActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    if (valid()) {
                        sendJsonData23();
                    } else {
                        LogCustom.Toast(QuestionListActivity.this, "Please enter all field");

                    }
                }
        }
    }


    private void call_getQuestion_APi() throws Exception {

        showProgressDialogue();

        QuestionsListRequestEntity questionsListRequestEntity = new QuestionsListRequestEntity();
        questionsListRequestEntity.setApikey(Constants.API_KEY);
        questionsListRequestEntity.setDriverid(prefManager.getId());
        questionsListRequestEntity.setTruckid(truckId);
        questionsListRequestEntity.setLogin_token(prefManager.getLogin_Token());
        presenter.checkList(questionsListRequestEntity);
    }


    public void setRecyclerAdapter() {
        custom_manu_adepter = new Question_Adapter(QuestionListActivity.this, responseData, QuestionListActivity.this);
        recyclerviewManu.setAdapter(custom_manu_adepter);
    }


    @Override
    public void OnItemClick(int position) {
        selectedId = position;
    }

    @Override
    public void rightClick(int position, boolean isSelected) {
        selectedId = position;

        responseData.get(position).setNA(false);
        responseData.get(position).setFail(false);
        responseData.get(position).setRight(isSelected);
        responseData.get(position).setIsSelected(Constants.isRight);
        responseData.get(position).setComment("");

        custom_manu_adepter.Update();

    }

    @Override
    public void commentClick(int position, String comment) {
        selectedId = position;
        responseData.get(position).setComment(comment);


    }


    @Override
    public void failClick(int position, boolean isSelected) {
        selectedId = position;
        responseData.get(position).setNA(false);
        responseData.get(position).setFail(isSelected);
        responseData.get(position).setIsSelected(Constants.isFail);
        responseData.get(position).setRight(false);


    }

    @Override
    public void naClick(int position, boolean isSelected) {
        selectedId = position;

        responseData.get(position).setNA(isSelected);
        responseData.get(position).setFail(false);
        responseData.get(position).setRight(false);
        responseData.get(position).setIsSelected(Constants.isNa);


    }

    @Override
    public void imageClick(int position) {
        selectedId = position;
        getImage();

    }

    @Override
    public void imageremoveClick(int position, int no) {
        selectedId = position;

        if (responseData.get(position).getImages().size() > 1) {
            responseData.get(position).getImages().remove(no);
        } else {
            responseData.get(position).getImages().remove(0);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            // do your logic here...

            responseData.get(selectedId).setImages(images);
            custom_manu_adepter.notifyDataSetChanged();

        }
        super.onActivityResult(requestCode, resultCode, data);  // THIS METHOD SHOULD BE HERE so that ImagePicker works with fragment
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
                .setMaxSize(2)                     //  Max images can be selected
                .setSavePath("ImagePicker")         //  Image capture folder name
                .setSelectedImages(responseData.get(selectedId).getImages())          //  Selected images
                .setKeepScreenOn(true)              //  Keep screen on when selecting images
                .start();

    }


    @OnClick({R.id.img_home, R.id.btn_logout})
    public void onViewClicked2(View view) {
        switch (view.getId()) {
            case R.id.img_home:
                Log.d("xxxxx", "xxx");

                Intent intent = new Intent(QuestionListActivity.this, MainActivity.class);
                startActivity(intent);

                break;
            case R.id.btn_logout:

                onBackPressed();

                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            showToast(getString(R.string.not_allow_back));
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }


    public boolean valid() {
        boolean vald = true;
        for (int i = 0; i < responseData.size(); i++) {
            if (!responseData.get(i).getIsSelected().equals("")) {
                if (responseData.get(i).getIsSelected().equals(Constants.isNa) && responseData.get(i).getComment().equals("")) {
                    vald = false;
                    break;
                } else if (responseData.get(i).getIsSelected().equals(Constants.isFail) && responseData.get(i).getComment().equals("")) {

                    vald = false;
                    break;
                }
            } else {
                vald = false;
                break;
            }
        }
        return vald;
    }


    public void sendJsonData23() {
        PrefManager prefManager = new PrefManager(getApplicationContext());
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObjectdatafull = new JSONObject();
        try {
            jsonObject.put("apikey", Constants.API_KEY);
            jsonObject.put("truckid", prefManager.getTruckId());
            jsonObject.put("driverid", prefManager.getId());
            JSONArray jsonArraydata = new JSONArray();
            for (int i = 0; i < responseData.size(); i++) {

                JSONObject jsonObjectdata = new JSONObject();

                jsonObjectdata.put("question_id", responseData.get(i).getId());
                jsonObjectdata.put("selected", responseData.get(i).getIsSelected());
                jsonObjectdata.put("comment", responseData.get(i).getComment());

                Log.d("xxxxx", "size image:" + responseData.get(i).getImages().size());

                JSONArray jsonArrayimage = new JSONArray();
                JSONObject jsonObjectimage = new JSONObject();

                for (int k = 0; k < responseData.get(i).getImages().size(); k++) {
                    jsonObjectimage.put("base64", "data:image/png;base64," + Utils.getFileToByte(responseData.get(i).getImages().get(k).getPath()));
                    jsonArrayimage.put(jsonObjectimage);
                }

                jsonObjectdata.put("Image", jsonArrayimage);
                jsonArraydata.put(jsonObjectdata);

            }

            jsonObject.put("data", jsonArraydata);

            jsonObjectdatafull.put("data", jsonObject);
            Log.d("xxxxxxx", "post data:" + jsonObjectdatafull.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        showProgressDialogue();
        QuestionListSubmitRequestEntity questionListSubmitRequestEntity = new QuestionListSubmitRequestEntity();
        questionListSubmitRequestEntity.setData(jsonObject.toString());
        questionListSubmitRequestEntity.setDriverid(prefManager.getId());
        questionListSubmitRequestEntity.setLogin_token(prefManager.getLogin_Token());
        presenter.submitQuestionary(questionListSubmitRequestEntity);
    }


    @Override
    public void onSuccessCheckList(QuestionListResponse response) {
        hideProgressDialogue();
        responseData = response.getData();


        for (int i = 0; i < responseData.size(); i++) {

            QuestionModel questionModel = new QuestionModel();
            questionModel.setCheckListId(responseData.get(i).getId());

        }
        if (responseData.size() == 0) {
            btn_submit.setText("NEXT");
        }
        setRecyclerAdapter();
    }

    @Override
    public void onErrorCheckList(String s) {
        {
            hideProgressDialogue();
            if (s.equals("invalidToken")) {
                prefManager.logout();

                Intent intent = new Intent(QuestionListActivity.this, Login_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                btn_submit.setText("NEXT");
            }

        }
    }

    @Override
    public void onSuccessSubmitQuest(CommonResponse commonResponse) {
        hideProgressDialogue();
        Intent intent = new Intent(QuestionListActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onErrorSubmitQuest(String error) {
        hideProgressDialogue();
        if (error.equals("invalidToken")) {
            prefManager.logout();
            Intent intent = new Intent(QuestionListActivity.this, Login_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}