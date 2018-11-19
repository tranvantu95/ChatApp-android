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
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.ging.chat.activity.MainActivity;
import com.ging.chat.config.Define;
import com.ging.chat.service.ChatService;
import com.ging.chat.utils.AppUtils;

public class NotificationPlayer {

    private static boolean currentVersionSupportBigNotification = AppUtils.currentVersionSupportBigNotification();
    private static boolean currentVersionSupportVectorDrawable = AppUtils.currentVersionSupportVectorDrawable();

    private Notification notification;

    public NotificationPlayer(Context context) {

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

    private void setTextColor(int viewId, int color) {
        notification.contentView.setTextColor(viewId, color);
        if(currentVersionSupportBigNotification)
            notification.bigContentView.setTextColor(viewId, color);
    }

    private void setTextViewText(int viewId, String text) {
        notification.contentView.setTextViewText(viewId, text);
        if(currentVersionSupportBigNotification)
            notification.bigContentView.setTextViewText(viewId, text);
    }

    private void setImageViewBitmap(int viewId, Bitmap bitmap) {
        notification.contentView.setImageViewBitmap(viewId, bitmap);
        if(currentVersionSupportBigNotification)
            notification.bigContentView.setImageViewBitmap(viewId, bitmap);
    }

    public void showAnswer(String answer) {
        showAnswer(Define.Answer.mapIds.get(answer));
    }

    private void showAnswer(int viewId) {
        clearAnswer();
        setTextColor(viewId, Color.RED);
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

    private static void setListeners(Context context, RemoteViews view) {
        Intent intent;
        PendingIntent pendingIntent;

        intent = new Intent(context.getApplicationContext(), ChatService.class);
        intent.setAction("emit_answer_a");
        intent.putExtra("answer", Define.Answer.A);

        pendingIntent = PendingIntent.getService(context.getApplicationContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.answer_a, pendingIntent);

        intent = new Intent(context.getApplicationContext(), ChatService.class);
        intent.setAction("emit_answer_b");
        intent.putExtra("answer", Define.Answer.B);

        pendingIntent = PendingIntent.getService(context.getApplicationContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.answer_b, pendingIntent);

        intent = new Intent(context.getApplicationContext(), ChatService.class);
        intent.setAction("emit_answer_c");
        intent.putExtra("answer", Define.Answer.C);

        pendingIntent = PendingIntent.getService(context.getApplicationContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.answer_c, pendingIntent);

        intent = new Intent(context.getApplicationContext(), ChatService.class);
        intent.setAction("emit_answer_d");
        intent.putExtra("answer", Define.Answer.D);

        pendingIntent = PendingIntent.getService(context.getApplicationContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.answer_d, pendingIntent);

        intent = new Intent(context, ChatService.class);
        intent.setAction("stop_service");

        pendingIntent = PendingIntent.getService(context.getApplicationContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btn_close, pendingIntent);
    }

}
