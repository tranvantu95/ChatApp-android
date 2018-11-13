package com.ging.chat;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.Log;

import com.ging.chat.model.AppModel;
import com.ging.chat.utils.General;
import com.ging.chat.utils.ModelUtils;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import com.ging.chat.config.AppData;
import com.ging.chat.config.Debug;
import com.ging.chat.config.Define;
import com.ging.chat.config.EventLog;

public class MyApplication extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {

    protected final String TAG = getClass().getSimpleName();

    public FirebaseStorage storage;
    public StorageReference storageRef;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Debug.TAG + TAG, "onCreate");

        // set support vector drawable for api < 21
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        // AppData
        AppData.initialize(this);

        SharedPreferences sharedPreferences = AppData.getInstance().getDefaultPref();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        General.typeView = AppData.getInstance().getTypeView();
        getModel(AppModel.class).getTypeView().setValue(General.typeView);

        // Firebase Analytics
//        EventLog.initialize(FirebaseAnalytics.getInstance(this));

        // Firebase Storage
//        storage = FirebaseStorage.getInstance("");
//        storageRef = storage.getReference();

        //
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            private int numActivityCreated;

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                numActivityCreated++;

                if(numActivityCreated == 1) {
                }

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                numActivityCreated--;
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    protected <Model extends ViewModel> Model getModel(Class<Model> clazz) {
        return ModelUtils.ofApp(this).get(clazz);
    }

}
