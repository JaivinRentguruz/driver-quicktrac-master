package com.evolution.quicktrack.login;

import com.evolution.quicktrack.constants.Constants;
import com.evolution.quicktrack.response.common.CommonResponse;
import com.evolution.quicktrack.response.login.LoginData;
import com.evolution.quicktrack.response.login.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.evolution.quicktrack.base.BaseContractor.apiService;

public class LoginPresenter implements LoginContractor.Presenter {

    private LoginContractor.View loginView;

    public LoginPresenter(LoginContractor.View contractorView) {
        this.loginView = contractorView;
    }

    @Override
    public void login(LoginRequestEntity loginRequestEntity) {
        Call<LoginResponse> call = apiService.login_api(loginRequestEntity.getApiKey(),loginRequestEntity.getLicenceNumber(),loginRequestEntity.getPassword(),loginRequestEntity.getDeviceId(),loginRequestEntity.getRefreshToken(),loginRequestEntity.getImei());
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body().getStatus().equals(Constants.API_STATUS)) {
                    loginView.onLoginSuccess(response.body());
                } else {
                    loginView.onLoginError(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginView.onLoginError(t.getMessage());
            }
        });
    }

    @Override
    public void forceLogOut(LoginData loginData) {
        Call<CommonResponse> call = apiService.force_logout_api(Constants.API_KEY, loginData.getDriverId(), loginData.getLogin_token(), loginData.getFcm_token());
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.body().getStatus().equals(Constants.API_STATUS)) {
                    loginView.onForLogOutSuccess(loginData,response.body().getMessage());
                } else {
                    loginView.onForLogOutError(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                loginView.onForLogOutError(t.getMessage());
            }
        });
    }
}
