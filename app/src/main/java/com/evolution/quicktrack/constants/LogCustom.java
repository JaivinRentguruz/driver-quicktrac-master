package com.evolution.quicktrack.constants;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.evolution.quicktrack.R;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.kaopiz.kprogresshud.KProgressHUD.Style;

public class LogCustom {
    public static void logd(String Tag, String msg) {
        Log.d("xxxx" + Tag, "" + msg);
    }

    public static void loge(String Tag, String msg) {
        Log.e("xxxx" + Tag, "" + msg);
    }

    public static void Toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static KProgressHUD loderProgressDialog2(Context context) {
        return KProgressHUD.create(context)
                .setCancellable(false)
                .setStyle(Style.SPIN_INDETERMINATE).setLabel(context.getResources().getString(R.string.pleasewait));
    }
    public static KProgressHUD popUpLoader(Context context) {
        return KProgressHUD.create(context)
                .setCancellable(false)
                .setStyle(Style.SPIN_INDETERMINATE)
                .setLabel(context.getResources().getString(R.string.pleasewait));
    }








}
