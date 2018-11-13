package com.ging.chat.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ging.chat.model.base.SwitchListModel;

public class AppData {

    private static AppData instance;

    public static AppData getInstance() {
        return instance;
    }

    public static void initialize(Context context) {
        instance = new AppData(context);
    }

    private SharedPreferences pref;

    public SharedPreferences getPref() {
        return pref;
    }

    private SharedPreferences defaultPref;

    public SharedPreferences getDefaultPref() {
        return defaultPref;
    }

    public AppData(Context context) {
        pref = context.getSharedPreferences("law_book", Context.MODE_PRIVATE);
        defaultPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveFirstOpenApp(boolean first) {
        pref.edit().putBoolean("first_open_app", first).apply();
    }

    public boolean getFirstOpenApp() {
        return pref.getBoolean("first_open_app", true);
    }

    public void saveAppVersionCode(int versionCode) {
        pref.edit().putInt("app_version_code", versionCode).apply();
    }

    public int getAppVersionCode() {
        return pref.getInt("app_version_code", 16);
    }

    public void saveUpdateAppConfig(boolean update) {
        pref.edit().putBoolean("update_app_config", update).apply();
    }

    public boolean getUpdateAppConfig() {
        return pref.getBoolean("update_app_config", true);
    }

    public void saveAppConfig(String appConfig) {
        pref.edit().putString("app_config", appConfig).apply();
    }

    public String getAppConfig() {
        return pref.getString("app_config", null);
    }

    public void saveTypeView(int typeView) {
        pref.edit().putInt("type_view", typeView).apply();
    }

    public int getTypeView() {
        return pref.getInt("type_view", SwitchListModel.GRID);
    }

}
