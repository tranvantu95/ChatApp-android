package com.ging.chatapp;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.AccessToken;
import com.ging.chatapp.api.ApiUtils;
import com.ging.chatapp.api.response.LoginResponse;
import com.ging.chatapp.model.AppModel;
import com.ging.chatapp.model.User;
import com.ging.chatapp.service.SocketService;
import com.ging.chatapp.utils.FBUtils;
import com.ging.chatapp.utils.General;
import com.ging.chatapp.utils.KeyUtils;
import com.ging.chatapp.utils.ModelUtils;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import com.ging.chatapp.config.AppData;
import com.ging.chatapp.config.Debug;
import com.ging.chatapp.config.Define;
import com.ging.chatapp.config.EventLog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyApplication extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {

    protected final String TAG = getClass().getSimpleName();

    public FirebaseStorage storage;
    public StorageReference storageRef;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Debug.TAG + TAG, "onCreate");

        Log.d(Debug.TAG + "SHA1", KeyUtils.getSHA1(this));
        Log.d(Debug.TAG + "KeyHash", KeyUtils.getKeyHash(this));

        Define.Answer.buildMapIds();

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

        //
        startService(new Intent(getApplicationContext(), SocketService.class));

        //
        if(FBUtils.isLoggedIn()) ApiUtils.getAPIService()
                .loginFacebook(AccessToken.getCurrentAccessToken().getToken())
                .enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                if(loginResponse != null) {
                    if(loginResponse.getStatus() == 1) {
                        User user = loginResponse.getData();
                        ModelUtils.ofApp().get(AppModel.class).getUser().setValue(user);
                    }
                    else {
//                        Toast.makeText(getContext(), loginResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {

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
