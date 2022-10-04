package com.evolution.quicktrack.login;

import com.evolution.quicktrack.base.BaseContractor;
import com.evolution.quicktrack.response.login.LoginData;
import com.evolution.quicktrack.response.login.LoginResponse;

public interface LoginContractor extends BaseContractor {
    interface View{
        void onLoginSuccess(LoginResponse loginResponse);
        void onLoginError(String message);

        void onForLogOutSuccess(LoginData loginData,String message);
        void onForLogOutError(String message);
    }
    interface Presenter{
        void login(LoginRequestEntity loginRequestEntity);
        void forceLogOut(LoginData loginData);
    }
}
