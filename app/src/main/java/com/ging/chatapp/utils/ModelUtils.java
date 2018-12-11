package com.ging.chatapp.utils;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.lifecycle.ViewModelStore;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ModelUtils extends ViewModelProviders {

    private static ViewModelStore appViewModelStore;

    private static ViewModelStore getAppViewModelStore() {
        if(appViewModelStore == null) {
            appViewModelStore = new ViewModelStore();
        }
        return appViewModelStore;
    }

    @NonNull
    @MainThread
    public static ViewModelProvider ofApp(@Nullable Application application) {
        return ofApp(application, null);
    }

    @NonNull
    @MainThread
    public static ViewModelProvider ofApp(@Nullable Application application, @Nullable ViewModelProvider.Factory factory) {
        if (application == null) {
            throw new IllegalStateException("Your activity/fragment is not yet attached to "
                    + "Application. You can't request ViewModel before onCreate call.");
        }
        if (factory == null) {
            factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application);
        }
        return new ViewModelProvider(getAppViewModelStore(), factory);
    }

    @NonNull
    @MainThread
    public static ViewModelProvider ofApp() {
        return ofApp(new ViewModelProvider.NewInstanceFactory());
    }

    @NonNull
    @MainThread
    public static ViewModelProvider ofApp(@NonNull ViewModelProvider.Factory factory) {
        return new ViewModelProvider(getAppViewModelStore(), factory);
    }

}
