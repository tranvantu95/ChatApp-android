package com.ging.chat.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.ging.chat.ChatLayer;
import com.ging.chat.NotificationPlayer;
import com.ging.chat.R;
import com.ging.chat.activity.MainActivity;
import com.ging.chat.config.Debug;
import com.ging.chat.model.ChatModel;
import com.ging.chat.utils.ModelUtils;

/**
 * Foreground service. Creates a head view.
 * The pending intent allows to go back to the settings activity.
 */
public class ChatService extends Service {

    private final static int FOREGROUND_ID = 999;

    private ChatLayer mChatLayer;

    private ServiceConnection notificationConnection;
    private NotificationService notificationService;
    private NotificationPlayer notificationPlayer;

    //
    private void bindNotificationService() {
        if(notificationConnection != null) return;
        Log.d(Debug.TAG, "bindNotificationService");

        notificationConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                NotificationService.LocalBinder binder = (NotificationService.LocalBinder) service;
                notificationService = binder.getService();
                notificationPlayer = notificationService.getNotificationPlayer();

                notificationService.showNotification(true);
                startForeground(NotificationService.NOTIFICATION_ID, notificationPlayer.getNotification());
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {}
        };

        bindService(new Intent(getApplicationContext(), NotificationService.class),
                notificationConnection, BIND_AUTO_CREATE);
    }

    private void unbindNotificationService() {
        if(notificationConnection != null) {
            Log.d(Debug.TAG, "unbindNotificationService");
            unbindService(notificationConnection);
            notificationConnection = null;
        }
    }

    private boolean isShowingNotification() {
        return notificationService != null && notificationService.isShowingNotification();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        logServiceStarted();

        initChatLayer();

//        PendingIntent pendingIntent = createPendingIntent();
//        Notification notification = createNotification(pendingIntent);
//        startForeground(FOREGROUND_ID, notification);

        bindNotificationService();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getAction() != null && intent.getAction().contains("emit_answer")) {
            String answer = intent.getStringExtra("answer");
            Log.d(Debug.TAG, "emitAnswer " + answer);

            ModelUtils.ofApp().get(ChatModel.class).getAnswer().setValue(answer);
            SocketService.emitAnswer(getApplicationContext(), answer);
            notificationService.showAnswer(answer);
        }
        else if(("stop_service").equals(intent.getAction())) {
            stopSelf();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unbindNotificationService();
        destroyChatLayer();

//        stopForeground(true);

        logServiceEnded();
    }

    public static Intent createIntentEmitAnswer(Context context, String answer) {
        Intent intent = new Intent(context.getApplicationContext(), ChatService.class);
        intent.setAction("emit_answer");
        intent.putExtra("answer", answer);
        return intent;
    }

    public static void emitAnswer(Context context, String answer) {
        context.getApplicationContext().startService(createIntentEmitAnswer(context, answer));
    }

    private void initChatLayer() {
        mChatLayer = new ChatLayer(this);
    }

    private void destroyChatLayer() {
        mChatLayer.destroy();
        mChatLayer = null;
    }

    private PendingIntent createPendingIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        return PendingIntent.getActivity(this, 0, intent, 0);
    }

    private Notification createNotification(PendingIntent intent) {
        return new Notification.Builder(this)
                .setContentTitle(getText(R.string.notificationTitle))
                .setContentText(getText(R.string.notificationText))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(intent)
                .build();
    }

    private void logServiceStarted() {
        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
    }

    private void logServiceEnded() {
        Toast.makeText(this, "Service ended", Toast.LENGTH_SHORT).show();
    }
}
