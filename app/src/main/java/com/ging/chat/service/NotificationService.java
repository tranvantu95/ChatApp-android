package com.ging.chat.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.RadioButton;

import com.ging.chat.NotificationPlayer;
import com.ging.chat.config.Debug;
import com.ging.chat.model.ChatModel;
import com.ging.chat.utils.ModelUtils;

public class NotificationService extends Service {
    
    private static final String TAG = "NotificationService";

    public static final int NOTIFICATION_ID = 2502;

    private static final String DATA = "player_data";
    private static final String IS_SHOWING_NOTIFICATION_KEY = "isShowingNotification";

    // binder
    private final IBinder mBinder = new NotificationService.LocalBinder();

    public class LocalBinder extends Binder {
        public NotificationService getService() {
            return NotificationService.this;
        }
    }

    private SharedPreferences preferences;

    public boolean isShowingNotification;

    private NotificationPlayer notificationPlayer;

    public NotificationPlayer getNotificationPlayer() {
        return notificationPlayer;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        preferences = getApplicationContext().getSharedPreferences(DATA, Context.MODE_PRIVATE);

//        isShowingNotification = preferences.getBoolean(IS_SHOWING_NOTIFICATION_KEY, false);
//        isShowingNotification = true; // test

        notificationPlayer = new NotificationPlayer(this);

    }

    public void showNotification(boolean startForeground) {
        Log.d(TAG, "showNotification");
        if(!isShowingNotification) {
            isShowingNotification = true;
            saveIsShowingNotification();
        }

        if(startForeground)
            startForeground(NOTIFICATION_ID, notificationPlayer.getNotification());
        else {
            stopForeground(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP);
            NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notificationPlayer.getNotification());
        }
    }

    public void deleteNotification(){
        Log.d(TAG, "deleteNotification");
        if(isShowingNotification) {
            isShowingNotification = false;
            saveIsShowingNotification();
        }

        stopForeground(true);
        NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID);
    }

    public boolean isShowingNotification() {
        if(!isShowingNotification) return false;

        // notificationPlayer can hidden by user settings
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NotificationManager nm = (NotificationManager)
                    getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

            if(nm != null) {
                StatusBarNotification[] activeNotifications = nm.getActiveNotifications();

                for (StatusBarNotification notification : activeNotifications)
                    if(notification.getId() == NOTIFICATION_ID) return true;

                return false;
            }
        }

        return true;
    }

    private void saveIsShowingNotification() {
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putBoolean(IS_SHOWING_NOTIFICATION_KEY, isShowingNotification);
//        editor.apply();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        if(intent.getAction() != null && intent.getAction().startsWith("answer")) {
            showAnswer(intent.getAction());

            String answer = intent.getAction().split("_")[1].toUpperCase();
            SocketService.emitAnswer(getApplicationContext(), answer);
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");

        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(TAG, "onRebind");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d(TAG, "onTaskRemoved");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

        deleteNotification();
    }

    private void showAnswer(String answer) {
        Log.d(TAG, "showAnswer " + answer);
        notificationPlayer.setAnswer(answer);
        showNotification(true);
        ModelUtils.ofApp().get(ChatModel.class).getAnswer().setValue(answer);
    }

    public static void setAnswer(Context context, String answer) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.setAction(answer);
        context.startService(intent);
    }
}
