package com.ging.chat.config;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import com.ging.chat.BuildConfig;

public class AppConfig {

    public static final String TAG = "AppConfig";

    private static final int APP_CONFIG_VERSION = 1;

    public static int appVersionCode = BuildConfig.VERSION_CODE;
    public static String appId = BuildConfig.APPLICATION_ID;
    public static String emailAddress = "chimchesaustudio@gmail.com";
    public static String fanPageUrl = "https://www.facebook.com/298996927287121";

}
