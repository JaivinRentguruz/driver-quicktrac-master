package com.evolution.quicktrack.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evolution.quicktrack.BaseActivity;
import com.evolution.quicktrack.Login_Activity;
import com.evolution.quicktrack.R;
import com.evolution.quicktrack.adapter.UserList_Adapter;
import com.evolution.quicktrack.callback.Recycler_ItemClickCallBack;
import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.constants.LogCustom;
import com.evolution.quicktrack.network.ApiCallInterface;
import com.evolution.quicktrack.network.ApiClient;
import com.evolution.quicktrack.response.userlist.UserDataItem;
import com.evolution.quicktrack.response.userlist.UserListResponse;
import com.evolution.quicktrack.util.PrefManager;
import com.evolution.quicktrack.util.RecyclerViewEmptySupport;
import com.evolution.quicktrack.util.Utils;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends BaseActivity implements Recycler_ItemClickCallBack {

    @BindView(R.id.recyclerview_manu)
    RecyclerViewEmptySupport recyclerview;
    @BindView(R.id.txt_nodata)
    TextView txtNodata;
    @BindView(R.id.emptyView)
    LinearLayout emptyView;
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.img_close)
    ImageView imgClose;
    @BindView(R.id.img_search)
    ImageView imgSearch;
    private KProgressHUD pd;
    public static String TAG = Login_Activity.class.getSimpleName();
    @BindView(R.id.btn_logout)
    ImageView btnLogout;


    private List<UserDataItem> recentresponseData=new ArrayList<>();
    private List<UserDataItem> newresponseData=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        callApi();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_userlist;
    }


    public void initView() {

        btnLogout.setVisibility(View.INVISIBLE);

        this.recyclerview.setHasFixedSize(true);
        this.recyclerview.setLayoutManager(new LinearLayoutManager(UserListActivity.this, LinearLayoutManager.VERTICAL, false));
        this.recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setEmptyView(emptyView);

        //mAdapter.getFilter().filter(query);


        recyclerview.setFocusable(true);
        editSearch.setCursorVisible(false);
        editSearch.setClickable(true);
        Utils.hideKeyview(UserListActivity.this,editSearch);


        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {



                if(charSequence.length()==1){
                    setNewUserAddapter(newresponseData);
                    imgClose.setVisibility(View.VISIBLE);
                    imgSearch.setVisibility(View.GONE);
                }

                custom_manu_adepter.getFilter().filter(charSequence);;
                if(charSequence.length()==0) {
                    setNewUserAddapter(recentresponseData);
                    imgClose.setVisibility(View.GONE);
                    imgSearch.setVisibility(View.VISIBLE);
                }




            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



    }



    @OnClick({R.id.img_close, R.id.img_search,R.id.edit_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_close:

                editSearch.setText("");
                imgClose.setVisibility(View.GONE);
                imgSearch.setVisibility(View.VISIBLE);
                Utils.hideKeyview(UserListActivity.this,editSearch);

                break;
            case R.id.img_search:


                break;
            case R.id.edit_search:


                editSearch.setCursorVisible(true);
                editSearch.requestFocus();

                break;
        }
    }



    public void callApi() {

        try {

            if (Utils.validInternet(getApplicationContext()))
                get_UserListApi();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }






    private void get_UserListApi() throws Exception {

        ApiCallInterface apiService = ApiClient.getClient().create(ApiCallInterface.class);
        pd = LogCustom.loderProgressDialog2(this);
        pd.show();

        PrefManager prefManager = new PrefManager(getApplicationContext());

//        Toast.makeText(TrackListActivity.this,"id :"+prefManager.getId(),Toast.LENGTH_LONG).show();

        Log.d("xxxx", "useerid:" + prefManager.getId());

        Call<UserListResponse> call = apiService.getUserList(prefManager.getLogin_Token(),Constants.API_KEY, prefManager.getId(), "loadFriends",prefManager.getId());
        call.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {

                Log.d(TAG, " : check status : " + response.body().toString());

                if (pd != null && pd.isShowing())
                    pd.dismiss();
                if (response.body().isStatus()) {

                    recentresponseData = response.body().getData().getFriends();
                    newresponseData= response.body().getData().getFriends();
                    setNewUserAddapter(recentresponseData);
                }
                else
                {

                    if(response.body().getMessage().equals("invalidToken"))
                    {
                        prefManager.logout();

                        Intent   intent =new Intent(UserListActivity.this,Login_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {
                LogCustom.logd(TAG, t.toString());
                if (pd != null && pd.isShowing())
                    pd.dismiss();
            }
        });
    }
    UserList_Adapter custom_manu_adepter;

   /* public void setRecentUserAddapter() {

        custom_manu_adepter  = new UserList_Adapter(UserListActivity.this, recentresponseData, UserListActivity.this,true);
        recyclerview.setAdapter(custom_manu_adepter);

    }*/

    public void setNewUserAddapter(List<UserDataItem> recentresponseData) {

        custom_manu_adepter  = new UserList_Adapter(UserListActivity.this, recentresponseData, UserListActivity.this,false);
        recyclerview.setAdapter(custom_manu_adepter);

    }

    @Override
    public void OnItemClick(int position) {

        Intent intent = new Intent(UserListActivity.this, ChatActivity.class);
        intent.putExtra(Constants.EXTA_FRNDID, recentresponseData.get(position).getFriendId());
        startActivity(intent);


    }


    @OnClick({R.id.img_home, R.id.btn_logout})
    public void onViewClicked2(View view) {
        switch (view.getId()) {
            case R.id.img_home:
                Log.d("xxxxx", "xxx");

                exitDilog();

                break;
            case R.id.btn_logout:

                break;


        }
    }

    public void exitDilog() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserListActivity.this);
        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.areyousureexit));
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onBackPressed();
            }
        });

        alertDialog.setNeutralButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        // Showing Alert Message
        alertDialog.show();

    }


}
