package com.ging.chat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.ging.chat.activity.MainActivity;
import com.ging.chat.service.ChatService;
import com.ging.chat.service.NotificationService;
import com.ging.chat.utils.AppUtils;

import java.util.HashMap;
import java.util.Map;

public class NotificationPlayer {

    private static boolean currentVersionSupportBigNotification = AppUtils.currentVersionSupportBigNotification();
    private static boolean currentVersionSupportVectorDrawable = AppUtils.currentVersionSupportVectorDrawable();

    private Notification notification;

    public static final String ANSWER_A = "answer_a";
    public static final String ANSWER_B = "answer_b";
    public static final String ANSWER_C = "answer_c";
    public static final String ANSWER_D = "answer_d";

    public static Map<String, Integer> answerMap = new HashMap<>();

    public NotificationPlayer(Context context) {

        answerMap.put(ANSWER_A, R.id.answer_a);
        answerMap.put(ANSWER_B, R.id.answer_b);
        answerMap.put(ANSWER_C, R.id.answer_c);
        answerMap.put(ANSWER_D, R.id.answer_d);

        String chanelId = "com.ging.chat.notification";
        String chanelName = "ChatApp";
        String channelDescription = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if(notificationManager != null) {
                NotificationChannel channel = new NotificationChannel(chanelId, chanelName, NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription(channelDescription);
                notificationManager.createNotificationChannel(channel);
            }
        }

        // The PendingIntent to launch our activity if the user selects this notification
        Intent intent = new Intent(context, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pMain = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

//        intent = new Intent(context, PlayerService.class);
//        intent.setAction(PlayerService.ACTION_DELETE_NOTIFICATION_PLAYER);
//        PendingIntent pDelete = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, chanelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("xxx")
                .setContentText("yyy")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pMain)
//                .setDeleteIntent(pDelete)
                .setAutoCancel(false)
                .setWhen(0)
                .setCustomContentView(new RemoteViews(
                        context.getApplicationContext().getPackageName(), R.layout.layout_notification));

        if(currentVersionSupportBigNotification)
            mBuilder.setCustomBigContentView(new RemoteViews(
                    context.getApplicationContext().getPackageName(), R.layout.layout_notification));

        notification = mBuilder.build();

        setListeners(context);

        //
        Resources resources = context.getResources();

    }

    public Notification getNotification() {
        return notification;
    }

    private void setTextViewText(int viewId, String text) {
        notification.contentView.setTextViewText(viewId, text);
        if(currentVersionSupportBigNotification)
            notification.bigContentView.setTextViewText(viewId, text);
    }

    private void setTextColor(int viewId, int color) {
        notification.contentView.setTextColor(viewId, color);
        if(currentVersionSupportBigNotification)
            notification.bigContentView.setTextColor(viewId, color);
    }

    private void setImageViewBitmap(int viewId, Bitmap bitmap) {
        notification.contentView.setImageViewBitmap(viewId, bitmap);
        if(currentVersionSupportBigNotification)
            notification.bigContentView.setImageViewBitmap(viewId, bitmap);
    }

    public void setAnswer(String answer) {
        setAnswer(answerMap.get(answer));
//        setTextViewText(R.id.answer_a, "G");
    }

    private void setAnswer(int viewId) {
        clearAnswer();
        setTextColor(viewId, Color.GREEN);
    }

    private void clearAnswer() {
        int color = Color.BLACK;
        setTextColor(R.id.answer_a, color);
        setTextColor(R.id.answer_b, color);
        setTextColor(R.id.answer_c, color);
        setTextColor(R.id.answer_d, color);
    }

    private void setListeners(Context context) {
        setListeners(context, notification.contentView);
        if(currentVersionSupportBigNotification)
            setListeners(context, notification.bigContentView);
    }

    public static void setListeners(Context context, RemoteViews view) {
        Intent answer_a = new Intent(context, NotificationService.class);
        answer_a.setAction(ANSWER_A);

        Intent answer_b = new Intent(context, NotificationService.class);
        answer_b.setAction(ANSWER_B);

        Intent answer_c = new Intent(context, NotificationService.class);
        answer_c.setAction(ANSWER_C);

        Intent answer_d = new Intent(context, NotificationService.class);
        answer_d.setAction(ANSWER_D);

        PendingIntent pa = PendingIntent.getService(context.getApplicationContext(), 0, answer_a, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.answer_a, pa);

        PendingIntent pb = PendingIntent.getService(context.getApplicationContext(), 0, answer_b, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.answer_b, pb);

        PendingIntent pc = PendingIntent.getService(context.getApplicationContext(), 0, answer_c, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.answer_c, pc);

        PendingIntent pd = PendingIntent.getService(context.getApplicationContext(), 0, answer_d, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.answer_d, pd);

        Intent iStop = new Intent(context, ChatService.class);
        iStop.setAction("stop_service");
        PendingIntent pStop = PendingIntent.getService(context.getApplicationContext(), 0, iStop, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btn_close, pStop);
    }
}
