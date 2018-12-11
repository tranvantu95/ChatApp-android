package com.ging.chatapp.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.ging.chatapp.NotificationPlayer;

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

        if(("show_answer").equals(intent.getAction())) {
            String answer = intent.getStringExtra("answer");
            showAnswer(answer);
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

    public void showAnswer(String answer) {
        notificationPlayer.showAnswer(answer);
        showNotification(true);
    }

    public static void showAnswer(Context context, String answer) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.setAction("show_answer");
        intent.putExtra("answer", answer);
        context.startService(intent);
    }

}
