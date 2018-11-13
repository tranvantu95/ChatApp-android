package com.ging.chat.config;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class EventLog {

    private static EventLog instance;

    public static EventLog getInstance() {
        return instance;
    }

    public static void initialize(FirebaseAnalytics firebaseAnalytics) {
        instance = new EventLog(firebaseAnalytics);
    }

    private FirebaseAnalytics firebaseAnalytics;

    private EventLog(FirebaseAnalytics firebaseAnalytics) {
        this.firebaseAnalytics = firebaseAnalytics;
    }

//    public void sendEventOpenApp(String from, String type) {
//        Bundle bundle = new Bundle();
//        bundle.putString(Define.Event.OPEN_FROM, from);
//        bundle.putString(Define.Event.OPEN_TYPE, type);
//        firebaseAnalytics.logEvent(Define.Event.OPEN_APP, bundle);
//    }

}
